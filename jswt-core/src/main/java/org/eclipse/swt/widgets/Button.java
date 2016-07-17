package org.eclipse.swt.widgets;

import org.eclipse.swt.events.SelectionListener;

public class Button extends Control {
  public Button(Composite parent, int style) {
    super(parent, style);
  }


  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

  public void addSelectionListener(SelectionListener listener) {
  }
}
