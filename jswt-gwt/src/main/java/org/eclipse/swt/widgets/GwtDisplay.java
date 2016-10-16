package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.kobjects.dom.Document;
import org.kobjects.dom.Element;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.IOException;
import java.io.InputStream;

public class GwtDisplay extends PlatformDisplay {

    static int id = 0;

    public static native void log(Object... args) /*-{
        switch (args.length) {
            case 1:
                $wnd.console.log(args[0]);
                break;
            case 2:
                $wnd.console.log(args[0], args[1]);
                break;
            case 3:
                $wnd.console.log(args[0], args[1], args[2]);
                break;
            case 4:
                $wnd.console.log(args[0], args[1], args[2], args[3]);
                break;
            default:
                $wnd.console.log(args);
        }
    }-*/;

    public void asyncExec(Runnable runnable) {
        runnable.run();                                   // FIXME
    }

    @Override
    public void addItem(Control control, String s, int index) {
        log("FIXME: GwtDisplay.addItem");
    }

    @Override
    public void addChild(Composite composite, Control control) {
        if (composite.getControlType() != Control.ControlType.TAB_FOLDER) {
            log("addChild to: ", composite.peer, "; child: ", control);
            ((Element) composite.peer).appendChild((Element) control.peer);
        }
    }

    @Override
    public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
        ((GwtTabFolder) tabFolder.peer).addTab(index, tabItem);
    }

    @Override
    public void addListener(Control control, int i, Listener listener) {
        log("FIXME: GwtDisplay.addListener");
    }

    @Override
    public Point computeSize(Control control, int wHint, int hHint, boolean b) {
        return new Point(
                wHint == SWT.DEFAULT ? Elements.getMinWidth((Element) control.peer) : wHint,
                hHint == SWT.DEFAULT ? Elements.getMinHeight((Element) control.peer) : hHint);
    }

    @Override
    public Object createControl(Control control) {
        log("createControl:", control);
        switch (control.getControlType()) {
            case TEXT:
                return Document.get().createElement("input");
            case BUTTON_PUSH:
                return Document.get().createElement("button");
            case BUTTON_CHECKBOX:
            case BUTTON_RADIO: {
                Element result = Document.get().createElement("label");
                Element input = Document.get().createElement("input");
                Element span = Document.get().createElement("span");
                result.appendChild(input);
                result.appendChild(span);
                String inputId = "i" + GwtDisplay.id++;
                result.setAttribute("for", inputId);
                input.setAttribute("id", inputId);
                if ((control.style & SWT.RADIO) != 0) {
                    input.setAttribute("type", "radio");
                    //    result.setAttribute("class", "mdl-radio mdl-js-radio mdl-js-ripple-effect");
                    //   input.setAttribute("class", "mdl-radio__button");
                    //   span.setAttribute("class", "mdl-radio__label");
                } else {
                    input.setAttribute("type", "checkbox");
                    //  result.setAttribute("class", "mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect");
                    //  input.setAttribute("class", "mdl-checkbox__input");
                    //  span.setAttribute("class", "mdl-checkbox__label");
                }
                return result;
            }
            case COMBO:
                return Document.get().createElement("select");
            case LIST: {
                Element result = Document.get().createElement("select");
                result.setAttribute("multiple", "multiple");
                return result;
            }
            case SCALE:
            case SLIDER: {
                Element result = Document.get().createElement("input");
                //    result.setAttribute("class", "mdl-slider mdl-js-slider");
                result.setAttribute("type", "range");
                return result;
            }
            case SHELL_ROOT: {
                Element shell = Document.get().createElement("div");
                shell.setAttribute("style", "width:100%;background-color:#eee;min-height:100vh;margin:auto;position:relative");
                //   Document.get().getBody().setAttribute("style", "min-height:100%");
                //   Document.get().getBody().getParentElement().setAttribute("style", "height:100%");
                Document.get().getBody().appendChild(shell);
                return shell;
            }
            case LABEL:
                return Document.get().createElement("div");
            case CANVAS:
                return Document.get().createElement("canvas");
            case GROUP:
            case COMPOSITE:
                return Document.get().createElement("div");
            case PROGRESS_BAR:
                return Document.get().createElement("progress");
            case TAB_FOLDER:
                return GwtTabFolder.create(Document.get());
            case SPINNER: {
                Element spinner = Document.get().createElement("input");
                spinner.setAttribute("type", "number");
                return spinner;
            }
            default:
                throw new RuntimeException("FIXME: GwtDisplay.createControl type " + control.getControlType());
        }
    }

    @Override
    public Object createImage(int width, int height) {
        Element canvas = Document.get().createElement("canvas");
        canvas.setAttribute("width", String.valueOf(width));
        canvas.setAttribute("height", String.valueOf(height));
        return canvas;
    }

    @Override
    public GC creatGCForPlatformImage(Object platformImage) {
        throw new RuntimeException("FIXME: GwtDisplay.GCForPlatformImage");
    }

    @Override
    public void disposeShell(Shell shell) {
        throw new RuntimeException("FIXME: GwtDisplay.disposeShell");

    }

    @Override
    public boolean isEnabled(Control control) {
        log("FIXME: GwtDisplay.isEnabled");
        return true;
    }

    @Override
    public Color getBackground(Control control) {
        log("FIXME: GwtDisplay.getBackground()");
        return null;
    }

    @Override
    public Rectangle getBounds(Control control) {
        JsArrayNumber bounds = Elements.getBounds((Element) control.peer);
        return new Rectangle(
                Math.round((float) bounds.get(0)),
                Math.round((float) bounds.get(1)),
                Math.round((float) bounds.get(2)),
                Math.round((float) bounds.get(3)));
    }

    @Override
    public Color getForeground(Control control) {
        log("FIXME: GwtDisplay.getForeground");
        return null;
    }

    @Override
    public Rectangle getImageBounds(Object platformImage) {
        throw new RuntimeException("FIXME: GwtDisplay.getImageBounds");
    }

    @Override
    public Insets getInsets(Scrollable scrollable) {
        return new Insets();
    }

    @Override
    public int getItemCount(Control control) {
        log("FIXME: GwtDisplay.getItemCount");
        return 0;
    }

    @Override
    public Monitor getMonitor(Control control) {
        log("FIXME: GwtDisplay.getMonitor");
        return new Monitor(new Rectangle(0, 0, 2000, 1000), new Rectangle(0, 0, 2000, 1000));
    }

    @Override
    public boolean getSelection(Button button) {
        log("FIXME: GwtDisplay.getSeletion");
        return false;
    }

    @Override
    public String getText(Control control) {
        if (control instanceof Shell) {
            return "TBD: Window title";
        }
        return ((Element) control.peer).getTextContent();
    }

    @Override
    public Object loadImage(InputStream stream) throws IOException {
        throw new RuntimeException("FIXME: GwtDisplay.loadImage");
    }

    @Override
    public void openShell(Shell shell) {
        //Element.getBody().appendChild((Element) shell.peer);
        log("FIXME: GwtDisplay.openShell");
    }

    @Override
    public void pack(Shell shell) {
        shell.layout(true, true);
    }

    @Override
    public void removeChild(Composite composite, Control control) {
        throw new RuntimeException("FIXME: GwtDisplay.removeChild");

    }

    @Override
    public void setBackground(Control control, Color color) {
        log("FIXME: GwtDisplay.setBackground()");
    }

    @Override
    public void setBackgroundImage(Control control, Image image) {
        log("FIXEM: GwtDisplay.setBackgroundImage()");
    }

    @Override
    public void setBounds(Control control, int x, int y, int w, int h) {
        if (control instanceof Shell) {
            return;
        }
        Element element = (Element) control.peer;
        Elements.setBounds(element, x, y, w, h);
        if (element.getLocalName().equals("canvas")) {
            // FIXME: Invalidate instead
            element.setAttribute("width", String.valueOf(w));
            element.setAttribute("height", String.valueOf(h));
            ((Canvas) control).drawBackground(new GwtGC(element), 0, 0, w, h);
        }
    }

    @Override
    public void setEnabled(Control control, boolean b) {
        log("FIXME: GwtDisplay.setEnabled");
    }

    @Override
    public void setFocus(Control control) {
        log("FIXME: GwtDisplay.setFocus");
    }

    @Override
    public void setForeground(Control control, Color color) {
        log("FIXME: GwtDisplay.setForegound");
    }

    @Override
    public void setText(Control control, String s) {
        if (control instanceof Shell) {
            Document.get().setTitle(s);
        } else {
            Element element = (Element) control.peer;
            if (element.getLocalName().equals("input")) {
                element.setAttribute("value", s);
            } else if (element.getLocalName().equals("label")) {
                element.getLastElementChild().setTextContent(s);
            } else {
                element.setTextContent(s);
            }
        }
    }

    @Override
    public void setVisible(Control control, boolean visible) {
        log("FIXME: GwtDisplay.setVisible");
    }

    @Override
    public void setMeasuredSize(Control control, int i, int i1) {
    }

    @Override
    public void setRange(Control control, int minimum, int maximum) {
        log("FIXME: GwtDisplay.setRange");
    }

    @Override
    public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
        log("FIXME: GwtDisplay.setSliderProperties");
    }

    @Override
    public void setSelection(Button button, boolean b) {
        log("FIXME: GwtDisplay.setSelection");
    }

    @Override
    public void setSelection(Control control, int selection) {
        log("FIXME: GwtDisplay.setSelection");
    }

    @Override
    public void setSelection(List list, int index, boolean selected) {
        log("FIME: GwtDisplay.setSelection");
    }

    @Override
    public void showPopupMenu(Menu menu) {
        throw new RuntimeException("FIXME: GwtDisplay.showPopupMenu");
    }

    @Override
    public void updateMenuBar(Decorations decorations) {
        throw new RuntimeException("FIXME: GwtDisplay.updateMenuBar");

    }

    @Override
    public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
        ((GwtTabFolder) tabFolder.peer).updateTab(index, tabItem);
    }

    @Override
    public void setImage(Control control, Image image) {
        log("GwtDisplay.setImage()");
    }

    @Override
    public void setAlignment(Control button, int alignment) {
        log("FIXME: GwtDisplay.setAlignment");
    }

    @Override
    public boolean isSelected(List list, int i) {
        log("FIXME: GwtDisplay.isSelected");
        return false;
    }

    @Override
    public void setFont(Control control, Font font) {
        log("FIXME: GwtDisplay.setFont");
    }

    @Override
    public void setItem(Control control, int index, String string) {
        log("FIXME: GwtDisplay.setItem");
    }

    @Override
    public Font getFont(Control control) {

        log("FIXME: GwtDisplay.setFont");
        return null;
    }

    @Override
    public String getItem(Control control, int i) {
        log("FIXME: GwtDisplay.getItem");
        return "Item " + i;
    }

    @Override
    public int getScrollBarSize(ScrolledComposite scrolledComposite, int i) {
        log("FIXME: GwtDisplay.getScrollBarSize");
        return 0;
    }

    @Override
    public int getSelection(Control control) {
        log("FIXME: GwtDisplay.getSelection");
        return 0;
    }

    @Override
    public void redraw(Control control, int i, int i1, int i2, int i3, boolean b) {
        log("FIXME: GwtDisplay.redraw");
    }

    @Override
    public void removeItems(Control control, int start, int end) {
        log("FIXME: GwtDisplay.removeItems");
    }

}