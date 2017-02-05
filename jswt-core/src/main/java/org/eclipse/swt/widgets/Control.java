package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

public class Control extends Widget {

  static final boolean DEBUG_LAYOUT = false;

  public void setCursor(Cursor cursor) {
    System.err.println("FIXME: setCursor: " + cursor);
  }

    public void moveAbove(Control other) {
      ArrayList<Control> list = getParent().children;
      list.remove(this);
      int newIndex = other == null ? list.size() : (1 + list.indexOf(other));
      list.add(newIndex, this);

      display.moveAbove(this, other);
    }

  public void moveBelow(Control other) {
    ArrayList<Control> list = getParent().children;
    list.remove(this);
    int newIndex = other == null ? 0 : list.indexOf(other);
    list.add(newIndex, this);

    display.moveBelow(this, other);
  }

  public Point toControl(Point point) {
    return toControl(point.x, point.y);
  }


  public Point toControl(int x, int y) {
    Point result = toDisplay(0, 0);
    result.x = x - result.x;
    result.y = y - result.y;
    return result;
  }

  public Point toDisplay(int x, int y) {
    Control control = this;
    do {
      Point p = control.getLocation();
      x += p.x;
      y += p.y;
      control = control.getParent();
    } while (control != null);
    return new Point(x, y);
  }

  public Point toDisplay(Point point) {
    return toDisplay(point.x, point.y);
  }

  enum ControlType {
      BUTTON,
      CANVAS, COMBO, COMPOSITE,
      GROUP,
      LABEL, LIST,
      PROGRESS_BAR,
      SCALE, SHELL, SLIDER, SCROLLED_COMPOSITE, SPINNER,
      TAB_FOLDER, TABLE, TOOLBAR, TEXT};

  Menu menu;
  Object layoutData;
  Object peer;
  Color foreground;
  Color background;
  Font font;
  Image backgroundImage;
  private long registeredListenerTypes;


  String depth() {
    return this instanceof Shell ? "" : ("  " + getParent().depth());
  }

  public Control(Composite parent, int style) {
    super(parent, style);
    if (parent != null) {
      this.peer = display.createControl(this);
      parent.children.add(this);
      if (!(parent instanceof ScrolledComposite) && !(parent instanceof ToolBar)) {
        display.addChild(parent, this);
      }
    }
  }

  public void addControlListener(ControlListener controlListener) {
    TypedListener typedListener = new TypedListener(controlListener);
    addListener(SWT.Resize, typedListener);
    addListener(SWT.Move, typedListener);
  }

  public void addListener(int eventType, Listener listener) {
    super.addListener(eventType, listener);
    if ((registeredListenerTypes & (1L << eventType)) == 0) {
      registeredListenerTypes |= 1L << eventType;
      display.addListener(this, eventType);
    }
  }

