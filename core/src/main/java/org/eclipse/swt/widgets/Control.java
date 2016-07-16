package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.kobjects.jswt.JswtDisplay;

public class Control extends Widget {

  Object layoutData;
  Object peer;
  JswtDisplay display;

  public Control(Composite parent, int style) {
    super(parent, style);
    if (parent != null) {
      this.display = parent.display;
      this.peer = display.createPeer(this);
      parent.children.add(this);
      display.addChild(parent, parent.peer, this, this.peer);
    }
  }

  public Point computeSize(int wHint, int hHint) {
    return computeSize(wHint, hHint, true);
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {
    if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
    if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
    if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
      return new Point(wHint, hHint);
    }
    return display.computeSize(this, peer, wHint, hHint, changed);
  }

  public int getBorderWidth() {
    return 0;
  }

  public Rectangle getBounds() {
    return display.getBounds(this, peer);
  }

  public Object getLayoutData() {
    return layoutData;
  }

  public void setBounds(int x, int y, int width, int height) {
    display.setBounds(this, peer, x, y, width, height);
  }

  public void setBounds(Rectangle rect) {
    setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  public void setLayoutData(Object layoutData) {
    this.layoutData = layoutData;
  }

}
