package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class Button extends Control {
  public Button(Composite parent, int style) {
    super(parent, style);
  }

  public void addSelectionListener(final SelectionListener listener) {
    addListener(SWT.Selection, new TypedListener(listener));
  }

  public boolean getSelection() {
    return display.getSelection(this);
  }

  public String getText() {
    return display.getText(this);
  }

  public void setSelection(boolean selected) {
    display.setSelection(this, selected);
  }

  public void setText(String text) {
    display.setText(this, text);
  }
}
