package org.kobjects.jswt;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import java.io.IOException;

public class ResourceLoader {
    public static Image loadImage(Device device, String path) throws IOException  {
        return new Image(device, 64, 64);
    }
}
