package org.eclipse.swt.widgets;

public class ProgressBar extends Control {
    private int minimum;
    private int maximum;


    public ProgressBar(Composite parent, int style) {
        super(parent, style);
        setMinimum(0);
        setMaximum(100);
        setSelection(0);
    }

    @Override
    ControlType getControlType() {
        return ControlType.PROGRESS_BAR;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getSelection() {
        return display.getSelection(this);
    }

    public void setMaximum(int i) {
        this.maximum = i;
        display.setRange(this, minimum, maximum);
    }

    public void setMinimum(int i) {
        this.minimum = i;
        display.setRange(this, minimum, maximum);
    }

    public void setSelection(int selection) {
        display.setSelection(this, selection);
    }
}
