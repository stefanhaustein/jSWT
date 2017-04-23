package org.kobjects.swt;


import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;

public class Dialogs {

    public static Promise<RGB> openColorDialog(ColorDialog dialog) {
        if (dialog instanceof PromiseDialog) {
            return ((PromiseDialog<RGB>) dialog).openPromise();
        }
        return new Promise().resolve(dialog.open());
    }

    public static Promise<FontData> openFontDialog(FontDialog dialog) {
        if (dialog instanceof PromiseDialog) {
            return ((PromiseDialog<FontData>) dialog).openPromise();
        }
        return new Promise().resolve(dialog.open());
    }

    public static Promise<String> openFileDialog(FileDialog dialog) {
        if (dialog instanceof PromiseDialog) {
            return ((PromiseDialog<String>) dialog).openPromise();
        }
        return new Promise().resolve(dialog.open());
    }
}
