package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;

public class SwingDisplay extends PlatformDisplay {
  int activeShells = 0;

  @Override
  public void addItem(Control control, String s, int index) {
    switch (control.getControlType()) {
      case COMBO:
        ((JComboBox<String>) control.peer).insertItemAt(s, index);
        break;
      case LIST:
        ((DefaultListModel) (((JList<String>) control.peer).getModel())).add(index, s);
        break;
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public void addChild(Composite parent, Control control) {
    if (parent instanceof ScrolledComposite) {
      ((javax.swing.JScrollPane) parent.peer).setViewportView((Component) control.peer);
    } else if (!(parent instanceof TabFolder)) {
      ((java.awt.Container) parent.peer).add((java.awt.Component) control.peer);
    }
  }

  @Override
  public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
    ((JTabbedPane) tabFolder.peer).add(new JPanel(), index);
  }

  @Override
  public void asyncExec(Runnable runnable) {
    SwingUtilities.invokeLater(runnable);
  }

  @Override
  public Object createControl(final Control control) {
    switch (control.getControlType()) {
      case BUTTON_CHECKBOX:
      case BUTTON_TOGGLE:
        return new JCheckBox();
      case BUTTON_RADIO: {
        Composite parent = control.getParent();
        ButtonGroup group = (ButtonGroup) parent.radioGroup;
        if (group == null) {
          parent.radioGroup = group = new ButtonGroup();
        }
        JRadioButton result = new JRadioButton();
        group.add(result);
        return result;
      }
      case BUTTON_ARROW: {
        String label;
        if ((control.style & SWT.UP) != 0) {
          label = "^";
        } else if ((control.style & SWT.DOWN) != 0) {
          label = "v";
        } else if ((control.style & SWT.LEFT) != 0) {
          label = "<";
        } else {
          label = ">";
        }
        return new JButton(label);
      }
      case BUTTON_PUSH:
        return new JButton();
      case COMBO:
        return new JComboBox<String>();
      case CANVAS:
        return new SwingSwtCanvas((Canvas) control);
      case COMPOSITE:
        return new JPanel(new SwingSwtLayoutManager((Composite) control));
      case GROUP: {
        JPanel panel = new JPanel(new SwingSwtLayoutManager((Composite) control));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        return panel;
      }
      case LABEL:
        return new javax.swing.JLabel();
      case LIST:
        return new javax.swing.JList<String>(new DefaultListModel<String>());
      case TEXT:
        return new javax.swing.JTextField();
      case SLIDER:
        return new javax.swing.JScrollBar((control.style & SWT.VERTICAL) != 0 ?
                javax.swing.JScrollBar.VERTICAL : javax.swing.JScrollBar.HORIZONTAL);
      case SCALE:
        return new javax.swing.JSlider((control.style & SWT.VERTICAL) != 0 ?
                javax.swing.JScrollBar.VERTICAL : javax.swing.JScrollBar.HORIZONTAL);
      case SCROLLED_COMPOSITE:
        return new javax.swing.JScrollPane();
      case SHELL_DIALOG:
      case SHELL_ROOT: {
        final Shell shell = (Shell) control;
        final java.awt.Window window;
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
      case TAB_FOLDER:
        return new JTabbedPane();
      default:
        throw new RuntimeException("Unrecognized component: " + control);
    }
  }

  @Override
  public Object createImage(int width, int height) {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  @Override
  public GC creatGCForPlatformImage(Object platformImage) {
    return new SwingGC(this, ((BufferedImage) platformImage).createGraphics());
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    Dimension d = ((Component) control.peer).getPreferredSize();
    return new Point(wHint == SWT.DEFAULT ? d.width : wHint, hHint == SWT.DEFAULT ? d.height : hHint);
  }

  @Override
  public void disposeShell(Shell shell) {
    SwingUtilities.getRoot((Component) shell.peer).setVisible(false);
  }

  @Override
  public Rectangle getBounds(Control control) {
    java.awt.Rectangle rect = new java.awt.Rectangle();
    ((java.awt.Component) control.peer).getBounds(rect);

    if (control.getControlType() == Control.ControlType.SHELL_DIALOG
            || control.getControlType() == Control.ControlType.SHELL_ROOT) {
      Component root = SwingUtilities.getRoot((java.awt.Component) control.peer);
      rect.x += root.getX();
      rect.y += root.getY();
    }

    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public Rectangle getImageBounds(Object platformImage) {
    BufferedImage image = (BufferedImage) platformImage;
    return new Rectangle(0, 0, image.getWidth(), image.getHeight());
  }

  @Override
  public boolean isEnabled(Control control) {
    return ((java.awt.Component) control.peer).isEnabled();
  }

  @Override
  public int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation) {
    JScrollPane scrollPane = (JScrollPane) ((Composite) scrolledComposite).peer;
    return orientation == SWT.HORIZONTAL ? scrollPane.getHorizontalScrollBar().getHeight()
            : scrollPane.getVerticalScrollBar().getWidth();
  }

  @Override
  public int getSelection(Control control) {
    switch (control.getControlType()) {
      case SLIDER:
        return ((JScrollBar) control.peer).getValue();
      case SCALE:
        return ((JSlider) control.peer).getValue();
      case COMBO:
        return ((JComboBox<String>) control.peer).getSelectedIndex();
      default:
        throw new RuntimeException("getSelection() not applicable to " + control.getControlType());
    }
  }

  @Override
  public boolean getSelection(Button button) {
    return ((AbstractButton) button.peer).isSelected();
  }

  @Override
  public String getText(Control control) {
    Component peer = (Component) control.peer;
    switch (control.getControlType()) {
      case BUTTON_CHECKBOX:
      case BUTTON_PUSH:
      case BUTTON_RADIO:
        return ((AbstractButton) peer).getText();
      case LABEL:
        return ((JLabel) peer).getText();
      case SHELL_DIALOG:
        return ((JDialog) SwingUtilities.getRoot(peer)).getTitle();
      case SHELL_ROOT:
        return ((JFrame) SwingUtilities.getRoot(peer)).getTitle();
      case TEXT:
        return ((JTextField) peer).getText();
      default:
        System.err.println(control.getControlType() + ".getText()");
        return "";
    }
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
    if (control instanceof Decorations) {
      Component root = SwingUtilities.getRoot(component);
      root.setLocation(x, y);
      component.setSize(width, height);
    } else {
      component.setBounds(x, y, width, height);
    }
  /*  if (component.getParent() instanceof JScrollPane) {
      component.getParent().doLayout();
    }*/
  }

  @Override
  public void setEnabled(Control control, boolean b) {
    ((java.awt.Component) control.peer).setEnabled(b);
  }

  @Override
  public void setFocus(Control control) {
    ((JComponent) control.peer).grabFocus();
  }

  @Override
  public void setText(Control control, String text) {
    Component peer = (Component) control.peer;
    switch (control.getControlType()) {
      case TEXT:
        ((JTextField) peer).setText(text);
        break;
      case BUTTON_CHECKBOX:
      case BUTTON_PUSH:
      case BUTTON_RADIO:
        ((AbstractButton) peer).setText(text);
        break;
      case LABEL:
        ((JLabel) peer).setText(text);
        break;
      case SHELL_ROOT:
        ((JFrame) SwingUtilities.getRoot(peer)).setTitle(text);
        break;
      case SHELL_DIALOG:
        ((JDialog) SwingUtilities.getRoot(peer)).setTitle(text);
        break;
      case GROUP:
        ((JPanel) peer).setBorder(new TitledBorder(text));
        break;
    }
  }

  @Override
  public void setVisible(Control control, boolean visible) {
    ((JComponent) control.peer).setVisible(visible);
  }

  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    // Relevant for Android.
  }

  @Override
  public void setRange(Control control, int minimum, int maximum) {
    switch (control.getControlType()) {
      case SLIDER: {
        JScrollBar scrollbar = (JScrollBar) control.peer;
        scrollbar.setMinimum(minimum);
        scrollbar.setMaximum(maximum);
        break;
      }
      case SCALE: {
        JSlider slider = (JSlider) control.peer;
        slider.setMinimum(minimum);
        slider.setMaximum(maximum);
        break;
      }
    }
  }

  @Override
  public void setSelection(Button button, boolean selected) {
    switch (button.getControlType()) {
      case BUTTON_TOGGLE:
      case BUTTON_CHECKBOX:
      case BUTTON_RADIO:
        ((AbstractButton) button.peer).setSelected(selected);
        break;
    }
  }

  @Override
  public void setSliderProperties(Control slider, int thumb, int increment, int pageIncrement) {
    if (slider.peer instanceof JScrollBar) {
      JScrollBar scrollbar = (JScrollBar) slider.peer;
      scrollbar.setUnitIncrement(increment);
      scrollbar.setBlockIncrement(pageIncrement);
      scrollbar.setVisibleAmount(thumb);
    } /*else if (slider.peer instanceof JSlider) {
      JSlider jSlider = (JSlider) slider.peer;
      jSlider.set
    }*/
  }

  @Override
  public void setSelection(Control control, int selection) {
    switch (control.getControlType()) {
      case SCALE:
        ((JSlider) control.peer).setValue(selection);
        break;
      case SLIDER:
        ((JScrollBar) control.peer).setValue(selection);
        break;
      case COMBO:
        ((JComboBox<String>) control.peer).setSelectedIndex(selection);
        break;
      default:
        throw new RuntimeException("setSelection() not applicable to " + control.getControlType());
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
  public void redraw(Control control, int x, int y, int w, int h, boolean all) {
    ((JComponent) control.peer).repaint(x, y, w, h);
  }

  @Override
  public void removeItems(Combo combo, int start, int end) {
    JComboBox<String> jComboBox = (JComboBox<String>) combo.peer;
    while (end >= start) {
      jComboBox.removeItemAt(end);
      end--;
    }
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
  public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
    JTabbedPane tabbedPane = (JTabbedPane) tabFolder.peer;
    if (tabItem.control != null) {
      tabbedPane.setTabComponentAt(index, (JComponent) tabItem.getControl().peer);
    }
    if (tabItem.text != null) {
      tabbedPane.setTitleAt(index, tabItem.text);
    }
  }

  @Override
  public void setImage(Control control, Image image) {
    switch (control.getControlType()) {
      case BUTTON_PUSH:
        ImageIcon imageIcon = new ImageIcon((java.awt.Image) image.peer);
        ((AbstractButton) control.peer).setIcon(imageIcon);
        break;
      case BUTTON_RADIO:
      case BUTTON_CHECKBOX:
      case BUTTON_TOGGLE:
        break;   // Image would overwrite control
      default:
        System.err.println("FIXME: SwingDisplay.setImage() for " + control.getControlType());  // FIXME
    }
  }

  @Override
  public void setAlignment(Control button, int alignment) {
    System.err.println("FIXME: SwingDisplay.setImage()");  // FIXME
  }

  @Override
  public String getItem(Control control, int i) {
    switch (control.getControlType()) {
      case COMBO:
        return ((JComboBox<String>) control.peer).getItemAt(i);
      case LIST:
        return ((JList<String>) control.peer).getModel().getElementAt(i);
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public Object loadImage(InputStream stream) throws IOException {
    return ImageIO.read(stream);
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
        switch (control.getControlType()) {
          case BUTTON_PUSH:
          case BUTTON_CHECKBOX:
          case BUTTON_RADIO: {
            AbstractButton button = (AbstractButton) component;
            if (!hasListener(button.getActionListeners())) {
              button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            }
            break;
          }/*
          case BUTTON_CHECKBOX:
          case BUTTON_RADIO: {
            AbstractButton button = (AbstractButton) component;
            System.out.println("changeListeners: " + Arrays.toString(button.getChangeListeners()));
            if (!hasListener(button.getChangeListeners())) {
              button.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            }
            break;
          }*/
          case SLIDER: {
            JScrollBar scrollbar = (JScrollBar) component;
            if (scrollbar.getAdjustmentListeners().length == 0) {
              scrollbar.addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            }
            break;
          }
          case SCALE: {
            JSlider slider = (JSlider) component;
            if (slider.getChangeListeners().length == 0) {
              slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            }
            break;
          }
        }
        break;
    }
  }

  private boolean hasListener(EventListener[] listeners) {
    for (EventListener listener: listeners) {
      if (!listener.getClass().getName().startsWith("javax.")) {
        return true;
      }
    }
    return false;
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

  @Override
  public int getItemCount(Control control) {
    switch (control.getControlType()) {
      case COMBO:
        return ((JComboBox<String>) control.peer).getItemCount();
      case LIST:
        return ((JList<String>) control.peer).getModel().getSize();
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public Monitor getMonitor(Control control) {
    System.err.println("FIXME: SwingDisplay.getMonitor()");   // FIXME
    return new Monitor(new Rectangle(0, 0, 2000, 1000), new Rectangle(0, 0, 2000, 1000));
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
