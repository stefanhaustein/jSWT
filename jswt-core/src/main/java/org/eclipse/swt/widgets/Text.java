package org.eclipse.swt.widgets;

public class Text extends Control {
  public Text(Composite parent, int style) {
    super(parent, style);
  }

  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }
}
