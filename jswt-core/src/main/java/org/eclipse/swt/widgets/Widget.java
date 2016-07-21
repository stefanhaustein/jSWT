package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.SWTEventListener;

import java.util.HashSet;

public abstract class Widget {

      	/* Global state flags */
    static final int DISPOSED = 1<<0;
    static final int CANVAS = 1<<1;
    static final int KEYED_DATA = 1<<2;
    static final int HANDLE = 1<<3;
    static final int DISABLED = 1<<4;
    static final int MENU = 1<<5;
    static final int OBSCURED = 1<<6;
    static final int MOVED = 1<<7;
    static final int RESIZED = 1<<8;
    static final int ZERO_WIDTH = 1<<9;
    static final int ZERO_HEIGHT = 1<<10;
    static final int HIDDEN = 1<<11;
    static final int FOREGROUND = 1<<12;
    static final int BACKGROUND = 1<<13;
    static final int FONT = 1<<14;
    static final int PARENT_BACKGROUND = 1<<15;
    static final int THEME_BACKGROUND = 1<<16;

    /* A layout was requested on this widget */
    static final int LAYOUT_NEEDED	= 1<<17;

    /* The preferred size of a child has changed */
    static final int LAYOUT_CHANGED = 1<<18;

    /* A layout was requested in this widget hierachy */
    static final int LAYOUT_CHILD = 1<<19;

    /* More global state flags */
    static final int RELEASED = 1<<20;
    static final int DISPOSE_SENT = 1<<21;
    static final int FOREIGN_HANDLE = 1<<22;
    static final int DRAG_DETECT = 1<<23;

    /* Notify of the opportunity to skin this widget */
    static final int SKIN_NEEDED = 1<<24;

    /* Should sub-windows be checked when EnterNotify received */
    static final int CHECK_SUBWINDOW = 1<<25;

    /* Default size for widgets */
    static final int DEFAULT_WIDTH	= 64;
    static final int DEFAULT_HEIGHT	= 64;

