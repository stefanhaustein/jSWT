package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;


public class AwtGC extends GC {
    final java.awt.Graphics graphics;
    Color foreground;
    Color background;
    Color current;

    AwtGC(java.awt.Graphics graphics) {
        super(null);
        this.graphics = graphics;
    }

    @Override
    public void setForeground(Color color) {
        foreground = color;
    }

    @Override
    public void setBackground(Color color) {
        background = color;
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (foreground != current) {
            graphics.setColor(new java.awt.Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue()));
            current = foreground;
        }
        graphics.drawLine(x1, y1, x2, y2);
    }
}
