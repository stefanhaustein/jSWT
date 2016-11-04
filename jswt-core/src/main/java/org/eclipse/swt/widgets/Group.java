package org.eclipse.swt.widgets;


public class Group extends Composite {
    String text;

    public Group(Composite parent, int style) {
        super(parent, style);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        display.setText(this, text);
    }

    public ControlType getControlType() {
        return ControlType.GROUP;
    }

    public String toString() {
        return super.toString() + ":" + getText();
    }
}
