package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Rectangle;

public class Shell extends Decorations {

  public Shell(Display display) {
    this(display, 0);
  }

  public Shell(Display display, int style) {
    super(null, style);
    this.display = (PlatformDisplay) display;
    this.peer = this.display.createPeer(this);
  }


  public Rectangle computeTrim (int x, int y, int width, int height) {
    checkWidget();
    Rectangle trim = super.computeTrim (x, y, width, height);
    PlatformDisplay.Insets insets = display.getInsets(this);
    trim.x -= insets.left;
    trim.y -= insets.top;
    trim.width += insets.left + insets.right;
    trim.height += insets.top + insets.bottom;
    return trim;
  }

  @Override
  public Rectangle getClientArea() {
    Rectangle bounds = getBounds();
    PlatformDisplay.Insets insets = display.getInsets(this);
    bounds.x = insets.left;
    bounds.y = insets.top;
    bounds.width -= insets.left + insets.right;
    bounds.height -= insets.top + insets.bottom;
    return bounds;
  }


  public void open() {
    display.openShell(this);
  }

  public void pack() {
    display.pack(this);
  }

  public void setDefaultButton(Button button) {
  }
}
