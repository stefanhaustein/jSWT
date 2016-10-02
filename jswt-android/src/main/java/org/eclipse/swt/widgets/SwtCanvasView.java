package org.eclipse.swt.widgets;

import android.content.Context;
import android.view.View;

public class SwtCanvasView extends SwtViewGroup {

    Canvas swtCanvas;

    public SwtCanvasView(Context context, Canvas swtCanvas) {
        super(context, swtCanvas);
        this.swtCanvas = swtCanvas;
        this.setWillNotDraw(false);
    }

    @Override
    public void onDraw(android.graphics.Canvas canvas) {
        swtCanvas.drawBackground(new AndroidGC(swtCanvas.getDisplay(), canvas), 0, 0, 999999, 99999);
    }
}
