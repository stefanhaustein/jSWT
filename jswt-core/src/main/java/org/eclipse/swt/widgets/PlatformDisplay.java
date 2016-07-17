package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class PlatformDisplay extends Display {

  public abstract Object createPeer(Control control);

  public abstract void openShell(Shell shell);

  public abstract Rectangle getBounds(Control control);

  public abstract Insets getInsets(Shell shell);

  public abstract Point computeSize(Control control, int wHint, int hHint, boolean changed);

  public abstract void setBounds(Control control, int x, int y, int width, int height);

  public abstract String getText(Control control);

  public abstract void setText(Control control, String text);

  public abstract void pack(Shell shell);

  public abstract void addChild(Composite parent, Control control);

  public static class Insets {
    public int top;
    public int left;
    public int right;
    public int bottom;
  }
}
