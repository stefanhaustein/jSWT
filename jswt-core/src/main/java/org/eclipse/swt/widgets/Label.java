package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class Label extends Control {
    private Image image;
  String text;
    public Label(Composite parent, int style) {
    super(parent, style);
  }

    public Image getImage() {
      return image;
    }

    public String getText() {
    return text;
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
      this.text = text;
      display.setText(this, text);
    }
}
