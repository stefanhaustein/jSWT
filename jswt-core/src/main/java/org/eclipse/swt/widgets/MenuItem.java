package org.eclipse.swt.widgets;

public class MenuItem extends Item {

    public Menu subMenu;

    public MenuItem(Menu parent, int style) {
        super(parent, style);
        parent.items.add(this);
    }


}
