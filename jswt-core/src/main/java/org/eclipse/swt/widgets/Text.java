package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;

public class Text extends Control {
  public Text(Composite parent, int style) {
    super(parent, style);
  }

  public void append(String text) {
    setText(getText() + text);
  }

  public void addKeyListener(KeyListener listener) {
    addListener(SWT.KeyDown, new TypedListener(listener));
    addListener(SWT.KeyUp, new TypedListener(listener));
  }

  public void addModifyListener(ModifyListener listener) {
    addListener(SWT.Modify, new TypedListener(listener));
  }

  public void copy() {
    System.err.println("FIXME: Text.copy()");  // FIXME
  }

  public void cut() {
    System.err.println("FIXME: Text.cut()");  // FIXME
  }


  ControlType getControlType() {
    return ControlType.TEXT;
  }

  public String getText() {
    return display.getText(this);
  }

  public void paste() {
    System.err.println("FIXME: Text.paste()");  // FIXME
  }

    public void selectAll() {
        System.err.println("FIXME: Text.selectAll()");  // FIXME
    }

  public void setText(String text) {
    display.setText(this, text);
  }

}
