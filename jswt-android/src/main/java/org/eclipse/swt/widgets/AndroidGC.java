package org.eclipse.swt.widgets;

import android.graphics.*;
import android.graphics.Canvas;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;

public class AndroidGC extends GC {
    final Paint backgroundPaint = new Paint();
    final Paint foregroundPaint = new Paint();
    Path path;
    RectF rect = new RectF();
    final Canvas canvas;

    public AndroidGC(android.graphics.Canvas canvas) {
        super(null);
        this.canvas = canvas;
        backgroundPaint.setStyle(Paint.Style.FILL);
        foregroundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, foregroundPaint);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
        canvas.drawOval(rect, foregroundPaint);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        canvas.drawRect(x, y, x + width, y + height, foregroundPaint);
    }

    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
        canvas.drawRoundRect(rect, arcWidth, arcHeight, foregroundPaint);
    }

    @Override
    public void drawText(String string, int x, int y, int flags) {
        canvas.drawText(string, x, y - foregroundPaint.ascent(), foregroundPaint);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
        canvas.drawOval(rect, backgroundPaint);
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
        canvas.drawRect(rect, backgroundPaint);
    }

    @Override
    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        rect.left = x;
        rect.top = y;
        rect.right = x + width;
        rect.bottom = y + height;
        canvas.drawRoundRect(rect, arcWidth, arcHeight, backgroundPaint);
    }

    @Override
    public void fillPolygon(int [] points) {
        if (points.length < 2) {
            return;
        }
        if (path == null) {
            path = new Path();
        } else {
            path.reset();
        }
        int n = points.length;
        path.moveTo(points[0], points[1]);
        for (int i = 2; i < n; i += 2) {
            path.lineTo(points[i], points[i + 1]);
        }
        canvas.drawPath(path, backgroundPaint);
    }


    @Override
    public void setFont(Font font) {
        FontData fd = font.getFontData()[0];
        foregroundPaint.setTextSize(fd.getHeight());
    }


    @Override
    public void setForeground(Color color) {
        foregroundPaint.setColor(0x0ff000000 | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue());
    }

    @Override
    public void setBackground(Color color) {
        backgroundPaint.setColor(0x0ff000000 | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue());
    }




}
