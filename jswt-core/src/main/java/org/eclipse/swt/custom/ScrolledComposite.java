package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class ScrolledComposite extends Composite {

    Control content;
    int minHeight;
    int minWidth;
    boolean expandHorizontal;
    boolean expandVertical;
    boolean alwaysShowScroll;
    boolean showFocusedContent;
    boolean showNextFocusedControl = true;
    Listener contentListener;
    int oldWidth;
    int oldHeight;


    public ScrolledComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new ScrolledCompositeLayout());
        contentListener = new Listener() {
            @Override
            public void handleEvent(Event e) {
                if (e.type != SWT.Resize) return;
                Rectangle bounds = getBounds();
                // FIXME: Should this be caught at a higher level already?
                if (bounds.width != oldWidth || bounds.height != oldHeight) {
                    oldHeight = bounds.height;
                    oldWidth = bounds.width;
                    layout(false);
                }
            }
        };
    }

    public void setContent(Control control) {
        if (this.content != null) {
            throw new RuntimeException("NYI");
        }
        this.content = control;
        if (this.content != null) {
            layout(false);
            this.content.addListener(SWT.Resize, contentListener);
            ((PlatformDisplay) getDisplay()).addChild(this, control);
        }
    }

    public boolean getExpandHorizontal() {
        return expandHorizontal;
    }

    public boolean getExpandVertical() {
        return expandHorizontal;
    }

    boolean needHScroll(Rectangle contentRect, boolean vVisible) {

        Rectangle hostRect = getClientArea();
        //int border = getBorderWidth();
        //hostRect.width -= 2*border;
        // ScrollBar vBar = getVerticalBar();
        if (vVisible) hostRect.width -= ((PlatformDisplay) getDisplay()).getScrollBarSize(this, SWT.VERTICAL);

        if (!expandHorizontal && contentRect.width > hostRect.width) return true;
        if (expandHorizontal && minWidth > hostRect.width) return true;
        return false;
    }

    boolean needVScroll(Rectangle contentRect, boolean hVisible) {
        //ScrollBar vBar = getVerticalBar();
        //if (vBar == null) return false;

        Rectangle hostRect = getClientArea();
        //int border = getBorderWidth();
        //hostRect.height -= 2*border;
        //ScrollBar hBar = getHorizontalBar();
        if (hVisible) hostRect.height -= ((PlatformDisplay) getDisplay()).getScrollBarSize(this, SWT.HORIZONTAL);

        if (!expandVertical && contentRect.height > hostRect.height) return true;
        if (expandVertical && minHeight > hostRect.height) return true;
        return false;
    }


    public void setExpandHorizontal(boolean expandHorizontal) {
        this.expandHorizontal = expandHorizontal;
    }

    public void setExpandVertical(boolean expandVertical) {
        this.expandVertical = expandVertical;
    }

    public void setMinSize(Point minSize) {
        minWidth = minSize.x;
        minHeight = minSize.y;
    }
}
