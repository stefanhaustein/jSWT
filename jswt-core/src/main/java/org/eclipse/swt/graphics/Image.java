package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.PlatformDisplay;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Image extends Resource {

    /** Constructs an instance of this class by loading its representation from the specified input stream. */
    public Image(Device device, InputStream stream) {
        super(device);
        try {
            peer = ((PlatformDisplay) device).loadImage(stream);
        } catch (IOException e) {
            SWT.error(SWT.ERROR_IO, e);
        }
    }

    /**
     * Constructs an empty instance of this class with the specified width and height.
     */
    public Image(Device device, int width, int height) {
        super(device);
        peer = ((PlatformDisplay) device).createImage(width, height);
    }

    /**
     * Constructs an empty instance of this class with the width and height of the specified rectangle.
     */
    public Image(Device device, Rectangle bounds) {
        this(device, bounds.width, bounds.height);
    }


    /**
     * Constructs an instance of this class by loading its representation from the file with the specified name.
     */
    public Image(Device device, String filename) {
        super(device);
        try {
            peer = ((PlatformDisplay) device).loadImage(new FileInputStream(filename));
        } catch (IOException e) {
            SWT.error(SWT.ERROR_IO, e);
        }
    }

    /**
     * This is not part of the public API -- used internally for resource loading support in the gwt case.
     * TODO: Hide via helper?
     */
    public Image(Device device, Object platformImage) {
        super(device);
        this.peer = platformImage;
    }

    public Rectangle getBounds() {
       return ((PlatformDisplay) device).getImageBounds(peer);
    }

}
