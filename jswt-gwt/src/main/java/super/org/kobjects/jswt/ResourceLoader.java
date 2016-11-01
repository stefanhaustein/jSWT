package org.kobjects.jswt;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import java.io.IOException;
import org.eclipse.swt.widgets.GwtDisplay;
import org.kobjects.dom.Document;
import org.kobjects.dom.Element;
import org.kobjects.dom.Event;
import org.kobjects.dom.EventListener;
import org.kobjects.promise.Promise;

public class ResourceLoader {
    public static Promise<Image> loadImage(final Device device, String path)   {
        final Promise<Image> result = new Promise<>();
        final Element img = Document.get().createElement("img");
        img.addEventListener("load", new EventListener() {
            @Override
            public void onEvent(Event event) {
                Image image = ((GwtDisplay) device).createImage(img);
                result.resolve(image);
            }
        });
        img.setAttribute("src", path);
        return result;
    }
}
