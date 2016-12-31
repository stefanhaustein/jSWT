package org.eclipse.swt.widgets;


import javax.swing.table.AbstractTableModel;

public class SwingSwtTableModel extends AbstractTableModel {
    Table table;

    SwingSwtTableModel(Table table) {
        this.table = table;
    }

    public String getColumnName(int col) {
        return table.getColumn(col).getText();
    }

    public int getRowCount() { return table.getItemCount(); }
    public int getColumnCount() { return table.getColumnCount(); }
    public Object getValueAt(int row, int col) {
        return table.getItem(row).getText(col);
    }
    public boolean isCellEditable(int row, int col)
        { return true; }

    public void setValueAt(Object value, int row, int col) {
        table.getItem(row).setText(col, String.valueOf(value));
        fireTableCellUpdated(row, col);
    }
}
