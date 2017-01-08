package org.eclipse.swt.widgets;


import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.eclipse.swt.R;
import org.eclipse.swt.SWT;


class AndroidShellView extends AndroidCompositeView {
    String text;
    Shell shell;

    // Dialog case
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    // Root shell case
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout mainLayout;
    Toolbar androidToolbar;
    ToolBar toolBar;
    ActionBarDrawerToggle actionBarDrawerToggle;


    AndroidShellView(Context context, final Shell shell) {
        super(context, shell);
        this.shell = shell;
        if (shell.parent != null) {
            dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(this);
        } else {
            // Absolute root
            drawerLayout = new android.support.v4.widget.DrawerLayout(getContext());

            // Main root: toolbar and content
            mainLayout = new LinearLayout(context);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            drawerLayout.addView(mainLayout, new DrawerLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            // Toolbar
            androidToolbar = new Toolbar(context);
            LinearLayout.LayoutParams androidToolbarParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            androidToolbarParams.gravity = 0;
            // androidToolbar.setBackgroundColor(0x0ffff8888);

            shell.peer = this;
            toolBar = new ToolBar(shell, SWT.FLAT);
            shell.children.remove(toolBar);
            View swtToolbarPeer = (View) toolBar.peer;
            this.removeView(swtToolbarPeer);
            androidToolbar.addView(swtToolbarPeer, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END));

            mainLayout.addView(androidToolbar, androidToolbarParams);
            // Navigation Drawer

            navigationView = new NavigationView(getContext());
            drawerLayout.addView(navigationView, new DrawerLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START));

            actionBarDrawerToggle = new ActionBarDrawerToggle(getAndroidDisplay().activity, drawerLayout, R.string.open, R.string.close);



            // this.setBackgroundColor(0x0ff88ff88);
            /*
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(android.view.MenuItem item) {
                 MenuItem result = getAndroidDisplay().findMenuItem(shell.menuBar, item.getTitle().toString());
                 if (result != null) {
                      result.notifyListeners(SWT.Selection, null);
                      drawerLayout.closeDrawers();
                      return true;
                 }
                 return false;
                }
            });
            */

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
            AndroidDisplay display = (AndroidDisplay) shell.display;
            if (display.topShell == shell) {
                ActionBar actionBar = display.activity.getSupportActionBar();
                if (text == null) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                    int cut = text.indexOf('-');
                    if (cut == -1) {
                        actionBar.setTitle(text);
                        actionBar.setSubtitle(null);
                    } else {
                        actionBar.setSubtitle(text.substring(cut + 1).trim());
                        actionBar.setTitle(text.substring(0, cut).trim());
                    }
                }
            }
        }
    }

    public void open() {
        if (dialogBuilder != null) {
            dialog = dialogBuilder.show();
        } else {

            AndroidDisplay display = (AndroidDisplay) shell.display;

            if (mainLayout.indexOfChild(this) == -1) {
                LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                contentLayoutParams.weight = 1;
                //  setBackgroundColor(0x0ffff8888);
                mainLayout.addView(this, contentLayoutParams);

            }


            display.activity.setContentView(drawerLayout);
            display.activity.setSupportActionBar(androidToolbar);

            display.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            display.activity.getSupportActionBar().setHomeButtonEnabled(true);

            display.topShell = shell;
            update();

            drawerLayout.invalidate();
        }
    }
}
