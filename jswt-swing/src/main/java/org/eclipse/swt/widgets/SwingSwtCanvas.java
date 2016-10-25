package org.eclipse.swt.widgets;

import java.awt.event.PaintEvent;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Rectangle;

public class SwingSwtCanvas extends JPanel {
    final Canvas swtCanvas;

    BufferedImage buffer;

    public SwingSwtCanvas(Canvas swtCanvas) {
        setLayout(new SwingSwtLayoutManager(swtCanvas));
        this.swtCanvas = swtCanvas;
    }

  //  @Override
   // public void update(Graphics g) {
  //      paint(g);
   // }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        GC gc = new SwingGC(swtCanvas.display, (Graphics2D) g);

        /*if ((swtCanvas.style & SWT.DOUBLE_BUFFERED) != 0) {
            if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
                buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
            swtCanvas.drawBackground(new SwingGC((Graphics2D) buffer.getGraphics()), 0, 0, getWidth(), getHeight());
            g.drawImage(buffer, 0, 0, null);
        } else {*/
            swtCanvas.drawBackground(gc, 0, 0, getWidth(), getHeight());

        Event event = new Event();
        event.gc = gc;
        event.setBounds(new Rectangle(0, 0, getWidth(), getHeight()));
        swtCanvas.notifyListeners(SWT.Paint, event);


    //    }
  //      paintChildren(g);
    }
}
