package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;

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
    super(null, style);
    this.display = (PlatformDisplay) display;
    this.parent = parent;
    this.peer = this.display.createControl(this);
  }

  ControlType getControlType() {
    return parent == null ? ControlType.SHELL_ROOT : ControlType.SHELL_DIALOG;
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
