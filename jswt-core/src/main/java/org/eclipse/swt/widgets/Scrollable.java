package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Rectangle;

public class Scrollable extends Control {
  public Scrollable(Composite parent, int style) {
    super(parent, style);
  }

  public Rectangle computeTrim (int x, int y, int width, int height) {
    checkWidget();
    Rectangle trim = new Rectangle(x, y, width, height);
    PlatformDisplay.Insets insets = display.getInsets(this);
    trim.x -= insets.left;
    trim.y -= insets.top;
    trim.width += insets.left + insets.right;
    trim.height += insets.top + insets.bottom;
    return trim;
  }

  public Rectangle getClientArea() {
    Rectangle bounds = getBounds();
    PlatformDisplay.Insets insets = display.getInsets(this);
    bounds.x = insets.left;
    bounds.y = insets.top;
    bounds.width -= insets.left + insets.right;
    bounds.height -= insets.top + insets.bottom;
    return bounds;
  }


    public ScrollBar getHorizontalBar() {
      return null;
    }

  public ScrollBar getVerticalBar() {
    return null;
  }

  public void scroll(int x, int cy, int cx, int cy1, int maxX, int maxY, boolean b) {
  }
}
