package org.eclipse.swt.widgets;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.eclipse.swt.R;

public class SwtActivity extends AppCompatActivity {

    DrawerLayout navigationDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AndroidDisplay display;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        navigationDrawer = new android.support.v4.widget.DrawerLayout(this);
//    final FrameLayout fl = new FrameLayout(this);
        //   fl.setId(CONTENT_VIEW_ID);
        navigationView = new NavigationView(this);

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        params.gravity = Gravity.START;
        navigationView.setLayoutParams(params);

//    drawer.addView(fl, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        navigationDrawer.addView(navigationView);

        this.setContentView(navigationDrawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer, R.string.open, R.string.close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        display = new AndroidDisplay(this);
    }

    public AndroidDisplay getDisplay() {
        return display;
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem) ||
            actionBarDrawerToggle.onOptionsItemSelected(menuItem);
    }
}
