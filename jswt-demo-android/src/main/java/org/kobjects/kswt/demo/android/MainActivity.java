package org.kobjects.kswt.demo.android;

import android.os.Bundle;

import android.view.WindowManager;
import org.eclipse.swt.examples.controlexample.ControlExample;

import org.eclipse.swt.widgets.SwtActivity;

public class MainActivity extends SwtActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ControlExample.start(getDisplay());
    }

}
