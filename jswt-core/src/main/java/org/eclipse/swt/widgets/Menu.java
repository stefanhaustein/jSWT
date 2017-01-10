package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

public class Menu extends Widget {

    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    int x = -1;
    int y = -1;

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
            if (x == -1) {
                if (parent instanceof Control) {
                    Rectangle bounds = ((Control) parent).getBounds();
                    Point global = ((Control) parent).toDisplay(bounds.width / 2, bounds.height / 2);
                    x = global.x;
                    y = global.y;
                    System.out.println("bounds: " + bounds);
                    System.out.println("global: " + global);
                } else {
                    System.err.println("show popup without parent control(-coordinates)");
                }
            }
            display.showPopupMenu(this, x, y);
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
        System.err.println("FIXME: Menu.setLoclation(x,y);");
    }
}
