package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Currently omitted: 
 * getItemHeight(), getTopIndex(), setFont(), setTopIndex(), showSelection()
 */
public class List extends Scrollable {
    public List(Composite parent, int style) {
        super(parent, style);
    }

    public void add(String name) {
        add(name, getItemCount());
    }

    public void add(String name, int index) {
        display.addItem(this, name, index);
    }

    public void addSelectionListener(final SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public void deselect(int index) {
        display.setIndexSelected(this, index, false);
    }

    public void deselect(int[] indices) {
        for (int i : indices) {
            display.setIndexSelected(this, i, false);
        }
    }

    public void deselect(int start, int end) {
        for (int i = start; i <= end; i++) {
            display.setIndexSelected(this, i, false);
        }
    }

    public void deselectAll() {
        deselect(0, getItemCount() - 1);
    }

    @Override
    ControlType getControlType() {
        return ControlType.LIST;
    }

    public String getItem(int index) {
        return display.getItem(this, index);
    }

    public String[] getItems() {
        String[] result = new String[getItemCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getItem(i);
        }
        return result;
    }

    public int getFocusIndex() {
        return display.getFocusIndex(this);
    }

    public int getItemCount() {
        return display.getItemCount(this);
    }

    public String[] getSelection() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelected(i)) {
                result.add(getItem(i));
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public int getSelectionCount() {
        int result = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelected(i)) {
                result++;
            }
        }
        return result;
    }

    public int getSelectionIndex() {
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelected(i)) {
                return i;
            }
        }
        return -1;
    }

    public int[] getSelectionIndices() {
        int[] result = new int[getSelectionCount()];
        int j = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelected(i)) {
                result[j++] = i;
            }
        }
        return result;
    }

    public int getTopIndex() {
        return display.getTopIndex(this);
    }

    public int indexOf(String string) {
        return indexOf(string, 0);
    }

    public int indexOf(String string, int start) {
        for (int i = start; i < getItemCount(); i++) {
            if (getItem(i).equals(string)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isSelected(int i) {
        return display.isSelected(this, i);
    }

    public void remove(int[] indices) {
        TreeSet<Integer> sorted = new TreeSet<>();
        for (int i : indices) {
            sorted.add(i);
        }
        for (Integer i : sorted.descendingSet()) {
            display.removeItems(this, i, i);
        }
    }

    public void remove(int index) {
        display.removeItems(this, index, index);
    }

    public void remove(int start, int end) {
        display.removeItems(this, start, end);
    }

    public void remove(String string) {
        int index = indexOf(string);
        if (index != -1) {
            remove(index);
        }
    }

    public void removeAll() {
        remove(0, getItemCount() - 1);
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }

    public void select(int[] indices) {
        for (int i : indices) {
            display.setIndexSelected(this, i, true);
        }
    }

    public void select(int i) {
        display.setIndexSelected(this, i, true);
    }

    public void select(int start, int end) {
        for (int i = start; i <= end; i++) {
            display.setIndexSelected(this, i, true);
        }
    }

    public void selectAll() {
        select(0, getItemCount() - 1);
    }

    public void setFont(Font font) {
        display.setFont(this, font);
    }

    public void setItem(int index, String string) {
        display.setItem(this, index, string);
    }

    public void setItems(String[] items) {
        removeAll();
        for (String s: items) {
            add(s);
        }
    }

    public void setSelection(int[] indices) {
        deselectAll();
        select(indices);
    }

    public void setSelection(String[] items) {
        deselectAll();
        for (String s: items) {
            int index = indexOf(s);
            if (index != -1) {
                select(index);
            }
        }
    }

    public void setSelection(int index) {
        setSelection(index, index);
    }

    public void setSelection(int start, int end) {
        for (int i = 0; i < getItemCount(); i++) {
            display.setIndexSelected(this, i, i >= start && i <= end);
        }
    }

    public void setTopIndex(int topIndex) {
        display.setTopIndex(this, topIndex);
    }

    public void showSelection() {
        display.showSelection(this);
    }
}
