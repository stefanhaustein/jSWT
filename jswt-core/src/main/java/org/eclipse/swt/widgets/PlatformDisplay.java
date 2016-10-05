package org.eclipse.swt.widgets;


import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.IOException;
import java.io.InputStream;

public abstract class PlatformDisplay extends Display {

  public static PlatformDisplay instance;  // For image loading.

  public abstract void addItem(Control control, String s, int index);

  public abstract void addChild(Composite parent, Control control);

  public abstract void addTab(TabFolder tabFolder, int index, TabItem tabItem);

  public abstract void addListener(Control control, int eventType, Listener listener);

  public abstract Point computeSize(Control control, int wHint, int hHint, boolean changed);

  public abstract Object createControl(Control control);

  public abstract Object createImage(int width, int height);

  public abstract GC creatGCForPlatformImage(Object platformImage);

  public abstract void disposeShell(Shell shell);

  public abstract boolean isEnabled(Control control);

  public abstract Rectangle getBounds(Control control);

  public abstract Rectangle getImageBounds(Object platformImage);

  public abstract Insets getInsets(Scrollable composite);

  public abstract int getItemCount(Control control);

  public abstract Monitor getMonitor(Control control);

  public abstract boolean getSelection(Button button);

  public abstract int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation);

  public abstract int getSelection(Control control);

  public abstract String getText(Control control);

  public abstract Object loadImage(InputStream stream) throws IOException;

  public abstract void openShell(Shell shell);

  public abstract void pack(Shell shell);

  public abstract void redraw(Control control, int x, int y, int w, int h, boolean all);

  public abstract void removeItems(Combo combo, int start, int end);

  public abstract void removeChild(Composite composite, Control child);

  public abstract void setBounds(Control control, int x, int y, int width, int height);

  public abstract void setEnabled(Control control, boolean b);

  public abstract void setFocus(Control control);

  public abstract void setMeasuredSize(Control control, int width, int height);

  public abstract void setRange(Control control, int minimum, int maximum);

  public abstract void setSliderProperties(Control control, int thumb, int increment, int pageIncrement);

  public abstract void setSelection(Button button, boolean selected);

  public abstract void setSelection(Control control, int selection);

  public abstract void setText(Control control, String text);

  public abstract void setVisible(Control control, boolean visible);

  public abstract void showPopupMenu(Menu menu);

  public abstract void updateMenuBar(Decorations decorations);

  public abstract void updateTab(TabFolder tabFolder, int index, TabItem tabItem);

  public abstract void setImage(Control control, Image image);

  public abstract void setAlignment(Control button, int alignment);

  public abstract String getItem(Control control, int i);



  public static class Insets {
    public int top;
    public int left;
    public int right;
    public int bottom;
  }
}
