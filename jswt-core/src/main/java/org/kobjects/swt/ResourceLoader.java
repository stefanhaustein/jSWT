package org.kobjects.swt;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class ResourceLoader {


    public static Promise<Image> loadImage(Device device, String path)  {
        Promise<Image> result = new Promise<>();
        try {
            InputStream is = ResourceLoader.class.getResourceAsStream(path);
            if (is == null) {
                return result.reject(new IOException("Resource not found: '" + path + "'"));
            }
            return result.resolve(new Image(device, is));
        } catch (Exception e) {
            return result.reject(e);
        }
    }
}
