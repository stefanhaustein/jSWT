package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class SwingDisplay extends PlatformDisplay {
  int activeShells = 0;

  HashMap<Widget, CheckboxGroup> checkBoxGroupMap = new HashMap<>();

  @Override
  public void asyncExec(Runnable runnable) {
    SwingUtilities.invokeLater(runnable);
  }

  @Override
  public Object createControl(final Control control) {
    if (control instanceof Button) {
      switch (control.style) {
        case SWT.CHECK:
          return new JCheckBox();
        case SWT.RADIO: {/*
          CheckboxGroup group = checkBoxGroupMap.get(control.parent);
          if (group == null) {
            group = new CheckboxGroup();
            checkBoxGroupMap.put(control.parent, group);
          }*/
          return new JRadioButton();
        }
        default:
          return new JButton();
      }
    }
    if (control instanceof Label) {
      return new javax.swing.JLabel();
    }
    if (control instanceof Text) {
      return new javax.swing.JTextField();
    }
    if (control instanceof Slider || control instanceof Scale) {
      return new javax.swing.JScrollBar((control.style & SWT.VERTICAL) != 0 ?
              javax.swing.JScrollBar.VERTICAL : javax.swing.JScrollBar.HORIZONTAL);
    }
    if (control instanceof ScrolledComposite) {
      return new javax.swing.JScrollPane();
    }
    if (control instanceof Shell) {
      Shell shell = (Shell) control;
      java.awt.Window window;
      if (shell.parent != null) {
        boolean modal = (shell.style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL)) != 0;
        window = new javax.swing.JDialog((javax.swing.JFrame) SwingUtilities.getRoot((Component) ((Shell) shell.parent).peer), modal);
      } else {
        window = new javax.swing.JFrame();
      }
      Container contentPane = ((RootPaneContainer) window).getContentPane();
      contentPane.setLayout(new SwingSwtLayoutManager((Composite) control));
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
      return contentPane;
    }
    // Must be last because many components inherit from Composite / Canvas
    if (control instanceof Canvas) {
      return new SwingSwtCanvas((Canvas) control);
    }
    if (control instanceof Composite) {
      return new JPanel(new SwingSwtLayoutManager((Composite) control));
    }
    throw new RuntimeException("Unrecognized component: " + control);
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    Dimension d = ((JComponent) control.peer).getPreferredSize();
    return new Point(wHint == SWT.DEFAULT ? d.width : wHint, hHint == SWT.DEFAULT ? d.height : hHint);
  }

  @Override
  public void disposeShell(Shell shell) {
    ((Window) shell.peer).setVisible(false);
  }

  @Override
  public Rectangle getBounds(Control control) {
    java.awt.Rectangle rect = new java.awt.Rectangle();
    ((java.awt.Component) control.peer).getBounds(rect);

    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public boolean getEnabled(Control control) {
    return ((java.awt.Component) control.peer).isEnabled();
  }

  @Override
  public boolean getSelection(Button button) {
    return (button.peer instanceof JCheckBox) ? ((JCheckBox) button.peer).isSelected() : false;
  }

  @Override
  public String getText(Control control) {
    Object peer = control.peer;
    if (peer instanceof JTextField) {
      return ((JTextField) peer).getText();
    }
    if (peer instanceof JButton) {
      return ((JButton) peer).getText();
    }
    if (peer instanceof JLabel) {
      return ((JLabel) peer).getText();
    }
    if (peer instanceof Window) {
      return ((Window) peer).getName();
    }
    if (peer instanceof JCheckBox) {
      return ((JCheckBox) peer).getText();
    }
    return "";
  }

  private void menuAddAll(Menu source, JMenu destination) {
    for (int i = 0; i < source.getItemCount(); i++) {
      final MenuItem item = source.getItem(i);
      if (item.subMenu == null) {
        JMenuItem awtItem = new JMenuItem(item.text);
        awtItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            item.notifyListeners(SWT.Selection, null);
          }
        });
        destination.add(awtItem);
      } else {
        JMenu awtMenu = new JMenu(item.text);
        menuAddAll(item.subMenu, awtMenu);
      }
    }
  }

  private void menuAddAll(Menu source, JPopupMenu destination) {
    for (int i = 0; i < source.getItemCount(); i++) {
      final MenuItem item = source.getItem(i);
      if (item.subMenu == null) {
        JMenuItem awtItem = new JMenuItem(item.text);
        awtItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            item.notifyListeners(SWT.Selection, null);
          }
        });
        destination.add(awtItem);
      } else {
        JMenu awtMenu = new JMenu(item.text);
        menuAddAll(item.subMenu, awtMenu);
      }
    }
  }


  @Override
  public void openShell(Shell shell) {
    if (shell.parent == null) {
      activeShells++;
    }
    SwingUtilities.getRoot((Component) shell.peer).setVisible(true);
  }

  @Override
  public void setBounds(Control control, int x, int y, int width, int height) {
    Component component = (Component) control.peer;
  /*  if (control instanceof Decorations) {
      component = SwingUtilities.getRoot(component);
    }*/
    component.setBounds(x, y, width, height);
  /*  if (component.getParent() instanceof JScrollPane) {
      component.getParent().doLayout();
    }*/
  }

  @Override
  public void setText(Control control, String text) {
    Object peer = control.peer;
    if (peer instanceof JTextField) {
      ((JTextField) peer).setText(text);
    } else if (peer instanceof JButton) {
      ((JButton) peer).setText(text);
    } else if (peer instanceof JLabel) {
      ((JLabel) peer).setText(text);
    } else if (peer instanceof JFrame) {
      ((JFrame) peer).setTitle(text);
    } else if (peer instanceof JDialog) {
      ((JDialog) peer).setTitle(text);
    } else if (peer instanceof JCheckBox) {
      ((JCheckBox) peer).setText(text);
    }
  }

  @Override
  public void updateMenuBar(Decorations decorations) {
    JMenuBar awtMenuBar = new JMenuBar();

    for (int i = 0; i < decorations.menuBar.getItemCount(); i++) {
      MenuItem item = decorations.menuBar.getItem(i);
      JMenu awtSubMenu = new JMenu(item.text);
      awtMenuBar.add(awtSubMenu);
      if (item.subMenu != null) {
        menuAddAll(item.subMenu, awtSubMenu);
      }
    }
    ((JFrame) SwingUtilities.getRoot((Component) decorations.peer)).setJMenuBar(awtMenuBar);
  }

  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    // Relevant for Android.
  }

  @Override
  public void setRange(Control control, int minimum, int maximum) {
    JComponent component = (JComponent) control.peer;
    if (component instanceof JScrollBar) {

      // Add one to account for the thumb size.
      if (control instanceof Scale) {
        maximum++;
      }

      JScrollBar scrollbar = (JScrollBar) component;
      scrollbar.setMinimum(minimum);
      scrollbar.setMaximum(maximum);
    }
  }

  @Override
  public void setSelection(Button button, boolean selected) {
    if (button.peer instanceof JCheckBox) {
        ((JCheckBox) button.peer).setSelected(selected);
    }
  }

  @Override
  public void setSliderProperties(Control slider, int thumb, int increment, int pageIncrement) {
    JScrollBar scrollbar = (JScrollBar) slider.peer;
    scrollbar.setUnitIncrement(increment);
    scrollbar.setBlockIncrement(pageIncrement);
    scrollbar.setVisibleAmount(thumb);
  }

  @Override
  public void setSelection(Control control, int selection) {
    Component component = (Component) control.peer;
    if (component instanceof JScrollBar) {
      JScrollBar scrollbar = (JScrollBar) component;
      if (scrollbar.getOrientation() == JScrollBar.VERTICAL && control instanceof Scale) {
        selection = scrollbar.getMaximum() - 1 - (selection - scrollbar.getMinimum());
      }
      scrollbar.setValue(selection);
    }
  }

  @Override
  public void showPopupMenu(Menu menu) {
    JPopupMenu popupMenu = new JPopupMenu();
    menuAddAll(menu, popupMenu);
    JComponent parent = (JComponent) ((Control) menu.parent).peer;
   // parent.add(popupMenu);
    popupMenu.show(parent, parent.getWidth() / 2, parent.getHeight() / 2);
  }

  @Override
  public int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation) {
    JScrollPane scrollPane = (JScrollPane) ((Composite) scrolledComposite).peer;
    return orientation == SWT.HORIZONTAL ? scrollPane.getHorizontalScrollBar().getHeight()
            : scrollPane.getVerticalScrollBar().getWidth();
  }

  @Override
  public int getSelection(Control control) {
    java.awt.Component component = (java.awt.Component) control.peer;
    if (component instanceof JScrollBar) {
      JScrollBar scrollbar = (JScrollBar) component;
      int selection = scrollbar.getValue();
      if (scrollbar.getOrientation() == JScrollBar.VERTICAL && control instanceof Scale) {
        selection = scrollbar.getMaximum() - 1 - (selection - scrollbar.getMinimum());
      }
      return selection;
    }
    return 0;
  }

  @Override
  public void redraw(Control control, int x, int y, int w, int h, boolean all) {
    ((JComponent) control.peer).repaint(x, y, w, h);
  }

  @Override
  public void pack(Shell shell) {
    ((Window) SwingUtilities.getRoot((Component) shell.peer)).pack();
  }

  @Override
  public void removeChild(Composite composite, Control child) {
      ((Container) composite.peer).remove((JComponent) child.peer);
  }


  @Override
  public void addChild(Composite parent, Control control) {
    if (parent instanceof ScrolledComposite) {
      ((javax.swing.JScrollPane) parent.peer).setViewportView((Component) control.peer);
    } else {
      ((java.awt.Container) parent.peer).add((java.awt.Component) control.peer);
    }
  }

  void notifyListeners(Control control, int type, Object awtEvent) {
      Event event = new Event();
      event.type = type;
      event.widget = control;
      event.display = this;

      if (awtEvent instanceof java.awt.event.MouseEvent) {
          java.awt.event.MouseEvent awtMouseEvent = (java.awt.event.MouseEvent) awtEvent;
          event.x = awtMouseEvent.getX();
          event.y = awtMouseEvent.getY();
          event.count = awtMouseEvent instanceof java.awt.event.MouseWheelEvent
                  ? ((java.awt.event.MouseWheelEvent) awtMouseEvent).getWheelRotation()
                  : awtMouseEvent.getClickCount();
      }

      control.notifyListeners(type, event);
  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    Component component = (Component) control.peer;
    switch (eventType) {
      case SWT.Modify:
        if (component instanceof JTextField) {
          JTextField textComponent = (JTextField) component;
         // if (textComponent.getDocument()..length == 0) {
            textComponent.getDocument().addDocumentListener(new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                notifyListeners(control, SWT.Modify, e);
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                notifyListeners(control, SWT.Modify, e);
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                notifyListeners(control, SWT.Modify, e);
              }

            });
        //  }
        }
      case SWT.MouseWheel:
        if (component.getMouseWheelListeners().length == 0) {
          component.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
              notifyListeners(control, SWT.MouseWheel, e);
            }
          });
        }
        break;
      case SWT.MouseMove:
        if (component.getMouseMotionListeners().length == 0) {
          component.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
              notifyListeners(control, SWT.MouseMove, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
              notifyListeners(control, SWT.MouseMove, e);
            }
          });
        }
        break;
      case SWT.MouseDoubleClick:
      case SWT.MouseDown:
      case SWT.MouseUp:
      case SWT.MouseEnter:
      case SWT.MouseExit:
        if (component.getMouseListeners().length == 0) {
          component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                control.notifyListeners(SWT.MouseDoubleClick, null);
              }
            }

            @Override
            public void mousePressed(MouseEvent e) {
              notifyListeners(control, SWT.MouseDown, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
              notifyListeners(control, SWT.MouseUp, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              notifyListeners(control, SWT.MouseEnter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
              notifyListeners(control, SWT.MouseExit, e);
            }
          });
        }
      case SWT.Move:
      case SWT.Resize:
      case SWT.Show:
      case SWT.Hide:
        if (component.getComponentListeners().length == 0) {
          component.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
              notifyListeners(control, SWT.Resize, e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
              notifyListeners(control, SWT.Move, e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
              notifyListeners(control, SWT.Show, e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
              notifyListeners(control, SWT.Hide, e);
            }
          });
        }
        break;
      case SWT.Selection:
        if (component instanceof JButton) {
          JButton button = (JButton) component;
          if (button.getActionListeners().length == 0) {
            button.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                notifyListeners(control, SWT.Selection, e);
              }
            });
          }
        } else if (component instanceof JScrollBar) {
          JScrollBar scrollbar = (JScrollBar) component;
          if (scrollbar.getAdjustmentListeners().length == 0) {
            scrollbar.addAdjustmentListener(new AdjustmentListener() {
              @Override
              public void adjustmentValueChanged(AdjustmentEvent e) {
                notifyListeners(control, SWT.Selection, e);
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

      if (scrollable.peer instanceof JScrollPane) {
        JScrollPane scrollPane = (JScrollPane) scrollable.peer;
    /*    if (result.right >= scrollPane.getVScrollbarWidth()) {
          result.right -= scrollPane.getVScrollbarWidth();
        }
        if (result.bottom > scrollPane.getHScrollbarHeight()) {
          result.bottom -= scrollPane.getHScrollbarHeight();
        }*/
      }

    }
    return result;
  }

/*
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
*/
}
