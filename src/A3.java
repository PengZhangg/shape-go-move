/*
 *  ============================================================================================
 *  A3.java : Extends JFrame and contains a panel where shapes move around on the screen.
 *  YOUR UPI: PZHA619
 *  ============================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import javax.swing.tree.*;

public class A3 extends JFrame {
	private AnimationViewer bouncingPanel; // panel for bouncing area
	JButton addNodeButton, removeNodeButton;
	JComboBox<ShapeType> shapesComboBox;
	JComboBox<PathType> pathComboBox;
	JTree tree;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new A3();
			}
		});
	}

	public A3() {
		super("Bouncing Application");
		JPanel mainPanel = setUpMainPanel();
		add(mainPanel, BorderLayout.CENTER);
		add(setUpToolsPanel(), BorderLayout.NORTH);
		addComponentListener(
				new ComponentAdapter() { // resize the frame and reset all margins for all shapes
					public void componentResized(ComponentEvent componentEvent) {
						bouncingPanel.resetMarginSize();
					}
				});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public JPanel setUpMainPanel() {
		JPanel mainPanel = new JPanel();
		bouncingPanel = new AnimationViewer();
		bouncingPanel.setPreferredSize(new Dimension(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT));
		JPanel modelPanel = setUpModelPanel();
		modelPanel.setPreferredSize(new Dimension(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT));
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, modelPanel, bouncingPanel);
		mainSplitPane.setResizeWeight(0.5);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setContinuousLayout(true);
		mainPanel.add(mainSplitPane);
		return mainPanel;
	}

	public JPanel setUpModelPanel() {
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setPreferredSize(new Dimension(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT / 2));
		tree = new JTree(bouncingPanel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		JScrollPane treeScrollpane = new JScrollPane(tree);
		JPanel treeButtonsPanel = new JPanel();
		addNodeButton = new JButton("Add Node");
		addNodeButton.addActionListener(new AddListener());
		removeNodeButton = new JButton("Remove Node");
		removeNodeButton.addActionListener(new RemoveListener());
		treeButtonsPanel.add(addNodeButton);
		treeButtonsPanel.add(removeNodeButton);
		treePanel.add(treeButtonsPanel, BorderLayout.NORTH);
		treePanel.add(treeScrollpane, BorderLayout.CENTER);
		return treePanel;
	}

	class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object lastNode = tree.getLastSelectedPathComponent();
			if (lastNode == null) {
				JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
			} else if (lastNode instanceof NestedShape) {
				bouncingPanel.addShapeNode((NestedShape) lastNode);
			} else {
				JOptionPane.showMessageDialog(null, "ERROR: Must select a NestedShape node.");
			}
		}
	}

	class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object lastComponent = tree.getLastSelectedPathComponent();
			if (lastComponent == null) {
				JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
			} else {
				if (lastComponent == tree.getModel().getRoot()) {
					JOptionPane.showMessageDialog(null, "ERROR: Must not remove the root.");
				} else if (lastComponent instanceof Shape) {
					bouncingPanel.removeNodeFromParent((Shape) lastComponent);
				} else {
					JOptionPane.showMessageDialog(null, "ERROR: Must select a NestedShape node.");
				}
			}
		}
	}

	public JPanel setUpToolsPanel() {
		shapesComboBox = new JComboBox<ShapeType>(new DefaultComboBoxModel<ShapeType>(ShapeType.values()));
		shapesComboBox.addActionListener(new ShapeActionListener());
		pathComboBox = new JComboBox<PathType>(new DefaultComboBoxModel<PathType>(PathType.values()));
		pathComboBox.addActionListener(new PathActionListener());
		JPanel toolsPanel = new JPanel();
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
		toolsPanel.add(new JLabel(" Shape: ", JLabel.RIGHT));
		toolsPanel.add(shapesComboBox);
		toolsPanel.add(new JLabel(" Path: ", JLabel.RIGHT));
		toolsPanel.add(pathComboBox);
		return toolsPanel;
	}

	class ShapeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			bouncingPanel.setCurrentShapeType((ShapeType) shapesComboBox.getSelectedItem());
		}
	}

	class PathActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			bouncingPanel.setCurrentPathType((PathType) pathComboBox.getSelectedItem());
		}
	}
}
