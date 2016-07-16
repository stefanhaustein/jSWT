package org.kobjects.jswt.android;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.kobjects.jswt.Insets;
import org.kobjects.jswt.JswtDisplay;


public class AndroidDisplay extends JswtDisplay {
  Activity activity;
  public AndroidDisplay(Activity activity) {
    this.activity = activity;
  }



  @Override
  public Object createPeer(Control control) {
    if (control instanceof Composite) {
      return new SwtViewGroup(activity, (Composite) control);
    }
    if (control instanceof Button) {
      return new android.widget.Button(activity);
    }
    if (control instanceof Text) {
      return new android.widget.EditText(activity);
    }
    if (control instanceof Label) {
      return new android.widget.TextView(activity);
    }
    throw new RuntimeException("Unrecognized control: " + control);
  }

  @Override
  public void openShell(Shell shell, Object peer) {
    SwtViewGroup view = (SwtViewGroup) peer;
    ActionBar actionBar = activity.getActionBar();
    String text = view.text;
    if (text != null) {
      actionBar.show();
      actionBar.setTitle(text);
    } else {
      actionBar.hide();
    }
    activity.setContentView((View) peer);
  }

  @Override
  public Rectangle getBounds(Control control, Object peer) {
    View view = (View) peer;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams instanceof SwtViewGroup.LayoutParams) {
      SwtViewGroup.LayoutParams lmlParams = (SwtViewGroup.LayoutParams) layoutParams;
      return new Rectangle(lmlParams.assignedX, lmlParams.assignedY,
          lmlParams.assignedWidth, lmlParams.assignedHeight);
    }

    return new Rectangle(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

  }

  @Override
  public Insets getInsets(Shell shell, Object peer) {
    return new Insets();
  }

  @Override
  public Point computeSize(Control control, Object peer, int wHint, int hHint, boolean changed) {
    View view = (View) peer;
    view.measure(
        wHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | wHint),
        hHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | hHint));
    return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());

  }

  @Override
  public void setBounds(Control control, Object peer, int x, int y, int width, int height) {
    View view = (View) peer;
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
  public String getText(Control control, Object peer) {
    if (peer instanceof TextView) {
      return ((TextView) peer).getText().toString();
    }
    if (peer instanceof SwtViewGroup) {
      return ((SwtViewGroup) peer).text;
    }
    return null;
  }

  @Override
  public void setText(Control control, Object peer, String text) {
    if (peer instanceof TextView) {
      ((TextView) peer).setText(text);
    } else if (peer instanceof SwtViewGroup) {
      ((SwtViewGroup) peer).text = text;
    }
  }

  @Override
  public void pack(Shell shell, Object peer) {

  }

  @Override
  public void addChild(Composite parent, Object parentPeer, Control control, Object peer) {
    ((ViewGroup) parentPeer).addView((View) peer);
  }
}
