package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;

public class Button extends Control {
  public Button(Composite parent, int style) {
    super(parent, style);
  }

  public void addSelectionListener(final SelectionListener listener) {
    addListener(SWT.Selection, new TypedListener(listener));
  }

  ControlType getControlType() {
    if ((style & SWT.CHECK) != 0) {
      return ControlType.BUTTON_CHECKBOY;
    }
    if ((style & SWT.RADIO) != 0) {
      return ControlType.BUTTON_RADIO;
    }
    return ControlType.BUTTON_PUSH;
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

  public void setImage(Image image) {
    display.setImage(this, image);
  }

  public void setAlignment(int alignment) {
    display.setAlignment(this, alignment);
  }
}
