package org.eclipse.swt.widgets;

import android.content.Context;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;

class AndroidCanvas extends AndroidCompositeView {

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

        Event event = new Event();
        event.gc = gc;
        event.setBounds(new Rectangle(0, 0, getWidth(), getHeight()));
        swtCanvas.notifyListeners(SWT.Paint, event);

    }
}
