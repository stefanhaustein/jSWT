package java.util;

public class ResourceBundle {
    public static ResourceBundle getBundle(String s) {
        return new ResourceBundle();
    }

    public String getString(String key) {
        return key;
    }
}
