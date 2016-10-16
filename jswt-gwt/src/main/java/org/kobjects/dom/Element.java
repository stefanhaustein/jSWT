package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public class Element extends JavaScriptObject {

    protected Element() {
    }

    public native final void appendChild(Element child) /*-{
        this.appendChild(child);
    }-*/;

    public native final CanvasRenderingContext2D getContext2d() /*-{
        return this.getContext('2d');
    }-*/;

    public native final Document getOwnerDocument() /*-{
        return this.ownerDocument;
    }-*/;

    public native final Element getFirstElementChild() /*-{
        return this.firstElementChild;
    }-*/;

    public native final Element getLastElementChild() /*-{
        return this.lastElementChild;
    }-*/;

    public native final Element getNextElementSibling() /*-{
        return this.nextElementSibling;
    }-*/;

    public native final Element getParentElement() /*-{
        return this.parentElement;
    }-*/;

    public native final String getTextContent() /*-{
        return this.textContent;
    }-*/;

    public native final void insertBefore(Element newItem, Element before) /*-{
        this.insertBefore(newItem, before);
    }-*/;

    public native final void replaceChild(Element newChild, Element oldChild) /*-{
        this.replaceChild(newChild, oldChild);
    }-*/;

    public native final void setAttribute(String name, String value) /*-{
        this.setAttribute(name, value);
    }-*/;

    public native final void setTextContent(String content) /*-{
        this.textContent = content;
    }-*/;

    public native final String getLocalName() /*-{
        return this.localName;
    }-*/;

}
