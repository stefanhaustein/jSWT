package org.eclipse.swt.widgets;

import org.kobjects.dom.Element;
import com.google.gwt.core.client.JavaScriptObject;

public final class Elements extends JavaScriptObject {

    protected Elements() {}

    public static native void setBounds(Element element, int x, int y, int width, int height) /*-{
        var style = element.style;
        style.display = "block";
        style.position = "absolute";
        style.boxSizing = "border-box";
        style.top = y + "px";
        style.left = x + "px";
        style.width = width + "px";
        style.height = height + "px";
    }-*/;

    public static native int getMinWidth(Element element) /*-{
        var style = element.style;
        style.display = "block";
        style.position = "absolute";
        style.boxSizing = "border-box";
        style.whiteSpace = "nowrap";
        var savedWidth = style.width;
        style.width = "";
        var result = element.offsetWidth;
        style.width = savedWidth;
        return result;
    }-*/;

    public static native int getMinHeight(Element element) /*-{
        var style = element.style;
        style.display = "block";
        style.position = "absolute";
        style.boxSizing = "border-box";
        var savedHeight = style.height;
        style.height = "";
        var result = element.offsetHeight;
        //$wnd.console.log("ScrollHeight: " + result + " clientHeight: " + this.clientHeight + " offsetHeight" + this.offsetHeight, this);
        style.height = savedHeight;
        return result;
    }-*/;
}
