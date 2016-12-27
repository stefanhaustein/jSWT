package org.eclipse.swt.widgets;


import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;

public class TableColumn extends Item {
    public TableColumn(Table parent, int style) {
        super(parent, style);
    }

    public TableColumn(Table parent, int style, int index) {
        super(parent, style, index);
    }

    public void setWidth(int i) {
    }

    public void addSelectionListener(SelectionListener listener) {
    }
}
