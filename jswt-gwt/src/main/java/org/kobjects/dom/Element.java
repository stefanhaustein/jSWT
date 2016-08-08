package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public final class Element extends JavaScriptObject {

    protected Element() {
    }


    public native void appendChild(Element child) /*-{
        this.appendChild(child);
    }-*/;

    public native CanvasRenderingContext2D getContext2d() /*-{
        return this.getContext('2d');
    }-*/;

    public native Element getLastElementChild() /*-{
        return this.lastElementChild;
    }-*/;

    public native Element getParentElement() /*-{
        return this.parentElement;
    }-*/;

    public native String getTextContent() /*-{
        return this.textContent;
    }-*/;

    public native void setAttribute(String name, String value) /*-{
        this.setAttribute(name, value);
    }-*/;

    public native void setTextContent(String content) /*-{
        this.textContent = content;
    }-*/;

    public native String getLocalName() /*-{
        return this.localName;
    }-*/;

}
