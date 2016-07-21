package org.eclipse.swt.widgets;


public class Shell extends Decorations {

  public Shell(Display display) {
    this(display, 0);
  }

  public Shell(Display display, int style) {
    super(null, style);
    this.display = (PlatformDisplay) display;
    this.peer = this.display.createControl(this);
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
