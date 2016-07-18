package org.eclipse.swt.graphics;


public class GC {
    int antialias;
    GC delegate;

    public GC(Image image) {
    }


    public void dispose() {
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        delegate.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawImage(Image original, int i, int i1, int i2, int i3, int i4, int i5, int round, int round1) {
        delegate.drawImage(original, i, i1, i2, i3, i4, i5, round, round1);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        delegate.drawLine(x1, y1, x2, y2);
    }

    public void drawPoint(int x, int y) {
        drawLine(x, y, x, y);
    }

    public void drawOval(int x, int y, int width, int height) {
        delegate.drawOval(x, y, width, height);
    }

    public void drawPolygon(int[] pointArray) {
        drawPolygon(pointArray);
        drawLine(pointArray[0], pointArray[1],
                pointArray[pointArray.length - 2], pointArray[pointArray.length - 1]);
    }

    public void drawPolyline(int[] pointArray) {
        for (int i = 0; i < pointArray.length - 2; i += 2) {
            drawLine(pointArray[i], pointArray[i + 1], pointArray[i + 2], pointArray[i + 3]);
        }
    }

    public void drawRectangle(int x, int y, int width, int height) {
        delegate.drawRectangle(x, y, width, height);
    }

    public void drawRectangle(Rectangle rect) {
        drawRectangle(rect.x, rect.y, rect.width, rect.height);
    }

    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        delegate.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        delegate.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillOval(int x, int y, int width, int height) {
        delegate.drawOval(x, y, width, height);
    }

    public void fillPolygon(int[] pointArray) {
        delegate.fillPolygon(pointArray);
    }

    public void fillRectangle(int x, int y, int width, int height) {
        delegate.drawRectangle(x, y, width, height);
    }

    public void fillRectangle(Rectangle rect) {
        fillRectangle(rect.x, rect.y, rect.width, rect.height);
    }

    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        delegate.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
    }

    public void setBackground(Color color) {
        delegate.setBackground(color);
    }

    public void setForeground(Color color) {
        delegate.setForeground(color);
    }

    public void setAntialias(int antialias) {
        delegate.setAntialias(antialias);
    }


}

