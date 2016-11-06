package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public final class Document extends JavaScriptObject {

    protected Document() {
    }

    public static native Document get() /*-{
        return $doc;
    }-*/;

    public native Element createElement(String name) /*-{
        return this.createElement(name);
    }-*/;

    public native Element getBody() /*-{
        return this.body;
    }-*/;

    public native Element getElementById(String id) /*-{
        return this.getElementById(id);
    }-*/;

    public native String getTitle() /*-{
        return this.title;
    }-*/;

    public native void setTitle(String title) /*-{
        this.title = title;
    }-*/;

}
