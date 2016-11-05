package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
import java.util.HashMap;
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

    static HashMap<Control.ControlType,HashMap<Integer,String>> styleMap = new HashMap<>();


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
        switch (composite.getControlType()) {
            case TAB_FOLDER:
                // Do nothing
                break;
            case GROUP:  // dialogs, too?
                parentElement.getFirstElementChild().appendChild(childElement);
                break;
            default:
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
                    case BUTTON:
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
                    case BUTTON_CHECK:
                    case BUTTON_RADIO:
                    case SCALE:
                    case SPINNER:
                        Element input = element.getLocalName().equals("label") ? element.getFirstElementChild() : element;
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
        if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
            Element element = ((Element) control.peer);
            Style style = element.getStyle();
            String savedWidth = style.getWidth();
            String savedHeight = style.getHeight();
            String savedWhiteSpace = style.getWhiteSpace();
            if (wHint == SWT.DEFAULT) {
                style.setWhiteSpace("nowrap");
                style.setWidth(null);
            } else {
                style.setWidth((String.valueOf(wHint) + "px"));
            }
            style.setHeight(hHint == SWT.DEFAULT ? "" : (String.valueOf(hHint) + "px"));
            // In the case of wrap, we add one pixel to account for rounding errors to avoid unintended wrapping.

            if (wHint == SWT.DEFAULT) {
                if (control.getControlType() == Control.ControlType.SLIDER && (control.style & SWT.VERTICAL) == 0) {
                    wHint = 128;
                } else {
                    wHint = element.getOffsetWidth();
                    if ((control.getStyle() & SWT.WRAP) != 0) {
                        wHint++;
                    }
                }
            }
            if (hHint == SWT.DEFAULT) {
                if (control.getControlType() == Control.ControlType.SLIDER && (control.style & SWT.VERTICAL) != 0) {
                    hHint = 128;
                } else {
                    hHint = element.getOffsetHeight();
                }
            }
            style.setWidth(savedWidth);
            style.setHeight(savedHeight);
            style.setWhiteSpace(savedWhiteSpace);
        }
        return new Point(wHint, hHint);
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

    /**
     * Creates an element with the given name and sets classes for the style flags.
     */
    private Element createControlElement(Control control, String name, Object... styleMap) {
        Element result = Document.get().createElement(name);
        StringBuilder sb = new StringBuilder();
        if (!name.startsWith("swt-")) {
            sb.append("swt-control ");
        }

        if (styleMap.length > 0) {
            int style = control.style;
            for (int i = 0; i < styleMap.length; i += 2) {
                if ((style & ((Integer) styleMap[i]).intValue()) != 0) {
                    sb.append((String) styleMap[i + 1]).append(' ');
                }
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
            result.setClassName(sb.toString());
        }
        return result;
    }

    @Override
    public Object createControl(final Control control) {
        switch (control.getControlType()) {
            case TEXT: {
                Element result;
                if ((control.style & SWT.MULTI) != 0) {
                    result = createControlElement(control, "textarea");
                    result.setAttribute("rows", "8");
                    result.getStyle().set("resize", "none");
                    /*
                    result.setAttribute("cols", "20");
                    if ((control.style & SWT.WRAP) != 0) {
                        result.setAttribute("wrap", "soft");
                    }*/
                } else {
                    result = createControlElement(control, "input");
                }
                if ((control.style & SWT.READ_ONLY) != 0) {
                    result.setReadOnly(true);
                }
                return result;
            }
            case BUTTON_ARROW:
            case BUTTON: {
                control.style |= SWT.PUSH;
                Element element = createControlElement(control, "button",
                        SWT.FLAT, "swt-flat",
                        SWT.WRAP, "swt-wrap",
                        SWT.BORDER, "swt-border");
                Element span = createElement("span");
                element.appendChild(span);
                return element;
            }
            case BUTTON_TOGGLE:
            case BUTTON_CHECK:
            case BUTTON_RADIO: {
                Element result = createControlElement(control, "label",
                        SWT.FLAT, "swt-flat",
                        SWT.WRAP, "swt-wrap",
                        SWT.BORDER, "swt-border");
                Element input = createElement("input");
                Element span = createElement("span");
                result.appendChild(input);
                result.appendChild(span);
                if (control.getControlType() == Control.ControlType.BUTTON_RADIO) {
                    input.setAttribute("type", "radio");
                    input.setAttribute("name", "" + control.getParent().hashCode());
                } else {
                    input.setAttribute("type", "checkbox");
                }
                return result;
            }
            case COMBO:
                return createControlElement(control, "select");
            case LIST: {
                Element result = createControlElement(control, "select");
                result.setAttribute("size", "8");
                if ((control.style & SWT.MULTI) != 0) {
                    result.setAttribute("multiple", "multiple");
                }
                return result;
            }
            case SLIDER: {
                Element result = createControlElement(control, "swt-slider");
                Element outer = createElement("swt-slider-outer");
                result.appendChild(outer);
                Element inner = createElement("swt-slider-inner");
                outer.appendChild(inner);
                Style style = outer.getStyle();
                if ((control.style & SWT.VERTICAL) != 0) {
                    style.setHeight("100%");
                    style.set("overflowX", "hidden");
                    style.set("overflowY", "scroll");
                } else {
                    control.style |= SWT.HORIZONTAL;
                    style.set("overflowX", "scroll");
                    style.set("overflowY", "hidden");
                }
                return result;
            }
            case SCALE: {
                Element result = createControlElement(control, "input");
                //    result.setAttribute("class", "mdl-slider mdl-js-slider");
                result.setAttribute("type", "range");
                if ((control.style & SWT.VERTICAL) != 0) {
                    result.setAttribute("orient", "vertical");
                    result.getStyle().set("WebkitAppearance", "slider-vertical");
                }
                return result;
            }
            case SHELL_ROOT: {
                Element shell = createElement("swt-shell-root");
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
            case SHELL: {
                Element background = createElement("swt-shell-background");
                background.setAttribute("style", "visibility:hidden;display:block;width:100%;min-height:100vh;margin:auto;position:absolute;left:0;top:0;background-color:rgba(0,0,0,0.3)");
                Element dialogShell = createControlElement(control, "swt-shell");
                dialogShell.setAttribute("style", "background-color: white;margin:auto");
                background.appendChild(dialogShell);
                //   Document.get().getBody().setAttribute("style", "min-height:100%");
                //   Document.get().getBody().getParentElement().setAttribute("style", "height:100%");
                //Document.get().getBody().appendChild(background);

                // FIXME: the dialog is inserted here because addChidl is not symmetrical: 
                // FIXME: addChild is not called for dialogs, but removeChild is called.

                ((Element) control.getParent().peer).appendChild(background);

                Element label = createElement("swt-shell-text");
                label.getStyle().setDisplay("none");
                dialogShell.appendChild(label);
                return dialogShell;

            }
            case LABEL: {
                Element result = createControlElement(control, "swt-label",
                        SWT.BORDER, "swt-border",
                        SWT.WRAP, "swt-wrap");
                if ((control.getStyle() & SWT.SEPARATOR) != 0) {
                    result.appendChild(createControlElement(control, "swt-label-separator",
                            SWT.BORDER, "swt-border",
                            SWT.HORIZONTAL, "swt-horizontal",
                            SWT.VERTICAL, "swt-vertical",
                            SWT.SHADOW_IN, "swt-shadow-in",
                            SWT.SHADOW_OUT, "swt-shadow-out",
                            SWT.SHADOW_NONE, "swt-shadow-none"));
                }
                return result;
            }
            case CANVAS:
                return createControlElement(control, "canvas");
            case GROUP: {
                Element result = createControlElement(control, "swt-group");
                // Not really a control, but simplifies styling...
                Element border = createControlElement(control, "swt-group-border",
                        SWT.BORDER, "swt-border",
                        SWT.SHADOW_ETCHED_IN, "swt-shadow-etched-in",
                        SWT.SHADOW_ETCHED_OUT, "swt-shadow-etched-out",
                        SWT.SHADOW_IN, "swt-shadow-in",
                        SWT.SHADOW_OUT, "swt-shadow-out",
                        SWT.SHADOW_NONE, "swt-shadow-none");
                result.appendChild(border);

                Element title = createElement("swt-group-text");
                title.getStyle().setDisplay("none");
                result.appendChild(title);
                return result;
            }
            case COMPOSITE:
                return createControlElement(control, "swt-composite");
            case PROGRESS_BAR:
                return createControlElement(control, "progress");
            case TAB_FOLDER:
                return GwtTabFolder.create(((TabFolder) control));
            case SPINNER: {
                Element spinner = createControlElement(control, "input");
                spinner.setAttribute("type", "number");
                return spinner;
            }
            default:
                throw new RuntimeException("FIXME: GwtDisplay.createControlElement type " + control.getControlType());
        }
    }

    @Override
    public Object createImage(int width, int height) {
        Element canvas = Document.get().createElement("canvas");
        canvas.setAttribute("width", String.valueOf(width));
        canvas.setAttribute("height", String.valueOf(height));
        return canvas;
    }

    public Image createImage(Element img) {
        return new Image(this, img);
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
        int x = element.getOffsetLeft();
        int y = element.getOffsetTop();

        if (control.getParent() != null && control.getParent().getControlType() == Control.ControlType.GROUP) {
            Element parentElement = element.getParentElement();
            Style parentStyle = Window.get().getComputedStyle(parentElement, null);
            x += Math.round(getPx(parentStyle, "marginLeft") + getPx(parentStyle, "borderLeftWidth"));
            y += Math.round(getPx(parentStyle, "marginTop") + getPx(parentStyle, "borderTopWidth"));
        }

        return new Rectangle(x, y, element.getOffsetWidth(), element.getOffsetHeight());
    }

    @Override
    public Color getForeground(Control control) {
        return cssToColor(((Element) control.peer).getStyle().getColor());
    }

    @Override
    public Rectangle getImageBounds(Object platformImage) {
        throw new RuntimeException("FIXME: GwtDisplay.getImageBounds");
    }

    private static float getPx(Style style, String propertyName) {
        String value = style.get(propertyName);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        if (value.endsWith("px")) {
            return Float.parseFloat(value.substring(0, value.length() - 2));
        }
        log("Can't parse style property value '" + value + "' for " + propertyName);
        return 0;
    }

    private static Insets computeInsets(Element element) {
        Insets insets = new Insets();
        Style style = Window.get().getComputedStyle(element, null);
        insets.top = Math.round(getPx(style, "marginTop") + getPx(style, "borderTopWidth") + getPx(style, "paddingTop"));
        insets.right = Math.round(getPx(style, "marginRight") + getPx(style, "borderRightWidth") + getPx(style, "paddingRight"));
        insets.bottom = Math.round(getPx(style, "marginBottom") + getPx(style, "borderBottomWidth") + getPx(style, "paddingBottom"));
        insets.left = Math.round(getPx(style, "marginLeft") + getPx(style, "borderLeftWidth") + getPx(style, "paddingLeft"));
        return insets;
    }


    @Override
    public Insets getInsets(Scrollable scrollable) {
        Element element = ((Element) scrollable.peer);
        switch (scrollable.getControlType()) {
            case TAB_FOLDER:
                return ((GwtTabFolder) element).getInsets();
            case GROUP:
                return computeInsets(element.getFirstElementChild());

            case SHELL: {
                Insets result = computeInsets(element);
/*                Insets result = new Insets();
                result.left = result.right = result.bottom = result.top = 12; */
                Element label = ((Element) scrollable.peer).getFirstElementChild();
                if (!"none".equals(label.getStyle().getDisplay())) {
                    result.top += getMinHeight(label);
                }
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
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case TEXT:
                if ((control.style & SWT.MULTI) != 0) {
                    return element.getTextContent();
                }
                return element.getValue();
            default:
                unsupported(control, "getText");
                return element.getTextContent();
        }
    }

    @Override
    public Object loadImage(InputStream stream) throws IOException {
        throw new RuntimeException("Synchronous image loading not supported in GWT.");
    }

    @Override
    public void openShell(Shell shell) {
        //Element.getBody().appendChild((Element) shell.peer);
    //
        if (shell.getControlType() == Control.ControlType.SHELL) {
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
        if (control.getControlType() == Control.ControlType.SHELL) {
            parentElement.removeChild(childElement.getParentElement());
        } else if (composite.getControlType() == Control.ControlType.GROUP) {
            parentElement.getFirstElementChild().removeChild(childElement);
        } else {
            parentElement.removeChild(childElement);
        }
    }

    @Override
    public void setBackground(Control control, Color color) {
        Element element = (Element) control.peer;
        String cssColor = colorToCss(color);
        if (control.getControlType() == Control.ControlType.GROUP) {
            element.getFirstElementChild().getStyle().setBackgroundColor(cssColor);
            element.getLastElementChild().getStyle().setBackgroundColor(cssColor);
        } else {
            element.getStyle().setBackgroundColor(cssColor);
        }
    }

    @Override
    public void setBackgroundImage(Control control, Image image) {
        Element element = (Element) control.peer;
        String src = image == null ? null : ((Element) image.peer).getAttribute("src");
        if (control.getControlType() == Control.ControlType.GROUP ||
            control.getControlType() == Control.ControlType.SHELL) {
                 element = element.getFirstElementChild();
        }
        element.getStyle().setBackgroundImage(src != null && !src.isEmpty() ? ("url(" + src + ")") : null);
    }

    @Override
    public void setBounds(Control control, int x, int y, int w, int h) {
        if (control.getControlType() == Control.ControlType.SHELL_ROOT) {
            return;
        }

        int sliderPos = -1;
        if (control.getControlType() == Control.ControlType.SLIDER) {
            sliderPos = ((Slider) control).getSelection();
        }

        Element element = (Element) control.peer;
        if (control.getParent().getControlType() == Control.ControlType.GROUP) {
            Element parentElement = element.getParentElement();
            Style parentStyle = Window.get().getComputedStyle(parentElement, null);
            x -= Math.round(getPx(parentStyle, "marginLeft") + getPx(parentStyle, "borderLeftWidth"));
            y -= Math.round(getPx(parentStyle, "marginTop") + getPx(parentStyle, "borderTopWidth"));
        }

        Style style = element.getStyle();
        style.setLeft(x + "px");
        style.setTop(y + "px");
        style.setWidth(w + "px");
        style.setHeight(h + "px");
        if (element.getLocalName().equals("canvas")) {
            element.setAttribute("width", String.valueOf(w));
            element.setAttribute("height", String.valueOf(h));
            redrawCanvas((Canvas) control, 0, 0, w, h);
        }

        if (sliderPos != -1) {
            Slider slider = (Slider) control;
            updateSlider(slider);
            slider.setSelection(sliderPos);
        }
    }

    @Override
    public void setEnabled(Control control, boolean b) {
        Element element = (Element) control.peer;
        if (element.getLocalName().equals("label")) {
            if (b) {
                element.getClassList().remove("swt-disabled");
            } else {
                element.getClassList().add("swt-disabled");
            }
            element = element.getFirstElementChild();
        }
        element.setDisabled(!b);
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
            case SHELL: {
                Element title = element.getFirstElementChild();
                title.setTextContent(s);
                title.setAttribute("style", s.isEmpty() ? "display:none" : "");
                break;
            }
            case GROUP: {
                Element title = element.getFirstElementChild().getNextElementSibling();
                title.setTextContent(s);
                title.setAttribute("style", s.isEmpty() ? "display:none" : "");
                break;
            }
            case BUTTON:
            case BUTTON_CHECK:
            case BUTTON_RADIO:
            case BUTTON_TOGGLE:
                element.getLastElementChild().setTextContent(s);
                break;
            case LABEL:
                if ((control.getStyle() & SWT.SEPARATOR) == 0) {
                    element.setTextContent(s);
                }
                break;
            case BUTTON_ARROW:
                break;
            case TEXT:
                if ((control.style & SWT.MULTI) != 0) {
                    element.setTextContent(s);
                } else {
                    element.setValue(s);
                }
            default:
                unsupported(control, "setText");
        }
    }

    @Override
    public void setVisible(Control control, boolean visible) {
        ((Element) control.peer).getStyle().setVisibility(visible ? "" : "hidden");
    }

    void updateSlider(Slider slider) {
        Element element = (Element) slider.peer;
        Element outer = element.getFirstElementChild();
        Element inner = outer.getFirstElementChild();

        float range = slider.getMaximum() - slider.getMinimum();
        float thumb = slider.getThumb();
        float percent = range / thumb * 100;
        if ((slider.style & SWT.VERTICAL) == 0) {
            inner.getStyle().setWidth(String.valueOf(percent) + "%");
        } else {
            inner.getStyle().setHeight(String.valueOf(percent) + "%");
        }
    }

    @Override
    public void setRange(Control control, int minimum, int maximum) {
        Element element = (Element) control.peer;
        if (control.getControlType() == Control.ControlType.PROGRESS_BAR) {
            element.setMax(String.valueOf(maximum - minimum));
        } else if (control.getControlType() == Control.ControlType.SLIDER) {
            updateSlider((Slider) control);
        } else {
            element.setMax(String.valueOf(maximum));
            element.setMin(String.valueOf(minimum));
        }
    }

    @Override
    public void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case SPINNER:
            case SCALE:
                element.setStep(String.valueOf(increment));
                break;
            case SLIDER:
                updateSlider((Slider) control);
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
            case BUTTON_CHECK:
                element.getFirstElementChild().setChecked(selection != 0);
                break;
            case LIST:
            case COMBO:
                element.setSelectedIndex(selection);
                break;
            case SLIDER: {
                Slider slider = (Slider) control;
                Element outer = element.getFirstElementChild();

                int logicalRange = slider.getMaximum() - slider.getMinimum();
                selection -= slider.getMinimum();

                if ((slider.style & SWT.VERTICAL) != 0) {
                    float physicalRange = outer.getScrollHeight();
                    int scrollPos = Math.round(selection * physicalRange / logicalRange);
                    outer.setScrollTop(Math.round(scrollPos));
                } else {
                    float physicalRange = outer.getScrollWidth();
                    int scrollPos = Math.round(selection * physicalRange / logicalRange);
                    outer.setScrollLeft(Math.round(scrollPos));
                }
                break;
            }
            case SCALE:
            case SPINNER:
                element.setValue(String.valueOf(selection));
                break;
            case PROGRESS_BAR:
                if ((control.style & SWT.INDETERMINATE) == 0) {
                    element.setValue(String.valueOf(selection - ((ProgressBar) control).getMinimum()));
                }
                break;
            case BUTTON:
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
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case BUTTON_CHECK:
            case BUTTON_RADIO:
            case BUTTON_TOGGLE:
                if(!element.getFirstElementChild().getNextElementSibling().getLocalName().equals("span")) {
                    element.removeChild(element.getFirstElementChild().getNextElementSibling());
                }
                if (image != null) {
                    Element img = (Element) image.peer;
                    element.insertBefore(img.cloneNode(false), element.getLastElementChild());
                }
                break;
            case LABEL:
                if ((control.getStyle() & SWT.SEPARATOR) == 0) {
                    if (image == null) {
                        element.setTextContent(((Label) control).getText());
                    } else {
                        element.setTextContent("");
                        Element img = (Element) image.peer;
                        element.appendChild(img.cloneNode(false));
                    }
                }
                break;

            case BUTTON:
                if(element.getFirstElementChild().getLocalName().equals("img")) {
                    element.removeChild(element.getFirstElementChild());
                }
                if (image != null) {
                    Element img = (Element) image.peer;
                    element.insertBefore(img.cloneNode(false), element.getLastElementChild());
                }
                break;
            default:
                unsupported(control, "setImage");
        }
    }

    @Override
    public void setAlignment(Control button, int alignment) {
        Element element = (Element) button.peer;
        String value = "";
        if (button.getControlType() == Control.ControlType.BUTTON_ARROW) {
            switch (alignment) {
                case SWT.LEFT:
                    value = "\u25c0";
                    break;
                case SWT.UP:
                    value = "\u25b2";
                    break;
                case SWT.DOWN:
                    value = "\u25bc";
                    break;
                default:
                    value = "\u25b6";
                    break;
            }
            element.setTextContent(value);
        } else {
            switch (alignment) {
                case SWT.LEFT:
                    value = "left";
                    break;
                case SWT.RIGHT:
                    value = "right";
                    break;
                case SWT.CENTER:
                    value = "center";
                    break;
            }
            element.getStyle().setTextAlign(value);
        }
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
            case BUTTON:
                return 0;
            case BUTTON_CHECK:
            case BUTTON_RADIO:
            case BUTTON_TOGGLE:
                Element input = element.getFirstElementChild();
                return input != null && input.getChecked() ? 1 : 0;
            case LIST:
            case COMBO:
                return element.getSelectedIndex();
            case SLIDER:
                Slider slider = (Slider) control;
                Element outer = element.getFirstElementChild();

                int logicalRange = slider.getMaximum() - slider.getMinimum();
                int scrollPos;
                float physicalRange;
                if ((slider.style & SWT.VERTICAL) != 0) {
                    physicalRange = outer.getScrollHeight();
                    scrollPos = outer.getScrollTop();
                } else {
                    physicalRange = outer.getScrollWidth();
                    scrollPos = outer.getScrollLeft();
                }
                return Math.round(scrollPos * logicalRange / physicalRange + slider.getMinimum());
            case SPINNER:
            case SCALE:{
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

    private void redrawCanvas(Canvas canvas, int x, int y, int w, int h) {
        Element element = (Element) canvas.peer;
        GwtGC gc = new GwtGC(element);
        canvas.drawBackground(new GwtGC(element), 0, 0, w, h);
        org.eclipse.swt.widgets.Event event = new org.eclipse.swt.widgets.Event();
        event.gc = gc;
        event.setBounds(new Rectangle(x, y, w, h)); canvas.notifyListeners(SWT.Paint, event);
    }

    @Override
    public void removeItems(Control control, int start, int end) {
        log("FIXME: GwtDisplay.removeItems");
    }

}