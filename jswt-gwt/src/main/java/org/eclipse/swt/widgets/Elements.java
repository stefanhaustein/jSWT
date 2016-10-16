package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import org.kobjects.dom.Element;

public final class Elements {

    private Elements() {}


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

    public static Element getChildElement(Element parent, int index) {
        Element child = parent.getFirstElementChild();
        while (index > 0 && child != null) {
            child = child.getNextElementSibling();
            index--;
        }
        return child;
    }

    public static native JsArrayNumber getBounds(Element element) /*-{
        return [0, 0, element.clientWidth, element.clientHeight];
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


    public static native String getDisplay(Element element) /*-{
        return element.style.display;
    }-*/;

    public static native void setDisplay(Element element, String display) /*-{
        element.style.display = display;
    }-*/;

}
