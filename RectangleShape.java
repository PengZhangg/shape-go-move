/*
 *	===============================================================================
 *	RectangleShape.java : A shape that is a rectangle.
 *  YOUR UPI: PZHA619
 *	=============================================================================== */

import java.awt.*;

class RectangleShape extends Shape {
	public RectangleShape(int x, int y, int w, int h, int panelWidth, int panelHeight, Color fillColor,
			Color borderColor, PathType pathType) {
		super(x, y, w, h, panelWidth, panelHeight, fillColor, borderColor, pathType);
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(borderColor);
		g.drawRect(x, y, width, height);
	}
}