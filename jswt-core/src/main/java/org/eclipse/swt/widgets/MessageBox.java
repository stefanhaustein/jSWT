package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;

public class MessageBox extends Dialog {
    public MessageBox(Shell parent) {
        this(parent, SWT.NONE);
    }
    
    public MessageBox(Shell parent, int style) {
        super(parent);
    }

    public void setText(String text) {
    }

    public void setMessage(String message) {
       
    }

    public int open() {
        return 0;
    }
}
