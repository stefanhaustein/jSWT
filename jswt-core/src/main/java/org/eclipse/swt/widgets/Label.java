package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Image;

public class Label extends Control {
    private Image image;
    public Label(Composite parent, int style) {
    super(parent, style);
  }

    public Image getImage() {
      return image;
    }

    public String getText() {
    return display.getText(this);
  }

    ControlType getControlType() {
    return ControlType.LABEL;
  }

    public void setAlignment(int alignment) {
      display.setAlignment(this, alignment);
    }

    public void setImage(Image image) {
      this.image = image;
      display.setImage(this, image);
    }

    public void setText(String text) {
      display.setText(this, text);
    }
}
