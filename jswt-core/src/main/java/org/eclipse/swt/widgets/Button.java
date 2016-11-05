package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;

public class Button extends Control {

  static final int ALIGNMENT_MASK = SWT.LEFT|SWT.RIGHT|SWT.UP|SWT.DOWN|SWT.CENTER;
  Image image;
    String text;

  public Button(Composite parent, int style) {
    super(parent, style);
    setAlignment(style & ALIGNMENT_MASK);
  }

  public void addSelectionListener(final SelectionListener listener) {
    addListener(SWT.Selection, new TypedListener(listener));
  }

  ControlType getControlType() {
    if ((style & SWT.CHECK) != 0) {
      return ControlType.BUTTON_CHECK;
    }
    if ((style & SWT.RADIO) != 0) {
      return ControlType.BUTTON_RADIO;
    }
    if ((style & SWT.TOGGLE) != 0) {
      return ControlType.BUTTON_TOGGLE;
    }
    if ((style & SWT.ARROW) != 0) {
      return ControlType.BUTTON_ARROW;
    }
    return ControlType.BUTTON;
  }

  public boolean getSelection() {
    return display.getSelection(this) != 0;
  }

  public String getText() {
      return text;
  }

  public void removeSelectionListener(SelectionListener listener) {
    removeListener(SWT.Selection, listener);
  }

  public void setSelection(boolean selected) {
    display.setSelection(this, selected ? 1 : 0);
  }

  public void setText(String text) {
        this.text = text;
      display.setText(this, text);
  }

  public void setImage(Image image) {
    this.image = image;
    display.setImage(this, image);
  }

  public void setAlignment(int alignment) {
    alignment &= ALIGNMENT_MASK;
    display.setAlignment(this, alignment);
    style = (style & ~ALIGNMENT_MASK) | alignment;
  }

  public String toString() {
    return super.toString() + ":" + getText();
  }

  public Image getImage() {
    return image;
  }
}
