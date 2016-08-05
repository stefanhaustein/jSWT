package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class Control extends Widget {

  Object layoutData;
  Object peer;

  public Control(Composite parent, int style) {
    super(parent, style);
    if (parent != null) {
      this.peer = display.createControl(this);
      parent.children.add(this);
      if (!(parent instanceof ScrolledComposite)) {
        display.addChild(parent, this);
      }
    }
  }

  public void addControlListener(ControlListener controlListener) {
    TypedListener typedListener = new TypedListener(controlListener);
    addListener(SWT.Resize, typedListener);
    addListener(SWT.Move, typedListener);
  }

  public void addMouseListener(MouseListener mouseListener) {
    TypedListener typedListener = new TypedListener(mouseListener);
    addListener(SWT.MouseUp, typedListener);
    addListener(SWT.MouseDown, typedListener);
    addListener(SWT.MouseDoubleClick, typedListener);
  }

  public void addMouseMoveListener(MouseMoveListener mouseMoveListener) {
    addListener(SWT.MouseMove, new TypedListener(mouseMoveListener));
  }

  public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
    addListener(SWT.MouseWheel, new TypedListener(mouseWheelListener));
  }

  public Point computeSize(int wHint, int hHint) {
    return computeSize(wHint, hHint, true);
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {
    Point size;
    if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
    if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
    if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
      size = new Point(wHint, hHint);
    } else {
      size = display.computeSize(this, wHint, hHint, changed);
    }
    display.setMeasuredSize(this, size.x, size.y);
    System.out.println(this + ".computeSize(" + wHint + ", " + hHint + "): " + size);
    return size;
  }

  public int getBorderWidth() {
    return 0;
  }

  public Rectangle getBounds() {
    // Find a way to improve this, e.g. hand in an object that is shared in some way
    return display.getBounds(this);
  }

  public Point getSize() {
    Rectangle bounds = getBounds();
    return new Point(bounds.width, bounds.height);
  }

  public Object getLayoutData() {
    return layoutData;
  }

  void markLayout (boolean changed, boolean all) {
	/* Do nothing */
  }

  public void redraw() {
    Rectangle bounds = getBounds();
    redraw(0, 0, bounds.width, bounds.height, true);
  }

  public void redraw(int x, int y, int w, int h, boolean all) {
    display.redraw(this, x, y, w, h, all);
  }


  public void setBounds(int x, int y, int width, int height) {
    System.out.println(this + ".setBounds(" + x + ", " + y + ", " + width + ", " + height);
    display.setBounds(this, x, y, width, height);
  }

  public void setBounds(Rectangle rect) {
    setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  public void setLayoutData(Object layoutData) {
    this.layoutData = layoutData;
  }

  public void setLocation(int x, int y) {
    Rectangle bounds = getBounds();
    setBounds(x, y, bounds.width, bounds.height);
  }

  public void setLocation(Point p) {
    setLocation(p.x, p.y);
  }

  public void setSize(int width, int height) {
    Rectangle bounds = getBounds();
    setBounds(bounds.x, bounds.y, width, height);
  }

  public void setSize(Point p) {
    setSize(p.x, p.y);
  }

  public String toString() {
    return this.getClass().getName() + "; peer: " + peer;
  }


  void updateLayout (boolean all) {
	/* Do nothing */
  }

}
