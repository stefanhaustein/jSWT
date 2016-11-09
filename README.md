# jSWT

jSWT is a stab at a pure Java subset of SWT with Android and GWT bindings.
Main motivation for this is to get FlowGrid running
on different platforms without re-implementing the UI.

![Screenshot](https://raw.githubusercontent.com/stefanhaustein/jSWT/master/img/screenshot.png)


## Component Support State

<table style="table-layout:fixed; vertical-align: top">
<tr>
  <td> Component
  <td> Common / API
  <td> Android
  <td> GWT
<tr>
  <td> Browser
  <td colspan="3" align="center"> Unsupported (but should be straightforward to add)
<tr>
  <td> Button
  <td> Complete
  <td>
  <td>
<tr>
  <td> -SWT.ARROW
  <td>
  <td colspan="2" align="center"> Rendered as push button with unicode arrows
<tr>
  <td> -SWT.CHECK
  <td>
  <td> No image, grayed state not supported.
  <td>
<tr>
  <td> -SWT.PUSH
  <td>
  <td> No image
  <td>
<tr>
  <td> -SWT.RADIO
  <td>
  <td> No image
  <td>
<tr>
  <td> -SWT.TOGGLE
  <td>
  <td colspan="2" align="center">Rendered as checkbox
<tr>
  <td> Canvas
  <td colspan="3"> Missing: scroll support, getCaret(), getIME(), scroll(), setCaret(), setIME()
<tr>
  <td> Combo
  <td> *
  <td> No free text
  <td>
<tr>
  <td> Composite
  <td> *
  <td>
  <td>
<tr>
  <td> CoolBar
  <td colspan="3" rowspan="4" align="center"> Not available
<tr>
  <td> CTabFolder
<tr>
  <td> DateTime
<tr>
  <td> ExpandBar
<tr>
  <td> Group
  <td> Complete
  <td> Currently no text and border
  <td>
<tr>
  <td> Label
  <td> Complete
  <td> Currently no wrapping, image and separator support
  <td>
<tr>
  <td> Link
  <td colspan="3" align="center"> Not available
<tr>
  <td> List
  <td> Complete
  <td> *
  <td> focusIndex/show/topIndex functionality not available.
<tr>
  <td> Menu
  <td> *
  <td>
  <td> Not available
<tr>
  <td> ProgressBar
  <td> Missing: getState(), setState()
  <td>
  <td>
<tr>
  <td> Sash
  <td colspan="3" align="center"> Not available
<tr>
  <td> ScrolledComposite
  <td> *
  <td>
  <td> Not available yet
<tr>
  <td> Shell
  <td colspan="3"> Only one root shell visible, several decorations missing, dialog shells can't be freely positioned.
<tr>
  <td> Slider
  <td> Complete
  <td> Mapped to Scale
  <td> (Doesn't seem to render on MacOS currently)
<tr>
  <td> Scale
  <td> Complete
  <td> Horizontal only
  <td>
<tr>
  <td> Spinner
  <td> *
  <td>
  <td>
<tr>
  <td> StyledText
  <td colspan="3" align="center"> Not available
<tr>
  <td> TabFolder
  <td>
  <td>
  <td>
<tr>
  <td> Table
  <td colspan="3" align="center"> Not available
<tr>
  <td> Text
  <td> *
  <td>
  <td>
<tr>
  <td> ToolBar
  <td colspan="3" rowspan="3" align="center"> Not available
<tr>
  <td> Tray
<tr>
  <td> Tree

</table>

*) Still needs to be documented, probably some methods missing

## Unsupported

- Display event loop. JswtDemo shows how to work around this, maintaining full SWT compatibility.

- SWT Dialogs rely on a blocking open() call, which won't work for GWT (and might be tricky to support on Android).
  One option might be to provide a showDialog(dialog, callback) method somewhere that would still work with SWT by
  calling open().

- Dialogs with platform button placement are provided at JFaces level only unfortunately, which is beyond scope,
  and the GWT threading problem seems to persist there.

