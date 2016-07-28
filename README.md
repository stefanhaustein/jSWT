# jSWT

jSWT is a stab at a pure Java subset of SWT with Android, AWT and (planned) GWT bindings.
It's not really in a useful state yet.

## Todo

- Spinner, ComboBox, Progress bars, Sliders
- Image support
- Figure out how to support Android context menus (probably via item.getToolbar() and let AWT return null).
- Figure out how to hide the toolbar on Android.
- Figure out how to support platform-style dialog buttons.

## Done

- 2016-07-28 Improved ScrolledComposite handling
- 2016-07-27 ScrollBar starting point
- 2016-07-26 Turned demo into a stateful class, landscape / portrait adjustment
- 2016-07-25 Checkbox / Radio button selection, EditText issues
- 2016-07-24 Some Dialog support, resize listeners, checkboxes
- 2016-07-23 Android menuBar support (via DrawerLayout)


## Unsupported

- Display event loop. JswtDemo shows how to work around this, maintaining full SWT compatibility.

- Trees and Tables won't be supported (at least initially) as they don't seem to make much sense on mobile.

- SWT Dialogs rely on a blocking open() call, which won't work for GWT (and might be tricky to support on Android).
  One option might be to provide a showDialog(dialog, callback) method somewhere that would still work with SWT by
  calling open().

- Dialogs with platform button placement are provided at JFaces level only unfortunately, which is beyond scope,
  and the GWT threading problem seems to persist there.

