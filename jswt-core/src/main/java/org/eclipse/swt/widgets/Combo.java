package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;

public class Combo extends Control {

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

    public void remove(int index) {
        remove(index, index);
    }

    public void remove(int start, int end) {
        display.removeItems(this, start, end);
    }

    public void removeAll() {
        remove(0, getItemCount() - 1);
    }

    public void setItems(String... items) {
        removeAll();
        for (int i = 0; i < items.length; i++) {
            add(items[i], i);
        }
    }

    public void select(int i) {
        display.setSelection(this, i);
    }

    public int getItemCount() {
        return display.getItemCount(this);
    }

    public String getText() {
        return display.getText(this);
    }

    public int getSelectionIndex() {
        return display.getSelection(this);
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public String getItem(int i) {
        return display.getItem(this, i);
    }

    public void setText(String text) {
        display.setText(this, text);
    }

    public void setVisibleItemCount(int itemCount) {
        System.err.println("FIXEM: Combo.setVisibleItemCount: " + itemCount);   // FIXME
    }
}
