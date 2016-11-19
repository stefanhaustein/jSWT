package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;

public class Combo extends Control {

    public static final int LIMIT = Integer.MAX_VALUE;

    private int textLimit = LIMIT;

    public Combo(Composite parent, int style) {
        super(parent, style);
    }

    ControlType getControlType() {
        return ControlType.COMBO;
    }

    public void add(String s) {
        add(s, getItemCount());
    }

    public void add(String s, int index) {
        display.addItem(this, s, index);
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public void addVerifyListener(VerifyListener listener) {
        addListener(SWT.Verify, new TypedListener(listener));
    }

    public void clearSelection() {
        display.setSelectionRange(this, 0, 0);
    }

    public void copy() {
        display.copy(this);
    }

    public void cut() {
        display.cut(this);
    }

    public void deselect(int index) {
        display.setIndexSelected(this, index, false);
    }

    public void deselectAll() {
        for (int i = display.getItemCount(this) - 1; i >= 0; i--) {
            display.setIndexSelected(this, i, false);
        }
    }

    public Point getCaretLocation() {
        return display.getCaretLocation(this);
    }

    public int getCaretPosition() {
        return display.getCaretPosition(this);
    }

    public String getItem(int i) {
        return display.getItem(this, i);
    }

    public int getItemCount() {
        return display.getItemCount(this);
    }

    public int getItemHeight() {
        return display.getItemHeight(this);
    }

    public String[] getItems() {
        String[] result = new String[getItemCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getItem(i);
        }
        return result;
    }

    public boolean getListVisible() {
        return display.getListVisible(this);
    }

    public int getOrientation() {
        return display.getOrientation(this);
    }

    public Point getSelection() {
        return display.getSelectedRange(this);
    }

    public int getSelectionIndex() {
        return display.getSelection(this);
    }

    public String getText() {
        return display.getText(this);
    }

    public int getTextHeight() {
        return display.getLineHeight(this);
    }

    public int getTextLimit() {
        return textLimit;
    }

    public int getVisibleItemCount() {
        return display.getVisibleItemCount(this);
    }

    public int indexOf(String s) {
        return indexOf(s, 0);
    }

    public int indexOf(String s, int start) {
        for (int i = start; i < getItemCount(); i++) {
            if (getItem(i).equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public void paste() {
        display.paste(this);
    }

    public void remove(int index) {
        remove(index, index);
    }

    public void remove(int start, int end) {
        display.removeItems(this, start, end);
    }

    public void removeAll() {
        remove(0, getItemCount() - 1);
    }

    public void removeModifyListener(ModifyListener listener) {
        removeListener(SWT.Modify, listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }

    public void removeVerifyListener(VerifyListener listener) {
        removeListener(SWT.Verify, listener);
    }

    public void select(int i) {
        display.setSelection(this, i);
    }

    public void setItem(int index, String string) {
        display.setItem(this, index, string);
    }

    public void setItems(String... items) {
        removeAll();
        for (int i = 0; i < items.length; i++) {
            add(items[i], i);
        }
    }

    public void setListVisible(boolean visible) {
        display.setListVisible(this, visible);
    }

    public void setSelection(Point selection) {
        display.setSelectionRange(this, selection.x, selection.y);
    }

    public void setText(String text) {
        display.setText(this, text);
    }

    public void setTextLimit(int limit) {
        this.textLimit = display.setTextLimit(this, limit);
    }

    public void setVisibleItemCount(int itemCount) {
        display.setVisibleItemCount(this, itemCount);
    }
}
