package org.eclipse.swt.widgets;


public class TableItem extends Item {
    public TableItem(Table parent, int style) {
        super(parent, style);
    }

    public TableItem(Table parent, int style, int index) {
        super(parent, style, index);
    }

    public void setText(String[] values) {
        throw new RuntimeException("NYI");
    }

    public String getText(int column) {
        throw new RuntimeException("NYI");
    }
}

