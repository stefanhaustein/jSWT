package org.eclipse.swt.widgets;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class TabFolder extends Composite {

    ArrayList<TabItem> itemList = new ArrayList<>();

    public TabFolder(Composite parent, int style) {
        super(parent, style);
        setBackground(display.getSystemColor(SWT.COLOR_GREEN));
    }

    ControlType getControlType() {
        return ControlType.TAB_FOLDER;
    }

    public void setSelection(int i) {
        display.setSelection(this, i);
    }

    public int getSelectionIndex() {
        return display.getSelection(this);
    }

    public TabItem[] getItems() {
        return itemList.toArray(new TabItem[itemList.size()]);
    }
}
