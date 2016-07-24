package org.eclipse.swt.widgets;

public class Decorations extends Canvas {

  Menu menuBar;

  public Decorations(Composite parent, int style) {
    super(parent, style);
  }

  public void setMenuBar(Menu menuBar) {
    this.menuBar = menuBar;
    display.updateMenuBar(this);
  }

  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

}
