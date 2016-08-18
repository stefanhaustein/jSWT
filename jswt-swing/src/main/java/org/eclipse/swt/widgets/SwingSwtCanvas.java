package org.eclipse.swt.widgets;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        /*if ((swtCanvas.style & SWT.DOUBLE_BUFFERED) != 0) {
            if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
                buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
            swtCanvas.drawBackground(new SwingGC((Graphics2D) buffer.getGraphics()), 0, 0, getWidth(), getHeight());
            g.drawImage(buffer, 0, 0, null);
        } else {*/
            swtCanvas.drawBackground(new SwingGC((Graphics2D) g), 0, 0, getWidth(), getHeight());
    //    }
  //      paintChildren(g);
    }
}
