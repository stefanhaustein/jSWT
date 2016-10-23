package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.kobjects.dom.*;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.IOException;
import java.io.InputStream;
import org.kobjects.dom.Event;

public class GwtDisplay extends PlatformDisplay {

    static String colorToCss(Color color) {
        return color == null ? null
                : "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + (color.getAlpha() / 255f) + ")";
    }

    static int getMinHeight(Element element) {
        Style style = element.getStyle();
        String savedHeight = style.getHeight();
        style.setHeight("");
        int minHeight = element.getOffsetHeight();
        style.setHeight(savedHeight);
        return minHeight;
    }

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
        if (control.getControlType() == Control.ControlType.COMBO) {
            log("#### Adding to combo: ", control, s, index);
        }

        Element element = (Element) control.peer;
        Element before = element.getChildren().get(index);
        Element newItem = createElement("option");
        newItem.setTextContent(s);
        element.insertBefore(newItem, before);
    }

    @Override
    public void addChild(Composite composite, Control control) {
        Element parentElement = (Element) composite.peer;
        Element childElement = (Element) control.peer;
        if (composite.getControlType() != Control.ControlType.TAB_FOLDER) {
            // log("addChild to: ", composite.peer, "; child: ", control);
            parentElement.appendChild(childElement);
        }
    }

    @Override
    public void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
        ((GwtTabFolder) tabFolder.peer).addTab(index, tabItem);
    }

    @Override
    public void addListener(final Control control, int eventType, Listener listener) {
        final Element element = (Element) control.peer;
        switch (eventType) {
            case SWT.Selection:
                switch (control.getControlType()) {
                    case BUTTON_PUSH:
                        element.addEventListener("click", new EventListener() {
                            @Override
                            public void onEvent(Event event) {
                                org.eclipse.swt.widgets.Event swtEvent = new org.eclipse.swt.widgets.Event();
                                swtEvent.widget = control;
                                swtEvent.display = GwtDisplay.this;
                                swtEvent.type = SWT.Selection;
                                control.notifyListeners(SWT.Selection, swtEvent);
                            }
                        });
                        break;
                    case BUTTON_CHECKBOX:
                    case BUTTON_RADIO:
                        Element input = element.getFirstElementChild();
                        input.addEventListener("change", new EventListener() {
                            @Override
                            public void onEvent(Event event) {
                                org.eclipse.swt.widgets.Event swtEvent = new org.eclipse.swt.widgets.Event();
                                swtEvent.widget = control;
                                swtEvent.display = GwtDisplay.this;
                                swtEvent.type = SWT.Selection;
                                control.notifyListeners(SWT.Selection, swtEvent);
                            }
                        });
                        break;
                    default:
                        log("FIXME GwtDisplay.addListener SWT.Selection", control.getControlType().name());
                }
            default:
                log("FIXME: GwtDisplay.addListener ", eventType);
        }

    }

    @Override
    public Point computeSize(Control control, int wHint, int hHint, boolean b) {
        Element element = ((Element) control.peer);
        Style style = element.getStyle();
        Point result;
        if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
            String savedWidth = style.getWidth();
            String savedHeight = style.getHeight();
            style.setWidth("");
            style.setHeight("");
            result = new Point(element.getOffsetWidth(), element.getOffsetHeight());
            style.setWidth(savedWidth);
            style.setHeight(savedHeight);
        } else if (wHint == SWT.DEFAULT) {
            String savedWidth = style.getWidth();
            style.setWidth("");
            result = new Point(element.getOffsetWidth(), hHint);
            style.setWidth(savedWidth);
        } else if (hHint == SWT.DEFAULT) {
            result = new Point(wHint, getMinHeight(element));
        } else {
            result = new Point(wHint, hHint);
        }
        return result;
    }

    private Color cssToColor(String css) {
        if (css == null) {
            return null;
        }
        css = css.trim();
        if (css.isEmpty()) {
            return null;
        }
        int start = css.indexOf('(');
        if (start != -1) {
            int end = css.indexOf(')');
            if (end != -1) {
                String[] parts = css.substring(start + 1, end).split(",");
                if (parts.length >= 3) {
                    int red = Integer.parseInt(parts[0].trim());
                    int green = Integer.parseInt(parts[1].trim());
                    int blue = Integer.parseInt(parts[2].trim());
                    if (parts.length >= 4) {
                        float alpha = Float.parseFloat(parts[3].trim());
                        return new Color(this, red, green, blue, Math.round(alpha * 255));
                    }
                    return new Color(this, red, green, blue);
                }
            }
        }
        log("FIXME: GwtDisplay.cssToColor can't convert " + css);
        return null;
    }


    private Element createElement(String name) {
        return Document.get().createElement(name);
    }
    private Element createControl(String name) {
        Element result = Document.get().createElement(name);
        if (!name.startsWith("jswt-")) {
            result.setClassName("jswt-control");
        }
        return result;
    }

    @Override
    public Object createControl(final Control control) {
        switch (control.getControlType()) {
            case TEXT:
                return createControl("input");
            case BUTTON_PUSH:
                return createControl("button");
//                        "mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--raised");
            case BUTTON_CHECKBOX:
            case BUTTON_RADIO: {
                Element result = createControl("label");
                Element input = createElement("input");
                Element span = createElement("span");
                result.appendChild(input);
                result.appendChild(span);
              //  String inputId = "i" + GwtDisplay.id++;
              //  result.setAttribute("for", inputId);
              // input.setAttribute("id", inputId);
                if ((control.style & SWT.RADIO) != 0) {
                    input.setAttribute("type", "radio");
                    input.setAttribute("name", "" + control.getParent().hashCode());
                   /* result.setAttribute("class", "mdl-radio mdl-js-radio mdl-js-ripple-effect");
                    input.setAttribute("class", "mdl-radio__button");
                    span.setAttribute("class", "mdl-radio__label"); */
                } else {
                    input.setAttribute("type", "checkbox");
/*                    result.setAttribute("class", "mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect");
                    input.setAttribute("class", "mdl-checkbox__input");
                    span.setAttribute("class", "mdl-checkbox__label"); */
                }
//                result.setAttribute("style", "block");
                //              Elements.upgradeElement(result);
                return result;
            }
            case COMBO:
                return createControl("select");
            case LIST: {
                Element result = createControl("select");
                result.setAttribute("size", "4");
                return result;
            }
            case SCALE:
            case SLIDER: {
                Element result = createControl("input");
                //    result.setAttribute("class", "mdl-slider mdl-js-slider");
                result.setAttribute("type", "range");
                return result;
            }
            case SHELL_ROOT: {
                Element shell = createElement("jswt-shell-root");
                shell.setAttribute("style", "display:block;width:100%;min-height:100vh;margin:auto;position:relative");
                //   Document.get().getBody().setAttribute("style", "min-height:100%");
                //   Document.get().getBody().getParentElement().setAttribute("style", "height:100%");
                Document.get().getBody().appendChild(shell);

                // FIXME
                Window.get().addEventListener("resize", new EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        // FIXME: Use cached sizes!
                        ((Shell) control).layout(true, true);
                    }
                });

                return shell;
            }
            case SHELL_DIALOG: {
                Element background = createElement("jswt-shell-dialog-background");
                background.setAttribute("style", "visibility:hidden;display:block;width:100%;min-height:100vh;margin:auto;position:absolute;left:0;top:0;background-color:rgba(0,0,0,0.3)");
                Element dialogShell = createControl("jswt-shell-dialog");
                dialogShell.setAttribute("style", "background-color: white;margin:auto");
                background.appendChild(dialogShell);
                //   Document.get().getBody().setAttribute("style", "min-height:100%");
                //   Document.get().getBody().getParentElement().setAttribute("style", "height:100%");
                //Document.get().getBody().appendChild(background);

                // FIXME: the dialog is inserted here because addChidl is not symmetrical: 
                // FIXME: addChild is not called for dialogs, but removeChild is called.

                ((Element) control.getParent().peer).appendChild(background);
                return dialogShell;

            }
            case LABEL:
                return createControl("jswt-label");
            case CANVAS:
                return createControl("canvas");
            case GROUP: {
                Element result = createControl("jswt-group");
                Element border = createElement("jswt-group-border");
                result.appendChild(border);

                int style = control.style;
                String className;
                if ((style & SWT.SHADOW_ETCHED_IN) != 0) {
                    className = "jswt-shadow-etched-in";
                } else if ((style & SWT.SHADOW_ETCHED_OUT) != 0) {
                    className = "jswt-shadow-etched-out";
                } else if ((style & SWT.SHADOW_IN) != 0) {
                    className = "jswt-shadow-in";
                } else if ((style & SWT.SHADOW_OUT) != 0) {
                    className = "jswt-shadow-out";
                } else if ((style & SWT.SHADOW_NONE) != 0) {
                    className = "jswt-shadow-none";
                } else {
                    className = null;
                }

                if ((style & SWT.BORDER) != 0) {
                    className = "jswt-border" + (className == null ? "" : (" " + className));
                }

                if (className != null) {
                    border.setAttribute("className", className);
                }

                Element title = createElement("jswt-group-label");
                title.getStyle().setDisplay("none");
                result.appendChild(title);
                return result;
            }
            case COMPOSITE:
                return createControl("jswt-composite");
            case PROGRESS_BAR:
                return createControl("progress");
            case TAB_FOLDER:
                return GwtTabFolder.create(((TabFolder) control));
            case SPINNER: {
                Element spinner = createControl("input");
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
    public void disposeRootShell(Shell shell) {
        throw new RuntimeException("FIXME: GwtDisplay.disposeRootShell");
    }

    @Override
    public boolean isEnabled(Control control) {
        return ((Element) control.peer).getDisabled();
    }

    @Override
    public Color getBackground(Control control) {
        return cssToColor(((Element) control.peer).getStyle().getBackgroundColor());
    }

    @Override
    public Rectangle getBounds(Control control) {
        Element element = ((Element) control.peer);
        return new Rectangle(
                element.getOffsetLeft(),
                element.getOffsetTop(),
                element.getOffsetWidth(),
                element.getOffsetHeight());
    }

    @Override
    public Color getForeground(Control control) {
        return cssToColor(((Element) control.peer).getStyle().getColor());
    }

    @Override
    public Rectangle getImageBounds(Object platformImage) {
        throw new RuntimeException("FIXME: GwtDisplay.getImageBounds");
    }

    @Override
    public Insets getInsets(Scrollable scrollable) {
        switch (scrollable.getControlType()) {
            case TAB_FOLDER:
                return ((GwtTabFolder) scrollable.peer).getInsets();
            case GROUP: {
                Insets result = new Insets();
                result.bottom = result.top = 16;
                result.left = result.right = 8;
                return result;
            }
            default:
                return new Insets();
        }
    }

    @Override
    public int getItemCount(Control control) {
        return ((Element) control.peer).getChildElementCount();
    }

    @Override
    public Monitor getMonitor(Control control) {
        log("FIXME: GwtDisplay.getMonitor");
        return new Monitor(new Rectangle(0, 0, 2000, 1000), new Rectangle(0, 0, 2000, 1000));
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
    //
        if (shell.getControlType() == Control.ControlType.SHELL_DIALOG) {
            Point dialogSize = shell.getSize();
            Point parentSize = shell.getParent().getSize();
            shell.setLocation((parentSize.x - dialogSize.x) / 2, (parentSize.y - dialogSize.y) / 3);
            ((Element) shell.peer).getParentElement().getStyle().setVisibility("visible");
        } else {
            shell.layout(true, true);   // FIXME
            shell.setVisible(true);
        }
    }

    @Override
    public void removeChild(Composite composite, Control control) {
        Element parentElement = (Element) composite.peer;
        Element childElement = (Element) control.peer;
        if (control.getControlType() == Control.ControlType.SHELL_DIALOG) {
            parentElement.removeChild(childElement.getParentElement());
        } else {
            parentElement.removeChild(childElement);
        }
    }

    @Override
    public void setBackground(Control control, Color color) {
        ((Element) control.peer).getStyle().setBackgroundColor(colorToCss(color));
    }

    @Override
    public void setBackgroundImage(Control control, Image image) {
        log("FIXME: GwtDisplay.setBackgroundImage");
    }

    @Override
    public void setBounds(Control control, int x, int y, int w, int h) {
        if (control.getControlType() == Control.ControlType.SHELL_ROOT) {
            return;
        }
        Element element = (Element) control.peer;
        Style style = element.getStyle();
        style.setLeft(x + "px");
        style.setTop(y + "px");
        style.setWidth(w + "px");
        style.setHeight(h + "px");
        if (element.getLocalName().equals("canvas")) {
            // FIXME: Invalidate instead
            element.setAttribute("width", String.valueOf(w));
            element.setAttribute("height", String.valueOf(h));
            ((Canvas) control).drawBackground(new GwtGC(element), 0, 0, w, h);
        }
    }

    @Override
    public void setEnabled(Control control, boolean b) {
        ((Element) control.peer).setDisabled(!b);
    }

    @Override
    public void setFocus(Control control) {
        log("FIXME: GwtDisplay.setFocus");
    }

    @Override
    public void setForeground(Control control, Color color) {
        ((Element) control.peer).getStyle().setColor(colorToCss(color));
    }

    @Override
    public void setText(Control control, String s) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case SHELL_ROOT:
                Document.get().setTitle(s);
                break;
            case GROUP: {
                Element title = element.getFirstElementChild().getNextElementSibling();
                title.setTextContent(s);
                title.setAttribute("style", s.isEmpty() ? "display:none" : "");
                break;
            }
            case COMBO:
                log("FIXME: GwtDisplay.setText for COMBO");
                break;
            case SHELL_DIALOG:
                log("FIXME: GwtDisplay.setText for SHELL_DIALOG");
                break;
            default: {
                if (element.getLocalName().equals("input")) {
                    element.setAttribute("value", s);
                } else if (element.getLocalName().equals("label")) {
                    element.getLastElementChild().setTextContent(s);
                } else {
                    element.setTextContent(s);
                }
            }
        }
    }

    @Override
    public void setVisible(Control control, boolean visible) {
        ((Element) control.peer).getStyle().setVisibility(visible ? "" : "hidden");
    }

    @Override
    public void setMeasuredSize(Control control, int i, int i1) {
    }

    @Override
    public void setRange(Control control, int minimum, int maximum) {
        Element element = (Element) control.peer;
        element.setMax(String.valueOf(maximum));
        element.setMin(String.valueOf(minimum));
    }

    @Override
    public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case SPINNER:
            case SCALE:
            case SLIDER:
                element.setStep(String.valueOf(increment));
                break;
            default:
                unsupported(control, "setSliderProperties");
        }
    }

    @Override
    public void setSelection(Control control, int selection) {
        Element element = ((Element) control.peer);
        switch (control.getControlType()) {
            case BUTTON_RADIO:
            case BUTTON_TOGGLE:
            case BUTTON_CHECKBOX:
                element.getFirstElementChild().setChecked(selection != 0);
                break;
            case LIST:
            case COMBO:
                element.setSelectedIndex(selection);
                break;
            case SLIDER:
            case SCALE:
                element.setValue(String.valueOf(selection));
                break;
            case BUTTON_PUSH:
            case BUTTON_ARROW:
                break;
            default:
                unsupported(control, "setSelection");
        }
    }

    @Override
    public void setIndexSelected(List control, int index, boolean selected) {
        ((Element) control.peer).getChildren().get(index).setSelected(selected);

    }

    @Override
    public void showPopupMenu(Menu menu) {
        throw new RuntimeException("FIXME: GwtDisplay.showPopupMenu");
    }

    void unsupported(Control control, String method) {
        log("GwtDisplay." + method + "() unsupported for type " + control.getControlType().name());
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
        return ((Element) list.peer).getChildren().get(i).getSelected();
    }

    @Override
    public void setFont(Control control, Font font) {
        log("FIXME: GwtDisplay.setFont");
    }

    @Override
    public void setItem(Control control, int index, String string) {
        Element element = (Element) control.peer;
        element.getChildren().get(index).setTextContent(string);
    }

    @Override
    public Font getFont(Control control) {
        log("FIXME: GwtDisplay.setFont");
        return null;
    }

    @Override
    public String getItem(Control control, int index) {
        Element element = (Element) control.peer;
        return element.getChildren().get(index).getTextContent();
    }

    @Override
    public int getScrollBarSize(ScrolledComposite scrolledComposite, int i) {
        log("FIXME: GwtDisplay.getScrollBarSize");
        return 0;
    }

    @Override
    public int getSelection(Control control) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case BUTTON_ARROW:
            case BUTTON_PUSH:
                return 0;
            case BUTTON_CHECKBOX:
            case BUTTON_RADIO:
            case BUTTON_TOGGLE:
                Element input = element.getFirstElementChild();
                return input != null && input.getChecked() ? 1 : 0;
            case LIST:
            case COMBO:
                return element.getSelectedIndex();
            case SPINNER:
            case SCALE:
            case SLIDER: {
                String value = element.getValue();
                if (value == null) {
                    return 0;
                }
                value = value.trim();
                if (value.isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(element.getValue());
            }
            default:
                unsupported(control, "getSelection");
                return -1;
        }
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