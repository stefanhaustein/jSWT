package org.eclipse.swt.graphics;


public class GC {
    int antialias;
    GC delegate;

    public GC(Image image) {
    }

    public void setBackground(Color color) {
        delegate.setBackground(color);
    }

    public void setForeground(Color color) {
        delegate.setForeground(color);
    }

    public void setAntialias(int antialias) {
        delegate.setAntialias(antialias);
    }

    public void drawImage(Image original, int i, int i1, int i2, int i3, int i4, int i5, int round, int round1) {
        delegate.drawImage(original, i, i1, i2, i3, i4, i5, round, round1);
    }

    public void dispose() {
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        delegate.drawLine(x1, y1, x2, y2);
    }


}

