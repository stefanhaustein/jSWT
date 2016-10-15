package org.eclipse.swt.widgets;

public class TabItem extends Item {

    Control control;

    public TabItem(TabFolder parent, int style) {
        this(parent, style, parent.itemList.size());
    }

    public TabItem(TabFolder parent, int style, int index) {
        super(parent, style);
        parent.itemList.add(index, this);
        display.addTab(parent, index, this);
    }

    public TabFolder getParent() {
        return (TabFolder) parent;
    }

    public Control getControl() {
        return control;
    }

    public void setText(String text) {
        super.setText(text);
        update();
    }

    public void setControl(Control control) {
        this.control = control;
        update();
    }

    private void update() {
        int index = getParent().itemList.indexOf(this);
        display.updateTab((TabFolder) parent, index, this);
    }
}
