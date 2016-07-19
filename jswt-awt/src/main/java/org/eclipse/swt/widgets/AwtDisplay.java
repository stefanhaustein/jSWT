package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.SWTEventListener;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.TimerTask;

public class AwtDisplay extends PlatformDisplay {
  int activeShells = 0;

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
      frame.setLayout(new SwtLayoutManager((Composite) control));

      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          frame.setVisible(false);
          // Hack...
          activeShells--;
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
              }
              if (activeShells == 0) {
                System.exit(0);
              }
            }
          }).start();
        }
      });

      return frame;
    }
    // BEFORE COMPOSITE!
    if (control instanceof Canvas) {
      return new SwtCanvas((Canvas) control);
    }
    if (control instanceof Composite) {
      return new java.awt.Panel(new SwtLayoutManager((Composite) control));
    }
    throw new RuntimeException("Unrecognized component: " + control);
  }

  @Override
  public void openShell(Shell shell) {
    activeShells++;
    ((java.awt.Frame) shell.peer).setVisible(true);
  }

  @Override
  public Rectangle getBounds(Control control) {
    java.awt.Rectangle rect = new java.awt.Rectangle();
    ((java.awt.Component) control.peer).getBounds(rect);

    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    Dimension d = ((Component) control.peer).getPreferredSize();
    return new Point(wHint == SWT.DEFAULT ? d.width : wHint, hHint == SWT.DEFAULT ? d.height : hHint);
  }

  @Override
  public void setBounds(Control control, int x, int y, int width, int height) {
    ((Component) control.peer).setBounds(x, y, width, height);
  }

  @Override
  public String getText(Control control) {
    Object peer = control.peer;
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
    return "";
  }

  @Override
  public void setText(Control control, String text) {
    Object peer = control.peer;
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
  public void pack(Shell shell) {
    ((java.awt.Frame) shell.peer).pack();
  }


  @Override
  public void addChild(Composite parent, Control control) {
    ((java.awt.Container) parent.peer).add((java.awt.Component) control.peer);
  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    java.awt.Component component = (Component) control.peer;
    switch (eventType) {
      case SWT.Selection:
        if (component instanceof java.awt.Button) {
          java.awt.Button button = (java.awt.Button) component;
          if (button.getActionListeners().length == 0) {
            button.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Event event = new Event();
                event.widget = control;
                event.type = eventType;
                control.listeners.sendEvent(event);
              }
            });
          }
        }
    }
  }

  @Override
  public Insets getInsets(Shell shell) {
    java.awt.Insets insets = ((Container) shell.peer).getInsets();
    Insets result = new Insets();
    result.left = insets.left;
    result.top = insets.top;
    result.right = insets.right;
    result.bottom = insets.bottom;
    return result;
  }
}
