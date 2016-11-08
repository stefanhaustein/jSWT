# jSWT

jSWT is a stab at a pure Java subset of SWT with Android, AWT and GWT bindings.
It's not really in a useful state yet. Main motivation for this is to get FlowGrid running
on differnet platforms without re-implementing the UI.

![Screenshot](https://raw.githubusercontent.com/stefanhaustein/jSWT/master/img/screenshot.png)


## Components

<table>
<tr>
  <td> Component
  <td> General / API
  <td> Android
  <td> GWT
<tr>
  <td> Browser
  <td colspan="3"> Unsupported (but should be straightforward to add)
<tr>
  <td> Button
  <td> setGrayed() / getGrayed() missing
<tr>
  <td> Button (SWT.ARROW)
  <td>
  <td colspan="2"> Push button with unicode arrows
<tr>
  <td> Button (SWT.CHECK)
  <td>
  <td> No image
  <td>
<tr>
  <td> Button (SWT.PUSH)
  <td>
  <td>
  <td>
<tr>
  <td> Button (SWT.RADIO)
  <td>
  <td> No image
  <td>


</table>



## Unsupported

- Display event loop. JswtDemo shows how to work around this, maintaining full SWT compatibility.

- Trees and Tables won't be supported (at least initially) as they don't seem to make much sense on mobile.

- SWT Dialogs rely on a blocking open() call, which won't work for GWT (and might be tricky to support on Android).
  One option might be to provide a showDialog(dialog, callback) method somewhere that would still work with SWT by
  calling open().

- Dialogs with platform button placement are provided at JFaces level only unfortunately, which is beyond scope,
  and the GWT threading problem seems to persist there.

