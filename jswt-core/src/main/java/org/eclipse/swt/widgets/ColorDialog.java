package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.kobjects.jswt.Callback;
import org.kobjects.jswt.CallbackDialog;

public class ColorDialog extends Dialog implements CallbackDialog<RGB> {

    RGB rgb;

    public ColorDialog(Shell parent) {
        super(parent);
    }


    @Override
    public void open(final Callback<RGB> callback) {
        final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Choose Color");

        shell.setLayout(new GridLayout());

        final Scale red = new Scale(shell, SWT.NONE);
        red.setMaximum(255);
        final Scale green = new Scale(shell, SWT.NONE);
        green.setMaximum(255);
        final Scale blue = new Scale(shell, SWT.NONE);
        blue.setMaximum(255);

        Composite buttonPanel = new Composite(shell, SWT.NONE);
        buttonPanel.setLayout(new RowLayout());

        final Button cancelButton = new Button(buttonPanel, SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                callback.cancel();
            }
        });

        final Button okButton = new Button(buttonPanel, SWT.PUSH);
        okButton.setText("Ok");
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                callback.run(new RGB(
                        red.getSelection(),
                        green.getSelection(),
                        blue.getSelection()));
            }
        });

        shell.pack();
        shell.open();
    }

    public RGB open() {
        throw new UnsupportedOperationException();
    }

    public void setRGB(RGB rgb) {
        this.rgb = rgb;
    }

}
