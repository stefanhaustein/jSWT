package org.eclipse.swt.widgets;

import java.util.ArrayList;

public class TabFolder extends Composite {

    ArrayList<TabItem> itemList = new ArrayList<>();

    public TabFolder(Composite parent, int style) {
        super(parent, style);
    }

    ControlType getControlType() {
        return ControlType.TAB_FOLDER;
    }

    public void setSelection(int i) {
    }
}
