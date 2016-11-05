package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public final class ClassList extends JavaScriptObject {
    protected ClassList() {
    }

    public native void add(String c) /*-{
        this.add(c)
    }-*/;

    public native boolean contains(String c) /*-{
        this.contains(c)
    }-*/;

    public native void remove(String c) /*-{
        this.remove(c)
    }-*/;

    public native void toggle(String c) /*-{
        this.toggle(c)
    }-*/;


}
