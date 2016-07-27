package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.SWTEventListener;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimerTask;

public class AwtDisplay extends PlatformDisplay {
  int activeShells = 0;

  HashMap<Widget, CheckboxGroup> checkBoxGroupMap = new HashMap<>();

  @Override
  public Object createControl(final Control control) {
    if (control instanceof Button) {
      switch (control.style) {
        case SWT.CHECK:
          return new Checkbox();
        case SWT.RADIO: {
          CheckboxGroup group = checkBoxGroupMap.get(control.parent);
          if (group == null) {
            group = new CheckboxGroup();
            checkBoxGroupMap.put(control.parent, group);
          }
          return new Checkbox(null, group, false);
        }
        default:
          return new java.awt.Button();
      }
    }
    if (control instanceof Label) {
      return new java.awt.Label();
    }
    if (control instanceof Text) {
      return new java.awt.TextField();
    }
    if (control instanceof Slider) {
      return new java.awt.Scrollbar((control.style & SWT.VERTICAL) != 0 ?
              java.awt.Scrollbar.VERTICAL : java.awt.Scrollbar.HORIZONTAL);
    }
    if (control instanceof ScrolledComposite) {
      return new java.awt.ScrollPane();
    }
    if (control instanceof Shell) {
      Shell shell = (Shell) control;
      java.awt.Window window;
      if (shell.parent != null) {
        boolean modal = (shell.style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL)) != 0;
        window = new java.awt.Dialog((java.awt.Frame) ((Shell) shell.parent).peer, modal);
      } else {
        window = new java.awt.Frame();
      }
      window.setLayout(new SwtLayoutManager((Composite) control));
      window.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          window.setVisible(false);
          // Hack...
          if (shell.parent == null) {
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
        }
      });
      return window;
    }
    // Must be last because many components inherit from Composite / Canvas
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
    if (shell.parent == null) {
      activeShells++;
    }
    ((java.awt.Component) shell.peer).setVisible(true);
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
  public boolean getSelection(Button button) {
    return (button.peer instanceof Checkbox) ? ((Checkbox) button.peer).getState() : false;
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
    if (peer instanceof java.awt.Checkbox) {
      return ((java.awt.Checkbox) peer).getLabel();
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
    } else if (peer instanceof java.awt.Dialog) {
      ((java.awt.Dialog) peer).setTitle(text);
    } else if (peer instanceof java.awt.Checkbox) {
      ((java.awt.Checkbox) peer).setLabel(text);
    }

  }

  private void menuAddAll(Menu source, java.awt.Menu destination) {
    for (int i = 0; i < source.getItemCount(); i++) {
      final MenuItem item = source.getItem(i);
      if (item.subMenu == null) {
        java.awt.MenuItem awtItem = new java.awt.MenuItem(item.text);
        awtItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            item.notifyListeners(SWT.Selection, null);
          }
        });
        destination.add(awtItem);
      } else {
        java.awt.Menu awtMenu = new java.awt.Menu(item.text);
        menuAddAll(item.subMenu, awtMenu);
      }
    }
  }

  @Override
  public void updateMenuBar(Decorations decorations) {
    MenuBar awtMenuBar = new MenuBar();

    for (int i = 0; i < decorations.menuBar.getItemCount(); i++) {
      MenuItem item = decorations.menuBar.getItem(i);
      java.awt.Menu awtSubMenu = new java.awt.Menu(item.text);
      awtMenuBar.add(awtSubMenu);
      if (item.subMenu != null) {
        menuAddAll(item.subMenu, awtSubMenu);
      }
    }
    ((Frame) decorations.peer).setMenuBar(awtMenuBar);
  }

  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    // Relevant for Android.
  }

  @Override
  public void setSelection(Button button, boolean selected) {
    if (button.peer instanceof Checkbox) {
        ((Checkbox) button.peer).setState(selected);
    }
  }

    @Override
  public void pack(Shell shell) {
    ((java.awt.Window) shell.peer).pack();
  }


  @Override
  public void addChild(Composite parent, Control control) {
    ((java.awt.Container) parent.peer).add((java.awt.Component) control.peer);
  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    java.awt.Component component = (Component) control.peer;
    switch (eventType) {
      case SWT.Move:
      case SWT.Resize:
      case SWT.Show:
      case SWT.Hide:
        if (component.getComponentListeners().length == 0) {
          component.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
              control.notifyListeners(SWT.Resize, null);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
              control.notifyListeners(SWT.Move, null);
            }

            @Override
            public void componentShown(ComponentEvent e) {
              control.notifyListeners(SWT.Show, null);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
              control.notifyListeners(SWT.Hide, null);
            }
          });
        }
        break;
      case SWT.Selection:
        if (component instanceof java.awt.Button) {
          java.awt.Button button = (java.awt.Button) component;
          if (button.getActionListeners().length == 0) {
            button.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                control.notifyListeners(SWT.Selection, null);
              }
            });
          }
        }
        break;
    }
  }

  @Override
  public Insets getInsets(Scrollable scrollable) {
    Insets result = new Insets();
    if (scrollable instanceof Composite) {
      java.awt.Insets insets = ((Container) scrollable.peer).getInsets();
      result.left = insets.left;
      result.top = insets.top;
      result.right = insets.right;
      result.bottom = insets.bottom;
    }
    return result;
  }

  static class RadioButton extends Checkbox {
    Checkbox other;
    RadioButton() {
      super(null, new CheckboxGroup(), false);
      other = new Checkbox(null, getCheckboxGroup(), false);
    }

    @Override
    public void setState(boolean state) {
      if (!state) {
        other.setState(true);
      } else {
        super.setState(true);
      }
    }
  }

}
