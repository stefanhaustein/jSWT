package org.eclipse.swt.widgets;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
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

public class SwingDisplay extends PlatformDisplay {
  int activeShells = 0;
  private Monitor monitor;


  @Override
  public void addItem(Control control, String s, int index) {
    switch (control.getControlType()) {
      case COMBO:
        ((JComboBox<String>) control.peer).insertItemAt(s, index);
        break;
      case LIST:
        ((DefaultListModel) (getList(control).getModel())).add(index, s);
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


  void unsupported(Control control, String method) {
    System.err.println("SwingDisplay." + method + "() unsupported for type " + control.getControlType().name());
  }

  @Override
  public Object createControl(final Control control) {
    switch (control.getControlType()) {
      case BUTTON:
        if ((control.style & SWT.RADIO) != 0) {
          Composite parent = control.getParent();
          ButtonGroup group = (ButtonGroup) parent.radioGroup;
          if (group == null) {
            parent.radioGroup = group = new ButtonGroup();
          }
          JRadioButton result = new JRadioButton();
          group.add(result);
          return result;
        }
        if ((control.style & (SWT.CHECK | SWT.TOGGLE)) != 0) {
          return new JCheckBox();
        }
        return new JButton();
      case COMBO: {
        JComboBox<String> result = new JComboBox<>();
        if ((control.style & SWT.READ_ONLY) == 0) {
          result.setEditable(true);
        }
        return result;
      }
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
      case LIST: {
        JList<String> list = new JList<String>(new DefaultListModel<String>());
        list.setSelectionMode((control.style & SWT.MULTI) == 0 ? ListSelectionModel.SINGLE_SELECTION
                : ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return new JScrollPane(list);
      }
      case TEXT:
        return new javax.swing.JTextField();
      case PROGRESS_BAR:
        return new JProgressBar();
      case SLIDER:
        return new javax.swing.JScrollBar((control.style & SWT.VERTICAL) != 0 ?
                javax.swing.JScrollBar.VERTICAL : javax.swing.JScrollBar.HORIZONTAL);
      case SCALE:
        return new javax.swing.JSlider((control.style & SWT.VERTICAL) != 0 ?
                javax.swing.JScrollBar.VERTICAL : javax.swing.JScrollBar.HORIZONTAL);
      case SCROLLED_COMPOSITE:
        return new javax.swing.JScrollPane();
      case SHELL: {
        final Shell shell = (Shell) control;
        final java.awt.Window window;
        if (shell.parent != null) {
          boolean modal = (shell.style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL)) != 0;
          window = new javax.swing.JDialog((javax.swing.JFrame) SwingUtilities.getWindowAncestor((Component) ((Shell) shell.parent).peer), modal);
        } else {
          window = new javax.swing.JFrame();
        }
        window.pack();  // Makes sure the insets are set.
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
      case SPINNER:
        return new JSpinner();
      case TAB_FOLDER:
        return new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
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
  public Rectangle getBounds(Control control) {
    java.awt.Rectangle rect = new java.awt.Rectangle();
    if (control.getControlType() == Control.ControlType.SHELL) {
      Component root = SwingUtilities.getRoot((java.awt.Component) control.peer);
      root.getBounds(rect);
    } else {
      ((Component) control.peer).getBounds(rect);
      if (control.getParent().getControlType() == Control.ControlType.SHELL) {
        Container container = (Container) control.getParent().peer;
        Window root = SwingUtilities.getWindowAncestor((java.awt.Component) control.peer);
        // Adjust for frame insets and menu bar only.
        if (root != null) {
          java.awt.Insets insets = root.getInsets();
          rect.x += insets.top + container.getY();
          rect.y += insets.left;
        }
      }
    }
    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public Color getForeground(Control control) {
    java.awt.Color awtColor = ((Component) control.peer).getForeground();
    return awtColor == null ? null : new Color(this, awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha());
  }

  @Override
  int getFocusIndex(List list) {
    unsupported(list, "getFocusIndex");
    return 0;
  }

  @Override
  boolean getGrayed(Button control) {
    unsupported(control, "getGrayed");
    return false;
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
  public Color getBackground(Control control) {
    java.awt.Color awtColor = ((Component) control.peer).getBackground();
    return awtColor == null ? null : new Color(this, awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha());
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
      case BUTTON:
        return ((AbstractButton) control.peer).isSelected() ? 1 : 0;
      case COMBO:
        return ((JComboBox<String>) control.peer).getSelectedIndex();
      case PROGRESS_BAR:
        return ((JProgressBar) control.peer).getValue();
      case SPINNER:
        return (Integer) ((JSpinner) control.peer).getValue();
      case SLIDER:
        return ((JScrollBar) control.peer).getValue();
      case LIST:
        return getList(control).getSelectedIndex();
      case SCALE:
        return ((JSlider) control.peer).getValue();
      default:
        throw new RuntimeException("getSelection() not applicable to " + control.getControlType());
    }
  }

  @Override
  public String getText(Control control) {
    return getJTextComponent(control).getText();
  }

  JMenuItem createMenuItem(final MenuItem swtMenuItem) {
    if ((swtMenuItem.style & SWT.CASCADE) != 0) {
      JMenu awtMenu = new JMenu();
      swtMenuItem.peer = awtMenu;
      menuAddAll(swtMenuItem.subMenu, awtMenu);
      updateItem(swtMenuItem);
      return awtMenu;
    }
    JMenuItem awtItem = (swtMenuItem.style & SWT.CHECK) != 0
              ? new JCheckBoxMenuItem() : new JMenuItem();
    swtMenuItem.peer = awtItem;
    awtItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          swtMenuItem.notifyListeners(SWT.Selection, null);
        }
      });
    updateItem(swtMenuItem);
    return awtItem;
  }

  private void menuAddAll(Menu source, JMenu destination) {
    if (source != null) {
      for (int i = 0; i < source.getItemCount(); i++) {
        destination.add(createMenuItem(source.getItem(i)));
      }
    }
  }

  private void menuAddAll(Menu source, JPopupMenu destination) {
    if (source != null) {
      for (int i = 0; i < source.getItemCount(); i++) {
        destination.add(createMenuItem(source.getItem(i)));
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
    if (control.getControlType() == Control.ControlType.SHELL) {
      Window root = SwingUtilities.getWindowAncestor(component);
      root.setLocation(x, y);
      root.setSize(width, height);
    } else {
      if (control.getParent().getControlType() == Control.ControlType.SHELL) {
        // Adjust for frame insets and menu bar only.
        Container container = (Container) control.getParent().peer;
        Window window = SwingUtilities.getWindowAncestor(component);
        if (window != null) {
          java.awt.Insets insets = window.getInsets();
          x -= insets.left;
          y -= insets.top + container.getY();
        }
      }
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
  public void setForeground(Control control, Color color) {
    ((Component) control.peer).setForeground(color == null ? null :
            new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
  }

  @Override
  void setGrayed(Button button) {
    // Not supported by Swing.
  }

  @Override
  public void setText(Control control, String text) {
    Component peer = (Component) control.peer;
    switch (control.getControlType()) {
      case TEXT:
        ((JTextField) peer).setText(text);
        break;
      case BUTTON:
        ((AbstractButton) peer).setText(removeAccelerators(text));
        break;
      case LABEL:
        if ((control.style & SWT.WRAP) != 0) {
          ((JLabel) peer).setText("<html>" + text.replace("\n", "<br>") + "</html>");
        } else {
          ((JLabel) peer).setText(text);
        }
        break;
      case SHELL:
        if (control.getParent() == null) {
          ((JFrame) SwingUtilities.getWindowAncestor(peer)).setTitle(text);
        } else {
          ((JDialog) SwingUtilities.getWindowAncestor(peer)).setTitle(text);
        }
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
      case BUTTON:
        ((AbstractButton) control.peer).setSelected(selection != 0);
        break;
      case SCALE:
        ((JSlider) control.peer).setValue(selection);
        break;
      case SLIDER:
        ((JScrollBar) control.peer).setValue(selection);
        break;
      case COMBO:
        ((JComboBox<String>) control.peer).setSelectedIndex(selection);
        break;
      case PROGRESS_BAR:
        ((JProgressBar) control.peer).setValue(selection);
        break;
      case SPINNER:
        ((JSpinner) control.peer).setValue(selection);
        break;
      case TAB_FOLDER:
        ((JTabbedPane) control.peer).setSelectedIndex(selection);
      default:
        unsupported(control, "setSelection");
    }
  }

  public JTextComponent getJTextComponent(Control control) {
    if (control.peer instanceof JComboBox) {
      JComboBox combo = (JComboBox) control.peer;
      ComboBoxEditor editor = combo.getEditor();
      return (JTextComponent) editor.getEditorComponent();
    }
    if (control.peer instanceof JTextComponent) {
      return (JTextComponent) control.peer;
    }
    return (JTextComponent) ((JScrollPane) control.peer).getViewport().getView();
  }

  public JList<String> getList(Control control) {
    return (JList<String>) ((JScrollPane) control.peer).getViewport().getView();
  }

  @Override
  public void setIndexSelected(Control control, int index, boolean selected) {
    switch (control.getControlType()) {
      case LIST: {
        JList<String> list = getList(control);
        if (selected) {
          list.addSelectionInterval(index, index);
        } else {
          list.removeSelectionInterval(index, index);
        }
        break;
      }
      case COMBO:
        ((JComboBox<String>) control.peer).setSelectedIndex(index);
        break;
      default:
        unsupported(control, "setSelectedIndex");
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
  public void removeItems(Control control, int start, int end) {
    switch (control.getControlType()) {
      case COMBO: {
        JComboBox<String> jComboBox = (JComboBox<String>) control.peer;
        while (end >= start) {
          jComboBox.removeItemAt(end--);
        }
        break;
      }
      case LIST:
        JList<String> list = getList(control);
        while (end >= start) {
          list.remove(end--);
        }
        break;
    }
  }

  @Override
  public void disposePeer(Control child) {
    if (child.getControlType() == Control.ControlType.SHELL) {
      SwingUtilities.getRoot((Component) child.peer).setVisible(false);
    } else {
      Composite parent = child.getParent();
      ((Container) parent.peer).remove((JComponent) child.peer);
    }
  }

  @Override
  public void setBackground(Control control, Color color) {
    ((Component) control.peer).setBackground(color == null ? null :
            new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
  }

  @Override
  public void setBackgroundImage(Control control, Image image) {
    unsupported(control, "serBackgroundimage");
  }

  @Override
  public void updateMenuBar(Decorations decorations) {
    JMenuBar awtMenuBar = new JMenuBar();
    for (int i = 0; i < decorations.menuBar.getItemCount(); i++) {
      MenuItem item = decorations.menuBar.getItem(i);
      JMenu awtSubMenu = new JMenu(item.text);
      item.peer = awtSubMenu;
      awtMenuBar.add(awtSubMenu);
      if (item.subMenu != null) {
        menuAddAll(item.subMenu, awtSubMenu);
      }
      updateItem(item);
    }
    ((JFrame) SwingUtilities.getRoot((Component) decorations.peer)).setJMenuBar(awtMenuBar);
  }

  @Override
  public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
    JTabbedPane tabbedPane = (JTabbedPane) tabFolder.peer;
    if (tabItem.control != null) {
      tabbedPane.setComponentAt(index, (JComponent) tabItem.getControl().peer);
    }
    if (tabItem.text != null) {
      tabbedPane.setTitleAt(index, tabItem.text);
    }
  }

  @Override
  Point getCaretLocation(Control control) {
    java.awt.Point awtPoint = getJTextComponent(control).getCaret().getMagicCaretPosition();
    return new Point(awtPoint.x, awtPoint.y);
  }

  @Override
  int getCaretPosition(Control control) {
    return getJTextComponent(control).getCaretPosition();
  }

  @Override
  int getItemHeight(Control control) {
    unsupported(control, "getItemHeight");
    return ((JComponent) control.peer).getFont().getSize();
  }

  @Override
  int getLineHeight(Control control) {
    return getJTextComponent(control).getFont().getSize();
  }

  @Override
  Point getSelectedRange(Control control) {
    JTextComponent textComponent = getJTextComponent(control);
    return new Point(textComponent.getSelectionStart(), textComponent.getSelectionEnd());
  }

  @Override
  int getTopPixel(Text text) {
    return 0;
  }

  @Override
  void paste(Control control) {
    getJTextComponent(control).paste();
  }

  @Override
  boolean setDoubleClickEnabled(Text text, boolean doubleClick) {
    return false;
  }

  @Override
  char setEchoChar(Text text, char echo) {
    return 0;
  }

  @Override
  boolean setEditable(Text text, boolean editable) {
    getJTextComponent(text).setEditable(editable);
    return editable;
  }

  @Override
  String setMessage(Text text, String message) {
    getJTextComponent(text).setToolTipText(message);
    return message;
  }

  @Override
  void setOrientation(Control control, int orientation) {
    ((Component) control.peer).setComponentOrientation(orientation == SWT.RIGHT_TO_LEFT
            ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
  }

  @Override
  boolean setRedraw(Text text, boolean redraw) {
    return false;
  }

  @Override
  int setTextLimit(Control control, int limit) {
    unsupported(control, "setTextLimit");
    return Integer.MAX_VALUE;
  }

  @Override
  int setTabs(Text text, int tabs) {
    return 0;
  }

  @Override
  void setSelectionRange(Control control, int start, int end) {
    JTextComponent jText = getJTextComponent(control);
    jText.setSelectionStart(start);
    jText.setSelectionEnd(end);
  }

  @Override
  int getOrientation(Control control) {
    ComponentOrientation orientation = ((Component) control.peer).getComponentOrientation();
    return orientation == ComponentOrientation.RIGHT_TO_LEFT ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
  }

  @Override
  boolean getListVisible(Combo control) {
    return ((JComboBox) control.peer).isPopupVisible();
  }

  @Override
  void setListVisible(Combo control, boolean visible) {
    ((JComboBox) control.peer).showPopup();
  }

  @Override
  void setVisibleItemCount(Combo combo, int itemCount) {
    unsupported(combo, "setVisibleItemCount");
  }

  @Override
  int getVisibleItemCount(Combo combo) {
    unsupported(combo, "getVisibleItemCount");
    return 5;
  }

  @Override
  void updateItem(Item item) {
    if (item.peer == null) {
      return;
    }
    switch (item.getItemType()) {
      case MENU_ITEM: {
        MenuItem menuItem = (MenuItem) item;
        if (item.peer instanceof JCheckBoxMenuItem) {
          ((JCheckBoxMenuItem) item.peer).setSelected(((MenuItem) item).getSelection());
        }
        JMenuItem awtItem = (JMenuItem) item.peer;
        awtItem.setEnabled(menuItem.getEnabled());
        awtItem.setText(menuItem.getText());
        break;
      }
    }
  }

  @Override
  void setTopIndex(Control control, int topIndex) {
    switch (control.getControlType()) {
      case LIST:
        getList(control).ensureIndexIsVisible(topIndex);
        break;
      default:
        unsupported(control, "setTopIndex");
    }
  }

  @Override
  void showSelection(Control control) {
    switch (control.getControlType()) {
      case LIST:
        getList(control).ensureIndexIsVisible(getSelection(control));
        break;
      default:
        unsupported(control, "showSelection");
    }
  }

  @Override
  public void setImage(Control control, Image image) {
    switch (control.getControlType()) {
      case BUTTON:
        if ((control.style & (SWT.RADIO | SWT.TOGGLE | SWT.CHECK | SWT.ARROW)) == 0) {
          ImageIcon imageIcon = new ImageIcon((java.awt.Image) image.peer);
          ((AbstractButton) control.peer).setIcon(imageIcon);
        }
        break;   // Image would overwrite control
      case LABEL: {
        ImageIcon imageIcon = new ImageIcon((java.awt.Image) image.peer);
        ((JLabel) control.peer).setIcon(imageIcon);
        break;
      }
      default:
        System.err.println("FIXME: SwingDisplay.setImage() for " + control.getControlType());  // FIXME
    }
  }

  @Override
  public void setAlignment(Control control, int alignment) {
    if (control.getControlType() == Control.ControlType.BUTTON && ((control.style & SWT.ARROW) != 0)) {
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
      ((JButton) control.peer).setText(label);
    }
  }

  @Override
  public boolean isSelected(List control, int i) {
    return getList(control).isSelectedIndex(i);
  }

  @Override
  public void setFont(Control control, Font font) {
    System.err.println("FIXME: SwingDisplay.setFont()");         // FIXME
  }

  @Override
  public void setItem(Control control, int index, String string) {
    switch (control.getControlType()) {
      case LIST:
        ((DefaultListModel<String>) getList(control).getModel()).set(index, string);
        break;
      default:
        System.err.println("FIXME: SwingDisplay.setItem()");
    }
  }

  @Override
  public Font getFont(Control control) {
    java.awt.Font awtFont = ((Component) control.peer).getFont();
    return awtFont == null ? null : new Font(this, awtFont.getName(), awtFont.getSize(),
            (awtFont.isBold() ? SWT.BOLD : 0) + (awtFont.isItalic() ? SWT.ITALIC : 0));
  }

  @Override
  int getTopIndex(Control control) {
    switch (control.getControlType()) {
      case LIST:
        return getList(control).getFirstVisibleIndex();
      default:
        unsupported(control, "getTopIndex");
        return 0;
    }
  }

  @Override
  public String getItem(Control control, int i) {
    switch (control.getControlType()) {
      case COMBO:
        return ((JComboBox<String>) control.peer).getItemAt(i);
      case LIST:
        return (getList(control)).getModel().getElementAt(i);
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
        component.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
              notifyListeners(control, SWT.MouseWheel, e);
            }
          });
        break;
      case SWT.MouseMove:
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
        break;
      case SWT.MouseDoubleClick:
      case SWT.MouseDown:
      case SWT.MouseUp:
      case SWT.MouseEnter:
      case SWT.MouseExit:
          component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                if (eventType == SWT.MouseDoubleClick) {
                  notifyListeners(control, SWT.MouseDoubleClick, e);
                }
              }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (eventType == SWT.MouseDown) {
                  notifyListeners(control, SWT.MouseDown, e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
              if (eventType == SWT.MouseDown) {
                notifyListeners(control, SWT.MouseUp, e);
              }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              if (eventType == SWT.MouseEnter) {
                notifyListeners(control, SWT.MouseEnter, e);
              }
            }

            @Override
            public void mouseExited(MouseEvent e) {
              if (eventType == SWT.MouseExit) {
                notifyListeners(control, SWT.MouseExit, e);
              }
            }
          });
          break;
      case SWT.Move:
      case SWT.Resize:
      case SWT.Show:
      case SWT.Hide:
        component.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
              if (eventType == SWT.Resize) {
                notifyListeners(control, SWT.Resize, e);
              }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
              if (eventType == SWT.Move) {
                notifyListeners(control, SWT.Move, e);
              }
            }

            @Override
            public void componentShown(ComponentEvent e) {
              if (eventType == SWT.Show) {
                notifyListeners(control, SWT.Show, e);
              }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
              if (eventType == SWT.Hide) {
                notifyListeners(control, SWT.Hide, e);
              }
            }
          });
        break;
      case SWT.Selection:
        switch (control.getControlType()) {
          case BUTTON: {
            AbstractButton button = (AbstractButton) component;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            break;
          }
          case SLIDER: {
            JScrollBar scrollbar = (JScrollBar) component;
            scrollbar.addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            break;
          }
          case SCALE: {
            JSlider slider = (JSlider) component;
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                  notifyListeners(control, SWT.Selection, e);
                }
              });
            break;
          }
        }
        break;
    }
  }

