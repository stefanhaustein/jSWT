package org.eclipse.swt.graphics;

public class Color extends Resource {
    int red;
    int green;
    int blue;
    int alpha;

    public Color(Device device, int red, int green, int blue) {
        this.device = device;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 255;
    }

    public Color(Device device, int red, int green, int blue, int alpha) {
        this.device = device;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(Device device, RGB rgb) {
        this(device, rgb.red, rgb.green, rgb.blue);
    }

    public Color(Device device, RGBA rgba) {
        this(device, rgba.rgb, rgba.alpha);
    }

    public Color(Device device, RGB rgb, int alpha) {
        this(device, rgb.red, rgb.green, rgb.blue, alpha);
    }

    public int getRed() {
        return red;
    }

    public RGB getRGB() {
        return new RGB(red, green, blue);
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }
}
