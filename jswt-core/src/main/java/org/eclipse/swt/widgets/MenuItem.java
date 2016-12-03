package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class MenuItem extends Item {

    Menu subMenu;
    private boolean enabled;
    private boolean selection;

    public MenuItem(Menu parent, int style) {
        super(parent, style);
        parent.items.add(this);
    }


    public void addSelectionListener(SelectionListener selectionListener) {
        addListener(SWT.Selection, new TypedListener(selectionListener));
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public void setSelection(boolean b) {
        this.selection = b;
    }

    public boolean getSelection() {
        return selection;
    }
}
