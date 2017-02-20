package org.eclipse.swt.widgets;

public class Decorations extends Canvas {

  Menu menuBar;
  String text;

  public Decorations(Composite parent, int style) {
    super(parent, style);
  }

  public void setMenuBar(Menu menuBar) {
    this.menuBar = menuBar;
    display.updateMenuBar(this);
  }

  public Menu getMenuBar() {
    return menuBar;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    display.setText(this, text);
  }

}
