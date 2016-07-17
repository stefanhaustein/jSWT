package org.kobjects.jswt.awt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class JswtLayoutManager implements LayoutManager {

  org.eclipse.swt.widgets.Composite swtComposite;

  JswtLayoutManager(org.eclipse.swt.widgets.Composite swtComposite) {
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
