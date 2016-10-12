package org.eclipse.swt.widgets;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;

public class Canvas extends Composite {
  public Canvas(Composite parent, int style) {
    super(parent, style);
  }

  public void drawBackground(GC gc, int x, int y, int width, int height) {
  }

  ControlType getControlType() {
    return ControlType.CANVAS;
  }
}
