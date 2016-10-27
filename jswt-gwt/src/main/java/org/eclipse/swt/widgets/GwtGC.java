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
    Font font;


    public GwtGC(Element canvas) {
        super(null);
        ctx = canvas.getContext2d();
        ctx.setTextBaseline("top");
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        ctx.beginPath();
        int endAngle = startAngle + arcAngle;
        double startRad = startAngle * Math.PI / 180;
        double endRad = endAngle * Math.PI / 180;
        ctx.arc(x + width / 2, y + height / 2, Math.min(width, height) / 2, startRad, endRad, arcAngle < 0);
        ctx.stroke();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        ctx.beginPath();
        ctx.moveTo(x1 + 0.5, y1 + 0.5);
        ctx.lineTo(x2 + 0.5, y2 + 0.5);
        ctx.stroke();
    }

    private CanvasRenderingContext2D roundRect(double x, double y, double width, double height, double rx, double ry) {
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

    private CanvasRenderingContext2D ellipse(double x, double y, double w, double h) {
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
        ellipse(x + 0.5, y + 0.5, width, height).stroke();
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        ctx.strokeRect(x + 0.5, y + 0.5, width, height);
    }

    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        roundRect(x + 0.5, y + 0.5, width, height, arcWidth, arcHeight).stroke();
    }

    @Override
    public void drawText(String s, int x, int y, int flags) {
        Color savedBackground = background;
        setBackground(foreground);
        ctx.fillText(s, x, y);
//        graphics.drawString(string, x, y + graphics.getFontMetrics().getAscent());
        setBackground(savedBackground);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        ctx.beginPath();
        ctx.moveTo(x, y);
        int endAngle = startAngle + arcAngle;
        double startRad = startAngle * Math.PI / 180;
        double endRad = endAngle * Math.PI / 180;
        ctx.arc(x + width / 2, y + height / 2, Math.min(width, height) / 2, startRad, endRad, arcAngle < 0);
        ctx.fill();
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

    String colorString(Color color) {
        return color.getAlpha() == 255 ?
                ("rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")") :
                ("rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha()/255f+")");
    }

    @Override
    public void setBackground(Color color) {
        if (color != background) {
            background = color;
            ctx.setFillStyle(colorString(color));
        }
    }

    @Override
    public void setForeground(Color color) {
        if (color != foreground) {
            foreground = color;
            ctx.setStrokeStyle(colorString(color));
        }
    }

    @Override
    public void setFont(org.eclipse.swt.graphics.Font font) {
        if (font != this.font) {
            this.font = font;
            FontData fontData = font.getFontData()[0];
            StringBuilder sb = new StringBuilder();
            int style = fontData.getStyle();
            if ((style & SWT.ITALIC) != 0) {
                sb.append("italic ");
            }
            if ((style & SWT.BOLD) != 0) {
                sb.append("bold ");
            }
            sb.append(fontData.getHeight());
            sb.append("px ");
            sb.append(fontData.getName());
            ctx.setFont(sb.toString());
        }
    }

    @Override
    public void setLineWidth(int width) {
        if (width != lineWidth) {
            this.lineWidth = width;
            ctx.setLineWidth(width);
        }
    }

    public void setLineCap(int cap) {
        if (cap != lineCap) {
            this.lineCap = cap;
            ctx.setLineCap(cap == SWT.CAP_ROUND ? "round" : cap == SWT.CAP_SQUARE ? "square" : "butt");
        }
    }

    public void setLineJoin(int join) {
        if (join != lineJoin) {
            lineJoin = join;
            ctx.setLineJoin(join == SWT.JOIN_MITER ? "miter" : join == SWT.JOIN_ROUND ? "round" : "bevel");
        }
    }

    public Point textExtent(String text, int flags) {
        return new Point(Math.round((float) ctx.measureText(text)), font == null ? 10 : font.getFontData()[0].getHeight());
    }

}
