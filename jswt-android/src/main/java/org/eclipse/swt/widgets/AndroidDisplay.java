package org.eclipse.swt.widgets;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.*;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import android.widget.Spinner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.InputStream;
import java.util.Arrays;


public class AndroidDisplay extends PlatformDisplay {

  static final String TAG = "AndroidDisplay";

  AppCompatActivity activity;
  Shell topShell;
  Context flatButtonContext;
  float pixelPerDp;

  private static int getArgb(Color color) {
    return (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | (color.getBlue());
  }

  private static void unsupported(Control control, String method) {
    Log.d(TAG, "unsupported for " + control.getControlType().name() + ": " + method);
  }

  private static ArrayAdapter<String> getArrayAdapter(Control control) {
    switch (control.getControlType()) {
      case COMBO:
        return (ArrayAdapter<String>) ((android.widget.Spinner) control.peer).getAdapter();
      case LIST:
        return (ArrayAdapter<String>) ((android.widget.ListView) control.peer).getAdapter();
      default:
        throw new IllegalArgumentException();
    }
  }

  public AndroidDisplay(AppCompatActivity activity) {
    this.activity = activity;
    flatButtonContext = new ContextThemeWrapper(activity,
            android.support.v7.appcompat.R.style.Widget_AppCompat_Button_Borderless);
    pixelPerDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, activity.getResources().getDisplayMetrics());
  }

  @Override
  public void asyncExec(Runnable runnable) {
    activity.runOnUiThread(runnable);
  }

  private void handleRadioGroup(Button button, boolean selected) {
    if (!selected) {
      return;
    }
    if ((button.style & SWT.RADIO) != 0) {
      Composite parent = (Composite) button.parent;
      for (Control child: parent.children) {
        if (child != button && (child.style & SWT.RADIO) != 0 && (child instanceof Button)) {
          ((CompoundButton) child.peer).setChecked(false);
        }
      }
    }
  }

