package org.eclipse.swt.widgets;

public class Decorations extends Canvas {

  public Decorations(Composite parent, int style) {
    super(parent, style);

  }

  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

}
