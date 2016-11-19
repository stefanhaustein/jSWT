package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;

public class Text extends Control {
  public static final int LIMIT = Integer.MAX_VALUE;
  public static final String DELIMITER = "\n";

  public Text(Composite parent, int style) {
    super(parent, style);
  }

  private boolean doubleClickEnabled;
  private char echoChar;
  private boolean editable = true;
  private String message;
  private int tabs;
  private int textLimit = LIMIT;
  private boolean redraw = true;

  public void addKeyListener(KeyListener listener) {
    addListener(SWT.KeyDown, new TypedListener(listener));
    addListener(SWT.KeyUp, new TypedListener(listener));
  }

  public void addModifyListener(ModifyListener listener) {
    addListener(SWT.Modify, new TypedListener(listener));
  }

  public void addSegmentListener(SegmentListener listener) {
    addListener(SWT.Segments, new TypedListener(listener));
  }

  public void addSelectionListener(SelectionListener listener) {
    addListener(SWT.Selection, new TypedListener(listener));
  }

  public void addVerifyListener(VerifyListener listener) {
    addListener(SWT.Verify, new TypedListener(listener));
  }

  public void append(String text) {
    setText(getText() + text);
  }

  public void clearSelection() {
    String text = getText();
    Point selection = getSelection();
    setText(text.substring(0, selection.x) + text.substring(selection.x));
  }

  public void copy() {
    display.copy(this);
  }

  public void cut() {
    display.cut(this);
  }


  public int getCaretLineNumber() {
    return getText().substring(0, getCaretPosition()).split(getLineDelimiter()).length;
  }

  public Point getCaretLocation() {
    return display.getCaretLocation(this);
  }

  int getCaretPosition() {
    return display.getCaretPosition(this);
  }

  public int getCharCount() {
    return getText().length();
  }

  public boolean getDoubleClickEnabled() {
    return doubleClickEnabled;
  }

  public char getEchoChar() {
    return echoChar;
  }

  public boolean getEditable() {
    return editable;
  }

  public int getLineCount() {
    return getText().split(getLineDelimiter()).length;
  }

  public String getLineDelimiter() {
    return "\n";
  }

  public int getLineHeight() {
    return display.getLineHeight(this);
  }

  public String getMessage() {
    return message;
  }

  //  Returns a Point whose x coordinate is the character position representing the start of the selected text, and whose y coordinate is the character position representing the end of the selection.
  public Point getSelection() {
    return display.getSelectedRange(this);
  }

//  Returns the number of selected characters.

  public int getSelectionCount() {
    Point range = getSelection();
    return range.y - range.x;
  }

  //  Gets the selected text, or an empty string if there is no current selection.
  public String getSelectionText() {
    Point range = getSelection();
    return getText().substring(range.x, range.y);
  }

  // Returns the number of tabs.
  public int getTabs() {
    return tabs;
  }

  public String getText() {
    return display.getText(this);
  }


  public String getText(int start, int end) {
    return getText().substring(start, end);
  }

  public char[] getTextChars() {
    return getText().toCharArray();
  }

  //  Returns the maximum number of characters that the receiver is capable of holding.
  public int getTextLimit() {
    return textLimit;
  }

  // Returns the zero-relative index of the line which is currently at the top of the receiver.
  public int getTopIndex() {
    return display.getTopIndex(this);
  }

  //  Returns the top pixel.
  public int getTopPixel() {
    return display.getTopPixel(this);
  }

  //  Inserts a string.
  public void insert(String string) {
    int pos = getCaretPosition();
    String text = getText();
    setText(text.substring(0, pos) + string + text.substring(pos));
  }

  //  Pastes text from clipboard.
  public void paste() {
    display.paste(this);
  }

  public void removeModifyListener(ModifyListener listener) {
    removeListener(SWT.Modify, listener);
  }

  public void removeSegmentListener(SegmentListener listener) {
    removeListener(SWT.Segments, listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    removeListener(SWT.Selection, listener);
  }

  public void removeVerifyListener(VerifyListener listener) {
    removeListener(SWT.Verify, listener);
  }

  //  Sets the double click enabled flag.
  public void setDoubleClickEnabled(boolean doubleClick) {
    doubleClickEnabled = display.setDoubleClickEnabled(this, doubleClick);
  }

  //  Sets the echo character.
  public void setEchoChar(char echo) {
    this.echoChar = display.setEchoChar(this, echo);
  }


  // Sets the editable state.
  public void setEditable(boolean editable) {
    this.editable = display.setEditable(this, editable);
  }

  //  Sets the widget message.
  public void setMessage(String message) {
    this.message = display.setMessage(this, message);
  }

  //  If the argument is false, causes subsequent drawing operations in the receiver to be ignored.
  public void setRedraw(boolean redraw) {
    this.redraw = display.setRedraw(this, redraw);
  }

  //  Sets the selection.
  public void setSelection(int start) {
    setSelection(start, getCharCount());
  }

  //  Sets the selection to the range specified by the given start and end indices.
  public void setSelection(int start, int end) {
    display.setSelectionRange(this, start, end);
  }

  public void setSelection(Point selection) {
    setSelection(selection.x, selection.y);
  }

  //  Sets the number of tabs.
  public void setTabs(int tabs) {
    this.tabs = display.setTabs(this, tabs);
  }

  public void setText(String text) {
    display.setText(this, text);
  }

  public void setTextChars(char[] text) {
    setText(new String(text));
  }

  //Sets the maximum number of characters that the receiver is capable of holding to be the argument.
  public void setTextLimit(int limit) {
    display.setTextLimit(this, limit);
  }

  //  Sets the zero-relative index of the line which is currently at the top of the receiver.
  public void setTopIndex(int index) {
    display.setTopIndex(this, index);
  }

  public void showSelection() {
    display.showSelection(this);
  }

  ControlType getControlType() {
    return ControlType.TEXT;
  }

  public void selectAll() {
    setSelection(0);
  }
}