/*
 * ==========================================================================================
 * NestedShape.java : Extends JComponent and implements Runnable, TreeModel
 * YOUR UPI: PZHA619
 * ==========================================================================================
 */

import java.util.*;
import java.awt.*;

public class NestedShape extends RectangleShape {
    private ArrayList<Shape> innerShapes = new ArrayList<>();

    public Shape createInnerShape(PathType pt, ShapeType st) {
        Shape innerShape = null;
        int innerWidth = width / 4;
        int innerHeight = height / 4;
        Color fillColor = getColor();
        Color borderColor = getBorderColor();

        if (st == ShapeType.SQUARE) {
            int minSide = Math.min(innerWidth, innerHeight);
            innerShape = new SquareShape(0, 0, minSide, width, height, fillColor, borderColor, pt);
        } else if (st == ShapeType.RECTANGLE) {
            innerShape = new RectangleShape(0, 0, innerWidth, innerHeight, width, height, fillColor, borderColor, pt);
        } else if (st == ShapeType.NESTED) {
            innerShape = new NestedShape(0, 0, innerWidth, innerHeight, width, height, fillColor, borderColor, pt);
        }

        if (innerShape != null) {
            innerShape.setParent(this);
            innerShapes.add(innerShape);
        }

        return innerShape;
    }

    public NestedShape(int x, int y, int w, int h, int panelWidth, int panelHeight, Color fillColor, Color borderColor,
            PathType pathType) {
        super(x, y, w, h, panelWidth, panelHeight, fillColor, borderColor, pathType);
        createInnerShape(PathType.BOUNCING, ShapeType.RECTANGLE);
    }

    public NestedShape(int w, int h) {
        super(0, 0, w, h, Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT, Shape.DEFAULT_COLOR,
                Shape.DEFAULT_BORDER_COLOR, PathType.BOUNCING);
    }

    public Shape getInnerShapeAt(int index) {
        return innerShapes.get(index);
    }

    public int getSize() {
        return innerShapes.size();
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.translate(x, y);
        for (Shape s : innerShapes) {
            s.draw(g);
            g.setColor(Color.BLACK);
            g.drawString(s.getLabel(), s.getX(), s.getY());
        }
        g.translate(-x, -y);
    }

    public void move() {
        super.move();
        for (Shape s : innerShapes) {
            s.move();
        }
    }

    public int indexOf(Shape s) {
        return innerShapes.indexOf(s);
    }

    public void addInnerShape(Shape s) {
        innerShapes.add(s);
        s.setParent(this);
    }

    public void removeInnerShape(Shape s) {
        innerShapes.remove(s);
        s.setParent(null);
    }

    public void removeInnerShapeAt(int index) {
        innerShapes.get(index).setParent(null);
        innerShapes.remove(index);
    }

    public ArrayList<Shape> getAllInnerShapes() {
        return innerShapes;
    }
}