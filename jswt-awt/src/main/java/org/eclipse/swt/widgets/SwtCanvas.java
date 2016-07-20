package org.eclipse.swt.widgets;

public class SwtCanvas extends java.awt.Container {
    final Canvas swtCanvas;

    public SwtCanvas(Canvas swtCanvas) {
        this.swtCanvas = swtCanvas;
    }

    public void paint(java.awt.Graphics g) {
        swtCanvas.drawBackground(new AwtGC(g), 0, 0, getWidth(), getHeight());
    }
}
