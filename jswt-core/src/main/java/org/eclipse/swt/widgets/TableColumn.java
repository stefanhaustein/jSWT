package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class TableColumn extends Item {
    int index;
    boolean resizable;
    boolean moveable;

    public TableColumn(Table parent, int style) {
        this(parent, style, parent.getColumnCount());
    }

    public TableColumn(Table parent, int style, int index) {
        super(parent, style, index);
        this.index = index;
        parent.addColumn(this);
    }

    public void setWidth(int i) {
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public void setToolTipText(String tooltip) {
    }

    public void pack() {
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean getMoveable() {
        return moveable;
    }

    public boolean getResizable() {

        return resizable;
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }
}
