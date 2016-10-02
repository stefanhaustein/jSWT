package org.kobjects.jswt.demo.swing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.SwingDisplay;

import org.kobjects.jswt.demo.JswtDemo;

public class SwingDemo {
  public static void main(String[] args) {
    Display display = new SwingDisplay();
    // new JswtDemo(display);

    Shell shell = new Shell(display, SWT.SHELL_TRIM);
    shell.setLayout(new FillLayout());
    ControlExample instance = new ControlExample(shell);
    shell.setText(ControlExample.getResourceString("window.title"));
    ControlExample.setShellSize(instance, shell);
    shell.open();
  }
}
