package org.kobjects.jswt;


import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;

public class Dialogs {


    public static void openColorDialog(ColorDialog dialog, Callback<RGB> callback) {
        if (dialog instanceof CallbackDialog) {
            ((CallbackDialog<RGB>) dialog).open(callback);
        } else {
            callback.run(dialog.open());
        }
    }
}
