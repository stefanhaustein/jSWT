package org.eclipse.swt.widgets;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.kobjects.swt.Promise;
import org.kobjects.swt.PromiseDialog;

public class FileDialog extends Dialog implements PromiseDialog<String> {
    enum Mode {
        SAVE, OPEN_ONE, OPEN_MULTIPLE
    }

    private final Table table;
    private final Shell shell;
    private final Combo pathCombo;
    private final SelectionListener pathSelectionListener;
    private final Mode mode;
    private final Text fileNameInput;

    File directory;
    Promise<String> promise;

    public FileDialog(Shell parent, int style) {
        super(parent);
        mode = (style & SWT.SAVE) != 0 ? Mode.SAVE : (style & SWT.MULTI) != 0 ? Mode.OPEN_MULTIPLE : Mode.OPEN_ONE;

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setLayout(new GridLayout(2, false));
        switch (mode) {
            case SAVE:
                shell.setText("Save File");
                break;
            case OPEN_MULTIPLE:
                shell.setText("Open Files");
                break;
            default:
                shell.setText("Open File");
                break;
        }


        if (mode == Mode.SAVE) {
            new Label(shell, SWT.NONE).setText("Save:");
            fileNameInput = new Text(shell, SWT.NONE);
            fileNameInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        } else {
            fileNameInput = null;
        }

        new Label(shell, SWT.NONE).setText("Path:");

        RowLayout pathLayout = new RowLayout();
        pathLayout.spacing = 0;
        Composite pathComposite = new Composite(shell, SWT.NONE);
        pathComposite.setLayout(pathLayout);
        pathComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        pathCombo = new Combo(pathComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        pathSelectionListener = new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int up = pathCombo.getItemCount() - 1 - pathCombo.getSelectionIndex();
                System.out.println("itemCount: " + pathCombo.getItemCount() + " sel idx: " + pathCombo.getSelectionIndex() + " up: " + up + " current: " + directory);
                File current = directory;
                while (up > 0 && current.getParentFile() != null){
                    current = current.getParentFile();
                    up--;
                }
                setDirectory(current);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        };

        table = new Table(shell, mode == Mode.OPEN_MULTIPLE ? SWT.CHECK : SWT.NONE);
        GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        tableGridData.minimumHeight = parent.getSize().y / 2;
        table.setLayoutData(tableGridData);

        table.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem selected = table.getItem(table.getSelectionIndex());
                File file = (File) selected.getData();
                if (file.isDirectory()) {
                    setDirectory(file);
                } else if (mode == Mode.OPEN_ONE) {
                    shell.dispose();
                     promise.resolve(file.getAbsolutePath());
                } else if (mode == Mode.SAVE) {
                    fileNameInput.setText(file.getName());
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        setDirectory(new File(System.getProperty("user.dir")));

        Composite buttonPanel = new Composite(shell, SWT.NONE);
        buttonPanel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
        buttonPanel.setLayout(new RowLayout());
        final Button cancelButton = new Button(buttonPanel, SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
                promise.reject(null);
            }
        });

        if (mode != Mode.OPEN_ONE) {
            final Button okButton = new Button(buttonPanel, SWT.PUSH);
            okButton.setText("Ok");
            okButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    if (mode == Mode.SAVE) {
                        shell.dispose();
                        promise.resolve(new File(directory, fileNameInput.getText()).getAbsolutePath());
                    } else {
                        throw new RuntimeException("NYI");
                    }
                }
            });
        }
    }

    private void setDirectory(File directory) {
        if (directory == this.directory) {
            return;
        }
        this.directory = directory;

        pathCombo.removeSelectionListener(pathSelectionListener);
        pathCombo.removeAll();
        File current = directory;
        do {
            String name = current.getName();
            pathCombo.add(name.isEmpty() ? "<root>" : name, 0);
            current = current.getParentFile();
        } while (current != null);
        pathCombo.select(pathCombo.getItemCount() - 1);
        pathCombo.addSelectionListener(pathSelectionListener);

        table.removeAll();

        for (File file: directory.listFiles()) {
            TableItem item = new TableItem(table, SWT.NONE);

            item.setText(0, file.getName());
            item.setData(file);
        }
    }


    public void setFilterExtensions(String[] strings) {
    }

    public void setFilterNames(String[] strings) {
    }

    public String open() {
        throw new RuntimeException("Use Dialogs.openFileDialog instead.");
    }

    public Promise<String> openPromise() {
        promise = new Promise<>();
        shell.pack();
        shell.open();
        return promise;
    }


    public String getFileName() {
        throw new RuntimeException("NYI");
    }

    public String getFilterPath() {
        throw new RuntimeException("NYI");
    }
}
