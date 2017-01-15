package org.eclipse.swt.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        /*
        if (display.topShell != null) {
            ((AndroidShellView) display.topShell.peer).actionBarDrawerToggle.syncState();
        }
        */
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*
        if (display.topShell != null) {
            ((AndroidShellView) display.topShell.peer).actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
        */

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
