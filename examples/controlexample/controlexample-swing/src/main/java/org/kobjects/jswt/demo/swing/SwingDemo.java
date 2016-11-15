package org.kobjects.jswt.demo.swing;

import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.SwingDisplay;

import javax.swing.UIManager;

public class SwingDemo {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Display display = new SwingDisplay();
        ControlExample.start(display);
    }
}
