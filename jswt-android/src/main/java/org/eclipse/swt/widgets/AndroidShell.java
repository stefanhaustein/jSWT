package org.eclipse.swt.widgets;


import android.content.Context;
import android.support.v7.app.AlertDialog;

class AndroidShell extends AndroidComposite {
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    String text;
    Shell shell;

    AndroidShell(Context context, Shell shell) {
        super(context, shell);
        this.shell = shell;
        if (shell.parent != null) {
            dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(this);
        }
    }

    void setText(String text) {
        this.text = text;

        if (dialogBuilder != null) {
            dialogBuilder.setTitle(text);
        } else {
            AndroidDisplay display = (AndroidDisplay) composite.display;
            if (display.topShell == composite) {
                ((AndroidDisplay) composite.display).activity.getSupportActionBar().setTitle(text);
            }
        }
    }


}
