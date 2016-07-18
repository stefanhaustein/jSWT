package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;


class AwtGC extends GC {
    final java.awt.Graphics graphics;
    Color foreground;
    Color background;
    Color current;

    AwtGC(java.awt.Graphics graphics) {
        super(null);
        this.graphics = graphics;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        useForegroundColor();
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        useForegroundColor();
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        useForegroundColor();
        graphics.drawRect(x, y, width, height);
    }

    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        useForegroundColor();
        graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawText(String string, int x, int y, int flags) {
        useForegroundColor();
        graphics.drawString(string, x, y + graphics.getFontMetrics().getAscent());
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        useBackgroundColor();
        graphics.fillOval(x, y, width, height);
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        useBackgroundColor();
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        useBackgroundColor();
        graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillPolygon(int [] points) {
        int[] xpoints = new int[points.length / 2];
        int[] ypoints = new int[points.length / 2];
        int n = xpoints.length;
        int p = 0;
        for (int i = 0; i < n; i++) {
            xpoints[i] = points[p++];
            ypoints[i] = points[p++];
        }
        useBackgroundColor();
        graphics.fillPolygon(xpoints, ypoints, n);
    }

    @Override
    public void setForeground(Color color) {
        foreground = color;
    }

    @Override
    public void setFont(Font font) {
        FontData fd = font.getFontData()[0];
        java.awt.Font awtFont = graphics.getFont().deriveFont((float) fd.getHeight());
        graphics.setFont(awtFont);
    }


    @Override
    public void setBackground(Color color) {
        background = color;
    }

    private void useBackgroundColor() {
        if (background != current) {
            graphics.setColor(new java.awt.Color(background.getRed(), background.getGreen(), background.getBlue()));
            current = background;
        }
    }

    private void useForegroundColor() {
        if (foreground != current) {
            graphics.setColor(new java.awt.Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue()));
            current = foreground;
        }
    }
}
