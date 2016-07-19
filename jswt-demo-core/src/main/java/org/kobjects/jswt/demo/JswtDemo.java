package org.kobjects.jswt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class JswtDemo {

    static int clickCount = 0;

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

        Composite leftBar = new Composite(shell, 0);
        leftBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

        RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        rowLayout.fill = true;
        leftBar.setLayout(rowLayout);

        final Label label = new Label(leftBar, 0);
        label.setText("Label");
        Text text = new Text(leftBar, 0);
        Button button = new Button(leftBar, 0);
        button.setText("Button");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                label.setText("Click " + ++clickCount);
            }
        });

        DemoCanvas demoCanvas = new DemoCanvas(shell);
        demoCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true /* expand horizontally */, true /* expand vertically */));

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;

        shell.setLayout (gridLayout);
        shell.pack ();
        shell.open ();
        return shell;
    }

}
