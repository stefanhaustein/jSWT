package org.eclipse.swt.widgets;

public class Label extends Control {
  public Label(Composite parent, int style) {
    super(parent, style);
  }

  public String getText() {
    return display.getText(this);
  }

  ControlType getControlType() {
    return ControlType.LABEL;
  }


    public void setText(String text) {
    display.setText(this, text);
  }
}
