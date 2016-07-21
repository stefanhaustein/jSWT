package org.eclipse.swt.widgets;

public class Decorations extends Canvas {

  Menu menuBar;

  public Decorations(Composite parent, int style) {
    super(parent, style);
  }

  public void setMenuBar(Menu menu) {
    this.menuBar = menuBar;
    display.setMenuBar(this, menu);
  }

  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

}
