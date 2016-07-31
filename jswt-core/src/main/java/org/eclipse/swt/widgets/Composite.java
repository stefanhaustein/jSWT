package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

public class Composite extends Scrollable {
  static final Control[] EMPTY_CONTROL_ARRAY = {};
  Layout layout;
  ArrayList<Control> children = new ArrayList<>();

  public Composite(Composite parent, int style) {
    super(parent, style);
  }

  public void layout() {
    layout(true);
  }

  public void layout(boolean changed) {
    if (layout != null) {
      layout.layout(this, true);
    }
  }

  public void setLayout(Layout layout) {
    this.layout = layout;
  }


  public Point computeSize(int wHint, int hHint, boolean changed) {
    checkWidget ();
    //display.runSkin();
    if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
    if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
    Point size;
    if (layout != null) {
      if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
        changed |= (state & LAYOUT_CHANGED) != 0;
        size = layout.computeSize (this, wHint, hHint, changed);
        state &= ~LAYOUT_CHANGED;
      } else {
         size = new Point (wHint, hHint);
      }
    } else {
      size = minimumSize (wHint, hHint, changed);
      if (size.x == 0) size.x = DEFAULT_WIDTH;
      if (size.y == 0) size.y = DEFAULT_HEIGHT;
    }
    if (wHint != SWT.DEFAULT) size.x = wHint;
    if (hHint != SWT.DEFAULT) size.y = hHint;
    Rectangle trim = computeTrim (0, 0, size.x, size.y);
    display.setMeasuredSize(this, trim.width, trim.height);
    return new Point (trim.width, trim.height);
  }

  Control[] _getChildren() {
    return children == null ? EMPTY_CONTROL_ARRAY :
        children.toArray(new Control[children.size()]);
  }


  public Control[] getChildren() {
    checkWidget();
    return _getChildren();
  }

  // Called from dispose
  void removeChild(Widget widget) {
    children.remove(widget);
    if (widget instanceof Control) {
      display.removeChild(this, (Control) widget);
    }
  }


  Point minimumSize (int wHint, int hHint, boolean changed) {
    Control [] children = _getChildren ();
    Rectangle clientArea = getClientArea ();
    int width = 0, height = 0;
    for (int i=0; i<children.length; i++) {
      Rectangle rect = children [i].getBounds ();
      width = Math.max (width, rect.x - clientArea.x + rect.width);
      height = Math.max (height, rect.y - clientArea.y + rect.height);
    }
    return new Point (width, height);
  }

}
