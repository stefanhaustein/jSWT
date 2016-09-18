package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class Scale extends Control {
    private int minimum;
    private int maximum;
    private int increment;
    private int pageIncrement;

    public Scale(Composite parent, int style) {
        super(parent, style);
        display.setRange(this, 0, 100);
        display.setSliderProperties(this, 1, 1, 10);
        display.setSelection(this, 50);
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    ControlType getControlType() {
        return ControlType.SCALE;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getPageIncrement() {
        return pageIncrement;
    }

    public int getSelection() {
        return display.getSelection(this);
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }

    public void setIncrement(int newValue) {
        if (this.pageIncrement != newValue) {
            this.pageIncrement = newValue;
            display.setSliderProperties(this, 1, increment, pageIncrement);
        }
    }

    public void setMaximum(int newValue) {
        if (this.maximum != newValue) {
            this.maximum = newValue;
            display.setRange(this, minimum, maximum);
        }
    }

    public void setMinimum(int newValue) {
        if (this.minimum != newValue) {
            this.minimum = newValue;
            display.setRange(this, minimum, maximum);
        }
    }

    public void setPageIncrement(int newValue) {
        if (this.pageIncrement != newValue) {
            this.pageIncrement = newValue;
            display.setSliderProperties(this, 1, increment, pageIncrement);
        }
    }

    public void setSelection(int selection) {
        display.setSelection(this, selection);
    }
}
