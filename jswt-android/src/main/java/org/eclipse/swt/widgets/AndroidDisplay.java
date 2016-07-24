package org.eclipse.swt.widgets;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


public class AndroidDisplay extends PlatformDisplay {

  AppCompatActivity activity;
  Shell topShell;
  DrawerLayout navigationDrawer;
  NavigationView navigationView;
  LinearLayout mainLayout;

  public AndroidDisplay(AppCompatActivity activity, final DrawerLayout navigationDrawer, NavigationView navigationView,
                        LinearLayout mainLayout) {
    this.activity = activity;
    this.navigationDrawer = navigationDrawer;
    this.navigationView = navigationView;
    this.mainLayout = mainLayout;

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(android.view.MenuItem item) {
        if (topShell != null && topShell.menuBar != null) {
          MenuItem result = findMenuItem(topShell.menuBar, item.getTitle().toString());
          if (result != null) {
            sendEvent(result, SWT.Selection);
            Event event = new Event();
            event.display = AndroidDisplay.this;
            event.widget = result;
            event.type = SWT.Selection;
            result.listeners.sendEvent(event);
            navigationDrawer.closeDrawers();
            return true;
          }
        }
        return false;
      }
    });
  }


  @Override
  public Object createControl(Control control) {
    if (control instanceof Button) {
      switch (control.style) {
        case SWT.RADIO:
          return new android.widget.RadioButton(activity);
        case SWT.CHECK:
          return new android.widget.CheckBox(activity);
        default:
          return new android.widget.Button(activity);
      }
    }
    if (control instanceof Text) {
      return new android.widget.EditText(activity);
    }
    if (control instanceof Label) {
      return new android.widget.TextView(activity);
    }
    if (control instanceof ScrolledComposite) {
      return new android.widget.ScrollView(activity);
    }
    if (control instanceof Shell) {
      return new SwtShellView(activity, (Shell) control);
    }
    // Should be last because some other options are subclasses of Composite / Canvas
    if (control instanceof Canvas) {
      return new SwtCanvasView(activity, (Canvas) control);
    }
    if (control instanceof Composite) {
      return new SwtViewGroup(activity, (Composite) control);
    }
    throw new RuntimeException("Unrecognized control: " + control);
  }

  @Override
  public void openShell(Shell shell) {
    SwtShellView view = (SwtShellView) shell.peer;
    if (view.dialog!= null) {
      view.dialog.show();
      return;
    }

    if (topShell != null) {
      if (topShell == shell) {
        return;
      }
      // TODO: stack
      mainLayout.removeView((View) topShell.peer);
    }
    topShell = shell;

    android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
    String text = view.text;
    if (text != null) {
      actionBar.show();
      actionBar.setTitle(text);
    } else {
      actionBar.hide();
    }

    mainLayout.addView(view);

    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
    params.weight = 1;

    // navigationDrawer.addView(view, 0);
    // TODO: update menu bar!
//    activity.setContentView(view);
  }

  @Override
  public Rectangle getBounds(Control control) {
    View view = (View) control.peer;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams instanceof SwtViewGroup.LayoutParams) {
      SwtViewGroup.LayoutParams lmlParams = (SwtViewGroup.LayoutParams) layoutParams;
      return new Rectangle(lmlParams.assignedX, lmlParams.assignedY,
          lmlParams.assignedWidth, lmlParams.assignedHeight);
    }

    return new Rectangle(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

  }

  @Override
  public Insets getInsets(Scrollable scrollable) {
    return new Insets();
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    View view = (View) control.peer;
    view.measure(
        wHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | wHint),
        hHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | hHint));
    return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
  }

  @Override
  public void setBounds(Control control, int x, int y, int width, int height) {
    View view = (View) control.peer;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams instanceof SwtViewGroup.LayoutParams) {
      SwtViewGroup.LayoutParams lmlParams = (SwtViewGroup.LayoutParams) layoutParams;
      lmlParams.assignedX = x;
      lmlParams.assignedY = y;
      lmlParams.assignedWidth = width;
      lmlParams.assignedHeight = height;
    }
  }

  @Override
  public String getText(Control control) {
    Object peer = control.peer;
    if (peer instanceof TextView) {
      return ((TextView) peer).getText().toString();
    }
    if (peer instanceof SwtShellView) {
      return ((SwtShellView) peer).text;
    }
    return null;
  }

  @Override
  public void setText(Control control, String text) {
    Object peer = control.peer;
    if (peer instanceof TextView) {
      ((TextView) peer).setText(text);
    } else if (peer instanceof SwtShellView) {
      ((SwtShellView) peer).setText(text);
    }
  }


  private MenuItem findMenuItem(Menu menu, String title) {
    for (int i = 0; i < menu.getItemCount(); i++) {
      MenuItem item = menu.getItem(i);
      if (item.subMenu != null) {
        MenuItem result = findMenuItem(item.subMenu, title);
        if (result != null) {
          return result;
        }
      } else if (item.getText().equals(title)) {
        return item;
      }
    }
    return null;
  }

  private void populateMenu(Menu sourceMenu, android.view.Menu androidMenu, boolean flattenFirst) {
    for (int i = 0; i < sourceMenu.getItemCount(); i++) {
      final MenuItem item = sourceMenu.getItem(i);
      if (item.subMenu == null) {
        //android.view.MenuItem androidItem =
        androidMenu.add(item.getText());
        /*
        androidItem.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(android.view.MenuItem menuItem) {
            Event event = new Event();
            event.display = AndroidDisplay.this;
            event.widget = item;
            event.type = SWT.Selection;
            item.listeners.sendEvent(event);
            return true;
          }
        });*/
      } else if (i == 0 && flattenFirst) {
        populateMenu(item.subMenu, androidMenu, false);
      } else {
        android.view.Menu androidSubMenu = androidMenu.addSubMenu(item.getText());
        populateMenu(item.subMenu, androidSubMenu, false);
      }
    }
  }

  @Override
  public void updateMenuBar(Decorations decorations) {
    android.view.Menu androidMenu = navigationView.getMenu();
    androidMenu.clear();
    populateMenu(decorations.menuBar, androidMenu, true);
  }

  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    if (control.peer instanceof SwtViewGroup) {
      ((SwtViewGroup) control.peer).setMeasuredSize(width, height);
    }
  }

  void sendEvent(Widget target, int eventType) {
    if (target.listeners != null) {
      Event event = new Event();
      event.display = this;
      event.widget = target;
      event.type = eventType;
      target.listeners.sendEvent(event);
    }
  }

  @Override
  public void pack(Shell shell) {
    ((View) shell.peer).invalidate();
  }

  @Override
  public void addChild(Composite parent, Control control) {
    ((ViewGroup) parent.peer).addView((View) control.peer);
  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    View view = (View) control.peer;
    switch (eventType) {
      case SWT.Selection:
        if (view instanceof android.widget.Button) {
          ((android.widget.Button) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendEvent(control, eventType);
            }
          });
        }
        break;
    }
  }
}
