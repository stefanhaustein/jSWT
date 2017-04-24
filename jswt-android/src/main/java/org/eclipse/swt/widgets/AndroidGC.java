package org.eclipse.swt.widgets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;

public class AndroidGC extends GC {

    static android.graphics.Path getAndroidPath(Path path) {
        if (path.peer == null) {
            android.graphics.Path path2D = new android.graphics.Path();
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
                        path2D.close();
                        break;
                    case SWT.PATH_CUBIC_TO:
                        path2D.cubicTo(points[j], points[j + 1], points[j + 2], points[j + 3], points[j + 4], points[j + 5]);
                        j += 6;
                        break;
                    case SWT.PATH_QUAD_TO:
                        path2D.quadTo(points[j], points[j + 1], points[j + 2], points[j + 3]);
                        j += 4;
                        break;
                }
            }
        }
        return (android.graphics.Path) path.peer;
    }

    final Paint backgroundPaint = new Paint();
    final Paint foregroundPaint = new Paint();
    final Paint textPaint = new Paint();
    android.graphics.Path path;
    RectF rectF = new RectF();
    Rect rect = new Rect();
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();
    final Canvas canvas;
    Color foregroundColor;
    Color backgroundColor;
    Matrix matrix;
    float[] swtElements;
    float[] androidElements;


    public AndroidGC(Device device, android.graphics.Canvas canvas) {
        super(device);
        this.canvas = canvas;
        backgroundPaint.setStyle(Paint.Style.FILL);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStyle(Paint.Style.FILL);
        setForeground(getDevice().getSystemColor(SWT.COLOR_WHITE));
        setBackground(getDevice().getSystemColor(SWT.COLOR_BLACK));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, foregroundPaint);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawArc(rectF, startAngle, arcAngle, false, foregroundPaint);
    }


    @Override
    public void drawImage(Image image, int x, int y) {
        canvas.drawBitmap((Bitmap) image.peer, x, y, null);
    }

    @Override
    public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = destX;
        dstRect.top = destY;
        dstRect.right = destX + destWidth;
        dstRect.bottom = destY + destHeight;

        canvas.drawBitmap((Bitmap) image.peer, srcRect, dstRect, null);
    }


    @Override
    public void drawOval(int x, int y, int width, int height) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawOval(rectF, foregroundPaint);
    }

    @Override
    public void drawPath(Path path) {
        canvas.drawPath(getAndroidPath(path), foregroundPaint);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        canvas.drawRect(x, y, x + width, y + height, foregroundPaint);
    }

    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawRoundRect(rectF, arcWidth, arcHeight, foregroundPaint);
    }

    @Override
    public void drawText(String string, int x, int y, int flags) {
        canvas.drawText(string, x, y - textPaint.ascent(), textPaint);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawArc(rectF, startAngle, arcAngle, true, backgroundPaint);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawOval(rectF, backgroundPaint);
    }

    @Override
    public void fillPath(Path path) {
        canvas.drawPath(getAndroidPath(path), backgroundPaint);
    }


    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawRect(rectF, backgroundPaint);
    }

    @Override
    public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + width;
        rectF.bottom = y + height;
        canvas.drawRoundRect(rectF, arcWidth, arcHeight, backgroundPaint);
    }

    @Override
    public void fillPolygon(int [] points) {
        if (points.length < 2) {
            return;
        }
        if (path == null) {
            path = new android.graphics.Path();
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
    public int getAlpha() {
        return foregroundPaint.getAlpha();
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }

    @Override
    public Color getForeground() {
        return foregroundColor;
    }

    @Override
    public FontMetrics getFontMetrics() {
        Paint.FontMetrics androidMetrics = textPaint.getFontMetrics();
        FontMetrics result = new FontMetrics(
                Math.round(-androidMetrics.ascent),
                Math.round(androidMetrics.descent),
                Math.round(textPaint.measureText(" ")),
                Math.round(Math.abs(androidMetrics.leading)),
                Math.round((androidMetrics.bottom - androidMetrics.top)));
        return result;
    }

    @Override
    public void getTransform(Transform transform) {
        if (swtElements == null) {
            transform.identity();
        } else {
            transform.setElements(swtElements[0], swtElements[1], swtElements[2], swtElements[3], swtElements[4], swtElements[5]);
        }
    }

    @Override
    public void setFont(Font font) {
        FontData fd = font.getFontData()[0];
        textPaint.setTextSize(fd.getHeight());
    }

    @Override
    public void setForeground(Color color) {
        foregroundColor = color;
        int argb = (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
        foregroundPaint.setColor(argb);
        textPaint.setColor(argb);
    }

    @Override
    public void setBackground(Color color) {
        backgroundColor = color;
        backgroundPaint.setColor((color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue());
    }

    @Override
    public void setLineCap(int lineCap) {
        switch (lineCap) {
            case SWT.CAP_FLAT:
                foregroundPaint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case SWT.CAP_ROUND:
                foregroundPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case SWT.CAP_SQUARE:
                foregroundPaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            default:
                System.err.println("setLineCap(): Unrecognized value: " + lineCap);
        }
    }

    @Override
    public void setLineJoin(int lineJoin) {
        switch (lineJoin) {
            case SWT.JOIN_BEVEL:
                foregroundPaint.setStrokeJoin(Paint.Join.BEVEL);
                break;
            case SWT.JOIN_MITER:
                foregroundPaint.setStrokeJoin(Paint.Join.MITER);
                break;
            case SWT.JOIN_ROUND:
                foregroundPaint.setStrokeJoin(Paint.Join.ROUND);
                break;
            default:
                System.err.println("setLineJoin(): Unrecognized value: " + lineJoin);
        }
    }

    @Override
    public void setLineWidth(int lineWidth) {
        foregroundPaint.setStrokeWidth(lineWidth);
    }

    @Override
    public void setAlpha(int alpha) {
        foregroundPaint.setAlpha(alpha);
        backgroundPaint.setAlpha(alpha);
    }

    @Override
    public void setAntialias(int value) {
        boolean aa = value != SWT.OFF;
        foregroundPaint.setAntiAlias(aa);
        backgroundPaint.setAntiAlias(aa);
        textPaint.setAntiAlias(aa);
    }

    public Point textExtent(String text, int flags) {
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return new Point(rect.width(), textPaint.getFontMetricsInt(null));
    }

    @Override
    public void setTransform(Transform transform) {
        if (matrix == null) {
            matrix = new Matrix();
            swtElements = new float[6];
            androidElements = new float[9];
            androidElements[Matrix.MPERSP_2] = 1;
        } else {
            canvas.restore();
        }
        canvas.save();
        transform.getElements(swtElements);
        androidElements[Matrix.MSCALE_X] = swtElements[0];
        androidElements[Matrix.MSKEW_X] = swtElements[1];
        androidElements[Matrix.MSCALE_Y] = swtElements[2];
        androidElements[Matrix.MSKEW_Y] = swtElements[3];
        androidElements[Matrix.MTRANS_X] = swtElements[4];
        androidElements[Matrix.MTRANS_Y] = swtElements[5];
        matrix.setValues(androidElements);
        canvas.concat(matrix);
    }

}
