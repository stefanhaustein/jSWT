package org.eclipse.swt.widgets;


import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class PlatformDisplay extends Display {

  public abstract void addChild(Composite parent, Control control);

  public abstract void addListener(Control control, int eventType, Listener listener);

  public abstract Point computeSize(Control control, int wHint, int hHint, boolean changed);

  public abstract Object createControl(Control control);

  public abstract void disposeShell(Shell shell);

  public abstract Rectangle getBounds(Control control);

  public abstract Insets getInsets(Scrollable composite);

  public abstract boolean getSelection(Button button);

  public abstract String getText(Control control);

  public abstract void openShell(Shell shell);

  public abstract void pack(Shell shell);

  public abstract void removeChild(Composite composite, Control child);

  public abstract void setBounds(Control control, int x, int y, int width, int height);

  public abstract void setText(Control control, String text);

  public abstract void setMeasuredSize(Control control, int width, int height);

  public abstract void setSelection(Button button, boolean selected);

  public abstract void showPopupMenu(Menu menu);

  public abstract void updateMenuBar(Decorations decorations);

  public abstract int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation);

  public abstract void redraw(Control control, int x, int y, int w, int h, boolean all);


  public static class Insets {
    public int top;
    public int left;
    public int right;
    public int bottom;
  }
}
