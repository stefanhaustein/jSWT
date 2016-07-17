package org.eclipse.swt.widgets;

import android.graphics.*;
import android.graphics.Canvas;
import org.eclipse.swt.graphics.GC;

public class AndroidGC extends GC {
    final Paint backgroundPaint = new Paint();
    final Paint foregroundPaint = new Paint();
    final Canvas canvas;

    public AndroidGC(android.graphics.Canvas canvas) {
        super(null);
        this.canvas = canvas;
        backgroundPaint.setStyle(Paint.Style.FILL);
        foregroundPaint.setStyle(Paint.Style.STROKE);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, foregroundPaint);
    }



}
