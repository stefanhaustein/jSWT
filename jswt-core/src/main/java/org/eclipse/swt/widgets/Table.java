package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;

public class Table extends Composite {
    ArrayList<TableColumn> columns = new ArrayList<>();
    ArrayList<TableItem> rows = new ArrayList<>();
    TableColumn sortColumn;
    boolean linesVisible;
    boolean headerVisible;
    int sortDirection;

    public Table(Composite parent, int style) {
        super(parent, style);
    }

    void addColumn(TableColumn column) {
        columns.add(column.index, column);
        for (int i = column.index + 1; i < columns.size(); i++) {
            columns.get(i).index = i;
        }
        display.addTableColumn(this, column);
    }

    void addItem(TableItem item) {
        rows.add(item.index, item);
        for (int i = item.index + 1; i < rows.size(); i++) {
            rows.get(i).index = i;
        }
        display.addTableItem(this, item);
    }

    void updateItem(TableItem item) {
        display.updateTableItem(this, item);
    }

    void updateColumn(TableColumn item) {
        display.updateTableColumn(this, item);
    }

    public void setHeaderVisible(boolean b) {
        System.out.println("FIXME: set header visible");
    }

    public TableItem[] getItems() {
        return rows.toArray(new TableItem[rows.size()]);
    }

    public TableItem[] getSelection() {
        throw new RuntimeException("NYI");
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getSelectionIndex() {
        throw new RuntimeException("NYI");
    }

    public int getItemCount() {
        return rows.size();
    }

    public TableItem getItem(int i) {
        return rows.get(i);
    }

    public void setSelection(int i) {
    }

    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

    public TableColumn getColumn(int column) {
       return columns.get(column);
    }

    public void setSortColumn(TableColumn column) {
        this.sortColumn = column;
    }

    public int getSelectionCount() {
        throw new RuntimeException("NYI");
    }

    public TableColumn[] getColumns() {
        return columns.toArray(new TableColumn[columns.size()]);
    }

    public boolean getHeaderVisible() {
        return headerVisible;
    }

    public boolean getLinesVisible() {
        return linesVisible;
    }

    public TableColumn getSortColumn() {
        return sortColumn;
    }

    public int getSortDirection() {
        return sortDirection;
    }

    public void setLinesVisible(boolean linesVisible) {
        this.linesVisible = linesVisible;
    }

    public TableItem getItem(Point coords) {
        System.out.println("FIXME: Table.getItem(point)");
        return null;
    }
}
