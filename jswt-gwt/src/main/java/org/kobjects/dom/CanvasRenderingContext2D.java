package org.kobjects.dom;

import com.google.gwt.core.client.JavaScriptObject;

public final class CanvasRenderingContext2D extends JavaScriptObject {
    protected CanvasRenderingContext2D() {
    }

    public native void arc(double x, double y, double r, double start, double end, boolean antiClockwise) /*-{
        $wnd.console.log("arc ", x, y, r, start, end, antiClockwise);
        this.arc(x, y, r, start, end, antiClockwise);
    }-*/;

    public native void beginPath() /*-{
        this.beginPath();
    }-*/;

    public native void closePath() /*-{
        this.closePath();
    }-*/;

    public native void fill() /*-{
        this.fill();
    }-*/;

    public native void fillRect(double x, double y, double width, double height) /*-{
        this.fillRect(x, y, width, height);
    }-*/;

    public native void fillText(String s, int x, int y) /*-{
        this.fillText(s, x, y);
    }-*/;

    public native void lineTo(double x, double y) /*-{
        this.lineTo(x, y);
    }-*/;

    public native void moveTo(double x, double y) /*-{
        this.moveTo(x, y);
    }-*/;

    public native void setFillStyle(String style) /*-{
        this.fillStyle = style;
    }-*/;

    public native void setFont(String s) /*-{
        this.font = s;
    }-*/;

    public native void setLineCap(String cap) /*-{
        this.lineCap = cap;
    }-*/;

    public native void setLineJoin(String join) /*-{
        this.lineJoin = join;
    }-*/;

    public native void setLineWidth(double width) /*-{
        this.lineWidth = width;
    }-*/;

    public native void setStrokeStyle(String style) /*-{
        this.strokeStyle = style;
    }-*/;

    public native void setTextBaseline(String value) /*-{
        this.textBaseline = value;
    }-*/;

    public native void stroke() /*-{
        this.stroke();
    }-*/;

    public native void bezierCurveTo(double d1, double d2, double d3, double d4, double d5, double d6) /*-{
        this.bezierCurveTo(d1, d2, d3, d4, d5, d6);
    }-*/;

    public native void quadraticCurveTo(double x, double y, double i, double y1) /*-{
        this.quadraticCurveTo(x, y, i, y1);
    }-*/;

    public native void strokeRect(double x, double y, double width, double height) /*-{
        this.strokeRect(x, y, width, height);
    }-*/;

    public native double measureText(String text) /*-{
        return this.measureText(text).width;
    }-*/;
}
