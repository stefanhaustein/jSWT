package org.kobjects.dom;


import com.google.gwt.core.client.JavaScriptObject;

public final class Window extends JavaScriptObject {
    protected Window() {

    };

    public static native Window get() /*-{
        return $wnd;
    }-*/;



    public native final void addEventListener(String type, final EventListener listener) /*-{
        this.addEventListener(type, function (event) {
            listener.@org.kobjects.dom.EventListener::onEvent(Lorg/kobjects/dom/Event;)(event);
        });
    }-*/;
}
