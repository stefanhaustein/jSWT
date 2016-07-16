package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Rectangle;

public class Scrollable extends Control {
  public Scrollable(Composite parent, int style) {
    super(parent, style);
  }

  public Rectangle computeTrim(int x, int y, int width, int height) {
    int border = 0;
    //if (fixedHandle != 0) border += OS.gtk_container_get_border_width (fixedHandle);
    //if (scrolledHandle != 0) border += OS.gtk_container_get_border_width (scrolledHandle);
    int trimX = x - border, trimY = y - border;
    int trimWidth = width + (border * 2), trimHeight = height + (border * 2);
    /*
    trimHeight += hScrollBarWidth ();
    trimWidth  += vScrollBarWidth ();
    if (scrolledHandle != 0) {
    if (OS.gtk_scrolled_window_get_shadow_type (scrolledHandle) != OS.GTK_SHADOW_NONE) {
        119			Point thickness = getThickness (scrolledHandle);
        120			int xthickness = thickness.x;
        121			int ythickness = thickness.y;
        122			trimX -= xthickness;
        123			trimY -= ythickness;
        124			trimWidth += xthickness * 2;
        125			trimHeight += ythickness * 2;
        126		}
      }
      */
    return new Rectangle (trimX, trimY, trimWidth, trimHeight);
  }

  public Rectangle getClientArea() {
    /*
    checkWidget ();
    forceResize ();
    int clientHandle = clientHandle ();
    GtkAllocation allocation = new GtkAllocation ();
    gtk_widget_get_allocation (clientHandle, allocation);
    int x = allocation.x;
    int y = allocation.y;
    int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
    int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
    return new Rectangle (x, y, width, height);
    */

    // Dummy for now;
    Rectangle bounds = getBounds();
    bounds.x = 0;
    bounds.y = 0;
    return bounds;
  }
}
