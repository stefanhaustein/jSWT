package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class TableColumn extends Item {
    int index;
    boolean resizable;
    boolean moveable;
    String tooltip;
    int width;

    public TableColumn(Table parent, int style) {
        this(parent, style, parent.getColumnCount());
    }

    public TableColumn(Table parent, int style, int index) {
        super(parent, style, index);
        this.index = index;
        parent.addColumn(this);
    }

    public void setWidth(int i) {
        if (this.width != i) {
            this.width = i;
            update();
        }
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public void setToolTipText(String tooltip) {
        if (!tooltip.equals(this.tooltip)) {
            this.tooltip = tooltip;
            update();
        }
    }

    public void pack() {
    }

    public void setMoveable(boolean moveable) {
        if (moveable != this.moveable) {
            this.moveable = moveable;
            update();
        }
    }

    public void setResizable(boolean resizable) {
        if (resizable != this.resizable) {
            this.resizable = resizable;
            update();
        }
    }

    public boolean getMoveable() {
        return moveable;
    }

    public boolean getResizable() {
        return resizable;
    }

    void update() {
        display.updateTableColumn((Table) parent, this);
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }
}
