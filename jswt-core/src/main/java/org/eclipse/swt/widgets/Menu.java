package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

public class Menu extends Widget {

    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    Point explicitLocation;

    Menu(Widget parent, int style) {
        super(parent, style);
    }

    public Menu(Control parent) {
        this(parent, 0);
    }

    public Menu(Decorations parent, int style) {
        this((Widget) parent, style);
    }

    public Menu(MenuItem parentItem) {
        this(parentItem, 0);
        parentItem.subMenu = this;
    }

    public int getItemCount() {
        return items.size();
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            if (explicitLocation != null) {
                display.showPopupMenu(this, null, explicitLocation.x, explicitLocation.y);
            } else {
                Widget widget = parent;
                while (widget != null && !(widget instanceof Control)) {
                    widget = widget.parent;
                }

                if (widget instanceof Control) {
                    display.showPopupMenu(this, (Control) widget, 0, 0);
                } else {
                    System.err.println("show popup without parent control(-coordinates)");
                    display.showPopupMenu(this, null, 0, 0);
                }
            }
        }
    }

    public MenuItem[] getItems() {
        throw new RuntimeException("NYI");
    }

    public void addMenuListener(MenuListener listener) {
        addListener(SWT.Hide, new TypedListener(listener));
        addListener(SWT.Show, new TypedListener(listener));
    }

    public void setLocation(int x, int y) {
        explicitLocation = new Point(x, y);
    }

    public void setLocation(Point point) {
        setLocation(point.x, point.y);
    }
}
