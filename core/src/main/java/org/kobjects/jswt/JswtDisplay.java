package org.kobjects.jswt;


import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class JswtDisplay extends Display {

  public abstract Object createPeer(Control control);

  public abstract void openShell(Shell shell, Object peer);

  public abstract Rectangle getBounds(Control control, Object peer);

  public abstract Insets getInsets(Shell shell, Object peer);

  public abstract Point computeSize(Control control, Object peer, int wHint, int hHint, boolean changed);

  public abstract void setBounds(Control control, Object peer, int x, int y, int width, int height);

  public abstract String getText(Control control, Object peer);

  public abstract void setText(Control control, Object peer, String text);

  public abstract void pack(Shell shell, Object peer);

  public abstract void addChild(Composite parent, Object parentPeer, Control control, Object peer1);
}
