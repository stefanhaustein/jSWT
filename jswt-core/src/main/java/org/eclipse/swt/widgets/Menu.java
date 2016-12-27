package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuListener;

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

    public MenuItem[] getItems() {
        throw new RuntimeException("NYI");
    }

    public void addMenuListener(MenuListener listener) {
        addListener(SWT.Hide, new TypedListener(listener));
        addListener(SWT.Show, new TypedListener(listener));
    }
}
