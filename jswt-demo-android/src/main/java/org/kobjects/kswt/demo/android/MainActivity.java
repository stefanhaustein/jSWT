package org.kobjects.kswt.demo.android;

import android.app.Activity;
import android.os.Bundle;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.AndroidDisplay;

import org.kobjects.jswt.demo.JswtDemo;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = new AndroidDisplay(this);
        //HelloWorld.setup(new AndroidDisplay(this));
        JswtDemo.run(display);
    }

}
