package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.FontData;

public class FontDialog extends Dialog {
    public FontDialog(Shell parent) {
        super(parent);
    }

    public void setFontList(FontData[] fontData) {
        throw new RuntimeException("NYI");
    }

    public FontData open() {
        throw new RuntimeException("NYI");
    }
}
