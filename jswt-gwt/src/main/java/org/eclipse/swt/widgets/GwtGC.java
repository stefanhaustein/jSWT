package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.kobjects.dom.CanvasRenderingContext2D;
import org.kobjects.dom.Element;


public class GwtGC extends GC {

    CanvasRenderingContext2D ctx;
    Color foreground;
    Color background;
    int lineWidth;
    int lineCap;
    int lineJoin;


    public GwtGC(Element canvas) {
        super(null);
        ctx = canvas.getContext2d();
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.lineTo(x2, y2);
        ctx.stroke();
    }

    private CanvasRenderingContext2D roundRect(int x, int y, int width, int height, int rx, int ry) {
        ctx.beginPath();
        ctx.moveTo(x + rx, y);
        ctx.lineTo(x + width - rx, y);
        ctx.quadraticCurveTo(x + width, y, x + width, y + ry);
        ctx.lineTo(x + width, y + height - ry);
        ctx.quadraticCurveTo(x + width, y + height, x + width - rx, y + height);
        ctx.lineTo(x + rx, y + height);
        ctx.quadraticCurveTo(x, y + height, x, y + height - ry);
        ctx.lineTo(x, y + ry);
        ctx.quadraticCurveTo(x, y, x + rx, y);
        ctx.closePath();
        return ctx;
    }

    private CanvasRenderingContext2D ellipse(int x, int y, int w, int h) {
        double kappa = .5522848,
                ox = (w / 2) * kappa, // control point offset horizontal
                oy = (h / 2) * kappa, // control point offset vertical
                xe = x + w,           // x-end
                ye = y + h,           // y-end
                xm = x + w / 2,       // x-middle
                ym = y + h / 2;       // y-middle

        ctx.beginPath();
        ctx.moveTo(x, ym);
        ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
        ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
        ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
        ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
        return ctx;
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        ellipse(x, y, width, height).stroke();
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        ctx.strokeRect(x, y, width, height);
    }

    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        roundRect(x, y, width, height, arcWidth, arcHeight).stroke();
    }

    @Override
    public void drawText(String string, int x, int y, int flags) {
//        graphics.drawString(string, x, y + graphics.getFontMetrics().getAscent());
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        ellipse(x, y, width, height).fill();
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        ctx.fillRect(x, y, width, height);
    }

    @Override
    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        roundRect(x, y, width, height, arcWidth, arcHeight).fill();
    }

    @Override
    public void fillPolygon(int [] points) {
        ctx.moveTo(points[0], points[1]);
        for (int i = 0; i < points.length; i += 2) {
            ctx.lineTo(points[i], points[i + 1]);
        }
        ctx.closePath();
        ctx.fill();
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
        // FIXME
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
    public void setFont(org.eclipse.swt.graphics.Font font) {
        // FIXME
    }

    @Override
    public void setLineWidth(int width) {
        if (width != lineWidth) {
            // FIXME
        }
    }

    public void setLineCap(int cap) {
        if (cap != lineCap) {
// FIXME
        }
    }

    public void setLineJoin(int join) {
        if (join != lineJoin) {
            lineJoin = join;     // FIXME
        }
    }

    public Point stringExtent(String text) {
        return new Point(Math.round((float) ctx.measureText(text)), 20);  // FIXME
    }

}
