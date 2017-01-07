package org.eclipse.swt.widgets;


import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.LinearLayout;
import org.eclipse.swt.R;
import org.eclipse.swt.SWT;

class AndroidShell extends AndroidComposite {
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


    AndroidShell(Context context, final Shell shell) {
        super(context, shell);
        this.shell = shell;
        if (shell.parent != null) {
            dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(this);
        } else {
            navigationDrawer = new android.support.v4.widget.DrawerLayout(getContext());
            navigationView = new NavigationView(getContext());

            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.START;
            navigationView.setLayoutParams(params);

            mainLayout = new LinearLayout(context);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            navigationDrawer.addView(mainLayout);
            navigationDrawer.addView(navigationView);

            shell.peer = this;
            toolBar = new ToolBar(shell, SWT.FLAT);
            androidToolbar = (Toolbar) toolBar.peer;
            this.removeView(androidToolbar);
            mainLayout.addView(androidToolbar);
            ((LinearLayout.LayoutParams) androidToolbar.getLayoutParams()).width = LinearLayout.LayoutParams.MATCH_PARENT;


            actionBarDrawerToggle = new ActionBarDrawerToggle(getAndroidDisplay().activity, navigationDrawer, R.string.open, R.string.close);

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

            display.activity.setSupportActionBar(androidToolbar);
            display.activity.setContentView(navigationDrawer);

            display.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            display.activity.getSupportActionBar().setHomeButtonEnabled(true);

            display.topShell = shell;
            update();
        }
    }
}
