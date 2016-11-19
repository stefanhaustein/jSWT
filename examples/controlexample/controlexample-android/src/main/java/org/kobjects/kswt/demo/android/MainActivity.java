package org.kobjects.kswt.demo.android;

import android.os.Bundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.examples.controlexample.ControlExample;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.SwtActivity;

public class MainActivity extends SwtActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shell shell = new Shell(getDisplay(), SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());
        shell.setText(ControlExample.getResourceString("window.title"));
        ControlExample instance = new ControlExample(shell);
        ControlExample.setShellSize(instance, shell);
        shell.open();
    }

}