  public void addKeyListener(KeyListener listener) {
    TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.KeyDown, typedListener);
    addListener(SWT.KeyUp, typedListener);
  }


  public void addMouseListener(MouseListener mouseListener) {
    TypedListener typedListener = new TypedListener(mouseListener);
    addListener(SWT.MouseUp, typedListener);
    addListener(SWT.MouseDown, typedListener);
    addListener(SWT.MouseDoubleClick, typedListener);
  }

  public void addMouseMoveListener(MouseMoveListener mouseMoveListener) {
    addListener(SWT.MouseMove, new TypedListener(mouseMoveListener));
  }

  public void addGestureListener(GestureListener gestureListener) {
    addListener(SWT.Gesture, new TypedListener(gestureListener));
  }

  public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
    addListener(SWT.MouseWheel, new TypedListener(mouseWheelListener));
  }

  public void addPaintListener(PaintListener listener) {
    addListener(SWT.Paint, new TypedListener(listener));
  }


  public Point computeSize(int wHint, int hHint) {
    return computeSize(wHint, hHint, true);
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {
    Point size;
    if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
    if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
    if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
      size = new Point(wHint, hHint);
    } else {
      size = display.computeSize(this, wHint, hHint, changed);
    }
    if (DEBUG_LAYOUT) {
      System.err.println(depth() + "computeSize(" + wHint + ", " + hHint + "): " + size + " for " + this);
    }
    return size;
  }

  public Color getBackground() {
   if (background == null) {
     background = display.getBackground(this);
   }
   return background;
  }

  public int getBorderWidth() {
    return 0;
  }

  public Rectangle getBounds() {
    Rectangle bounds = new Rectangle(0, 0, 0,0);
    display.getLocation(this, bounds, null);
    display.getSize(this, bounds, null);
    return bounds;
  }

  ControlType getControlType() {
        throw new RuntimeException("Abstract");
    }

  public boolean getEnabled() {
        return display.isEnabled(this);
    }


  public Font getFont() {
    if (font == null) {
      font = display.getFont(this);
    }
    return font;
  }


  public Color getForeground() {
    if (foreground == null) {
      foreground = display.getForeground(this);
    }
    return foreground;
  }

  public Object getLayoutData() {
        return layoutData;
    }

  public Point getLocation() {
    Point result = new Point(0, 0);
    display.getLocation(this, null, result);
    return result;
  }

  public Menu getMenu() {
      return menu;
  }

  public Monitor getMonitor() {
        return display.getMonitor(this);
    }

  public int getOrientation() {
      return display.getOrientation(this);
  }

  public Composite getParent() {
    return (Composite) parent;
  }

  public Point getSize() {
    Point size = new Point(0, 0);
    display.getSize(this, null, size);
    return size;
  }

  public Shell getShell() {
    Widget current = this;
    while (!(current instanceof Shell)) {
      current = current.parent;
    }
    return (Shell) current;
  }

  public boolean getVisible() {
    return display.isVisible(this);
  }

  public boolean isEnabled() {
    return display.isEnabled(this);
  }

  public boolean isVisible() {
    return display.isVisible(this);
  }

  void markLayout (boolean changed, boolean all) {
	/* Do nothing */
  }

  public void redraw() {
    Rectangle bounds = getBounds();
    redraw(0, 0, bounds.width, bounds.height, true);
  }

  public void redraw(int x, int y, int w, int h, boolean all) {
    display.redraw(this, x, y, w, h, all);
  }

  public void setBackground(Color color) {
    this.background = color;
    display.setBackground(this, color);
  }

  public void setBackgroundImage(Image image) {
     if (backgroundImage != image) {
       backgroundImage = image;
       display.setBackgroundImage(this, image);
     }
  }

  public void setBounds(int x, int y, int width, int height) {
    if (DEBUG_LAYOUT) {
      System.err.println(depth() + "setBounds(" + x + ", " + y + ", " + width + ", " + height + ") for " + this);
    }
    display.setLocation(this, x, y);
    display.setSize(this, width, height);
  }

  public void setBounds(Rectangle rect) {
    setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  public void setEnabled(boolean b) {
    display.setEnabled(this, b);
  }

  public void setForeground(Color color) {
    this.foreground = color;
    display.setForeground(this, color);
  }

  public void setLayoutData(Object layoutData) {
    this.layoutData = layoutData;
  }

  public void setMenu(Menu popup) {
    this.menu = popup;
    System.err.println("FIXME: Control.setMenu()");  // FIXME
  }

  public void setFont(Font font) {
    if (font != this.font) {
      this.font = font;
      display.setFont(this, font);
    }
  }

  public void setFocus() {
    display.setFocus(this);
  }

  public void setLocation(int x, int y) {
    display.setLocation(this, x, y);
  }

  public void setLocation(Point p) {
    display.setLocation(this, p.x, p.y);
  }

  public void setSize(int width, int height) {
    display.setSize(this, width, height);
  }

  public void setSize(Point p) {
    setSize(p.x, p.y);
  }


  //  Sets the orientation of the receiver, which must be one of the constants SWT.LEFT_TO_RIGHT or SWT.RIGHT_TO_LEFT.
  void setOrientation(int orientation) {
    display.setOrientation(this, orientation);
  }

  public void setTextDirection(int textDirection) {
      System.err.println("FIXME: Control.setTextDirection()");
  }


  public void setVisible(boolean visible) {
    display.setVisible(this, visible);
  }

  public String toString() {
    return getControlType().name();
  }


  void updateLayout (boolean all) {
	/* Do nothing */
  }

}
