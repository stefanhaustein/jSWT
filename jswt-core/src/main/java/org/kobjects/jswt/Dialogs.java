package org.kobjects.jswt;


import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.FontDialog;

public class Dialogs {


    public static void open(ColorDialog dialog, Callback<RGB> callback) {
        if (dialog instanceof CallbackDialog) {
            ((CallbackDialog<RGB>) dialog).open(callback);
        } else {
            callback.run(dialog.open());
        }
    }

    public static void open(FontDialog dialog, Callback<FontData> callback) {
        if (dialog instanceof CallbackDialog) {
            ((CallbackDialog<FontData>) dialog).open(callback);
        } else {
            callback.run(dialog.open());
        }
    }
}
