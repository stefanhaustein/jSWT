package org.eclipse.swt.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

class AndroidCompositeView extends ViewGroup {

    Composite composite;
    String text;  // Group label

    AndroidCompositeView(Context context, Composite container) {
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
        if (Control.DEBUG_LAYOUT) {
            System.err.println(composite.depth() + "enter onLayout(" + changed + ", " + left + ", " + top + "," + right + ", " + bottom + ") for " + composite);
        }
        // if (changed) {
            composite.layout(true, true);
        // }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            LayoutParams childLayoutParams = getChildLayoutParams(i);
            childView.layout(
                    childLayoutParams.marginLeft,
                    childLayoutParams.marginTop,
                    childLayoutParams.marginLeft + childLayoutParams.width,
                    childLayoutParams.marginTop + childLayoutParams.height);
        }
        if (Control.DEBUG_LAYOUT) {
            System.err.println(composite.depth() + "exit onLayout(" + changed + ", " + left + ", " + top + "," + right + ", " + bottom + ") for " + composite);
        }
    }

    private static String specToString(int measureSpec) {
        int size = View.MeasureSpec.getSize(measureSpec);
        int mode = View.MeasureSpec.getMode(measureSpec);

        switch(mode) {
            case MeasureSpec.EXACTLY:
                return "exactly " + size;
            case MeasureSpec.AT_MOST:
                return "at most " + size;
            case MeasureSpec.UNSPECIFIED:
                return "unspec " + size;
            default:
                return "invalid mode " + size;
        }

    }


    private String specToString(int widthMeasureSpec, int heightMeasureSpec) {
        return specToString(widthMeasureSpec) + " x " + specToString(heightMeasureSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize);
        } else {
            if (Control.DEBUG_LAYOUT) {
                System.err.println(composite.depth() + "enter onMeasure(" + specToString(widthMeasureSpec, heightMeasureSpec) + ") for " + composite);
            }
            Point size = composite.computeSize(
                    widthMode == MeasureSpec.EXACTLY ? widthSize : SWT.DEFAULT,
                    heightMode == MeasureSpec.EXACTLY ? heightSize : SWT.DEFAULT);

            setMeasuredDimension(size.x, size.y);
            if (Control.DEBUG_LAYOUT) {
                System.err.println(composite.depth() + "exit onMeasure(" + widthMode + ":" + widthSize + " x " + heightMode + ":" + heightSize + ") = " + size + " for " + composite);
            }
        }

    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        composite.notifyListeners(SWT.Resize, null);
    }


    /** 
     * This method just enables calling setMeasuredDimension from the outside.
     */
    void setMeasuredSize(int width, int height) {
        setMeasuredDimension(width, height);
    }

    private LayoutParams getChildLayoutParams(int i) {
        return (LayoutParams) getChildAt(i).getLayoutParams();
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    class LayoutParams extends ViewGroup.LayoutParams {
        int marginLeft;
        int marginTop;
        LayoutParams() {
           super(WRAP_CONTENT, WRAP_CONTENT);
        }
    }

    AndroidDisplay getAndroidDisplay() {
        return (AndroidDisplay) composite.display;
    }

}
