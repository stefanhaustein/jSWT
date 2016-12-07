package org.kobjects.swt;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class Validator implements VerifyListener {

    public static final int TYPE_CLASS_TEXT = 1;
    public static final int TYPE_CLASS_NUMBER = 2;
    public static final int TYPE_CLASS_MASK = 0xf;

    public static final int TYPE_NUMBER_FLAG_DECIMAL = 0x00002000;
    public static final int TYPE_NUMBER_FLAG_SIGNED = 0x00001000;

    private final Text text;
    private final int type;

    public Validator(Text text, int type) {
        this.text = text;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void verifyText(VerifyEvent e) {
        final String old = text.getText();
        final String full = old.substring(0, e.start) + e.text + old.substring(e.end);
        switch (type & TYPE_CLASS_MASK) {
            case TYPE_CLASS_NUMBER:
                if (!verifyNumber(full)) {
                    e.doit = false;
                }
                break;
        }
    }

    boolean verifyNumber(String s) {
        int i = (s.startsWith("-") && (type & TYPE_NUMBER_FLAG_SIGNED) != 0) ? 1 : 0;
        boolean dotAllowed = (type & TYPE_NUMBER_FLAG_DECIMAL) != 0;
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '0') {
                continue;
            }
            if (c == '.' && dotAllowed) {
                dotAllowed = false;
                continue;
            }
            return false;
        }
        return true;
    }

    public static void add(Text text, int inputType) {
        if (inputType != 0 && inputType != TYPE_CLASS_TEXT) {
            text.addVerifyListener(new Validator(text, inputType));
        }
    }
}
