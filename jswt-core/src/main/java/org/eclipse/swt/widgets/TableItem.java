package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;


public class TableItem extends Item {
    int index;
    ArrayList<Cell> cells = new ArrayList<>();
    Color background;
    Color foregorund;
    Font font;
    boolean checked;

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
        update();
    }

    public String getText(int column) {
        String result = getCell(column).text;
        if (result == null) {
            result = text;
        }
        return result == null ? "" : result;
    }

    public void setText(int col, String s) {
        getCell(col).text = s;
        update();
    }

    public void setBackground(int i, Color cellBackgroundColor) {
        getCell(i).background = cellBackgroundColor;
        update();
    }

    public Color getBackground(int i) {
        Color result = getCell(i).background;
        return result == null ? background : result;
    }

    public void setForeground(int i, Color cellForegroundColor) {
        getCell(i).foreground = cellForegroundColor;
        update();
    }

    public Color getForeground(int i) {
        Color result = getCell(i).foreground;
        return result == null ? foregorund : result;
    }

    public void setFont(Font cellFont) {
        setFont(0, cellFont);
        update();
    }

    public void setFont(int i, Font cellFont) {
        getCell(i).font = cellFont;
        update();
    }

    public Font getFont(int i) {
        Font result = getCell(i).font;
        return result == null ? font : result;
    }

    public void setImage(int i, Image image) {
        getCell(i).image = image;
        update();
    }

    public void setForeground(Color itemForegroundColor) {
        this.foregorund = itemForegroundColor;
        update();
    }

    public void setBackground(Color itemBackgroundColor) {
        background = itemBackgroundColor;
        update();
    }

    public void setChecked(boolean b) {
        checked = b;
        update();
    }

    public boolean getChecked() {
        return checked;
    }

    void update() {
        display.updateTableItem((Table) parent, this);
    }

    public Image getImage(int i) {
        return getCell(i).image;
    }


    class Cell {
        String text;
        Image image;
        Color foreground;
        Color background;
        Font font;
    }
}

