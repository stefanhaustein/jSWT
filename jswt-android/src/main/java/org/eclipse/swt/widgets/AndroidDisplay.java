package org.eclipse.swt.widgets;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.PopupMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.InputStream;


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
            result.notifyListeners(SWT.Selection, null);
            navigationDrawer.closeDrawers();
            return true;
          }
        }
        return false;
      }
    });
  }

  @Override
  public void asyncExec(Runnable runnable) {
    activity.runOnUiThread(runnable);
  }

  public void handleRadioGroup(Button button, boolean selected) {
    if (button.style != SWT.RADIO || !selected) {
      return;
    }
    CompoundButton androidButton = (CompoundButton) button.peer;
    if (button.style == SWT.RADIO) {
      Composite parent = (Composite) button.parent;
      for (Control child: parent.children) {
        if (child != button && child.style == SWT.RADIO && (child instanceof Button)) {
          ((CompoundButton) child.peer).setChecked(false);
        }
      }
    }
  }


  @Override
  public Object createControl(final Control control) {
    switch (control.getControlType()) {
      case BUTTON_PUSH:
        return new AppCompatButton(activity);
      case BUTTON_CHECKBOX:
        return new AppCompatCheckBox(activity);
      case BUTTON_RADIO: {
        RadioButton radioButton = new AppCompatRadioButton(activity);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            handleRadioGroup((Button) control, b);
            control.notifyListeners(SWT.Selection, null);
          }
        });
        return radioButton;
      }
      case TEXT: {
        AppCompatEditText editText = new AppCompatEditText(activity);
        if ((control.style & SWT.MULTI) == 0) {
          editText.setMaxLines(1);
        }
        return editText;
      }
      case LABEL:
        return new AppCompatTextView(activity);
      case SCROLLED_COMPOSITE:
        return new android.widget.ScrollView(activity);
      case SCALE:
      case SLIDER:
        return new android.widget.SeekBar(activity);
      case SHELL_DIALOG:
      case SHELL_ROOT:
        return new SwtShellView(activity, (Shell) control);
      case CANVAS:
        return new SwtCanvasView(activity, (Canvas) control);
      case COMPOSITE:
        return new SwtViewGroup(activity, (Composite) control);
      default:
        throw new RuntimeException("Unrecognized control type " + control.getControlType() + " for " + control);
    }
  }

  @Override
  public void disposeShell(Shell shell) {
    SwtShellView shellView = (SwtShellView) shell.peer;
    if (shellView.dialog != null) {
      shellView.dialog.hide();
      shellView.dialog = null;
    }
  }

  @Override
  public boolean isEnabled(Control control) {
    return true;                                 // FIXME
  }

  @Override
  public void openShell(Shell shell) {
    SwtShellView view = (SwtShellView) shell.peer;
    if (view.dialogBuilder != null) {
      view.dialog = view.dialogBuilder.show();
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
  public Rectangle getImageBounds(Object platformImage) {
    throw new RuntimeException("NYI");

  }

  @Override
  public Insets getInsets(Scrollable scrollable) {
    return new Insets();
  }

  @Override
  public int getItemCount(Combo combo) {
    throw new RuntimeException("NYI");

  }

  @Override
  public boolean getSelection(Button button) {
    return (button.peer instanceof CompoundButton) ? ((CompoundButton) button.peer).isChecked() : false;
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    View view = (View) control.peer;
    if (view instanceof EditText) {
      ((EditText) view).setMaxWidth(Integer.MAX_VALUE);
    }
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
    if (view instanceof EditText) {
      ((EditText) view).setMaxWidth(width);
    }
  }

  @Override
  public void setEnabled(Control control, boolean b) {
    throw new RuntimeException("NYI");

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
  public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
    throw new RuntimeException("NYI");

  }

  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    if (control.peer instanceof SwtViewGroup) {
      ((SwtViewGroup) control.peer).setMeasuredSize(width, height);
    } else {
      ((View) control.peer).measure(View.MeasureSpec.EXACTLY | width, View.MeasureSpec.EXACTLY | height);
    }
  }

  @Override
  public void setRange(Control control, int minimum, int maximum) {
    ((android.widget.SeekBar) control.peer).setMax(maximum);
    if (minimum != 0) {
      System.err.println("FIXME: setRanger(): Minimum ignored!");   // FIXME
    }
  }

  @Override
  public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
    System.err.println("FIXME: setSliderProperties()");   // FIXME

  }

  @Override
  public void setSelection(Button button, boolean selected) {
    if (!(button.peer instanceof CompoundButton)) {
      return;
    }
    handleRadioGroup(button, selected);
    ((CompoundButton) button.peer).setChecked(selected);
  }

  @Override
  public void setSelection(Control control, int selection) {
    switch (control.getControlType()) {
      case SLIDER:
      case SCALE:
        ((android.widget.SeekBar) control.peer).setProgress(selection);
        break;
      default:
        throw new RuntimeException("NYI: setSelection() for " + control.getControlType());
    }
  }

  @Override
  public void showPopupMenu(final Menu menu) {
    View anchor = (View) ((Control) menu.getParent()).peer;
    PopupMenu popupMenu = new PopupMenu(activity, anchor);
    populateMenu(menu, popupMenu.getMenu(), false);
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(android.view.MenuItem item) {
        findMenuItem(menu, item.getTitle().toString());
        return true;
      }
    });
    popupMenu.show();
  }

  @Override
  public int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation) {
    System.err.println("getScrollBarSize(): Returning fake value 1");  // FIXME
    return 1;
  }

  @Override
  public int getSelection(Control control) {
    switch (control.getControlType()) {
      case SCALE:
      case SLIDER:
        return ((android.widget.SeekBar) control.peer).getProgress();
      default:
        throw new RuntimeException("NYI: getSelection() for " + control.getControlType());
    }
  }

  @Override
  public void redraw(Control control, int x, int y, int w, int h, boolean all) {
    ((View) control.peer).invalidate(x, y, x+w, y+h);
  }

  @Override
  public void removeItems(Combo combo, int start, int end) {
    throw new RuntimeException("NYI");

  }

  @Override
  public void pack(Shell shell) {
    ((View) shell.peer).invalidate();
  }

  @Override
  public void removeChild(Composite composite, Control child) {
    ((ViewGroup) composite.peer).removeView((View) child.peer);
  }

  @Override
  public void addItem(Combo combo, String s, int index) {
    throw new RuntimeException("NYI");
  }

  @Override
  public void addChild(Composite parent, Control control) {
    ((ViewGroup) parent.peer).addView((View) control.peer);
  }

  @Override
  public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
    throw new RuntimeException("NYI");

  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    View view = (View) control.peer;
    switch (eventType) {
      case SWT.MouseDown:
      case SWT.MouseUp:
        view.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
              case MotionEvent.ACTION_DOWN:
              case MotionEvent.ACTION_POINTER_DOWN:
                control.notifyListeners(SWT.MouseDown, null);
                return true;
              case MotionEvent.ACTION_UP:
              case MotionEvent.ACTION_POINTER_UP:
                control.notifyListeners(SWT.MouseUp, null);
                return true;
            }
            return false;
          }
        });
        break;

      case SWT.Selection:
        if (view instanceof android.widget.Button) {
          ((android.widget.Button) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              control.notifyListeners(eventType, null);
            }
          });
        } else if (view instanceof android.widget.CheckBox) {
          ((android.widget.CheckBox) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              control.notifyListeners(SWT.Selection, null);
            }
          });
        }
        break;
    }
  }

  public Image createImage(int width, int height) {
    throw new RuntimeException("NYI");
  }

  @Override
  public GC creatGCForPlatformImage(Object platformImage) {
    throw new RuntimeException("NYI");
  }

  public Image loadImage(InputStream is) {
    throw new RuntimeException("NYI");
  }

}
