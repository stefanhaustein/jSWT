package org.kobjects.jswt;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class ResourceLoader {


    public static Image loadImage(Device device, String path) throws IOException {
        InputStream is = ResourceLoader.class.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("Resource not found: '" + path + "'");
        }
        return new Image(device, is);
    }
}
