package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public class Style extends JavaScriptObject {

    protected Style() {
    }

    public native final void setVisibility(String visibility) /*-{
        this.visibility = visibility;
    }-*/;

    public native final String getVisibility() /*-{
        return this.visibility;
    }-*/;

    public native final void setDisplay(String value) /*-{
        this.display = value;
    }-*/;

    public native final String getDisplay() /*-{
        return this.display;
    }-*/;
}
