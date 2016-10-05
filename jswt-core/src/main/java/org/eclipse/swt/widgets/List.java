package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class List extends Control {
    public List(Composite parent, int style) {
        super(parent, style);
    }

    public void addSelectionListener(final SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    @Override
    ControlType getControlType() {
        return ControlType.LIST;
    }

    public void add(String name) {
        add(name, getItemCount());
    }

    public void add(String name, int index) {
        display.addItem(this, name, index);
    }

    public int getItemCount() {
        return display.getItemCount(this);
    }

    public int getSelectionIndex() {
        System.err.println("FIXME: List.getSelectionIndex()");
        return -1;
    }
}
