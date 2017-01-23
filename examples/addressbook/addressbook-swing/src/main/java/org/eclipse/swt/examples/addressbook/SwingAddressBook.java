package org.eclipse.swt.examples.addressbook;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.PlatformDisplay;
import org.eclipse.swt.widgets.SwingDisplay;

/**
 * Created by haustein on 23.01.17.
 */
public class SwingAddressBook {

    public static void main(String[] args) {
        Display display = new SwingDisplay();
        AddressBook addressBook = new AddressBook();
        addressBook.open(display);
    }
}
