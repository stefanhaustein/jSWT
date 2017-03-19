package org.eclipse.swt.graphics;

public class Cursor extends Resource {

    private final int style;
    public Cursor(Device device, int style) {
        super(device);
        this.style = style;
    }
}