  @Override
  public Object createControl(final Control control) {
    switch (control.getControlType()) {
      case BUTTON: {
        if ((control.style & SWT.TOGGLE) != 0) {
          ToggleButton toggleButton = new ToggleButton(activity);
          toggleButton.setTextOn("");
          toggleButton.setTextOff("");
          toggleButton.setText("");

          if ((control.style & SWT.RADIO) != 0) {
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                handleRadioGroup((Button) control, b);
                control.notifyListeners(SWT.Selection, null);
              }
            });
          }
          return toggleButton;
        }
        if ((control.style & SWT.RADIO) != 0) {
          RadioButton radioButton = new AppCompatRadioButton(activity);
          radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              handleRadioGroup((Button) control, b);
              control.notifyListeners(SWT.Selection, null);
            }
          });
          return radioButton;
        }
        if ((control.style & (SWT.CHECK)) != 0) {
          return new AppCompatCheckBox(activity);
        }
        if ((control.style & SWT.FLAT) != 0) {
          return new AppCompatButton(flatButtonContext, null, 0);
        }
        AppCompatButton button = new AppCompatButton(activity);
        return button;
      }
      case COMBO: {
        android.widget.Spinner spinner = new android.widget.Spinner(activity);
        spinner.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line));
        return spinner;
      }
      case TEXT: {
        AppCompatEditText editText = new AppCompatEditText(activity);
        if ((control.style & SWT.MULTI) == 0) {
          editText.setMaxLines(1);
        }
        return editText;
      }
      case LABEL:
        return new AppCompatTextView(activity);
      case LIST: {
        ListView listView = new ListView(activity);
        if ((control.style & SWT.SINGLE) != 0) {
          listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_single_choice));
          listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else if ((control.style & SWT.MULTI) != 0) {
          listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_multiple_choice));
          listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } else {
          listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1));
          listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
        return listView;
      }
      case SCROLLED_COMPOSITE:
        return new android.widget.ScrollView(activity);
      case SCALE:
      case SLIDER:
        return new android.widget.SeekBar(activity);
      case SHELL:
        return new AndroidShellView(activity, (Shell) control);
      case TABLE:
        return new AndroidTableView(activity, (Table) control);
      case TAB_FOLDER:
        return new AndroidTabFolder(activity);
      case CANVAS:
        return new AndroidCanvas(activity, (Canvas) control);
      case GROUP:
      case COMPOSITE:
      case TOOLBAR:
        return new AndroidCompositeView(activity, (Composite) control);
      case PROGRESS_BAR: {
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate((control.style & SWT.INDETERMINATE) != 0);
        return progressBar;
      }
      case SPINNER: {
        EditText editText = new EditText(activity);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        return editText;
      }
      default:
        throw new RuntimeException("Unrecognized control type " + control.getControlType() + " for " + control);
    }
  }

  private Color createColor(int argb) {
    return new Color(this, (argb >> 16) & 255, (argb >> 8) & 255, argb & 255, (argb >> 24) & 255);
  }

  @Override
  public boolean isEnabled(Control control) {
    return ((View) control.peer).isEnabled();
  }

  @Override
  public Color getBackground(Control control) {
    View view = (View) control.peer;
    while (true) {
      Drawable bg = view.getBackground();
      if (bg instanceof ColorDrawable) {
        ColorDrawable cd = (ColorDrawable) bg;
        return createColor(cd.getColor());
      }
      if (!(view.getParent() instanceof View)) {
        return createColor(0);
      }
      view = (View) view.getParent();
    }
  }

  @Override
  public void openShell(Shell shell) {
    ((AndroidShellView) shell.peer).open();

    // drawerLayout.addView(view, 0);
    // TODO: update menu bar!
//    activity.setContentView(view);
  }

  @Override
  public Rectangle getBounds(Control control) {
    View view = (View) control.peer;
    return new Rectangle(view.getLeft(), view.getTop(), view.getMeasuredWidth(), view.getMeasuredHeight());
  }

  @Override
  public Color getForeground(Control control) {
    if (control.peer instanceof TextView) {
      return createColor(((TextView) control.peer).getCurrentTextColor());
    }
    return createColor(0xffffffff);
  }

  @Override
  int getFocusIndex(List list) {
    unsupported(list, "getFocusIndex");
    return -1;
  }

  @Override
  boolean getGrayed(Button control) {
    return false;
  }

  @Override
  public Rectangle getImageBounds(Object platformImage) {
    Bitmap bitmap = (Bitmap) platformImage;
    return new Rectangle(0, 0, bitmap.getWidth(), bitmap.getHeight());
  }

  @Override
  public Insets getInsets(Scrollable scrollable) {
    return new Insets();
  }


  @Override
  public Monitor getMonitor(Control control) {
    int w = activity.getResources().getDisplayMetrics().widthPixels;
    int h = activity.getResources().getDisplayMetrics().heightPixels;
    return new Monitor(new Rectangle(0, 0, w, h), new Rectangle(0, 0, w, h));
  }


  @Override
  public int getItemCount(Control control) {
    return getArrayAdapter(control).getCount();
  }

  @Override
  public Point computeSize(Control control, int wHint, int hHint, boolean changed) {
    View view = (View) control.peer;
    if (view instanceof EditText) {
      ((EditText) view).setMaxWidth(Integer.MAX_VALUE);
    }
    view.measure(
        wHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | wHint),
        hHint == SWT.DEFAULT ? View.MeasureSpec.UNSPECIFIED : (View.MeasureSpec.EXACTLY | hHint));
    return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
  }

  @Override
  public void setBounds(Control control, int x, int y, int width, int height) {
    if (control instanceof Shell) {
      return;
    }

    View view = (View) control.peer;
    view.measure(View.MeasureSpec.EXACTLY | width,
            View.MeasureSpec.EXACTLY | height);
    view.setLeft(x);
    view.setTop(y);

    view.setLeft(x);
    view.setTop(y);

    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams != null) {
      layoutParams.width = View.MeasureSpec.EXACTLY | width;
      layoutParams.height = View.MeasureSpec.EXACTLY | height;
    } else {
      System.err.println("setBounds for " + control + ": LayoutParams are null");
    }
    if (view instanceof EditText) {
      ((EditText) view).setMaxWidth(width);
    }
  }

  @Override
  public void setEnabled(Control control, boolean b) {
    ((View) control.peer).setEnabled(b);
  }

  @Override
  public void setFocus(Control control) {
    ((View) control.peer).requestFocus();
  }

  @Override
  public void setForeground(Control control, Color color) {
    if (control.peer instanceof TextView && color != null) {
      TextView tv = (TextView) control.peer;
      tv.setTextColor(getArgb(color));
    }
  }

  @Override
  void setGrayed(Button button) {
    unsupported(button, "setGrayed");
  }

  @Override
  public String getText(Control control) {
    Object peer = control.peer;
    if (peer instanceof TextView) {
      return ((TextView) peer).getText().toString();
    }
    if (peer instanceof AndroidShellView) {
      return ((AndroidShellView) peer).text;
    }
    if (peer instanceof Spinner) {
      return (String) (((Spinner) peer).getSelectedItem());
    }
    if (peer instanceof AndroidCompositeView) {
      return ((AndroidCompositeView) peer).getText();
    }
    return null;
  }

  @Override
  public void setText(Control control, String text) {
    Object peer = control.peer;
    if (peer instanceof android.widget.Button) {
      String rawText = removeAccelerators(text);
      ((android.widget.Button) peer).setText(rawText);
      if (peer instanceof ToggleButton) {
        ToggleButton toggleButton = (ToggleButton) peer;
        toggleButton.setTextOn(rawText);
        toggleButton.setTextOff(rawText);
      }
    } else if (peer instanceof TextView) {
      ((TextView) peer).setText(text);
    } else if (peer instanceof AndroidShellView) {
      ((AndroidShellView) peer).setText(text);
    } else if (peer instanceof AndroidCompositeView) {
      ((AndroidCompositeView) peer).setText(text);
    }
  }

  @Override
  public void setVisible(Control control, boolean visible) {
    ((View) control.peer).setVisibility(visible ? View.VISIBLE : View.GONE);
  }

