package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class Shell extends Decorations {

  public Shell(Shell parent) {
    this(parent.display, parent, SWT.DIALOG_TRIM);
  }

  public Shell(Shell parent, int style) {
    this(parent.display, parent, style);
  }

  public Shell(Display display) {
    this(display, null, SWT.SHELL_TRIM);
  }

  public Shell(Display display, int style) {
    this(display, null, style);
  }

  private Shell(Display display, Shell parent, int style) {
    super(null, style);  // parent is still set below.
    this.display = (PlatformDisplay) display;
    this.parent = parent;
    this.peer = this.display.createControl(this);
  }

  ControlType getControlType() {
    return ControlType.SHELL;
  }

  public void open() {
    display.openShell(this);
  }

  public void setDefaultButton(Button button) {
  }

    public void setFullScreen(boolean fullScreen) {
      System.err.println("FIXME: Shell.setFullScreen()");
    }

  public void setImage(Image image) {
    display.setImage(this, image);
  }
}
