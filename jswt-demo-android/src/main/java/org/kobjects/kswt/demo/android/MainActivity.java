package org.kobjects.kswt.demo.android;

import android.app.Activity;
import android.os.Bundle;

import org.eclipse.swt.snippets.Snippet108;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.AndroidDisplay;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = new AndroidDisplay(this);
        //HelloWorld.setup(new AndroidDisplay(this));
        Snippet108.run(display);
    }

}
