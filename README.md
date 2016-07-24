# jSWT

jSWT is a stab at a pure Java subset of SWT with Android, AWT and (planned) GWT bindings.
It's not really in a useful state yet.



## Problems

- MessageBox.open() won't work in GWT
- Dialogs with platform button placement are provided at JFaces level only, which is beyond scope, and the GWT
  threading problem persists there to some extent

-