package org.eclipse.swt.graphics;



public class Resource {

  public Object peer;
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
