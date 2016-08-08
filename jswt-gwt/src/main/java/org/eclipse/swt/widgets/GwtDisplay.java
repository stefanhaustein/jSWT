package org.eclipse.swt.widgets;

import org.kobjects.dom.Document;
import org.kobjects.dom.Element;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class GwtDisplay extends PlatformDisplay {

    @Override
    public void addChild(Composite composite, Control control) {
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
            Document.get().getBody().appendChild(shell);
            return shell;
        }

        return Document.get().createElement("div");
    }

    @Override
    public void disposeShell(Shell shell) {

    }

    @Override
    public Rectangle getBounds(Control control) {
        return new Rectangle(10, 10, 100, 100);
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
        Elements.setBounds((Element) control.peer, x, y, w, h);
    }

    @Override
    public void setText(Control control, String s) {
        if (control instanceof Shell) {
            Element.setTitle(s);
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