package org.kobjects.jswt.demo.swing;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.SwingDisplay;

import org.kobjects.jswt.demo.JswtDemo;

public class SwingDemo {
  public static void main(String[] args) {
    Display display = new SwingDisplay();
    new JswtDemo(display);

  }
}
