package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;

public class Text extends Control {
  public Text(Composite parent, int style) {
    super(parent, style);
  }

  public void addModifyListenr(ModifyListener modifyListener) {
    addListener(SWT.Modify, new TypedListener(modifyListener));
  }

  ControlType getControlType() {
    return ControlType.TEXT;
  }

  public String getText() {
    return display.getText(this);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

}