    /* GTK signals data
    static final int ACTIVATE = 1;
    static final int BUTTON_PRESS_EVENT = 2;
    static final int BUTTON_PRESS_EVENT_INVERSE = 3;
    static final int BUTTON_RELEASE_EVENT = 4;
    static final int BUTTON_RELEASE_EVENT_INVERSE = 5;
    115 	static final int CHANGED = 6;
    116 	static final int CHANGE_VALUE = 7;
    117 	static final int CLICKED = 8;
    118 	static final int COMMIT = 9;
    119 	static final int CONFIGURE_EVENT = 10;
    120 	static final int DELETE_EVENT = 11;
    121 	static final int DELETE_RANGE = 12;
    122 	static final int DELETE_TEXT = 13;
    123 	static final int ENTER_NOTIFY_EVENT = 14;
    124 	static final int EVENT = 15;
    125 	static final int EVENT_AFTER = 16;
    126 	static final int EXPAND_COLLAPSE_CURSOR_ROW = 17;
    127 	static final int EXPOSE_EVENT = 18;
    128 	static final int DRAW = EXPOSE_EVENT;
    129 	static final int EXPOSE_EVENT_INVERSE = 19;
    130 	static final int FOCUS = 20;
    131 	static final int FOCUS_IN_EVENT = 21;
    132 	static final int FOCUS_OUT_EVENT = 22;
    133 	static final int GRAB_FOCUS = 23;
    134 	static final int HIDE = 24;
    135 	static final int INPUT = 25;
    136 	static final int INSERT_TEXT = 26;
    137 	static final int KEY_PRESS_EVENT = 27;
    138 	static final int KEY_RELEASE_EVENT = 28;
    139 	static final int LEAVE_NOTIFY_EVENT = 29;
    140 	static final int MAP = 30;
    141 	static final int MAP_EVENT = 31;
    142 	static final int MNEMONIC_ACTIVATE = 32;
    143 	static final int MOTION_NOTIFY_EVENT = 33;
    144 	static final int MOTION_NOTIFY_EVENT_INVERSE = 34;
    145 	static final int MOVE_FOCUS = 35;
    146 	static final int OUTPUT = 36;
    147 	static final int POPULATE_POPUP = 37;
    148 	static final int POPUP_MENU = 38;
    149 	static final int PREEDIT_CHANGED = 39;
    150 	static final int REALIZE = 40;
    151 	static final int ROW_ACTIVATED = 41;
    152 	static final int SCROLL_CHILD = 42;
    153 	static final int SCROLL_EVENT = 43;
    154 	static final int SELECT = 44;
    155 	static final int SHOW = 45;
    156 	static final int SHOW_HELP = 46;
    157 	static final int SIZE_ALLOCATE = 47;
    158 	static final int STYLE_SET = 48;
    159 	static final int SWITCH_PAGE = 49;
    160 	static final int TEST_COLLAPSE_ROW = 50;
    161 	static final int TEST_EXPAND_ROW = 51;
    162 	static final int TEXT_BUFFER_INSERT_TEXT = 52;
    163 	static final int TOGGLED = 53;
    164 	static final int UNMAP = 54;
    165 	static final int UNMAP_EVENT = 55;
    166 	static final int UNREALIZE = 56;
    167 	static final int VALUE_CHANGED = 57;
    168 	static final int VISIBILITY_NOTIFY_EVENT = 58;
    169 	static final int WINDOW_STATE_EVENT = 59;
    170 	static final int ACTIVATE_INVERSE = 60;
    171 	static final int DAY_SELECTED = 61;
    172 	static final int MONTH_CHANGED = 62;
    173 	static final int STATUS_ICON_POPUP_MENU = 63;
    174 	static final int ROW_INSERTED = 64;
    175 	static final int ROW_DELETED = 65;
    176 	static final int DAY_SELECTED_DOUBLE_CLICK = 66;
    177 	static final int ICON_RELEASE = 67;
    178 	static final int SELECTION_DONE = 68;
    179 	static final int START_INTERACTIVE_SEARCH = 69;
    180 	static final int BACKSPACE = 70;
    181 	static final int BACKSPACE_INVERSE = 71;
    182 	static final int COPY_CLIPBOARD = 72;
    183 	static final int COPY_CLIPBOARD_INVERSE = 73;
    184 	static final int CUT_CLIPBOARD = 74;
    185 	static final int CUT_CLIPBOARD_INVERSE = 75;
    186 	static final int PASTE_CLIPBOARD = 76;
    187 	static final int PASTE_CLIPBOARD_INVERSE = 77;
    188 	static final int DELETE_FROM_CURSOR = 78;
    189 	static final int DELETE_FROM_CURSOR_INVERSE = 79;
    190 	static final int MOVE_CURSOR = 80;
    191 	static final int MOVE_CURSOR_INVERSE = 81;
    192 	static final int DIRECTION_CHANGED = 82;
    193 	static final int CREATE_MENU_PROXY = 83;
    194 	static final int LAST_SIGNAL = 84;
    */

    Widget parent;
    int style;
    int state;
    PlatformDisplay display;
    EventTable listeners = new EventTable();

    public Widget(Widget parent, int style) {
      this.parent = parent;
     this.style = style;
    }

    protected void checkWidget() {
        if ((state & DISPOSED) != 0) {
            SWT.error(SWT.ERROR_WIDGET_DISPOSED);
        }
    }

    protected void checkSubclass() {
    }

    public static int checkBits(int style, int leftToRight, int rightToLeft, int i, int i1, int i2, int i3) {
        return 0;
    }

    public void dispose() {
        state |= DISPOSED;
    }

    public void error(int code) {
        SWT.error(code);
    }

    public Display getDisplay() {
        return display;
    }

    public Widget getParent() {
        return parent;
    }

    public int getStyle() {
        return style;
    }

    public boolean isDisposed() {
        return (state & DISPOSED) != 0;
    }

    void releaseWidget () {
    }
}
