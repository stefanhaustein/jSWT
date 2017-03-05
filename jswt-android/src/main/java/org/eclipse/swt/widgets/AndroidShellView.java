package org.eclipse.swt.widgets;


import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
    Toolbar androidToolbar;
    ToolBar toolBar;
    ActionBarDrawerToggle actionBarDrawerToggle;


    AndroidShellView(Context context, final Shell shell) {
        super(context, shell);
        this.shell = shell;
        if (shell.parent != null) {
            dialogBuilder = new AlertDialog.Builder(getContext());
            int sideBorderWidth = Math.round(getAndroidDisplay().pixelPerDp * 20);
            dialogBuilder.setView(this, sideBorderWidth, 0, sideBorderWidth, 0);
        } else {
            // Absolute root
            drawerLayout = new android.support.v4.widget.DrawerLayout(getContext());

            AndroidDisplay display = (AndroidDisplay) shell.getDisplay();
            display.rootLayout.addView(drawerLayout, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


            // Main root: toolbar and content
            drawerLayout.addView(this, new DrawerLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            shell.peer = this;

            WindowManager windowManager = display.activity.getWindowManager();
            android.view.Display androidDisplay = windowManager.getDefaultDisplay();

            Resources resources = display.activity.getResources();
            int resourceId = resources.getIdentifier( "status_bar_height", "dimen", "android" );

            int w = androidDisplay.getWidth();
            int h = androidDisplay.getHeight() - (resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0);
            setMeasuredDimension(w, h);

            // Toolbar

            if ((shell.style & SWT.TITLE) != 0) {
                androidToolbar = new Toolbar(context);
                androidToolbar.setTitle("xxx");
                androidToolbar.setSubtitle("yyy");

                TypedValue value = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
                androidToolbar.setBackgroundColor(value.data);

                // androidToolbar.setBackgroundColor(0x0ffff8888);
                toolBar = new ToolBar(shell, SWT.FLAT);

                shell.children.remove(toolBar);
                View swtToolbarPeer = (View) toolBar.peer;
                this.removeView(swtToolbarPeer);
                androidToolbar.addView(swtToolbarPeer, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END));
                addView(androidToolbar);
                androidToolbar.measure(MeasureSpec.EXACTLY | w, MeasureSpec.UNSPECIFIED);
            }

            // Navigation Drawer

            navigationView = new NavigationView(getContext());
            drawerLayout.addView(navigationView, new DrawerLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START));

            actionBarDrawerToggle = new ActionBarDrawerToggle(getAndroidDisplay().activity, drawerLayout, R.string.open, R.string.close);
       }
    }

    void setText(String text) {
        this.text = text;
        update();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (androidToolbar != null) {
            androidToolbar.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
        }
    }

    void update() {
        if (dialogBuilder != null) {
            dialogBuilder.setTitle(text);
            if (dialog != null) {
                dialog.setTitle(text);
            }
        } else {
            if (androidToolbar != null) {
                int cut = text.indexOf('-');
                if (cut == -1) {
                    androidToolbar.setTitle(text);
                    androidToolbar.setSubtitle(null);
                } else {
                    androidToolbar.setSubtitle(text.substring(cut + 1).trim());
                    androidToolbar.setTitle(text.substring(0, cut).trim());
                }
                androidToolbar.invalidate();
            }
        }
    }

    public void open() {
        if (dialogBuilder != null) {
            dialog = dialogBuilder.show();
        } else {
            AndroidDisplay display = getAndroidDisplay();

            drawerLayout.setVisibility(VISIBLE);

            if (toolBar != null) {
                display.activity.setSupportActionBar(androidToolbar);

                display.activity.getSupportActionBar().setHomeButtonEnabled(true);
                display.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                androidToolbar.invalidate();
            } else {
                display.activity.setSupportActionBar(null);
            }

            actionBarDrawerToggle.syncState();

            if (display.topShell != shell && display.topShell != null) {
                ((AndroidShellView) display.topShell.peer).drawerLayout.setVisibility(INVISIBLE);
            }
            display.topShell = shell;
            //update();

            drawerLayout.invalidate();
        }
    }
}
