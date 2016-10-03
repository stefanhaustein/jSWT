package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Rectangle;

public class Monitor {

    private final Rectangle bounds;
    private final Rectangle clientArea;

    Monitor(Rectangle bounds, Rectangle clientArea) {
        this.bounds = bounds;
        this.clientArea = clientArea;
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getClientArea() {
        return new Rectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
    }
}
