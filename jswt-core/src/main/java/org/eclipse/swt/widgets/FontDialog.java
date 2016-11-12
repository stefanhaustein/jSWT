package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.kobjects.swt.Promise;
import org.kobjects.swt.PromiseDialog;

public class FontDialog extends Dialog implements PromiseDialog<FontData> {

    private FontData[] fontData;

    public FontDialog(Shell parent) {
        super(parent);
    }

    public FontData open() {
        throw new UnsupportedOperationException("Use org.kobjects.Dialogs.openColorDialog()");
    }

    public Promise<FontData> openPromise() {
        final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Select Font");

        GridLayout gridlayout = new GridLayout(2, false);
        gridlayout.marginHeight = 0;
        gridlayout.marginWidth = 0;
        shell.setLayout(gridlayout);

        new Label(shell, SWT.NONE).setText("Name");
        final Text nameText = new Text(shell, SWT.SINGLE);

        new Label(shell, SWT.NONE).setText("Height");
        final Text heightText = new Text(shell, SWT.SINGLE);

        new Label(shell, SWT.NONE).setText("Style");
        final Combo styleCombo = new Combo(shell, SWT.NONE);
        styleCombo.setItems("Normal", "Bold", "Italic", "Bold Italic");

        if (fontData != null && fontData.length > 0) {
            nameText.setText(fontData[0].getName());
            heightText.setText("" + fontData[0].getHeight());
        }

        Composite buttonPanel = new Composite(shell, SWT.NONE);
        GridData panelData = new GridData();
        panelData.horizontalSpan = 2;
        panelData.horizontalAlignment = SWT.RIGHT;
        buttonPanel.setLayoutData(panelData);
        buttonPanel.setLayout(new RowLayout());

        final Promise result = new Promise();

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
            public void widgetSelected(SelectionEvent event) {
                String name = nameText.getText();
                int height = 12;
                try {
                    height = Integer.parseInt(heightText.getText());
                } catch (NumberFormatException e) {
                    //
                }
                int style;
                switch (styleCombo.getSelectionIndex()) {
                    case 1:
                        style = SWT.BOLD;
                        break;
                    case 2:
                        style = SWT.ITALIC;
                        break;
                    case 3:
                        style = SWT.BOLD | SWT.ITALIC;
                        break;
                    default:
                        style = SWT.NONE;
                }
                shell.dispose();
                result.resolve(new FontData(name, height, style));
            }
        });
        shell.pack();
        shell.open();

        return result;
    }


    public void setFontList(FontData[] fontData) {
        this.fontData = fontData;
    }

}
