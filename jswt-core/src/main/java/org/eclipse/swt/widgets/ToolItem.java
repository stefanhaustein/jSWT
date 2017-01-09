package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class ToolItem extends Item {

    Control control;

    public ToolItem(ToolBar parent, int style) {
        this(parent, style, parent.getItemCount());
    }

    public ToolItem(ToolBar parent, int style, int index) {
        super(parent, style, index);
        parent.items.add(index, this);

        int parentButtonStyle = parent.style & SWT.FLAT;

        switch (style) {
            case SWT.PUSH:
                control = new Button(parent, style | parentButtonStyle);
                break;
            case SWT.CHECK:
                control = new Button(parent, parentButtonStyle | SWT.TOGGLE);
                break;
            case SWT.RADIO:
                control = new Button(parent, style | parentButtonStyle | SWT.TOGGLE);
                break;
            case SWT.SEPARATOR:
                control = new Label(parent, SWT.SEPARATOR | (parent.vertical ? SWT.HORIZONTAL : SWT.VERTICAL));
                break;
            case SWT.DROP_DOWN:
                control = new Button(parent, SWT.PUSH | parentButtonStyle);
                break;
            default:
                SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        display.addChild(parent, control);
    }

    public ToolBar getParent() {
        return (ToolBar) parent;
    }

    public void setToolTipText(String s) {
        System.err.println("TBD: ToolItem.setToolTipText");
    }

    @Override
    public void update() {
        if (style != SWT.SEPARATOR) {
            Button button = (Button) control;
            button.setImage(image);
            button.setText(text);
        }
    }

    public void setWidth(int x) {
    }

    public void setControl(Combo combo) {
        System.err.println("TBD: ToolItem.setControl()");
    }

    public void addSelectionListener(final SelectionListener listener) {
        if (style != SWT.SEPARATOR) {
            Button button = (Button) control;
            button.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    e.widget = ToolItem.this;
                    listener.widgetSelected(e);
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    e.widget = ToolItem.this;
                    listener.widgetDefaultSelected(e);
                }
            });
        }
    }

    public void dispose() {
        if (control != null) {
            control.dispose();
        }
    }

    public void setEnabled(boolean enabled) {
        control.setEnabled(enabled);
    }
}
