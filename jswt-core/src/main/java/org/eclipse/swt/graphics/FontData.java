package org.eclipse.swt.graphics;

public class FontData {
    String name;
    int height;
    int style;

    public FontData() {
    }

    public FontData(String name, int height, int style) {
        this.name = name;
        this.height = height;
        this.style = style;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public int getStyle() {
        return style;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(int style) {
        this.style = style;
    }


}
