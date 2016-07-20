package org.eclipse.swt.custom;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.PlatformDisplay;

public class ScrolledComposite extends Composite {

    Control content;
    int minHeight;
    int minWidth;
    boolean expandHorizontal;
    boolean expandVertical;
    boolean alwaysShowScroll;
    boolean showFocusedContent;
    boolean showNextFocusedControl = true;

    public ScrolledComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new ScrolledCompositeLayout());
    }

    public void setContent(Control control) {
        if (this.content != null) {
            throw new RuntimeException("NYI");
        }
        this.content = control;
        ((PlatformDisplay) getDisplay()).addChild(this, control);
    }

    public boolean getExpandHorizontal() {
        return expandHorizontal;
    }

    public boolean getExpandVertical() {
        return expandHorizontal;
    }

    public void setExpandHorizontal(boolean expandHorizontal) {
        this.expandHorizontal = expandHorizontal;
    }

    public void setExpandVertical(boolean expandVertical) {
        this.expandVertical = expandVertical;
    }
}
