package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;

public class Slider extends Control {
    private int minimum;
    private int maximum;
    private int increment;
    private int pageIncrement;
    private int thumb;

    public Slider(Composite parent, int style) {
        super(parent, style);
        setValues(50, 0, 100, 10, 1, 10);
    }

    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    ControlType getControlType() {
        return ControlType.SLIDER;
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



        public int getThumb() {
        return thumb;
    }

    public void removeSelectionListener(SelectionListener listener) {
        removeListener(SWT.Selection, listener);
    }

    public void setIncrement(int newValue) {
        if (this.pageIncrement != newValue) {
            this.pageIncrement = newValue;
            display.setSliderProperties(this, thumb, increment, pageIncrement);
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
            display.setSliderProperties(this, thumb, increment, pageIncrement);
        }
    }

    public void setSelection(int selection) {
        display.setSelection(this, selection);
    }

    public void setThumb(int newValue) {
        if (this.thumb != newValue) {
            this.thumb = newValue;
            display.setSliderProperties(this, thumb, increment, pageIncrement);
        }
    }

    public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
        if (this.minimum != minimum || this.maximum != maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
            display.setRange(this, minimum, maximum);
        }
        if (this.thumb != thumb || this.increment != increment || this.pageIncrement != pageIncrement) {
            this.thumb = thumb;
            this.increment = increment;
            this.pageIncrement = pageIncrement;
            display.setSliderProperties(this, thumb, increment, pageIncrement);
        }
        display.setSelection(this, selection);
    }

    public int getIncrement() {
        return increment;
    }
}
