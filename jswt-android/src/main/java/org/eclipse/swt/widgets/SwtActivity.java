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

    DrawerLayout navigationDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AndroidDisplay display;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        navigationDrawer = new android.support.v4.widget.DrawerLayout(this);
//    final FrameLayout fl = new FrameLayout(this);
        //   fl.setId(CONTENT_VIEW_ID);
        navigationView = new NavigationView(this);

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        Toolbar toolbar = new Toolbar(this);
//        toolbar.setTitle("WTF? Why?");
 //       toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        params.gravity = Gravity.START;
        navigationView.setLayoutParams(params);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(toolbar);

//    drawer.addView(fl, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        navigationDrawer.addView(mainLayout);
        navigationDrawer.addView(navigationView);

        this.setContentView(navigationDrawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer, R.string.open, R.string.close) /*{
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
              //  drawerView.requestLayout();
            }
        }*/;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        display = new AndroidDisplay(this, navigationDrawer, navigationView, mainLayout);
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
