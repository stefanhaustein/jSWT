package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;

public class Device {
    private final Color colorTransparent = new Color (this, 0xFF,0xFF,0xFF,0);
    private final Color colorBlack = new Color (this, 0,0,0);
    private final Color colorDarkRed = new Color (this, 0x80,0,0);
    private final Color colorDarkGreen = new Color (this, 0,0x80,0);
    private final Color colorDarkYellow = new Color (this, 0x80,0x80,0);
    private final Color colorDarkBlue = new Color (this, 0,0,0x80);
    private final Color colorDarkMagenta = new Color (this, 0x80,0,0x80);
    private final Color colorDarkCyan = new Color (this, 0,0x80,0x80);
    private final Color colorGray = new Color (this, 0xC0,0xC0,0xC0);
    private final Color colorDarkGray = new Color (this, 0x80,0x80,0x80);
    private final Color colorRed = new Color (this, 0xFF,0,0);
    private final Color colorGreen = new Color (this, 0,0xFF,0);
    private final Color colorYellow = new Color (this, 0xFF,0xFF,0);
    private final Color colorBlue = new Color (this, 0,0,0xFF);
    private final Color colorMagenta = new Color (this, 0xFF,0,0xFF);
    private final Color colorCyan = new Color (this, 0,0xFF,0xFF);
    private final Color colorWhite = new Color (this, 0xFF,0xFF,0xFF);

    public Color getSystemColor(int id) {
        switch(id) {
            case SWT.COLOR_BLACK:
                return colorBlack;
            case SWT.COLOR_BLUE:
                return colorBlue;
            case SWT.COLOR_CYAN:
                return colorCyan;
            case SWT.COLOR_DARK_BLUE:
                return colorDarkBlue;
            case SWT.COLOR_DARK_CYAN:
                return colorDarkCyan;
            case SWT.COLOR_DARK_GRAY:
                return colorDarkGray;
            case SWT.COLOR_DARK_GREEN:
                return colorDarkGreen;
            case SWT.COLOR_DARK_MAGENTA:
                return colorDarkMagenta;
            case SWT.COLOR_DARK_RED:
                return colorDarkRed;
            case SWT.COLOR_DARK_YELLOW:
                return colorDarkYellow;
            case SWT.COLOR_GRAY:
                return colorGray;
            case SWT.COLOR_GREEN:
                return colorGreen;
            case SWT.COLOR_MAGENTA:
                return colorMagenta;
            case SWT.COLOR_RED:
                return colorRed;
            case SWT.COLOR_TRANSPARENT:
                return colorTransparent;
            case SWT.COLOR_WHITE:
                return colorWhite;
            case SWT.COLOR_YELLOW:
                return colorYellow;
            default:
                return colorBlack;
        }
    }
}
