package org.kobjects.jswt.demo.gwt;

import com.google.gwt.core.client.EntryPoint;
import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.widgets.GwtDisplay;



public class GwtDemo implements EntryPoint {

    public void onModuleLoad() {
        GwtDisplay.log("A");
        ControlExample.start(new GwtDisplay());
        GwtDisplay.log("B");
    }
}
