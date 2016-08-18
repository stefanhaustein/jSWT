package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Color;

import java.awt.*;


class SwingGC extends GC {
    final java.awt.Graphics2D graphics;
    Color foreground;
    Color background;
    Color current;

    int lineCap = SWT.CAP_FLAT;
    int lineJoin = SWT.JOIN_BEVEL;
    int lineWidth = 1;

    SwingGC(java.awt.Graphics2D graphics) {
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
    public Color getBackground() {
        return background;
    }

    @Override
    public Color getForeground() {
        return foreground;
    }

    @Override
    public void setAntialias(int antialias) {
        if (antialias == SWT.ON) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else if (antialias == SWT.OFF) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }

    @Override
    public void setBackground(Color color) {
        background = color;
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
    public void setLineWidth(int width) {
        if (width != lineWidth) {
            lineWidth = width;
            updateStroke();
        }
    }

    public void setLineCap(int cap) {
        if (cap != lineCap) {
            lineCap = cap;
            updateStroke();
        }
    }

    public void setLineJoin(int join) {
        if (join != lineJoin) {
            lineJoin = join;
            updateStroke();
        }
    }

    public Point stringExtent(String string) {
        FontMetrics metrics = graphics.getFontMetrics();
        return new Point(metrics.stringWidth(string), metrics.getHeight());
    }

    private void updateStroke() {
        graphics.setStroke(new BasicStroke(lineWidth,
                lineCap == SWT.CAP_ROUND ? BasicStroke.CAP_ROUND :
                        lineCap == SWT.CAP_SQUARE ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                lineJoin == SWT.JOIN_MITER ? BasicStroke.JOIN_MITER :
                        lineJoin == SWT.JOIN_ROUND ? BasicStroke.JOIN_ROUND : BasicStroke.JOIN_BEVEL));
    }

    private void useBackgroundColor() {
        if (background != current) {
            graphics.setColor(background == null ? java.awt.Color.WHITE
                    : new java.awt.Color(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha()));
            current = background;
        }
    }

    private void useForegroundColor() {
        if (foreground != current) {
            graphics.setColor(foreground == null ? java.awt.Color.BLACK
                    : new java.awt.Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue()));
            current = foreground;
        }
    }
}
