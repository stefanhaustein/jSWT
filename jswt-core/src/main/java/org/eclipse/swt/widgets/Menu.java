package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

public class Menu extends Widget {

    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    Rectangle location;
    boolean explicitLocation;

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
            Widget widget = parent;
            if (!explicitLocation) {
                while (widget != null && !(widget instanceof Control)) {
                    widget = widget.parent;
                }

                if (widget instanceof Control) {
                    location = ((Control) widget).getBounds();
                    Point global = ((Control) widget).toDisplay(0, 0);
                    location.x = global.x;
                    location.y = global.y;
                } else {
                    System.err.println("show popup without parent control(-coordinates)");
                }
            }
            display.showPopupMenu(this, location);
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
        explicitLocation = true;
        location = new Rectangle(x, y, 0, 0);
    }
}