/*
  MenuItem findMenuItem(Menu menu, String title) {
    for (int i = 0; i < menu.getItemCount(); i++) {
      MenuItem item = menu.getItem(i);
      if (item.subMenu != null) {
        MenuItem result = findMenuItem(item.subMenu, title);
        if (result != null) {
          return result;
        }
      } else if (item.getText().equals(title)) {
        return item;
      }
    }
    return null;
  }
*/
  private void populateMenu(Menu sourceMenu, android.view.Menu androidMenu, boolean flattenFirst) {
    for (int i = 0; i < sourceMenu.getItemCount(); i++) {
      final MenuItem item = sourceMenu.getItem(i);
      if (item.subMenu == null) {
        android.view.MenuItem androidItem = androidMenu.add(item.getText());
        androidItem.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(android.view.MenuItem menuItem) {
            AndroidShellView androidShellView = (AndroidShellView) topShell.peer;
            androidShellView.drawerLayout.closeDrawer(androidShellView.navigationView, false);
            Event event = new Event();
            event.display = AndroidDisplay.this;
            event.widget = item;
            event.type = SWT.Selection;
            item.listeners.sendEvent(event);
            return true;
          }
        });
      } else if (i == 0 && flattenFirst) {
        populateMenu(item.subMenu, androidMenu, false);
      } else {
        android.view.Menu androidSubMenu = androidMenu.addSubMenu(item.getText());
        populateMenu(item.subMenu, androidSubMenu, false);
      }
    }
  }

  @Override
  public void updateMenuBar(Decorations decorations) {
    AndroidShellView androidShell = ((AndroidShellView) decorations.peer);
    if (androidShell.navigationView != null) {
      android.view.Menu androidMenu = androidShell.navigationView.getMenu();
      androidMenu.clear();
      populateMenu(decorations.menuBar, androidMenu, true);
    }
  }

  @Override
  public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
    AndroidTabFolder androidTabFolder = (AndroidTabFolder) tabFolder.peer;
    androidTabFolder.updateTab(index, tabItem);
  }

  @Override
  void setTopIndex(Control control, int topIndex) {
    unsupported(control, "setTopIndex");
  }

  @Override
  void showSelection(Control control) {
    unsupported(control, "showSelection");
  }

  @Override
  Point getCaretLocation(Control control) {
    switch (control.getControlType()) {
      case TEXT: {
        EditText editText = ((EditText) control.peer);
        android.text.Layout layout = editText.getLayout();
        int pos = editText.getSelectionStart();
        int line = layout.getLineForOffset(pos);
        int baseline = layout.getLineBaseline(line);
        int ascent = layout.getLineAscent(line);
        return new Point(Math.round(layout.getPrimaryHorizontal(pos)), Math.round(baseline + ascent));
      }
      default:
        unsupported(control, "getCaretLocation");
        return new Point(0, 0);
    }
  }

  @Override
  int getCaretPosition(Control control) {
      switch (control.getControlType()) {
        case TEXT:
          return ((EditText) control.peer).getSelectionStart();
        default:
          unsupported(control, "getCaretPosition");
          return 0;
      }
  }

  @Override
  int getItemHeight(Control control) {
    unsupported(control, "getItemHeight");
    return 0;
  }

  @Override
  int getLineHeight(Control control) {
    switch (control.getControlType()) {
      case TEXT:
        return ((EditText) control.peer).getLineHeight();
      default:
        unsupported(control, "getLineHeight");
        return 0;
    }
  }

  @Override
  Point getSelectedRange(Control control) {
    switch (control.getControlType()) {
      case TEXT: {
        EditText editText = (EditText) control.peer;
        return new Point(editText.getSelectionStart(), editText.getSelectionEnd());
      }
      default:
        unsupported(control, "getSelectedRange");
        return new Point(0, 0);
    }
  }

  @Override
  int getTopPixel(Text control) {
    return ((EditText) control.peer).getScrollY();
  }

  @Override
  void paste(Control control) {
    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    CharSequence paste = clipboard.getPrimaryClip().getItemAt(0).getText();
    switch (control.getControlType()) {
      case TEXT: {
        EditText editText = (EditText) control.peer;
        String oldContent = editText.getText().toString();
        String newContent = oldContent.substring(0, editText.getSelectionStart()) + paste
                + oldContent.substring(editText.getSelectionEnd());
        editText.setText(newContent);
        break;
      }
      default:
        unsupported(control, "paste");
    }
  }

  @Override
  boolean setDoubleClickEnabled(Text text, boolean doubleClick) {
    return false;
  }

  @Override
  char setEchoChar(Text control, final char echo) {
    EditText editText = (EditText) control.peer;
    editText.setTransformationMethod(echo == 0 ? null : new PasswordTransformationMethod() {
      @Override
      public CharSequence getTransformation(CharSequence charSequence, View view) {
        char[] fill = new char[charSequence.length()];
        Arrays.fill(fill, echo);
        return new String(fill);
      }
    });
    return echo;
  }

  @Override
  boolean setEditable(Text control, boolean editable) {
    EditText editText = (EditText) control.peer;
    KeyListener keyListener = editText.getKeyListener();
    if (editable && keyListener == null) {
      editText.setKeyListener((KeyListener) editText.getTag());
    } else if (!editable && keyListener != null) {
      editText.setTag(keyListener);
      editText.setKeyListener(null);
    }
    return editable;
  }

  @Override
  String setMessage(Text text, String message) {
    ((EditText) text.peer).setHint(message);
    return message;
  }

  @Override
  void setOrientation(Control control, int orientation) {
    ((View) control.peer).setTextDirection(
            orientation == SWT.RIGHT_TO_LEFT ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
  }

  @Override
  boolean setRedraw(Text text, boolean redraw) {
    return false;
  }

  @Override
  int setTextLimit(Control control, int limit) {
    switch (control.getControlType()) {
      case TEXT:
        ((EditText) control.peer).setFilters(new InputFilter[] {new InputFilter.LengthFilter(limit)});
        break;
      default:
        unsupported(control, "setTextLimit");
    }
    return limit;
  }

  @Override
  int setTabs(Text text, int tabs) {
    return 0;
  }

  @Override
  void setSelectionRange(Control control, int start, int end) {
    switch (control.getControlType()) {
      case TEXT:
        ((EditText) control.peer).setSelection(start, end);
        break;
      default:
        unsupported(control, "setSelectionRange");
        break;
    }
  }

  @Override
  int getOrientation(Control control) {
    return ((View) control.peer).getTextDirection() == View.TEXT_DIRECTION_RTL ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
  }

  @Override
  boolean getListVisible(Combo control) {
    unsupported(control, "getListVisible");
    return false;
  }

  @Override
  void setListVisible(Combo control, boolean visible) {
    unsupported(control, "setListVisible");
  }

  @Override
  void setVisibleItemCount(Combo combo, int itemCount) {
    unsupported(combo, "setVisibleItemCount");
  }

  @Override
  int getVisibleItemCount(Combo combo) {
    unsupported(combo, "getVisibleItemCount");
    return 0;
  }


  @Override
  void addTableColumn(Table table, TableColumn column) {
    ((AndroidTableView) table.peer).getAdapter().notifyDataSetChanged();
  }

  @Override
  void addTableItem(Table table, TableItem item) {
    ((AndroidTableView) table.peer).getAdapter().notifyItemInserted(item.index);
  }

  @Override
  void updateTableColumn(Table table, TableColumn item) {
    ((AndroidTableView) table.peer).getAdapter().notifyDataSetChanged();
  }

  @Override
  void updateTableItem(Table table, TableItem item) {
    ((AndroidTableView) table.peer).getAdapter().notifyItemChanged(item.index);
  }

  @Override
  void removeTableColumn(Table table, TableColumn column) {
    ((AndroidTableView) table.peer).getAdapter().notifyDataSetChanged();
  }

  @Override
  void removeTableItem(Table table, TableItem item) {
    ((AndroidTableView) table.peer).getAdapter().notifyItemRemoved(item.index);
  }

  @Override
  void moveAbove(Control control, Control other) {

  }

  @Override
  void updateTable(Table table) {

  }

  @Override
  ToolBar getToolBar(Shell shell) {
    return ((AndroidShellView) shell.peer).toolBar;
  }

  @Override
  void updateMenuItem(MenuItem item) {

  }

  @Override
  public void setImage(Control control, Image image) {
    if (control.peer instanceof TextView && (control.peer instanceof ToggleButton || !(control.peer instanceof CompoundButton))) {
      // Regular button, but might turn into image button.
      TextView textView = (TextView) control.peer;
      if (textView instanceof android.widget.Button) {
        int minWidth = (textView.getText().length() == 0 && image != null)
                ? Math.round(24 * pixelPerDp) + image.getBounds().width : Math.round(88 * pixelPerDp);
        textView.setMinWidth(minWidth);
        textView.setMinimumWidth(minWidth);
      }
      Drawable icon = image == null ? null : new BitmapDrawable(activity.getResources(), (Bitmap) image.peer);
      ((android.widget.TextView) control.peer).setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }
  }

  @Override
  public void setAlignment(Control control, int alignment) {
    if (control.getControlType() == Control.ControlType.BUTTON && ((control.style & SWT.ARROW) != 0)) {
      String label;
      if ((control.style & SWT.UP) != 0) {
        label = "^";
      } else if ((control.style & SWT.DOWN) != 0) {
        label = "v";
      } else if ((control.style & SWT.LEFT) != 0) {
        label = "<";
      } else {
        label = ">";
      }
      ((android.widget.Button) control.peer).setText(label);
    }
  }

  @Override
  public boolean isSelected(List list, int i) {
    unsupported(list, "isSelected");
    return false;
  }

  @Override
  boolean isVisible(Control control) {
    return ((View) control.peer).getVisibility() == View.VISIBLE;
  }

  @Override
  public void setFont(Control control, Font font) {
    unsupported(control, "setFont");
  }

  @Override
  public void setItem(Control control, int index, String string) {
    ArrayAdapter<String> adapter = getArrayAdapter(control);
    String old = adapter.getItem(index);
    adapter.remove(old);
    adapter.insert(string, index);
  }

  @Override
  public Font getFont(Control control) {
    int fontSize = 16;
    if (control.peer instanceof TextView) {
      fontSize = Math.round(((TextView) control.peer).getTextSize());
    }
    return new Font(this, "", fontSize, 0);
  }

  @Override
  int getTopIndex(Control control) {
    return 0;
  }

  @Override
  public String getItem(Control control, int i) {
    return getArrayAdapter(control).getItem(i);
  }

  /*
  @Override
  public void setMeasuredSize(Control control, int width, int height) {
    if (control.peer instanceof AndroidComposite) {
      ((AndroidComposite) control.peer).setMeasuredSize(width, height);
    } else {
      ((View) control.peer).measure(View.MeasureSpec.EXACTLY | width, View.MeasureSpec.EXACTLY | height);
    }
  }
*/

  @Override
  public void setRange(Control control, int minimum, int maximum) {
    if (control.peer instanceof android.widget.ProgressBar) {
      ((android.widget.ProgressBar) control.peer).setMax(maximum);
      if (minimum != 0) {
        System.err.println("FIXME: AndroidDisplay.setRange(): Minimum ignored!");   // FIXME
      }
    } else {
      System.err.println("FIXME: AndroidDisplay.setRange(): ignored! " + control.getControlType());  // FIXME
    }
  }

  @Override
  public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
    System.err.println("FIXME: setSliderProperties()");   // FIXME
  }

  @Override
  public void setSelection(Control control, int selection) {
    switch (control.getControlType()) {
      case BUTTON:
        if ((control.style & SWT.RADIO) != 0) {
          handleRadioGroup((Button) control, selection != 0);
        }
        if ((control.style & (SWT.RADIO | SWT.TOGGLE | SWT.CHECK)) != 0) {
          ((CompoundButton) control.peer).setChecked(selection != 0);
        }
        break;
      case SLIDER:
      case SCALE:
      case PROGRESS_BAR:
        ((android.widget.ProgressBar) control.peer).setProgress(selection);
        break;
      case SPINNER:
        ((android.widget.EditText) control.peer).setText(String.valueOf(selection));
        break;
      default:
        System.err.println("FIXME: setSelection() for " + control.getControlType());
    }
  }

  @Override
  public void setIndexSelected(Control control, int index, boolean selected) {
    unsupported(control, "setSelectedIndex");
  }

  @Override
  public void showPopupMenu(final Menu menu) {
    View anchor = (View) ((Control) menu.getParent()).peer;
    PopupMenu popupMenu = new PopupMenu(activity, anchor);
    populateMenu(menu, popupMenu.getMenu(), false);
    /*popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(android.view.MenuItem item) {
        findMenuItem(menu, item.getTitle().toString());
        return true;
      }
    }); */
    popupMenu.show();
  }

  @Override
  public int getScrollBarSize(ScrolledComposite scrolledComposite, int orientation) {
    System.err.println("getScrollBarSize(): Returning fake value 1");  // FIXME
    return 1;
  }

  @Override
  public int getSelection(Control control) {
    switch (control.getControlType()) {
      case BUTTON:
        if ((control.style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
          return ((CompoundButton) control.peer).isChecked() ? 1 : 0;
        }
        return 0;
      case SCALE:
      case SLIDER:
      case PROGRESS_BAR:
        return ((android.widget.ProgressBar) control.peer).getProgress();
      case SPINNER:
        try {
          return Integer.parseInt(((android.widget.EditText) control.peer).getText().toString());
        } catch (NumberFormatException e) {
          return 0;
        }
      case TABLE:
        return ((AndroidTableView) control.peer).selectedIndex;
      default:
        System.err.println("NYI: getSelection() for " + control.getControlType());
        return 0;
    }
  }

  @Override
  public void redraw(Control control, int x, int y, int w, int h, boolean all) {
    ((View) control.peer).invalidate(x, y, x+w, y+h);
  }

  @Override
  public void removeItems(Control control, int start, int end) {
    ArrayAdapter<String> adapter = getArrayAdapter(control);
    for (int i = end; i >= start; i--) {
      adapter.remove(adapter.getItem(i));
    }
  }

  @Override
  public void disposePeer(Control control) {
    if (control.getControlType() == Control.ControlType.SHELL) {
      AndroidShellView shellView = (AndroidShellView) control.peer;
      if (shellView.dialog != null) {
        shellView.dialog.hide();
        shellView.dialog = null;
      }
    } else {
      Composite parent = control.getParent();
      ((ViewGroup) parent.peer).removeView((View) control.peer);
    }
  }

  @Override
  public void setBackground(Control control, Color color) {

  }

  @Override
  public void setBackgroundImage(Control control, Image image) {

  }

  @Override
  public void addItem(Control control, String s, int index) {
    switch (control.getControlType()) {
      case LIST:
        ((ArrayAdapter<String>) ((ListView) control.peer).getAdapter()).insert(s, index);
        break;
      case COMBO:
        ((ArrayAdapter<String>) ((android.widget.Spinner) control.peer).getAdapter()).insert(s, index);
        break;
      default:
        System.err.println("AndroidDisplay.addItem " + control.getControlType());
    }
  }

  @Override
  public void addChild(Composite parent, Control child) {
    if (!(parent instanceof TabFolder)) {
      View childView = (View) child.peer;
      ((ViewGroup) parent.peer).addView(childView);
    }
  }

  @Override
  public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
    AndroidTabFolder androidTabFolder = (AndroidTabFolder) tabFolder.peer;
    androidTabFolder.addTab(index, tabItem);
  }

  @Override
  public void addListener(final Control control, final int eventType, Listener listener) {
    View view = (View) control.peer;
    switch (eventType) {
      case SWT.MouseMove:
      case SWT.MouseDown:
      case SWT.MouseUp:
        view.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
              case MotionEvent.ACTION_DOWN:
              case MotionEvent.ACTION_POINTER_DOWN:
                notifyListeners(control, SWT.MouseDown, motionEvent);
                return true;
              case MotionEvent.ACTION_UP:
              case MotionEvent.ACTION_POINTER_UP:
                notifyListeners(control, SWT.MouseUp, motionEvent);
                return true;
              case MotionEvent.ACTION_MOVE:
                notifyListeners(control, SWT.MouseMove, motionEvent);
                return true;
            }
            return false;
          }
        });
        break;

      case SWT.Modify:
        if (view instanceof TextView) {
          ((TextView) view).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
              control.notifyListeners(SWT.Modify, null);
            }
          });
        }
        break;

      case SWT.Selection:
        if (view instanceof android.widget.Button) {
          ((android.widget.Button) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              control.notifyListeners(eventType, null);
            }
          });
        } else if (view instanceof android.widget.CheckBox) {
          ((android.widget.CheckBox) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              control.notifyListeners(SWT.Selection, null);
            }
          });
        }
        break;
    }


  }

  @Override
  void copy(Control control) {
    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    switch (control.getControlType()) {
      case TEXT: {
        EditText editText = (EditText) control.peer;
        String oldContent = editText.getText().toString();
        String copy = oldContent.substring(editText.getSelectionStart(), editText.getSelectionEnd());
        clipboard.setPrimaryClip(ClipData.newPlainText(null, copy));
        break;
      }
      default:
        unsupported(control, "copy");
    }

  }

  @Override
  void cut(Control control) {
    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    switch (control.getControlType()) {
      case TEXT: {
        EditText editText = (EditText) control.peer;
        String oldContent = editText.getText().toString();
        String cut = oldContent.substring(editText.getSelectionStart(), editText.getSelectionEnd());
        String newContent = oldContent.substring(0, editText.getSelectionStart())
                + oldContent.substring(editText.getSelectionEnd());
        editText.setText(newContent);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, cut));
        break;
      }
      default:
        unsupported(control, "cut");
    }
  }

  public Object createImage(int width, int height) {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
  }

  @Override
  public GC createGCForPlatformImage(Object platformImage) {
    return new AndroidGC(this, new android.graphics.Canvas((Bitmap) platformImage));

  }

  @Override
  public Object loadImage(InputStream is) {

    Bitmap result = BitmapFactory.decodeStream(is);
    /*
    System.out.println("Image size: " + result.getWidth() + "x" + result.getHeight());
    for (int y = 0; y < result.getHeight(); y++) {
      for (int x = 0; x < result.getWidth(); x++) {
        int rgb = result.getPixel(x, y);
        System.out.print(Integer.toHexString(rgb));
        System.out.print(",");
      }
      System.out.println();
    }
*/
    return result;
  }


  void notifyListeners(Control control, int type, MotionEvent motionEvent) {
    Event event = new Event();
    event.x = Math.round(motionEvent.getX());
    event.y = Math.round(motionEvent.getY());
    control.notifyListeners(type, event);
  }

}
