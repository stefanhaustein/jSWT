package org.kobjects.jswt.demo.gwt;

import com.google.gwt.core.client.EntryPoint;
import org.eclipse.swt.widgets.GwtDisplay;
import org.kobjects.jswt.demo.JswtDemo;

public class GwtDemo implements EntryPoint {

    public void onModuleLoad() {
        new JswtDemo(new GwtDisplay());
    }
}
