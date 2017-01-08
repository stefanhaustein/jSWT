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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

class AndroidShellView extends AndroidCompositeView {
    String text;
    Shell shell;

    // Dialog case
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    // Root shell case
    DrawerLayout navigationDrawer;
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
            navigationDrawer = new android.support.v4.widget.DrawerLayout(getContext());
            navigationView = new NavigationView(getContext());

            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, MATCH_PARENT);
            params.gravity = Gravity.START;
            navigationView.setLayoutParams(params);

            mainLayout = new LinearLayout(context);
            mainLayout.setOrientation(LinearLayout.VERTICAL);

            navigationDrawer.addView(mainLayout);
            navigationDrawer.addView(navigationView);

            ViewGroup.LayoutParams mainParams = mainLayout.getLayoutParams();
            mainParams.width = MATCH_PARENT;
            mainParams.height = MATCH_PARENT;

            androidToolbar = new Toolbar(context);
            mainLayout.addView(androidToolbar);
            ((LinearLayout.LayoutParams) androidToolbar.getLayoutParams()).width = MATCH_PARENT;

            shell.peer = this;
            toolBar = new ToolBar(shell, SWT.FLAT);
            View swtToolbarPeer = (View) toolBar.peer;
            this.removeView(swtToolbarPeer);
            androidToolbar.addView(swtToolbarPeer);
            ((Toolbar.LayoutParams) swtToolbarPeer.getLayoutParams()).gravity = Gravity.RIGHT;

            actionBarDrawerToggle = new ActionBarDrawerToggle(getAndroidDisplay().activity, navigationDrawer, R.string.open, R.string.close);

            /*
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(android.view.MenuItem item) {
                 MenuItem result = getAndroidDisplay().findMenuItem(shell.menuBar, item.getTitle().toString());
                 if (result != null) {
                      result.notifyListeners(SWT.Selection, null);
                      navigationDrawer.closeDrawers();
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
            // Hack to avoid adding self to mainLayout while under construction.
            if (mainLayout.indexOfChild(this) == -1) {
                mainLayout.addView(this);
                ((LinearLayout.LayoutParams) this.getLayoutParams()).weight = 1;
            }

            AndroidDisplay display = (AndroidDisplay) shell.display;

            display.activity.setContentView(navigationDrawer);
            display.activity.setSupportActionBar(androidToolbar);

            display.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            display.activity.getSupportActionBar().setHomeButtonEnabled(true);

            display.topShell = shell;
            update();
        }
    }
}
