package org.eclipse.swt.widgets;


import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class SwingSwtTableModel extends AbstractTableModel {
    Table table;
    JTable jTable;
    JTableHeader header;
    int columnOffset;
    int checkColumn = -1;

    SwingSwtTableModel(Table table) {
        this.table = table;
        if ((table.style & SWT.CHECK) != 0) {
            checkColumn = 0;
            columnOffset = 1;
        }
    }

    public String getColumnName(int col) {
        return (col != checkColumn && col - columnOffset < table.getColumnCount())
                ? table.getColumn(col - columnOffset).getText() : "";
    }

    public int getRowCount() { return table.getItemCount(); }
    public int getColumnCount() { return columnOffset + Math.max(1, table.getColumnCount()); }
    public Object getValueAt(int row, int col) {
        return col == checkColumn ? table.getItem(row).getChecked() : table.getItem(row).getText(col - columnOffset);
    }
    public boolean isCellEditable(int row, int col)
        { return col == checkColumn; }

    public void setValueAt(Object value, int row, int col) {
        if (col == checkColumn) {
            table.getItem(row).setChecked((Boolean) value);
        } else {
            table.getItem(row).setText(col - columnOffset, String.valueOf(value));
        }
        fireTableCellUpdated(row, col);
    }

    public void setJTable(JTable table) {
        this.jTable = table;
        header = table.getTableHeader();
        table.setTableHeader(null);
        table.setDefaultRenderer(String.class, new CellRenderer());
    }

    public Class getColumnClass(int col) {
        return col == checkColumn ? Boolean.class : String.class;
    }

    class CellRenderer extends DefaultTableCellRenderer {


        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {

            JLabel cellRenderLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            TableItem tableItem = SwingSwtTableModel.this.table.getItem(row);

            Image image = tableItem.getImage(col - columnOffset);
            if (image == null) {
                cellRenderLabel.setIcon(null);
            } else {
                cellRenderLabel.setIcon(new ImageIcon((java.awt.Image) image.peer));
            }
                        /*

            if (s.equalsIgnoreCase("yellow")) {
                c.setForeground(Color.YELLOW);
                c.setBackground(Color.gray);
            } else {
                c.setForeground(Color.black);
                c.setBackground(Color.WHITE);
            }
            */

            return cellRenderLabel;
        }
    }

}