  @Override
  void copy(Control control) {
    switch (control.getControlType()) {
      case TEXT:
        getJTextComponent(control).copy();
        break;
      default:
        unsupported(control, "copy");
    }
  }

  @Override
  void cut(Control control) {
    switch (control.getControlType()) {
      case TEXT:
        getJTextComponent(control).cut();
    }
  }

  @Override
  public Insets getInsets(Scrollable scrollable) {
    Insets result = new Insets();
    if (scrollable instanceof Composite) {
      Container container = ((Container) scrollable.peer);
      java.awt.Insets insets = container.getInsets();
      if (scrollable.getControlType() == Control.ControlType.SHELL) {
        java.awt.Insets outer = SwingUtilities.getWindowAncestor((Component) scrollable.peer).getInsets();
        result.left = insets.left + outer.left;
        result.top = insets.top + outer.top + container.getY();  // Menu bar
        result.right = insets.right + outer.right;
        result.bottom = insets.bottom + outer.bottom;
      } else {
        result.left = insets.left;
        result.top = insets.top;
        result.right = insets.right;
        result.bottom = insets.bottom;
      }

    /*  if (scrollable.peer instanceof JScrollPane) {
        JScrollPane scrollPane = (JScrollPane) scrollable.peer;
        if (result.right >= scrollPane.getVScrollbarWidth()) {
          result.right -= scrollPane.getVScrollbarWidth();
        }
        if (result.bottom > scrollPane.getHScrollbarHeight()) {
          result.bottom -= scrollPane.getHScrollbarHeight();
        }
      }*/

    }
    return result;
  }

  @Override
  public int getItemCount(Control control) {
    switch (control.getControlType()) {
      case COMBO:
        return ((JComboBox<String>) control.peer).getItemCount();
      case LIST:
        return getList(control).getModel().getSize();
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public Monitor getMonitor(Control control) {
    Window window = SwingUtilities.getWindowAncestor(((Component) control.peer));
    java.awt.Rectangle bounds = window.getGraphicsConfiguration().getBounds();
    java.awt.Insets insets = window.getToolkit().getScreenInsets(window.getGraphicsConfiguration());
    return new Monitor(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
            new Rectangle(bounds.x + insets.left, bounds.y + insets.top,
                    bounds.width - insets.left - insets.right,
                    bounds.height - insets.top - insets.bottom));
  }
}
