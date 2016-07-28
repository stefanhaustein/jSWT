package org.kobjects.jswt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class JswtDemo {


    public static void main (String [] args) {
        Display display = new Display();

        JswtDemo jswtDemo = new JswtDemo(display);

        while (!jswtDemo.shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    final Display display;
    final Shell shell;
    final GridLayout mainLayout;
    final GridLayout controlLayout = new GridLayout(1, true);
    final GridData scrolledCompositeGridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    int clickCount = 0;

    public JswtDemo(Display display) {
        this.display = display;
        shell = new Shell (display);
        shell.setText("jSWT Demo");

        ScrolledComposite scrolledComposite = new ScrolledComposite(shell, 0);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        Composite controlComposite = new Composite(scrolledComposite, 0);
        scrolledComposite.setContent(controlComposite);
        scrolledComposite.setLayoutData(scrolledCompositeGridData);

        controlComposite.setLayout(controlLayout);

        final Label label = new Label(controlComposite, 0);
        label.setText("Label");
        label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        final Label label2 = new Label(controlComposite, 0);
        label2.setText("Another label");
        label2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Text text = new Text(controlComposite, 0);
        text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        Button button = new Button(controlComposite, SWT.PUSH);
        button.setText("Button");
        button.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                label.setText("Click " + ++clickCount);
            }
        });

        new Button(controlComposite, SWT.RADIO).setText("Radio 1");
        new Button(controlComposite, SWT.RADIO).setText("Radio 2");
        new Button(controlComposite, SWT.RADIO).setText("Radio 3");
        new Button(controlComposite, SWT.CHECK).setText("Checkbox");

        Slider slider = new Slider(controlComposite, SWT.HORIZONTAL);
        slider.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        DemoCanvas demoCanvas = new DemoCanvas(shell);
        demoCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true /* expand horizontally */, true /* expand vertically */));

        mainLayout = new GridLayout(2, false);
        mainLayout.marginWidth = 0;
        mainLayout.marginHeight = 0;

        Menu menuBar = new Menu(shell);
        MenuItem fileMenuItem = new MenuItem(menuBar, SWT.DROP_DOWN);
        fileMenuItem.setText("File");
        Menu fileMenu = new Menu(fileMenuItem);

        MenuItem aboutItem = new MenuItem(fileMenu, 0);
        aboutItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell dialogShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                dialogShell.setLayout(new RowLayout());
                dialogShell.setText("Alert");
                Label label = new Label(dialogShell, 0);
                label.setText("About dialog text");
                Button button = new Button(dialogShell, 0);
                button.setText("Ok");
                dialogShell.pack();
                dialogShell.open();
            }
        });

        aboutItem.setText("About");
        MenuItem openItem = new MenuItem(fileMenu, 0);
        openItem.setText("Open");
        openItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("OPEN");
            }
        });

        shell.addControlListener(new ControlListener() {
            @Override
            public void controlMoved(ControlEvent e) {
            }

            @Override
            public void controlResized(ControlEvent e) {
                adjustLayout();
            }
        });

        shell.setMenuBar(menuBar);

        shell.setLayout (mainLayout);
        shell.pack ();

        shell.setBounds(100, 100, shell.getSize().x * 2, shell.getSize().y);

        shell.open ();
    }

    void adjustLayout() {
        Rectangle bounds = shell.getClientArea();
        if (bounds.width > bounds.height) {
            mainLayout.numColumns = 2;
            controlLayout.numColumns = 1;
            scrolledCompositeGridData.grabExcessHorizontalSpace = false;
            scrolledCompositeGridData.grabExcessVerticalSpace = true;
        } else {
            mainLayout.numColumns = 1;
            controlLayout.numColumns = 2;
            scrolledCompositeGridData.grabExcessHorizontalSpace = true;
            scrolledCompositeGridData.grabExcessVerticalSpace = false;
        }
        shell.layout();
    }



}
