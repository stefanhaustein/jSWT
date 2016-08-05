package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Point;

import java.awt.*;

public class SwtLayoutManager implements LayoutManager {

  org.eclipse.swt.widgets.Composite swtComposite;

  SwtLayoutManager(org.eclipse.swt.widgets.Composite swtComposite) {
    this.swtComposite = swtComposite;
  }

  @Override
  public void addLayoutComponent(String name, Component comp) {

  }

  @Override
  public void removeLayoutComponent(Component comp) {

  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
   /* if (parent.getParent() instanceof ScrollPane) {
      org.eclipse.swt.graphics.Rectangle bounds = swtComposite.getBounds();
      return new Dimension(bounds.width, bounds.height);
    } */
    Point p = swtComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    return new Dimension(p.x, p.y);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    Point p = swtComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    return new Dimension(p.x, p.y);
  }

  @Override
  public void layoutContainer(Container parent) {
    swtComposite.layout();
  }
}
