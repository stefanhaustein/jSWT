package org.eclipse.swt.widgets;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;

public class ToolBar extends Composite {

    final ArrayList<ToolItem> items = new ArrayList<>();
    boolean vertical;

    public ToolBar(Composite parent, int style) {
        super(parent, style & ~(SWT.H_SCROLL| SWT.V_SCROLL));
        vertical = (style & SWT.VERTICAL) != 0;
        RowLayout rowLayout = new RowLayout(vertical ? SWT.VERTICAL : SWT.HORIZONTAL);
        rowLayout.wrap = (style & SWT.WRAP) != 0;
        setLayout(rowLayout);
    }

    @Override
    ControlType getControlType() {
        return ControlType.TOOLBAR;
    }


    public int getItemCount() {
        return items.size();
    }

    public ToolItem getItem(int index) {
        return items.get(index);
    }

    public ToolItem[] getItems() {
        return items.toArray(new ToolItem[items.size()]);
    }

    public int indexOf(ToolItem item) {
        return items.indexOf(item);
    }

}
