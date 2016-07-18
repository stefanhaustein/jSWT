package org.kobjects.jswt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class JswtDemo {


    public static void main (String [] args) {
        Display display = new Display();

        Shell shell = run(display);

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    public static Shell run(Display display) {
        Shell shell = new Shell (display);
        shell.setText("jSWT Demo");

        new DemoCanvas(shell);

        shell.setLayout (new FillLayout(SWT.HORIZONTAL));
        shell.pack ();
        shell.open ();
        return shell;
    }

}
