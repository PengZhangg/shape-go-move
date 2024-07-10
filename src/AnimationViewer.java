/*
 * ==========================================================================================
 * AnimationViewer.java : Moves shapes around on the screen according to different paths.
 * It is the main drawing area where shapes are added and manipulated.
 * YOUR UPI: PZHA619
 * ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListDataListener;
import java.lang.reflect.Field;

class AnimationViewer extends JComponent implements Runnable, TreeModel {
	private Thread animationThread = null; // the thread for animation
	private static int DELAY = 120; // the current animation speed
	private ShapeType currentShapeType = Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType = Shape.DEFAULT_PATHTYPE; // the current path type
	private Color currentColor = Shape.DEFAULT_COLOR; // the current fill colour of a shape
	private Color currentBorderColor = Shape.DEFAULT_BORDER_COLOR;
	private int currentPanelWidth = Shape.DEFAULT_PANEL_WIDTH, currentPanelHeight = Shape.DEFAULT_PANEL_HEIGHT,
			currentWidth = Shape.DEFAULT_WIDTH, currentHeight = Shape.DEFAULT_HEIGHT;
	private String currentLabel = Shape.DEFAULT_LABEL;
	protected NestedShape root;
	private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();

	public AnimationViewer() {
		start();
		this.root = new NestedShape(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT);
	}

	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Shape currentShape : root.getAllInnerShapes()) {
			currentShape.move();
			currentShape.draw(g);
			currentShape.drawString(g);
		}
	}

	public void resetMarginSize() {
		currentPanelWidth = getWidth();
		currentPanelHeight = getHeight();
		for (Shape currentShape : root.getAllInnerShapes()) {
			currentShape.resetPanelSize(currentPanelWidth, currentPanelHeight);
		}
	}

	public NestedShape getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof NestedShape) {
			return ((NestedShape) node).getAllInnerShapes().isEmpty();
		}
		return true;
	}

	public boolean isRoot(Shape selectedNode) {
		return selectedNode == root;
	}

	public Object getChild(Object parent, int index) {
		if (parent instanceof NestedShape) {
			NestedShape shape = (NestedShape) parent;
			ArrayList<Shape> children = shape.getAllInnerShapes();
			if (index >= 0 && index < children.size()) {
				return children.get(index);
			}
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if (parent instanceof NestedShape) {
			NestedShape shape = (NestedShape) parent;
			return shape.getAllInnerShapes().size();
		}
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof NestedShape) {
			NestedShape shape = (NestedShape) parent;
			ArrayList<Shape> children = shape.getAllInnerShapes();
			return children.indexOf(child);
		}
		return -1;
	}

	public void addTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.add(tml);
	}

	public void removeTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.remove(tml);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	public void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
		System.out.printf("Called fireTreeNodesInserted: path=%s, childIndices=%s, children=%s\n",
				Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
		final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
		for (TreeModelListener tml : treeModelListeners) {
			tml.treeNodesInserted(event);
		}
	}

	public void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
		System.out.printf("Called fireTreeNodesRemoved: path=%s, childIndices=%s, children=%s\n", Arrays.toString(path),
				Arrays.toString(childIndices), Arrays.toString(children));
		final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
		for (TreeModelListener tml : treeModelListeners) {
			tml.treeNodesRemoved(event);
		}
	}

	public void addShapeNode(NestedShape selectedNode) {
		Shape newChild = selectedNode.createInnerShape(currentPathType, currentShapeType);
		int index = selectedNode.getAllInnerShapes().indexOf(newChild);
		int[] childIndices = { index };
		Object[] children = { newChild };
		fireTreeNodesInserted(this, selectedNode.getPath(), childIndices, children);
	}

	public void removeNodeFromParent(Shape selectedNode) {
		NestedShape parent = selectedNode.getParent();
		int index = parent.indexOf(selectedNode);
		parent.removeInnerShape(selectedNode);
		int[] childIndices = { index };
		Object[] children = { selectedNode };
		fireTreeNodesRemoved(this, parent.getPath(), childIndices, children);
	}

	// you don't need to make any changes after this line ______________
	public String getCurrentLabel() {
		return currentLabel;
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public Color getCurrentBorderColor() {
		return currentBorderColor;
	}

	public void setCurrentShapeType(ShapeType value) {
		currentShapeType = value;
	}

	public void setCurrentPathType(PathType value) {
		currentPathType = value;
	}

	public ShapeType getCurrentShapeType() {
		return currentShapeType;
	}

	public PathType getCurrentPathType() {
		return currentPathType;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void start() {
		animationThread = new Thread(this);
		animationThread.start();
	}

	public void stop() {
		if (animationThread != null) {
			animationThread = null;
		}
	}

	public void run() {
		Thread myThread = Thread.currentThread();
		while (animationThread == myThread) {
			repaint();
			pause(DELAY);
		}
	}

	private void pause(int milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException ie) {
		}
	}
}
