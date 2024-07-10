/* ==============================================
 *  Shape.java : The superclass of all shapes.
 *  A shape defines various properties, including selected, colour, width and height.
 *  YOUR UPI: PZHA619
 *  ===============================================================================
 */

import java.awt.*;

abstract class Shape {
    public static final PathType DEFAULT_PATHTYPE = PathType.BOUNCING;
    public static final ShapeType DEFAULT_SHAPETYPE = ShapeType.RECTANGLE;
    public static final int DEFAULT_X = 0, DEFAULT_Y = 0, DEFAULT_WIDTH = 200, DEFAULT_HEIGHT = 100,
            DEFAULT_PANEL_WIDTH = 600, DEFAULT_PANEL_HEIGHT = 400;
    public static final Color DEFAULT_COLOR = Color.orange, DEFAULT_BORDER_COLOR = Color.black;
    public int x, y, width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT, panelWidth = DEFAULT_PANEL_WIDTH,
            panelHeight = DEFAULT_PANEL_HEIGHT; // the bouncing area
    protected Color color = DEFAULT_COLOR, borderColor = DEFAULT_BORDER_COLOR;
    protected MovingPath path = new BouncingPath(1, 2);
    public static final String DEFAULT_LABEL = "0";
    private static int numberOfShapes = 0;
    protected String label = DEFAULT_LABEL;
    protected NestedShape parent;

    public NestedShape getParent() {
        return parent;
    }

    public void setParent(NestedShape s) {
        parent = s;
    }

    public Shape[] getPath() {
        return getPathToRoot(this, 0);
    }

    public Shape[] getPathToRoot(Shape aShape, int depth) {
        Shape[] returnShapes;
        if (aShape == null) {
            if (depth == 0)
                return null;
            else
                returnShapes = new Shape[depth];
        } else {
            depth++;
            returnShapes = getPathToRoot(aShape.getParent(), depth);
            returnShapes[returnShapes.length - depth] = aShape;
        }
        return returnShapes;
    }

    public Shape(int x, int y, int w, int h, int panelWidth, int panelHeight, Color fillColor, Color borderColor,
            PathType pathType) {
        this.x = x;
        this.y = y;
        label = "" + numberOfShapes++;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        width = w;
        height = h;
        color = fillColor;
        this.borderColor = borderColor;
        if (pathType == PathType.BOUNCING)
            path = new BouncingPath(1, 2);
        else
            path = new DownRightPath(5, 5);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void drawString(Graphics g) {
        g.setColor(Color.black);
        g.drawString("" + label, x, y + 10);
    }

    public String toString() {
        return String.format("%s,%s,%dx%d,%s,%dx%d", this.getClass().getName(), path.getClass().getSimpleName(), width,
                height, label, panelWidth, panelHeight);
    }

    public void move() {
        path.move();
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int w) {
        if (w < DEFAULT_PANEL_WIDTH && w > 0)
            width = w;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int h) {
        if (h < DEFAULT_PANEL_HEIGHT && h > 0)
            height = h;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color fillColor) {
        color = fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void resetPanelSize(int w, int h) {
        panelWidth = w;
        panelHeight = h;
    }

    /*
     * Inner class
     * ===================================================================== Inner
     * class
     * MovingPath : The superclass of all paths. It is an inner class.
     * A path can change the current position of the shape.
     * =============================================================================
     * ==
     */
    
    abstract class MovingPath {
        protected int deltaX, deltaY; // moving distance

        public MovingPath(int dx, int dy) {
            deltaX = dx;
            deltaY = dy;
        }

        public MovingPath() {
        }

        public abstract void move();

        public String toString() {
            return getClass().getSimpleName();
        };
    }

    class BouncingPath extends MovingPath {
        public BouncingPath(int dx, int dy) {
            super(dx, dy);
        }

        public void move() {
            x = x + deltaX;
            y = y + deltaY;
            if ((x < 0) && (deltaX < 0)) {
                deltaX = -deltaX;
                x = 0;
            } else if ((x + width > panelWidth) && (deltaX > 0)) {
                deltaX = -deltaX;
                x = panelWidth - width;
            }
            if ((y < 0) && (deltaY < 0)) {
                deltaY = -deltaY;
                y = 0;
            } else if ((y + height > panelHeight) && (deltaY > 0)) {
                deltaY = -deltaY;
                y = panelHeight - height;
            }
        }
    }

    class DownRightPath extends MovingPath {
        public static final int INTERVAL = 5;
        private int count = 0;

        public DownRightPath(int dx, int dy) {
            super(dx, dy);
        }

        public void move() {
            if (count > INTERVAL) {
                x += deltaX;
                if ((x + width > panelWidth) && (deltaX > 0))
                    x = 0;
                count = 0;
            } else {
                y += deltaY;
                if ((y + height > panelHeight) && (deltaY > 0))
                    y = 0;
                count += 1;
            }
        }
    }
}