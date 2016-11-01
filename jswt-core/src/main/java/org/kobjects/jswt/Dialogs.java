package org.kobjects.jswt;


import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.kobjects.promise.Promise;

public class Dialogs {

    public static Promise<RGB> openColorDialog(ColorDialog dialog) {
        if (dialog instanceof PromiseDialog) {
            return ((PromiseDialog<RGB>) dialog).openPromise();
        }
        return new Promise().resolve(dialog.openPromise());
    }

    public static Promise<FontData> openFontDialog(FontDialog dialog) {
        if (dialog instanceof PromiseDialog) {
            return ((PromiseDialog<FontData>) dialog).openPromise();
        }
        return new Promise().resolve(dialog.openPromise());
    }
}
