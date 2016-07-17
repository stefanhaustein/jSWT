package org.eclipse.swt.graphics;

public class Color {
    int red;
    int green;
    int blue;
    RGB rgb;

    public Color(Device device, int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(Device device, RGB rgb) {
        this(device, rgb.red, rgb.green, rgb.blue);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
