package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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

  /**
   * Clears any data that has been cached by a Layout for all widgets that
   * are in the parent hierarchy of the changed control up to and including the
   * receiver.  If an ancestor does not have a layout, it is skipped.
   *
   * @param changed an array of controls that changed state and require a recalculation of size
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li>
   *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 3.1
   */
  public void changed(Control[] changed) {
    checkWidget ();
    if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
    for (int i=0; i<changed.length; i++) {
      Control control = changed [i];
      if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
      if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
      boolean ancestor = false;
      Composite composite = (Composite) control.parent;
      while (composite != null) {
        ancestor = composite == this;
        if (ancestor) break;
        composite = (Composite) composite.parent;
      }
      if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
    }
    for (int i=0; i<changed.length; i++) {
      Control child = changed [i];
      Composite composite = (Composite) child.parent;
      while (child != this) {
        if (composite.layout == null || !composite.layout.flushCache (child)) {
          composite.state |= LAYOUT_CHANGED;
        }
        child = composite;
        composite = (Composite) child.parent;
      }
    }
  }

  @Override
  public Point computeSize(int wHint, int hHint, boolean changed) {
    checkWidget ();

    if (this instanceof TabFolder) {
      return super.computeSize(wHint, hHint, changed);
    }

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
    System.out.println(this + ".computeSize(" + wHint + ", " + hHint + "): " + trim.width + "x" + trim.height);

    return new Point (trim.width, trim.height);
  }

  Control[] _getChildren() {
    return children == null ? EMPTY_CONTROL_ARRAY :
        children.toArray(new Control[children.size()]);
  }

  ControlType getControlType() {
    return this instanceof ScrolledComposite ? ControlType.SCROLLED_COMPOSITE : ControlType.COMPOSITE;
  }

  public Control[] getChildren() {
    checkWidget();
    return _getChildren();
  }

  public Layout getLayout() {
    checkWidget();
    return layout;
  }

  public void layout() {
    layout(true, false);
  }

  public void layout(boolean changed) {
    layout(changed, false);
  }

  public void layout (boolean changed, boolean all) {
    checkWidget ();
    if (layout == null && !all) return;
    markLayout (changed, all);
    updateLayout (all);
  }

  @Override
  void markLayout (boolean changed, boolean all) {
    if (layout != null) {
      state |= LAYOUT_NEEDED;
      if (changed) state |= LAYOUT_CHANGED;
    }
    if (all) {
      Control [] children = _getChildren ();
      for (int i=0; i<children.length; i++) {
        children [i].markLayout (changed, all);
      }
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
  // Called from dispose
  void removeChild(Widget widget) {
    children.remove(widget);
    if (widget instanceof Control) {
      display.removeChild(this, (Control) widget);
    }
  }
  public void setLayout(Layout layout) {
    this.layout = layout;
  }

  @Override
  void updateLayout (boolean all) {
   /* Composite parent = findDeferredControl ();
    if (parent != null) {
      parent.state |= LAYOUT_CHILD;
      return;
    } */
    if ((state & LAYOUT_NEEDED) != 0) {
      boolean changed = (state & LAYOUT_CHANGED) != 0;
      state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
      // display.runSkin();
      layout.layout (this, changed);
    }
    if (all) {
      state &= ~LAYOUT_CHILD;
      Control [] children = _getChildren ();
      for (Control child: children) {
        child.updateLayout (all);
      }
    }
  }

}
