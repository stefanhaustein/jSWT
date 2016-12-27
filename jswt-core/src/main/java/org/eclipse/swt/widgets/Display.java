package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class Display extends Device {

    private Color black = new Color(this, 0, 0, 0);
    private Color blue = new Color(this, 255, 0, 0);
    private Color white = new Color(this, 0, 0, 0);
    private Color red = new Color(this, 255, 0, 0);

    /*
    public static boolean isValidClass(Class<? extends Dialog> aClass) {
        return true;
    }

    public void wakeThread() {
    }

    public void wake() {
    }

    public void sendPreEvent(int none) {
    }

    public boolean isDisposed() {
        return false;
    }

    public void sendPostEvent(int none) {
    }

    void setSynchronizer(Synchronizer synchronizer) {

    }

    public boolean isValidThread() {
        return true;
    }
*/

    public boolean readAndDispatch() {
        throw new RuntimeException("Display.readAndDispatch not supported in jSWT.");
    }

    public boolean sleep() {
        System.out.println("Display.sleep ignored.");
        return false;
    }

    public void asyncExec(Runnable runnable) {
        throw new RuntimeException("Implement in platform display!");
    }

    public void dispose() {
    }

    public Point map(Control from, Control to, int x, int y) {
        System.err.println("FIXME: Display.map(from, to, x, y)");    // FIXME
        return new Point(x, y);
    }

    public Point map(Control from, Control to, Point p) {
        return map(from, to, p.x, p.y);
    }

    public Rectangle getClientArea() {
        System.err.println("FIXME: Display.getClientArea()");
        return new Rectangle(0, 0, 2000, 1000);
    }

    public Cursor getSystemCursor(int cursor) {
        System.err.println("FIXME: Display.getSystemCursor()");
        return new Cursor(this, cursor);
    }
}
