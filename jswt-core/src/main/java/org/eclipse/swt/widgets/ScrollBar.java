package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class ScrollBar extends Widget {

    ScrollBar(Widget parent) {
        super(parent, SWT.NONE);
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public void setMaximum(int maxX) {
    }

    public void setThumb(int width) {
    }

    public void setPageIncrement(int width) {
    }

    public int getSelection() {
        return 0;
    }
}
