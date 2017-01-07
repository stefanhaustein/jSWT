package org.eclipse.swt.widgets;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

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
        update();
    }

    void update() {
        if (dialogBuilder != null) {
            dialogBuilder.setTitle(text);
        } else {
            AndroidDisplay display = (AndroidDisplay) composite.display;
            if (display.topShell == composite) {
                ActionBar actionBar = ((AndroidDisplay) composite.display).activity.getSupportActionBar();
                if (text == null) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                    int cut = text.lastIndexOf('-');
                    if (cut == -1) {
                        actionBar.setTitle(text);
                        actionBar.setSubtitle(null);
                    } else {
                        actionBar.setTitle(text.substring(cut + 1).trim());
                        actionBar.setSubtitle(text.substring(0, cut).trim());
                    }
                }
            }
        }
    }

}
