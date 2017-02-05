package org.eclipse.swt.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.eclipse.swt.SWT;

/**
 * The goal is to make this a convenience class, not a requirement -- and to hand in all required information
 * (an Activity and a to the Display constructor.
 */
public class SwtActivity extends AppCompatActivity {

    AndroidDisplay display;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        display = new AndroidDisplay(this);
    }

    public AndroidDisplay getDisplay() {
        return display;
    }

    @Override
    public void onBackPressed() {
        if (display.topShell == null) {
            super.onBackPressed();
        } else {
            Event event = new Event();
            event.keyCode = SWT.ARROW_LEFT;
            event.stateMask = SWT.ALT;
            display.topShell.notifyListeners(SWT.KeyDown, event);
            display.topShell.notifyListeners(SWT.KeyUp, event);
            if (event.doit) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem menuItem) {
        if (super.onOptionsItemSelected(menuItem)) {
            return true;
        }
        if (display.topShell != null) {
            return ((AndroidShellView) display.topShell.peer).actionBarDrawerToggle.onOptionsItemSelected(menuItem);
        }
        return false;
    }
}
