package org.kobjects.dom;


import com.google.gwt.core.client.JavaScriptObject;

public final class Navigator extends JavaScriptObject {
    protected Navigator() {

    }


    public native String getUserAgent() /*-{
        return this.userAgent;
    }-*/;
}
