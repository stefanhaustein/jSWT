package org.eclipse.swt.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import org.eclipse.swt.R;

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
        if (display.topShell != null) {
            ((AndroidShell) display.topShell.peer).actionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (display.topShell != null) {
            ((AndroidShell) display.topShell.peer).actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem menuItem) {
        if (super.onOptionsItemSelected(menuItem)) {
            return true;
        }
        if (display.topShell != null) {
            return ((AndroidShell) display.topShell.peer).actionBarDrawerToggle.onOptionsItemSelected(menuItem);
        }
        return false;
    }
}
