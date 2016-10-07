package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
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
        throw new RuntimeException("NYI");
    }

    @Override
    public void addChild(Composite composite, Control control) {
        log("addChild to: ", composite.peer, "; child: ", control);
        ((Element) composite.peer).appendChild((Element) control.peer);

    }

    @Override
    public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void addListener(Control control, int i, Listener listener) {
        throw new RuntimeException("NYI");

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
        if (control instanceof Text) {
            return Document.get().createElement("input");
        }
        if (control instanceof Button) {
            if ((control.style & (SWT.CHECK|SWT.RADIO)) != 0) {
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
            Element button = Document.get().createElement("button");
         //   button.setAttribute("class",
          //          "mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent");
            return button;
        }
        if (control instanceof Slider) {
            Element result = Document.get().createElement("input");
        //    result.setAttribute("class", "mdl-slider mdl-js-slider");
            result.setAttribute("type", "range");
            return result;
        }
        if (control instanceof Shell) {
            Element shell = Document.get().createElement("div");
            shell.setAttribute("style", "width:100%;background-color:#eee;min-height:100vh;margin:auto;position:relative");
         //   Document.get().getBody().setAttribute("style", "min-height:100%");
         //   Document.get().getBody().getParentElement().setAttribute("style", "height:100%");
            Document.get().getBody().appendChild(shell);
            return shell;
        }
        if (control instanceof Canvas) {
            return Document.get().createElement("canvas");
        }

        return Document.get().createElement("div");
    }

    @Override
    public Object createImage(int width, int height) {
        throw new RuntimeException("NYI");

    }

    @Override
    public GC creatGCForPlatformImage(Object platformImage) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void disposeShell(Shell shell) {
        throw new RuntimeException("NYI");

    }

    @Override
    public boolean isEnabled(Control control) {
        throw new RuntimeException("NYI");

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
    public Rectangle getImageBounds(Object platformImage) {
        throw new RuntimeException("NYI");

    }

    @Override
    public Insets getInsets(Scrollable scrollable) {
        return new Insets();
    }

    @Override
    public int getItemCount(Control control) {
        throw new RuntimeException("NYI");
    }

    @Override
    public Monitor getMonitor(Control control) {
        throw new RuntimeException("NYI");
    }

    @Override
    public boolean getSelection(Button button) {
        throw new RuntimeException("NYI");

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
        throw new RuntimeException("NYI");

    }

    @Override
    public void openShell(Shell shell) {

        //Element.getBody().appendChild((Element) shell.peer);
        throw new RuntimeException("NYI");

    }

    @Override
    public void pack(Shell shell) {
        shell.layout(true, true);
    }

    @Override
    public void removeChild(Composite composite, Control control) {
        throw new RuntimeException("NYI");

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
        throw new RuntimeException("NYI");

    }

    @Override
    public void setFocus(Control control) {
        throw new RuntimeException("NYI");
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
        throw new RuntimeException("NYI");
    }

    @Override
    public void setMeasuredSize(Control control, int i, int i1) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void setRange(Control control, int minimum, int maximum) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void setSelection(Button button, boolean b) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void setSelection(Control control, int selection) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void showPopupMenu(Menu menu) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void updateMenuBar(Decorations decorations) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void setImage(Control control, Image image) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void setAlignment(Control button, int alignment) {
        throw new RuntimeException("NYI");
    }

    @Override
    public String getItem(Control control, int i) {
        throw new RuntimeException("NYI");
    }

    @Override
    public int getScrollBarSize(ScrolledComposite scrolledComposite, int i) {
        throw new RuntimeException("NYI");

    }

    @Override
    public int getSelection(Control control) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void redraw(Control control, int i, int i1, int i2, int i3, boolean b) {
        throw new RuntimeException("NYI");

    }

    @Override
    public void removeItems(Combo combo, int start, int end) {
        throw new RuntimeException("NYI");

    }
}