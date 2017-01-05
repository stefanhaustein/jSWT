package org.eclipse.swt.graphics;


public class Font extends Resource {
    final FontData[] fontData;

    public Font(Device device, FontData fd) {
        this.fontData = new FontData[]{fd};
    }

    public Font(Device device, FontData[] fd) {
        this.fontData = new FontData[fd.length];
        System.arraycopy(fd, 0, fontData, 0, fontData.length);
    }

    public Font(Device device, String name, int height, int style) {
        this(device, new FontData(name, height, style));
    }

    public FontData[] getFontData() {
        FontData[] result = new FontData[fontData.length];
        for (int i = 0; i < result.length; i++) {
            FontData fd = fontData[i];
            result[i] = new FontData(fd.name, fd.height, fd.style);
        }
        return result;
    }

    public void dispose() {
    }
}
