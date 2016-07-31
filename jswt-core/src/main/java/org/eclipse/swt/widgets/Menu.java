package org.eclipse.swt.widgets;

import java.util.ArrayList;

public class Menu extends Widget {

    ArrayList<MenuItem> items = new ArrayList<MenuItem>();

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
            display.showPopupMenu(this);
        }
    }
}
