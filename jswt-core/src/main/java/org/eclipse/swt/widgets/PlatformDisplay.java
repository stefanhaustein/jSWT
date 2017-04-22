package org.eclipse.swt.widgets;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Clients should not directly interact with this class except for the constructor
 */
public abstract class PlatformDisplay extends Display {

    /**
     * Kept for image loading. Not part of the public API.
     */
    // FIXME: Should just loading be static and visible instead, delegating to a corresponding hidden instance method?
    public static PlatformDisplay instance;

    static String removeAccelerators(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '&') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    abstract void addItem(Control control, String s, int index);

    /**
     * Not part of the public API; used internally to delay child addition (e.g. in ScrolledComposite and ToolBar)
     */
    public abstract void addChild(Composite parent, Control control);

    abstract void addTab(TabFolder tabFolder, int index, TabItem tabItem);

    abstract void addListener(Control control, int eventType); // , Listener listener);

    abstract void copy(Control control);

    abstract void cut(Control control);

    abstract Point computeSize(Control control, int wHint, int hHint, boolean changed);

    abstract Object createControl(Control control);

    public abstract Object createImage(int width, int height);

    public abstract GC createGCForPlatformImage(Object platformImage);

    abstract void disposePeer(Control child);

    abstract Color getBackground(Control control);

    abstract void getLocation(Control control, Rectangle bounds, Point location);

    abstract void getSize(Control control, Rectangle bounds, Point size);

    abstract Color getForeground(Control control);

    abstract int getFocusIndex(List list);

    abstract boolean getGrayed(Button control);

    /**
     * Visible for the image implementation. Not part of the public API.
     */
    public abstract Rectangle getImageBounds(Object platformImage);

    public abstract Insets getInsets(Scrollable composite);

    abstract String getItem(Control control, int index);

    abstract int getItemCount(Control control);

    abstract Monitor getMonitor(Control control);

    /**
     * Not part of the public API; used by ScrolledComposite. Might make sense to support via ScrollBar widgets instead.
     */
    public abstract int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation);

    abstract int getSelection(Control control);

    /** Used for Combo and Text only b/c speed keys */
    abstract String getText(Control control);

    abstract Font getFont(Control control);

    abstract int getTopIndex(Control control);

    abstract boolean isEnabled(Control control);

    abstract boolean isSelected(List list, int i);

    abstract boolean isVisible(Control control);

    /**
     * Used internally for image loading; not part of the public API
     */
    public abstract Object loadImage(InputStream stream) throws IOException;

    abstract void openShell(Shell shell);

    abstract void redraw(Control control, int x, int y, int w, int h, boolean all);

    abstract void removeItems(Control control, int start, int end);

    abstract void setAlignment(Control button, int alignment);

    abstract void setBackground(Control control, Color color);

    abstract void setBackgroundImage(Control control, Image image);

    abstract void setEnabled(Control control, boolean b);

    abstract void setFocus(Control control);

    abstract void setFont(Control control, Font font);

    abstract void setForeground(Control control, Color color);

    abstract void setGrayed(Button button);

    abstract void setImage(Control control, Image image);

    abstract void setIndexSelected(Control control, int index, boolean selected);

    abstract void setItem(Control control, int index, String string);

    abstract void setRange(Control control, int minimum, int maximum);

    abstract void setSliderProperties(Control control, int thumb, int increment, int pageIncrement);

    abstract void setSelection(Control control, int selection);

    abstract void setText(Control control, String text);

    abstract void setVisible(Control control, boolean visible);

    abstract void showPopupMenu(Menu menu, Control anchor, int x, int y);

    abstract void updateMenuBar(Decorations decorations);

    abstract void updateTab(TabFolder tabFolder, int index, TabItem tabItem);

    abstract void setTopIndex(Control control, int topIndex);

    abstract void showSelection(Control control);

    abstract Point getCaretLocation(Control text);

    abstract int getCaretPosition(Control text);

    abstract int getItemHeight(Control control);

    abstract int getLineHeight(Control control);

    abstract Point getSelectedRange(Control control);

    abstract int getTopPixel(Text text);

    abstract void paste(Control control);

    abstract boolean setDoubleClickEnabled(Text text, boolean doubleClick);

    abstract char setEchoChar(Text text, char echo);

    abstract boolean setEditable(Text text, boolean editable);

    abstract String setMessage(Text text, String message);

    abstract void setOrientation(Control control, int orientation);

    abstract boolean setRedraw(Text text, boolean redraw);

    abstract int setTextLimit(Control control, int limit);

    abstract int setTabs(Text text, int tabs);

    abstract void setSelectionRange(Control control, int start, int end);

    abstract int getOrientation(Control control);

    abstract boolean getListVisible(Combo control);

    abstract void setListVisible(Combo control, boolean visible);

    abstract void setVisibleItemCount(Combo combo, int itemCount);

    abstract int getVisibleItemCount(Combo combo);

    abstract void updateMenuItem(MenuItem item);

    abstract void addTableColumn(Table table, TableColumn column);

    abstract void addTableItem(Table table, TableItem item);

    abstract void updateTableColumn(Table table, TableColumn item);

    abstract void updateTableItem(Table table, TableItem item);

    abstract void removeTableColumn(Table table, TableColumn column);

    abstract void removeTableItem(Table table, TableItem item);

    abstract void moveAbove(Control control, Control other);

    abstract void updateTable(Table table);

    abstract ToolBar getToolBar(Shell shell);

    abstract void setLocation(Control control, int x, int y);

    abstract void setSize(Control control, int width, int height);

    abstract void moveBelow(Control control, Control other);

    abstract void setMenu(Control control, Menu menu);

    abstract void updateMenu(Menu menu);


    public static class Insets {
        public int top;
        public int left;
        public int right;
        public int bottom;
    }
}
