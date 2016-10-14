package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;

// TODO: 
public class Spinner extends Control {
    private int minimum;
    private int maximum;
    private int increment;
    private int pageIncrement;
    private int digits;

    public Spinner(Composite parent, int style) {
        super(parent, style);
    }

    public ControlType getControlType() {
        return ControlType.SPINNER;
    }

    public void addModifyListener(ModifyListener listener) {
        addListener(SWT.Modify, new TypedListener(listener));
    }


    public void addSelectionListener(SelectionListener listener) {
        addListener(SWT.Selection, new TypedListener(listener));
    }

    public String copy() {
        System.out.println("FIXME: Spinner.copy()");
        return "";
    }

    public String cut() {
        System.out.println("FIXME: Spinner.cut()");
        return "";
    }

    public int getDigits() {
        return digits;
    }

    public int getIncrement() {
        return increment;
    }

    public int getPageIncrement() {
        return pageIncrement;
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

    public void setIncrement(int i) {
        this.increment = i;
        display.setSliderProperties(this, 1, increment, pageIncrement);
    }

    public void setMaximum(int i) {
        this.maximum = i;
        display.setRange(this, minimum, maximum);
    }

    public void setMinimum(int i) {
        this.minimum = i;
        display.setRange(this, minimum, maximum);
    }

    public void setPageIncrement(int i) {
        this.pageIncrement = i;
        display.setSliderProperties(this, 1, increment, pageIncrement);
    }

    public void setSelection(int i) {
        display.setSelection(this, i);
    }

    public void setDigits(int i) {
        this.digits = i;
        System.out.println("FIXME: Spinner.setDigits()");
    }
}
