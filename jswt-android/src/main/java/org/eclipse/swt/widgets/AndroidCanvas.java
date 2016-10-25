package org.eclipse.swt.widgets;

import android.content.Context;

class AndroidCanvas extends AndroidComposite {

    Canvas swtCanvas;

    public AndroidCanvas(Context context, Canvas swtCanvas) {
        super(context, swtCanvas);
        this.swtCanvas = swtCanvas;
        this.setWillNotDraw(false);
    }

    @Override
    public void onDraw(android.graphics.Canvas canvas) {
        AndroidGC gc = new AndroidGC(swtCanvas.getDisplay(), canvas);
        swtCanvas.drawBackground(gc, 0, 0, 999999, 99999);
    }
}
