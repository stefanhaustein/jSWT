package org.eclipse.swt.graphics;

/**
 * Created by haustein on 12.07.16.
 */
public class Image {
    public Image(Device device, ImageDataProvider imageDataProvider) {

    }

    public Image(Device device, ImageData imageData) {

    }

    public ImageData getImageDataAtCurrentZoom() {
        throw new RuntimeException("NYI");
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return false;
    }
}
