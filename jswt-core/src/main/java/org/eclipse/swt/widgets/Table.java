package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;

public class Table extends Composite {
    public Table(Composite parent, int style) {
        super(parent, style);
    }


    public void setHeaderVisible(boolean b) {
        System.out.println("FIXME: set header visible");
    }

    public TableItem[] getItems() {
        throw new RuntimeException("NYI");
    }

    public TableItem[] getSelection() {
        throw new RuntimeException("NYI");
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public int getColumnCount() {
        throw new RuntimeException("NYI");
    }

    public int getSelectionIndex() {
        throw new RuntimeException("NYI");
    }

    public int getItemCount() {
        throw new RuntimeException("NYI");
    }

    public TableItem getItem(int i) {
        throw new RuntimeException("NYI");
    }

    public void setSelection(int i) {
    }

    public void setSortDirection(int up) {
    }

    public TableColumn getColumn(int column) {
        throw new RuntimeException("NYI");
    }

    public void setSortColumn(TableColumn column) {
    }

    public int getSelectionCount() {
        throw new RuntimeException("NYI");
    }
}
