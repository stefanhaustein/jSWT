package org.kobjects.jswt.awt;

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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

public class AwtDisplay extends JswtDisplay {
  @Override
  public Object createPeer(Control control) {
    if (control instanceof Button) {
      return new java.awt.Button();
    }
    if (control instanceof Label) {
      return new java.awt.Label();
    }
    if (control instanceof Text) {
      return new java.awt.TextField();
    }
    if (control instanceof Shell) {
      java.awt.Frame frame = new java.awt.Frame();
      frame.setLayout(new JswtLayoutManager((Composite) control));
      return frame;
    }
    throw new RuntimeException("Unrecognized component: " + control);
  }

  @Override
  public void openShell(Shell shell, Object peer) {
    ((java.awt.Frame) peer).setVisible(true);
  }

  @Override
  public Rectangle getBounds(Control control, Object peer) {
    java.awt.Rectangle rect = new java.awt.Rectangle();
    ((java.awt.Component) peer).getBounds(rect);

    System.out.println("bounds: " + rect);

    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public Point computeSize(Control control, Object peer, int wHint, int hHint, boolean changed) {
    Dimension d = ((Component) peer).getPreferredSize();
    return new Point(wHint == SWT.DEFAULT ? d.width : wHint, hHint == SWT.DEFAULT ? d.height : hHint);
  }

  @Override
  public void setBounds(Control control, Object peer, int x, int y, int width, int height) {
    ((Component) peer).setBounds(x, y, width, height);
  }

  @Override
  public String getText(Control control, Object peer) {
    if (peer instanceof java.awt.TextComponent) {
      return ((java.awt.TextComponent) peer).getText();
    }
    if (peer instanceof java.awt.Button) {
      return ((java.awt.Button) peer).getLabel();
    }
    if (peer instanceof java.awt.Label) {
      return ((java.awt.Label) peer).getText();
    }
    if (peer instanceof java.awt.Frame) {
      return ((java.awt.Frame) peer).getTitle();
    }
    return null;
  }

  @Override
  public void setText(Control control, Object peer, String text) {
    if (peer instanceof java.awt.TextComponent) {
      ((java.awt.TextComponent) peer).setText(text);
    } else if (peer instanceof java.awt.Button) {
      ((java.awt.Button) peer).setLabel(text);
    } else if (peer instanceof java.awt.Label) {
      ((java.awt.Label) peer).setText(text);
    } else if (peer instanceof java.awt.Frame) {
      ((java.awt.Frame) peer).setTitle(text);
    }
  }

  @Override
  public void pack(Shell shell, Object peer) {
    ((java.awt.Frame) peer).pack();
  }

  @Override
  public void addChild(Composite parent, Object parentPeer, Control control, Object peer) {
    ((java.awt.Container) parentPeer).add((java.awt.Component) peer);
  }

  @Override
  public Insets getInsets(Shell shell, Object peer) {
    java.awt.Insets insets = ((Container) peer).getInsets();
    Insets result = new Insets();
    result.left = insets.left;
    result.top = insets.top;
    result.right = insets.right;
    result.bottom = insets.bottom;
    return result;
  }
}
