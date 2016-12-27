package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class MenuItem extends Item {

    Menu subMenu;
    private boolean enabled = true;
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
        display.updateItem(this);
    }

    public void setSelection(boolean b) {
        this.selection = b;
        display.updateItem(this);
    }

    public boolean getSelection() {
        return selection;
    }

    @Override
    ItemType getItemType() {
        return ItemType.MENU_ITEM;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setMenu(Menu menu) {
        throw new RuntimeException("???");
    }

    public void setAccelerator(int i) {
        throw new RuntimeException("???");
    }
}
