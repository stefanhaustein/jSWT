package org.eclipse.swt.widgets;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.eclipse.swt.R;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class AndroidDisplay extends PlatformDisplay {
  SwtActivity activity;

  public AndroidDisplay(SwtActivity activity) {

    this.activity = activity;


  }


  @Override
  public Object createControl(Control control) {
    if (control instanceof Button) {
      return new android.widget.Button(activity);
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
      return new SwtViewGroup(activity, (Composite) control);
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
    SwtViewGroup view = (SwtViewGroup) shell.peer;
    android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
    String text = view.text;
    if (text != null) {
      actionBar.show();
      actionBar.setTitle(text);
    } else {
      actionBar.hide();
    }
    activity.navigationDrawer.addView(view);
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
    if (peer instanceof SwtViewGroup) {
      return ((SwtViewGroup) peer).text;
    }
    return null;
  }

  @Override
  public void setText(Control control, String text) {
    Object peer = control.peer;
    if (peer instanceof TextView) {
      ((TextView) peer).setText(text);
    } else if (peer instanceof SwtViewGroup) {
      ((SwtViewGroup) peer).text = text;
    }
  }

  private void populateMenu(Menu sourceMenu, android.view.Menu androidMenu, boolean flattenFirst) {
    for (int i = 0; i < sourceMenu.getItemCount(); i++) {
      MenuItem item = sourceMenu.getItem(i);
      if (item.subMenu == null) {
        androidMenu.add(item.getText());
      } else if (i == 0 && flattenFirst) {
        populateMenu(item.subMenu, androidMenu, false);
      } else {
        android.view.Menu androidSubMenu = androidMenu.addSubMenu(item.getText());
        populateMenu(item.subMenu, androidSubMenu, false);
      }
    }
  }

  @Override
  public void setMenuBar(Decorations decorations, Menu menu) {
    android.view.Menu androidMenu = activity.navigationView.getMenu();
    androidMenu.clear();
    populateMenu(menu, androidMenu, true);
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
              Event event = new Event();
              event.type = eventType;
              event.widget = control;
              control.listeners.sendEvent(event);
            }
          });
        }
    }
  }
}
