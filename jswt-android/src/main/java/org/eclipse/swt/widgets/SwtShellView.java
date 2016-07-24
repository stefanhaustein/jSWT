package org.eclipse.swt.widgets;


import android.content.Context;
import android.support.v7.app.AlertDialog;

public class SwtShellView extends SwtViewGroup {
    AlertDialog.Builder dialog;
    String text;
    Shell shell;

    SwtShellView(Context context, Shell shell) {
        super(context, shell);
        this.shell = shell;
        if (shell.parent != null) {
            dialog = new AlertDialog.Builder(getContext());
            dialog.setView(this);
        }
    }

    void setText(String text) {
        this.text = text;

        if (dialog != null) {
            dialog.setTitle(text);
        } else {
            AndroidDisplay display = (AndroidDisplay) composite.display;
            if (display.topShell == composite) {
                ((AndroidDisplay) composite.display).activity.getSupportActionBar().setTitle(text);
            }
        }
    }


}
