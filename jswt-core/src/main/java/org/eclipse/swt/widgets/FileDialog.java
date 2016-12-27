package org.eclipse.swt.widgets;

public class FileDialog extends Dialog {
    public FileDialog(Shell parent, int style) {
        super(parent);
    }

    public void setFilterExtensions(String[] strings) {
    }

    public void setFilterNames(String[] strings) {
    }

    public String open() {
        throw new RuntimeException("NYI");
    }

    public String getFileName() {
        throw new RuntimeException("NYI");
    }

    public String getFilterPath() {
        throw new RuntimeException("NYI");
    }
}
