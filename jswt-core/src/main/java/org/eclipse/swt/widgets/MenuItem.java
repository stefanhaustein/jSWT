package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class MenuItem extends Item {

    public Menu subMenu;

    public MenuItem(Menu parent, int style) {
        super(parent, style);
        parent.items.add(this);
    }


    public void addSelectionListener(SelectionListener selectionListener) {
        addListener(SWT.Selection, new TypedListener(selectionListener));
    }
}
