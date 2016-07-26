package org.kobjects.jswt.demo.awt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.AwtDisplay;

import org.kobjects.jswt.demo.JswtDemo;

public class AwtDemo {
  public static void main(String[] args) {
    Display display = new AwtDisplay();
    new JswtDemo(display);

  }
}
