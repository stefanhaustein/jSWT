package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;

public class MenuItem extends Item {

    public Menu subMenu;

    public MenuItem(Menu parent, int style) {
        super(parent, style);
        parent.items.add(this);
    }


    public void addSelectionListener(SelectionAdapter selectionListener) {
        addListener(SWT.Selection, new TypedListener(selectionListener));
    }
}
