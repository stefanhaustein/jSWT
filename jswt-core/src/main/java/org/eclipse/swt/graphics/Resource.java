package org.eclipse.swt.graphics;

import org.eclipse.swt.widgets.Display;

public class Resource {

  boolean disposed;
  protected Device device;

  public boolean isDisposed() {
    return disposed;
  }

  public void dispose() {
    disposed = true;
  }

  public Device getDevice() {
    return device;
  }
}
