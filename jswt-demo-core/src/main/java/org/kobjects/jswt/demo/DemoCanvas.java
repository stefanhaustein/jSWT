package org.kobjects.jswt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class DemoCanvas extends Canvas {
    final Color white;
    final Color gray;
    final Color black;

    public DemoCanvas(Composite parent) {
        super(parent, 0);
        white = new Color(getDisplay(), 255, 255, 255);
        gray = new Color(getDisplay(), 127, 127, 127);
        black = new Color(getDisplay(), 0, 0, 0);

        final Menu menu = new Menu(this);
        new MenuItem(menu, SWT.CASCADE).setText("Dummy Item 1");
        new MenuItem(menu, SWT.CASCADE).setText("Dummy Item 2");
        new MenuItem(menu, SWT.CASCADE).setText("Dummy Item 3");

        addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {

            }

            @Override
            public void mouseDown(MouseEvent e) {

            }

            @Override
            public void mouseUp(MouseEvent e) {
                menu.setVisible(true);
            }
        });
    }

    @Override
    public void drawBackground(GC gc, int x, int y, int width, int height) {
      /*  gc.setForeground(new Color(getDisplay(), 255, 0, 0));
        for (int i = 0; i < 1000; i += 25) {
            gc.drawLine(0, i, 10000, i);
            gc.drawLine(i, 0, i, 10000);
        }
*/
        Rectangle bounds = getBounds();

        int m = bounds.width > bounds.height ? bounds.height : bounds.width;


        int step = Math.max(1, m / 10);
        m = step * 10;

        gc.setBackground(new Color(getDisplay(), 127, 127, 127));
        gc.fillRectangle(0, 0, bounds.width, bounds.height);

        gc.setForeground(white);

        for (int i = step/2; i < bounds.width; i+= step) {
            gc.drawLine(i, 0, i, bounds.height);
        }

        for (int i = step/2; i < bounds.height; i+= step) {
            gc.drawLine(0, i, bounds.width, i);
        }

        int dx = (bounds.width - m) / 2;
        int dy = (bounds.height - m) / 2;

        gc.setBackground(white);
        gc.fillRectangle(dx + step, dy + 5 * step, 8*step, 4*step);

        int w = 8*step;
        for (int i = 0; i < w; i++) {

            int j = (i * 255) / w;

            RGB rgb = new RGB(j*360f/255.0f, 1f, 1f);
            gc.setForeground(new Color (getDisplay(), rgb));
            gc.drawLine (dx + i + step, dy + step,
                    dx + i + step, dy + 3*step+step/2);

            gc.setForeground(new Color(getDisplay(), j, j, j));
            gc.drawLine (dx + i + step, dy + 3*step+step/2, dx + i+step, dy + 5*step);
        }

        /*
        for (int i = 0; i < 8; i++) {
            gc.setColor (new Color (((i & 1) * 255) ,
                                   ((i & 2) >> 1) * 255 ,
                                   ((i & 4) >> 2) *255));

            gc.fillRect ((i+1) * step, step, step, 2*step+step/2);

            int gray = 255 / 8 * i;
            gc.setColor (new Color (gray, gray, gray));

            gc.fillRect ((i+1) * step, 3*step+step/2, step, step+step/2);
        }
        */

        gc.setForeground(white);
        gc.drawOval (dx, dy, m, m);

        int tx = 1 * step + step / 3 + dx;
        int ty = 5 * step + step / 2 + dy;

        gc.setFont(new Font(getDisplay(), "SansSerif", step * 2 / 3, 0));

        gc.setForeground(black);

        boolean small = m < 100;

        gc.drawString
                ((small ? "ttl " : "total mem: ")
                        + Runtime.getRuntime().totalMemory()/1000000+"MB", tx, ty, true);

        long free = Runtime.getRuntime().freeMemory ();

        gc.drawString
                ((small ? "fr " : "free mem: ")
                        + free/1000000+"MB", tx, ty+step, true);

        int count = 0;
        while (true) {
            Runtime.getRuntime().gc();
            long newFree = Runtime.getRuntime().freeMemory ();
            if (newFree <= free) break;
            count ++;
            free = newFree;
        }

        gc.drawString
                ((small ? "gc " : "post gc: ")
                        + "(" + count +")"+free/1000000+"MB", tx, ty+2*step, true);

    }

}
