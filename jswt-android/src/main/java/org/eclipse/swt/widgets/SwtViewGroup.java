package org.eclipse.swt.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.eclipse.swt.widgets.Composite;

class SwtViewGroup extends ViewGroup {

    Composite composite;
    String text;

    SwtViewGroup(Context context, Composite container) {
        super(context);
        this.composite = container;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            composite.layout();
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            LayoutParams childLayoutParams = getChildLayoutParams(i);
            childView.layout(
                    childLayoutParams.assignedX,
                    childLayoutParams.assignedY,
                    childLayoutParams.assignedX + childLayoutParams.assignedWidth,
                    childLayoutParams.assignedY + childLayoutParams.assignedHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    private LayoutParams getChildLayoutParams(int i) {
        return (LayoutParams) getChildAt(i).getLayoutParams();
    }

    class LayoutParams extends ViewGroup.LayoutParams {
        int assignedX;
        int assignedY;
        int assignedWidth;
        int assignedHeight;

        LayoutParams() {
           super(WRAP_CONTENT, WRAP_CONTENT);
        }
    }
}
