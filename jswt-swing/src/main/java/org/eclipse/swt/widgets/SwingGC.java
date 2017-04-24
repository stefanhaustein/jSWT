package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.eclipse.swt.graphics.Transform;


class SwingGC extends GC {

    static Path2D getPath2D(Path path) {
        if (path.peer == null) {
            Path2D path2D = new Path2D.Float();
            path.peer = path2D;
            PathData pathData = path.getPathData();
            int j = 0;
            float[] points = pathData.points;
            for (int i = 0; i < pathData.types.length; i++) {
                switch (pathData.types[i]) {
                    case SWT.PATH_MOVE_TO:
                        path2D.moveTo(points[j], points[j + 1]);
                        j += 2;
                        break;
                    case SWT.PATH_LINE_TO:
                        path2D.lineTo(points[j], points[j + 1]);
                        j += 2;
                        break;
                    case SWT.PATH_CLOSE:
                        path2D.closePath();
                        break;
                    case SWT.PATH_CUBIC_TO:
                        path2D.curveTo(points[j], points[j + 1], points[j + 2], points[j + 3], points[j + 4], points[j + 5]);
                        j += 6;
                        break;
                    case SWT.PATH_QUAD_TO:
                        path2D.quadTo(points[j], points[j + 1], points[j + 2], points[j + 3]);
                        j += 4;
                        break;
                }
            }
        }
        return (Path2D) path.peer;
    }


    final java.awt.Graphics2D graphics;
    org.eclipse.swt.graphics.Color foreground;
    Color background;
    Color current;
    Font font;

    int lineCap = SWT.CAP_FLAT;
    int lineJoin = SWT.JOIN_BEVEL;
    int lineWidth = 1;
    int alpha = 255;

    SwingGC(PlatformDisplay display, Graphics2D graphics) {
        super(display);
        this.graphics = graphics;
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        useForegroundColor();
        graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawImage(org.eclipse.swt.graphics.Image image, int x, int y) {
        graphics.drawImage((BufferedImage) image.peer, x, y, null /* observer */);
    }

    public void drawImage(org.eclipse.swt.graphics.Image image, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight) {
/*
        int dx1,
        int dy1,
        int dx2,
        int dy2,
        int sx1,
        int sy1,
        int sx2,
        int sy2,
  */
        graphics.drawImage((BufferedImage) image.peer,
                dstX, dstY, dstX + dstWidth, dstY + dstHeight,
                srcX, srcY, srcX + srcWidth, srcY + srcHeight,  null);
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
    public void drawPath(Path path) {
        useForegroundColor();
        graphics.draw(getPath2D(path));
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

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        useBackgroundColor();
        graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        useBackgroundColor();
        graphics.fillOval(x, y, width, height);
    }

    @Override
    public void fillPath(Path path) {
        useBackgroundColor();
        graphics.fill(getPath2D(path));
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
    public int getAlpha() {
        return alpha;
    }

    @Override
    public void getTransform(Transform transform) {
        AffineTransform affineTransform = graphics.getTransform();
        transform.setElements(
                (float) affineTransform.getScaleX(), (float) affineTransform.getShearX(),
                (float) affineTransform.getShearY(), (float) affineTransform.getScaleY(),
                (float) affineTransform.getTranslateX(), (float) affineTransform.getTranslateY());
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public FontMetrics getFontMetrics() {
        java.awt.FontMetrics awtMetrics = graphics.getFontMetrics();
        FontMetrics result = new FontMetrics(awtMetrics.getAscent(), awtMetrics.getDescent(), awtMetrics.charWidth(' '), awtMetrics.getLeading(),
                awtMetrics.getHeight());
        return result;
    }

    @Override
    public Color getForeground() {
        return foreground;
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != this.alpha) {
            this.alpha = alpha;
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha/255f));
        }
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
        if (font != this.font) {
            this.font = font;
            FontData fd = font.getFontData()[0];
            java.awt.Font awtFont = graphics.getFont().deriveFont((float) fd.getHeight());
            graphics.setFont(awtFont);
        }
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

    public void setTransform(Transform transform) {
        float[] elements = new float[6];
        transform.getElements(elements);
        AffineTransform affineTransform = new AffineTransform(
                elements[0], elements[2], elements[1], elements[3], elements[4], elements[5]);
        graphics.setTransform(affineTransform);
    }

    public Point textExtent(String string, int flags) {
        java.awt.FontMetrics metrics = graphics.getFontMetrics();
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
                    : new java.awt.Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(), foreground.getAlpha()));
            current = foreground;
        }
    }
}
