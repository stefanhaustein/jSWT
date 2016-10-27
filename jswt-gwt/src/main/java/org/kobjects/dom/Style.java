package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public class Style extends JavaScriptObject {

    protected Style() {
    }

    public native final String getBackgroundColor() /*-{
        return this.backgroundColor;
    }-*/;

    public native final String getColor() /*-{
        return this.color;
    }-*/;

    public native final String getDisplay() /*-{
        return this.display;
    }-*/;

    public native final String getHeight() /*-{
        return this.height;
    }-*/;

    public native final String getVisibility() /*-{
        return this.visibility;
    }-*/;

    public native final String getWidth() /*-{
        return this.width;
    }-*/;

    public native final void setBackgroundColor(String s) /*-{
        this.backgroundColor = s;
    }-*/;

    public native final void setColor(String s) /*-{
        this.color = s;
    }-*/;

    public native final void setDisplay(String value) /*-{
        this.display = value;
    }-*/;

    public native final void setHeight(String height) /*-{
        this.height = height;
    }-*/;

    public native final void setLeft(String s) /*-{
        this.left = s;
    }-*/;

    public native final void setTop(String s) /*-{
        this.top = s;
    }-*/;

    public native final void setVisibility(String visibility) /*-{
        this.visibility = visibility;
    }-*/;

    public native final void setWidth(String width) /*-{
        this.width = width;
    }-*/;

    public native final String get(String propertyName) /*-{
        return this[propertyName];
    }-*/;
}
