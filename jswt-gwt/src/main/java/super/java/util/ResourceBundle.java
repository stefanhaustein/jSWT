package java.util;

import com.google.gwt.core.client.JavaScriptObject;

public class ResourceBundle {

    public static ResourceBundle getBundle(String name) {
        return new ResourceBundle(name);
    }

    private static native String getString(JavaScriptObject object, String name) /*-{
        return object[name];
    }-*/;

    private static native JavaScriptObject getRootObject(String name) /*-{
        return $wnd.gwtResources[name];
    }-*/;

    private String name;
    private JavaScriptObject data;

    ResourceBundle(String name) {
        this.name = name;
        data = getRootObject(name);
    }

    public final String getString(String key) {
        return getString(data, key);
    }
}
