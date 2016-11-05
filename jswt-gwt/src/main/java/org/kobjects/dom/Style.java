package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public final class Style extends JavaScriptObject {

    protected Style() {
    }

    public native String get(String propertyName) /*-{
        return this[propertyName];
    }-*/;

    public native String getBackgroundColor() /*-{
        return this.backgroundColor;
    }-*/;

    public native String getColor() /*-{
        return this.color;
    }-*/;

    public native String getDisplay() /*-{
        return this.display;
    }-*/;

    public native String getHeight() /*-{
        return this.height;
    }-*/;

    public native String getTextAlign() /*-{
        return this.textAlign;
    }-*/;

    public native String getVisibility() /*-{
        return this.visibility;
    }-*/;

    public native String getWidth() /*-{
        return this.width;
    }-*/;

    public native String getWhiteSpace() /*-{
        return this.whiteSpace;
    }-*/;

    public native void set(String name, String value) /*-{
        this[name] = value;
    }-*/;

    public native void setBackgroundColor(String s) /*-{
        this.backgroundColor = s;
    }-*/;

    public native void setColor(String s) /*-{
        this.color = s;
    }-*/;

    public native void setDisplay(String value) /*-{
        this.display = value;
    }-*/;

    public native void setHeight(String height) /*-{
        this.height = height;
    }-*/;

    public native void setLeft(String s) /*-{
        this.left = s;
    }-*/;

    public native void setTextAlign(String s) /*-{
        this.textAlign = s;
    }-*/;

    public native void setTop(String s) /*-{
        this.top = s;
    }-*/;

    public native void setVisibility(String visibility) /*-{
        this.visibility = visibility;
    }-*/;

    public native void setWidth(String width) /*-{
        this.width = width;
    }-*/;

    public native void setWhiteSpace(String ws) /*-{
        this.whiteSpace = ws;
    }-*/;

    public native void setBackgroundImage(String s) /*-{
        this.backgroundImage = s;
    }-*/;

}
