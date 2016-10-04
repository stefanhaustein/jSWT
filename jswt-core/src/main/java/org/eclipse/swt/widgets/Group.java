package org.eclipse.swt.widgets;


public class Group extends Composite {
    public Group(Composite parent, int style) {
        super(parent, style);
    }

    public void setText(String text) {
        display.setText(this, text);
    }

    public ControlType getControlType() {
        return ControlType.GROUP;
    }
}
