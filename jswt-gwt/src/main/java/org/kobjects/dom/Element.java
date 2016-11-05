package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Element extends JavaScriptObject {

    protected Element() {
    }

    public native final void addEventListener(String type, final EventListener listener) /*-{
        this.addEventListener(type, function (event) {
           listener.@org.kobjects.dom.EventListener::onEvent(Lorg/kobjects/dom/Event;)(event);
        });
    }-*/;

    public native final void appendChild(Element child) /*-{
        this.appendChild(child);
    }-*/;

    public native final void focus() /*-{
        this.focus();
    }-*/;

    public native final String getAttribute(String name) /*-{
        return this.getAttribute(name);
    }-*/;

    public native final String getClassName() /*-{
        return this.className;
    }-*/;

    public native final int getChildElementCount() /*-{
        return this.childElementCount;
    }-*/;

    public native final CanvasRenderingContext2D getContext2d() /*-{
        return this.getContext('2d');
    }-*/;

    public native final JsArray<Element> getChildren() /*-{
        return this.children;
    }-*/;

    public native final ClassList getClassList() /*-{
        return this.classList;
    }-*/;

    public native final boolean getChecked() /*-{
        return this.checked;
    }-*/;

    public native final int getClientHeight() /*-{
        return this.clientHeight;
    }-*/;

    public native final int getClientWidth() /*-{
        return this.clientWidth;
    }-*/;

    public native final boolean getDisabled() /*-{
        return this.disabled;
    }-*/;

    public native final Element getFirstElementChild() /*-{
        return this.firstElementChild;
    }-*/;

    public native final int getHeight() /*-{
        return this.height;
    }-*/;

    public native final Element getLastElementChild() /*-{
        return this.lastElementChild;
    }-*/;

    public native final Element getNextElementSibling() /*-{
        return this.nextElementSibling;
    }-*/;

    public native final int getOffsetLeft() /*-{
        return this.offsetTop;
    }-*/;

    public native final int getOffsetTop() /*-{
        return this.offsetTop;
    }-*/;

    public native final int getOffsetHeight() /*-{
        return this.offsetHeight;
    }-*/;

    public native final int getOffsetWidth() /*-{
        return this.offsetWidth;
    }-*/;

    public native final Document getOwnerDocument() /*-{
        return this.ownerDocument;
    }-*/;

    public native final int getSelectedIndex() /*-{
        return this.selectedIndex;
    }-*/;

    public native final Element getParentElement() /*-{
        return this.parentElement;
    }-*/;

    public native final Style getStyle() /*-{
        return this.style;
    }-*/;

    public native final boolean getSelected() /*-{
        return this.selected;
    }-*/;

    public native final String getTextContent() /*-{
        return this.textContent;
    }-*/;

    public native final String getValue() /*-{
        return this.value;
    }-*/;

    public native final int getWidth() /*-{
        return this.width;
    }-*/;

    public native final int getScrollHeight() /*-{
        return this.scrollHeight;
    }-*/;

    public native final int getScrollLeft() /*-{
        return this.scrollLeft;
    }-*/;

    public native final int getScrollTop() /*-{
        return this.scrollTop;
    }-*/;

    public native final int getScrollWidth() /*-{
        return this.scrollWidth;
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

    public native final void setChecked(boolean b) /*-{
        this.checked = b;
    }-*/;

    public native final void setDisabled(boolean b) /*-{
        this.disabled = b;
    }-*/;

    public native final void setSelected(boolean b) /*-{
        this.selected = b;
    }-*/;

    public native final void setTextContent(String content) /*-{
        this.textContent = content;
    }-*/;

    public native final String getLocalName() /*-{
        return this.localName;
    }-*/;

    public native final void setClassName(String s) /*-{
        this.className = s;
    }-*/;

    public native final int setSelectedIndex(int index) /*-{
        this.selectedIndex = index;
    }-*/;

    public native final void removeAttribute(String name) /*-{
        this.removeAttribute(name);
    }-*/;

    public native final void removeChild(Element child) /*-{
        this.removeChild(child);
    }-*/;

    public native final void setMax(String maximum) /*-{
        this.max = maximum;
    }-*/;

    public native final void setMin(String minimum) /*-{
        this.min = minimum;
    }-*/;

    public native final void setValue(String value) /*-{
        this.value = value;
    }-*/;

    public native final void setScrollLeft(int px) /*-{
        this.scrollLeft = px;
    }-*/;

    public native final void setScrollTop(int px) /*-{
        this.scrollTop = px;
    }-*/;

    public native final void setStep(String value) /*-{
        this.step = value;
    }-*/;

    public native final Element cloneNode(boolean deep) /*-{
        return this.cloneNode(deep);
    }-*/;

    public native final void setReadOnly(boolean b) /*-{
        this.readOnly = b;
    }-*/;
}
