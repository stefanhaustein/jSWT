package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

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
        throw new RuntimeException("N/A on android.");
    }

    public void sleep() {
        throw new RuntimeException("N/A on android.");
    }

    public void asyncExec(Runnable runnable) {
        throw new RuntimeException("Implement in platform display!");
    }

    public void dispose() {
    }
}
