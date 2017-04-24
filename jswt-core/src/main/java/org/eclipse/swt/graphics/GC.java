package org.eclipse.swt.graphics;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.PlatformDisplay;



public class GC extends Resource {
    GC delegate;

    protected GC(Device device) {
        super(device);
    }

    public GC(Image image) {
        super (image.device);
        this.delegate = ((PlatformDisplay) device).createGCForPlatformImage(image.peer);
    }

    public void dispose() {
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        delegate.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawImage(Image image, int x, int y) {
        delegate.drawImage(image, x, y);
    }

    public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight,
                                       int dstX, int dstY, int dstWidth, int dstHeight) {
        delegate.drawImage(image, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        delegate.drawLine(x1, y1, x2, y2);
    }

    public void drawPath(Path path) {
        delegate.drawPath(path);
    }

    public void drawPoint(int x, int y) {
        drawLine(x, y, x, y);
    }

    public void drawString(String string, int x, int y, boolean isTransparent) {
        drawText(string, x, y, isTransparent ? SWT.DRAW_TRANSPARENT : 0);
    }

    public void drawString(String string, int x, int y) {
        drawText(string, x, y, 0);
    }

    public void drawText(String string, int x, int y, boolean isTransparent) {
        drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB | (isTransparent ? SWT.DRAW_TRANSPARENT : 0));
    }

    public void drawText(String string, int x, int y) {
        drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB );
    }

    public void drawText(String string, int x, int y, int flags) {
        delegate.drawText(string, x, y, flags);
    }

    public void drawOval(int x, int y, int width, int height) {
        delegate.drawOval(x, y, width, height);
    }

    public void drawPolygon(int[] pointArray) {
        drawPolyline(pointArray);
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
        delegate.fillOval(x, y, width, height);
    }

    public void fillPath(Path path) {
        delegate.fillPath(path);
    }

    public void fillPolygon(int[] pointArray) {
        delegate.fillPolygon(pointArray);
    }

    public void fillRectangle(int x, int y, int width, int height) {
        delegate.fillRectangle(x, y, width, height);
    }

    public void fillRectangle(Rectangle rect) {
        fillRectangle(rect.x, rect.y, rect.width, rect.height);
    }

    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        delegate.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
    }

    public int getAlpha() {
        return delegate.getAlpha();
    }

    public Color getBackground() {
        return delegate.getBackground();
    }

    public Color getForeground() {
        return delegate.getForeground();
    }

    public Font getFont() {
        return delegate.getFont();
    }

    public FontMetrics getFontMetrics() {
        return delegate.getFontMetrics();
    }

    public void getTransform(Transform transform) {
        delegate.getTransform(transform);
    }

    public void setAlpha(int alpha) {
        delegate.setAlpha(alpha);
    }

    public void setBackground(Color color) {
        delegate.setBackground(color);
    }

    public void setForeground(Color color) {
        delegate.setForeground(color);
    }

    public void setFont(Font font) {
        delegate.setFont(font);
    }

    public void setAntialias(int antialias) {
        delegate.setAntialias(antialias);
    }

    public void setLineCap(int cap) {
        delegate.setLineCap(cap);
    }

    public void setLineJoin(int join) {
        delegate.setLineJoin(join);
    }

    public void setLineWidth(int width) {
        delegate.setLineWidth(width);
    }

    public void setTransform(Transform transform) {
        delegate.setTransform(transform);
    }

    public Point stringExtent(String text) {
        return textExtent(text, 0);
    }

    public Point textExtent(String text) {
        return textExtent(text, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
    }

    public Point textExtent(String text, int flags) {
        return delegate.textExtent(text, flags);
    }
}

