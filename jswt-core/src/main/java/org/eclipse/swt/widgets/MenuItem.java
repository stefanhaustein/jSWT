package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class MenuItem extends Item {

    Menu subMenu;
    private boolean enabled = true;
    private boolean selection;

    public MenuItem(Menu parent, int style) {
        super(parent, style);
        parent.addItem(this);
    }


    public void addSelectionListener(SelectionListener selectionListener) {
        addListener(SWT.Selection, new TypedListener(selectionListener));
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
        update();
    }

    public void setSelection(boolean b) {
        this.selection = b;
        update();
    }

    public boolean getSelection() {
        return selection;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setMenu(Menu menu) {
        subMenu = menu;
    }

    public void setAccelerator(int i) {
        System.err.println("FIXME: MenuItem.setAccelerator()");
    }

    void update() {
        if (peer != null) {
            display.updateMenuItem(this);
        }
    }

}
