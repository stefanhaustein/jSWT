package org.eclipse.swt.widgets;

import android.content.Context;
import android.view.View;

public class SwtCanvasView extends View {

    Canvas swtCanvas;

    public SwtCanvasView(Context context, Canvas swtCanvas) {
        super(context);
        this.swtCanvas = swtCanvas;
    }

    @Override
    public void onDraw(android.graphics.Canvas canvas) {
        swtCanvas.drawBackground(new AndroidGC(canvas), 0, 0, 999999, 99999);
    }
}
