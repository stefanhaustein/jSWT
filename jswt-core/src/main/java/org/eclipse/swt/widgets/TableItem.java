package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;


public class TableItem extends Item {
    int index;
    ArrayList<Cell> cells = new ArrayList<>();


    public TableItem(Table parent, int style) {
        this(parent, style, parent.getItemCount());
    }

    public TableItem(Table parent, int style, int index) {
        super(parent, style, index);
        this.index = index;
        parent.addItem(this);

    }

    private Cell getCell(int index) {
        while(cells.size() <= index) {
            cells.add(new Cell());
        }
        return cells.get(index);
    }
    public void setText(String[] newValues) {
        for (int i = 0; i < newValues.length; i++) {
            getCell(i).text = newValues[i];
        }

    }

    public String getText(int column) {
        return column >= cells.size() ? "" : cells.get(column).text;
    }

    public void setText(int col, String s) {
        getCell(col).text = s;
    }

    public void setBackground(int i, Color cellBackgroundColor) {
        getCell(i).background = cellBackgroundColor;
    }

    public Color getBackground(int i) {
        return getCell(i).background;
    }

    public void setForeground(int i, Color cellForegroundColor) {
        getCell(i).foreground = cellForegroundColor;
    }

    public Color getForeground(int i) {
        return getCell(i).foreground;
    }

    public void setFont(int i, Font cellFont) {
        getCell(i).font = cellFont;
    }

    public Font getFont(int i) {
        return getCell(i).font;
    }

    public void setImage(int i, Image image) {
        getCell(i).image = image;

    }

    public void setForeground(Color itemForegroundColor) {
        System.out.println("FIXME: TableItem.setForeground() -- foreground = itemForegroundColor;");
    }

    public void setBackground(Color itemBackgroundColor) {
        System.out.println("FIXME: TableItem.setBackground()");
    }

    class Cell {
        String text;
        Image image;
        Color foreground;
        Color background;
        Font font;
    }
}

