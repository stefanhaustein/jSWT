package org.kobjects.jswt.demo;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class OperationCanvas extends Canvas {
    public OperationCanvas(Composite parent) {
        super(parent, 0);
    }

    @Override
    public void drawBackground(GC gc, int x, int y, int width, int height) {
        gc.setForeground(new Color(getDisplay(), 255, 0, 0));
        for (int i = 0; i < 1000; i += 25) {
            gc.drawLine(0, i, 10000, i);
            gc.drawLine(i, 0, i, 10000);
        }
    }

}
