package org.kobjects.kswt.demo.android;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.AndroidDisplay;

import org.eclipse.swt.widgets.SwtActivity;
import org.kobjects.jswt.demo.JswtDemo;

public class MainActivity extends SwtActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new JswtDemo(getDisplay());
    }

}
