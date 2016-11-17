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

    public native final Style getComputedStyle(Element element) /*-{
        return this.getComputedStyle(element);
    }-*/;

    public native final Style getComputedStyle(Element element, String pseudoElement) /*-{
        return this.getComputedStyle(element, pseudoElement);
    }-*/;

    public native final Navigator getNavigator() /*-{
        return this.navigator;
    }-*/;
}
