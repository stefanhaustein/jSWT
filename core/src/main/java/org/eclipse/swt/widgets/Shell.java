package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Rectangle;
import org.kobjects.jswt.Insets;
import org.kobjects.jswt.JswtDisplay;

public class Shell extends Decorations {

  public Shell(Display display) {
    this(display, 0);
  }

  public Shell(Display display, int style) {
    super(null, style);
    this.display = (JswtDisplay) display;
    this.peer = this.display.createPeer(this);
  }


  public Rectangle computeTrim (int x, int y, int width, int height) {
    checkWidget();
    Rectangle trim = super.computeTrim (x, y, width, height);
    Insets insets = display.getInsets(this, peer);
    trim.x -= insets.left;
    trim.y -= insets.top;
    trim.width += insets.left + insets.right;
    trim.height += insets.top + insets.bottom;
    return trim;
  }

  @Override
  public Rectangle getClientArea() {
    Rectangle bounds = getBounds();
    Insets insets = display.getInsets(this, peer);
    bounds.x = insets.left;
    bounds.y = insets.top;
    bounds.width -= insets.left + insets.right;
    bounds.height -= insets.top + insets.bottom;
    return bounds;
  }


  public void open() {
    display.openShell(this, peer);
  }

  public void pack() {
    display.pack(this, peer);
  }

  public void setDefaultButton(Button button) {
  }
}
