package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
import org.kobjects.dom.Document;
import org.kobjects.dom.Element;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class GwtDisplay extends PlatformDisplay {

    public static native void log(Object... args) /*-{
        $wnd.console.log(args);
    }-*/;

    public void asyncExec(Runnable runnable) {
        runnable.run();                                   // FIXME
    }

    @Override
    public void addChild(Composite composite, Control control) {
        log("addChild to: ", composite.peer, "; child: ", control);
        ((Element) composite.peer).appendChild((Element) control.peer);

    }

    @Override
    public void addListener(Control control, int i, Listener listener) {

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
                input.setAttribute("type", (control.style & SWT.RADIO) != 0 ? "radio" : "checkbox");
                return result;
            }
            Element button = Document.get().createElement("button");
            button.setAttribute("class",
                    "mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent");
            return button;
        }
        if (control instanceof Shell) {
            Element shell = Document.get().createElement("div");
            shell.setAttribute("style", "width:100%;max-width:1024px;background-color:#eee;min-height:100vh;margin:auto;position:relative");
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
    public void disposeShell(Shell shell) {

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
    public Insets getInsets(Scrollable scrollable) {
        return new Insets();
    }

    @Override
    public boolean getSelection(Button button) {
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
    public void openShell(Shell shell) {

        //Element.getBody().appendChild((Element) shell.peer);
    }

    @Override
    public void pack(Shell shell) {
        shell.layout(true, true);
    }

    @Override
    public void removeChild(Composite composite, Control control) {

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
    public void setMeasuredSize(Control control, int i, int i1) {

    }

    @Override
    public void setSelection(Button button, boolean b) {

    }

    @Override
    public void showPopupMenu(Menu menu) {

    }

    @Override
    public void updateMenuBar(Decorations decorations) {

    }

    @Override
    public int getScrollBarSize(ScrolledComposite scrolledComposite, int i) {
        return 0;
    }

    @Override
    public void redraw(Control control, int i, int i1, int i2, int i3, boolean b) {

    }
}