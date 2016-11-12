package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.kobjects.swt.Promise;
import org.kobjects.swt.PromiseDialog;

public class ColorDialog extends Dialog implements PromiseDialog<RGB> {

    RGB rgb;

    public ColorDialog(Shell parent) {
        super(parent);
    }

    @Override
    public Promise<RGB> openPromise() {
        final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Choose Color");

        GridLayout gridlayout = new GridLayout(2, false);
        gridlayout.marginHeight = 0;
        gridlayout.marginWidth = 0;
        shell.setLayout(gridlayout);

        new Label(shell, SWT.NONE).setText("Red");
        final Scale red = new Scale(shell, SWT.NONE);
        red.setMaximum(255);

        new Label(shell, SWT.NONE).setText("Green");
        final Scale green = new Scale(shell, SWT.NONE);
        green.setMaximum(255);

        new Label(shell, SWT.NONE).setText("Blue");
        final Scale blue = new Scale(shell, SWT.NONE);
        blue.setMaximum(255);

        Composite buttonPanel = new Composite(shell, SWT.NONE);
        GridData panelData = new GridData();
        panelData.horizontalSpan = 2;
        panelData.horizontalAlignment = SWT.RIGHT;
        buttonPanel.setLayoutData(panelData);
        buttonPanel.setLayout(new RowLayout());

        final Promise<RGB> result = new Promise();

        final Button cancelButton = new Button(buttonPanel, SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
                result.reject(null);
            }
        });

        final Button okButton = new Button(buttonPanel, SWT.PUSH);
        okButton.setText("Ok");
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
                result.resolve(new RGB(
                        red.getSelection(),
                        green.getSelection(),
                        blue.getSelection()));
            }
        });

        shell.pack();
        shell.open();

        return result;
    }

    public RGB open() {
        throw new UnsupportedOperationException("Use org.kobjects.jswt.Dialogs.openColorDialog() instead.");
    }

    public void setRGB(RGB rgb) {
        this.rgb = rgb;
    }

}
