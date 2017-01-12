package org.eclipse.swt.widgets;

import com.google.gwt.core.client.JsArrayNumber;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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

    static Element findList(Control control) {
        Element element = (Element) control.peer;
        if (element.getLocalName().equals("select")) {
            return element;
        }
        String id = element.getAttribute("list");
        log("id", id, element);
        element = Document.get().getElementById(id);
        return element;
    }

    static int getMinHeight(Element element) {
        Style style = element.getStyle();
        String savedHeight = style.getHeight();
        style.setHeight("");
        int minHeight = element.getOffsetHeight();
        style.setHeight(savedHeight);
        return minHeight;
    }

    static native void log(Object... args) /*-{
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

    ArrayList<Shell> rootShells = new ArrayList<Shell>();

    public GwtDisplay() {
        Window.get().addEventListener("resize", new EventListener() {
            @Override
            public void onEvent(Event event) {
                // FIXME: Top only, rest on demand?
                for (Shell shell: rootShells){
                    shell.layout(true, true);
                }
            }
        });

    }

    public void asyncExec(Runnable runnable) {
        runnable.run();                                   // FIXME
    }

    @Override
    void addItem(Control control, String s, int index) {
        Element element = findList(control);
        Element before = element.getChildren().get(index);
        Element newItem = createElement("option");
        newItem.setTextContent(s);
        element.insertBefore(newItem, before);
    }

    // FIXME: Remove addChild() from SWT, move the work to createPeer!
    @Override
    public void addChild(Composite parent, Control child) {
        Element parentElement = (Element) parent.peer;
        Element childElement = (Element) child.peer;
        switch (parent.getControlType()) {
            case TAB_FOLDER:
                // Do nothing
                break;
            case SHELL:
            case GROUP:
                parentElement.getFirstElementChild().appendChild(childElement);
                break;
            default:
                parentElement.appendChild(childElement);
        }
    }

    @Override
    void addTab(TabFolder tabFolder, int index, TabItem tabItem) {
        ((GwtTabFolder) tabFolder.peer).addTab(index, tabItem);
    }

    @Override
    void addListener(final Control control, int eventType, Listener listener) {
        final Element element = (Element) control.peer;
        switch (eventType) {
            case SWT.Selection:
                if (element.getLocalName().equals("button")) {
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
                } else {
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
                }
            default:
                log("FIXME: GwtDisplay.addListener ", eventType);
        }
    }

    @Override
    void copy(Control control) {
        unsupported(control, "cut");
    }

    @Override
    void cut(Control control) {
        Element element = (Element) control.peer;
        String oldText = getText(control);
        setText(control, oldText.substring(0, element.getSelectionStart()) + oldText.substring(element.getSelectionEnd()));
    }

    @Override
    Point computeSize(Control control, int wHint, int hHint, boolean b) {
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
    Object createControl(final Control control) {
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
            case BUTTON: {
                if ((control.style & (SWT.RADIO | SWT.CHECK | SWT.TOGGLE)) != 0) {
                    Element result = createControlElement(control, "label",
                            SWT.FLAT, "swt-flat",
                            SWT.WRAP, "swt-wrap",
                            SWT.BORDER, "swt-border");
                    Element input = createElement("input");
                    Element span = createElement("span");
                    result.appendChild(input);
                    result.appendChild(span);
                    if ((control.style & SWT.RADIO) != 0) {
                        input.setAttribute("type", "radio");
                        input.setAttribute("name", "" + control.getParent().hashCode());
                    } else   {
                        input.setAttribute("type", "checkbox");
                    }
                    return result;
                }
                Element element = createControlElement(control, "button",
                        SWT.FLAT, "swt-flat",
                        SWT.WRAP, "swt-wrap",
                        SWT.BORDER, "swt-border");
                Element span = createElement("span");
                element.appendChild(span);
                return element;
            }
            case COMBO: {
                if ((control.style & SWT.READ_ONLY) != 0) {
                    return createControlElement(control, "select");
                }
                String id = generateId();
                Element input = createControlElement(control, "input");
                input.setAttribute("list", id);

                Element datalist = createElement("datalist");
                datalist.setAttribute("id", id);
                Element container = Document.get().getBody();
                container.appendChild(datalist);

                return input;
            }
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

                if (Window.get().getNavigator().getUserAgent().indexOf("Mac OS X") != -1) {
                    outer.getClassList().add("mac-scrollbar");
                }

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
                } else {
                    control.style |= SWT.HORIZONTAL;
                }
                return result;
            }
            case SHELL: {
                Element element = createControlElement(control, "swt-shell");
                if (control.getParent() == null) {
                    element.getClassList().add("swt-root");
                    Document.get().getBody().appendChild(element);
                    rootShells.add((Shell) control);
                    control.style &= ~(SWT.BORDER | SWT.TITLE);

                } else {
                    element.getClassList().add("swt-child");
                    Element background = createElement("swt-shell-background");
                    background.getStyle().setVisibility("hidden");
                    Document.get().getBody().appendChild(background);
                    background.appendChild(element);
                }
                Element border = createControlElement(control, "swt-shell-decoration",
                        SWT.NO_TRIM, "swt-no-trim",
                        SWT.NO_MOVE, "swt-no-move",
                        SWT.CLOSE, "swt-close",
                        SWT.TITLE, "swt-title",
                        SWT.MIN, "swt-min",
                        SWT.MAX, "swt-max",
                        SWT.BORDER, "swt-border",
                        SWT.RESIZE, "swt-resize",
                        SWT.ON_TOP, "swt-on-top",
                        SWT.TOOL, "swt-tool",
                        SWT.SHEET, "swt-sheet");
                element.appendChild(border);

                if ((control.style & SWT.TITLE) != 0) {
                    Element label = createElement("swt-shell-text");
                    element.appendChild(label);
                }

                return element;
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
                Element border = createControlElement(control, "swt-group-decoration",
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
            case TABLE:
                return createControlElement(control, "table");
            case TOOLBAR:
                return createControlElement(control, "swt-toolbar");
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

    static int nextId = 0;

    private static String generateId() {
        return "swtid" + String.valueOf(nextId++);
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
    public GC createGCForPlatformImage(Object platformImage) {
        return new GwtGC((Element) platformImage);
    }

    @Override
    boolean isEnabled(Control control) {
        return ((Element) control.peer).getDisabled();
    }

    @Override
    Color getBackground(Control control) {
        return cssToColor(((Element) control.peer).getStyle().getBackgroundColor());
    }

    @Override
    void getLocation(Control control, Rectangle bounds, Point location) {
        Element element = ((Element) control.peer);
        int x = element.getOffsetLeft();
        int y = element.getOffsetTop();

        Composite parent = control.getParent();
        if (parent != null && (parent.getControlType() == Control.ControlType.GROUP
                || parent.getControlType() == Control.ControlType.SHELL)) {
            Element parentElement = element.getParentElement();
            Style parentStyle = Window.get().getComputedStyle(parentElement, null);
            x += Math.round(getPx(parentStyle, "marginLeft") + getPx(parentStyle, "borderLeftWidth"));
            y += Math.round(getPx(parentStyle, "marginTop") + getPx(parentStyle, "borderTopWidth"));
        }

        if (bounds != null) {
            bounds.x = x;
            bounds.y = y;
        }
        if (location != null) {
            location.x = x;
            location.y = y;
        }
    }

    @Override
    void getSize(Control control, Rectangle bounds, Point size) {
        Element element = ((Element) control.peer);
        if (bounds != null) {
            bounds.width = element.getOffsetWidth();
            bounds.height = element.getOffsetHeight();
        }
        if (size != null) {
            size.x = element.getOffsetWidth();
            size.y = element.getOffsetHeight();
        }
    }

    @Override
    Color getForeground(Control control) {
        return cssToColor(((Element) control.peer).getStyle().getColor());
    }

    @Override
    int getFocusIndex(List list) {
        return -1;
    }

    @Override
    boolean getGrayed(Button control) {
        return ((Element) control.peer).getIndeterminate();
    }

    @Override
    public Rectangle getImageBounds(Object platformImage) {
        Element canvas = (Element) platformImage;
        // TODO(haustein): Use properties instead (?!)
        return new Rectangle(0, 0,
                Integer.parseInt(canvas.getAttribute("width")),
                Integer.parseInt(canvas.getAttribute("height")));
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
    public Insets getInsets(Scrollable control) {
        Element element = ((Element) control.peer);
        switch (control.getControlType()) {
            case TAB_FOLDER:
                return ((GwtTabFolder) element).getInsets();
            case GROUP:
            case SHELL: {
                if (control.getParent() == null) {
                    return new Insets();
                }
                Insets result = computeInsets(element.getFirstElementChild());
                /*
                Element label = ((Element) control.peer).getFirstElementChild();
                if (!"none".equals(label.getStyle().getDisplay())) {
                    result.top += getMinHeight(label);
                }
                */
                return result;
            }
            default:
                return new Insets();
        }
    }

    @Override
    int getItemCount(Control control) {
        return findList(control).getChildElementCount();
    }

    @Override
    Monitor getMonitor(Control control) {
        log("FIXME: GwtDisplay.getMonitor");
        return new Monitor(new Rectangle(0, 0, 2000, 1000), new Rectangle(0, 0, 2000, 1000));
    }

    @Override
    String getText(Control control) {
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
    void openShell(Shell shell) {
        Element moveToTop = (Element) shell.peer;
        if (shell.getParent() == null) {
            shell.layout(true, true);   // FIXME
            rootShells.remove(shell);
            rootShells.add(shell);
            updateWindowTitle();
        } else {
            Point dialogSize = shell.getSize();
            Point parentSize = shell.getParent().getSize();
            shell.setLocation((parentSize.x - dialogSize.x) / 2, (parentSize.y - dialogSize.y) / 3);
            ((Element) shell.peer).getParentElement().getStyle().setVisibility("visible");
            moveToTop = moveToTop.getParentElement();
        }
        Document.get().getBody().appendChild(moveToTop);
        shell.setVisible(true);
    }

    @Override
    void disposePeer(Control control) {
        Element element = (Element) control.peer;
        if (control.getControlType() == Control.ControlType.SHELL && control.getParent() != null) {
            Element background = element.getParentElement();
            background.getParentElement().removeChild(background);
        } else {
            Element parentElement = element.getParentElement();
            if (parentElement != null) {
                parentElement.removeChild(element);
            }
        }

        if (control.getControlType() == Control.ControlType.SHELL && control.getParent() == null) {
            rootShells.remove((Shell) control);
            updateWindowTitle();
        }
    }

    @Override
    void setBackground(Control control, Color color) {
        Element element = (Element) control.peer;
        String cssColor = colorToCss(color);
        if (control.getControlType() == Control.ControlType.GROUP ||
                control.getControlType() == Control.ControlType.SHELL) {
            element.getFirstElementChild().getStyle().setBackgroundColor(cssColor);
            element.getLastElementChild().getStyle().setBackgroundColor(cssColor);
        } else {
            element.getStyle().setBackgroundColor(cssColor);
        }
    }

    @Override
    void setBackgroundImage(Control control, Image image) {
        Element element = (Element) control.peer;
        String src = image == null ? null : ((Element) image.peer).getAttribute("src");
        if (control.getControlType() == Control.ControlType.GROUP ||
            control.getControlType() == Control.ControlType.SHELL) {
                 element = element.getFirstElementChild();
        }
        element.getStyle().setBackgroundImage(src != null && !src.isEmpty() ? ("url(" + src + ")") : null);
    }

    @Override
    void setLocation(Control control, int x, int y) {
        Composite parent = control.getParent();
        if (parent == null) {
            return;
        }

        Element element = (Element) control.peer;
        if (parent.getControlType() == Control.ControlType.GROUP
                || parent.getControlType() == Control.ControlType.SHELL) {
            Element parentElement = element.getParentElement();
            Style parentStyle = Window.get().getComputedStyle(parentElement, null);
            x -= Math.round(getPx(parentStyle, "marginLeft") + getPx(parentStyle, "borderLeftWidth"));
            y -= Math.round(getPx(parentStyle, "marginTop") + getPx(parentStyle, "borderTopWidth"));
        }

        Style style = element.getStyle();
        style.setLeft(x + "px");
        style.setTop(y + "px");
    }

    @Override
    void setSize(Control control, int w, int h) {
        Composite parent = control.getParent();
        if (parent == null) {
            return;
        }

        int sliderPos = -1;
        if (control.getControlType() == Control.ControlType.SLIDER) {
            sliderPos = ((Slider) control).getSelection();
        }

        Element element = (Element) control.peer;

        Style style = element.getStyle();
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
    void setEnabled(Control control, boolean b) {
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
    void setFocus(Control control) {
        log("FIXME: GwtDisplay.setFocus");
    }

    @Override
    void setForeground(Control control, Color color) {
        ((Element) control.peer).getStyle().setColor(colorToCss(color));
    }

    @Override
    void setGrayed(Button button) {
        if ((button.style & SWT.CHECK) != 0) {
            ((Element) button.peer).setIndeterminate(true);
        }
    }

    @Override
    void setText(Control control, String text) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case BUTTON:
                if ((control.style & SWT.ARROW) == 0) {
                    element.getLastElementChild().setTextContent(removeAccelerators(text));
                }
                break;
            case GROUP: {
                Element decoration = element.getFirstElementChild();
                Element title = element.getLastElementChild();
                if (text == null) {
                    text = "";
                }
                title.setTextContent(text);
                if (text.isEmpty()) {
                    decoration.getClassList().remove("swt-title");
                    title.getStyle().setDisplay("none");
                } else {
                    decoration.getClassList().add("swt-title");
                    title.getStyle().setDisplay(null);
                }
                break;
            }
            case SHELL:
                if (control.getParent() == null) {
                    updateWindowTitle();
                } else if ((control.style & SWT.TITLE) != 0) {
                    Element title = element.getLastElementChild();
                    title.setTextContent(text);
                }
               break;
            case LABEL:
                if ((control.getStyle() & SWT.SEPARATOR) == 0) {
                    element.setTextContent(text);
                }
                break;
            case TEXT:
                if ((control.style & SWT.MULTI) != 0) {
                    element.setTextContent(text);
                } else {
                    element.setValue(text);
                }
            default:
                unsupported(control, "setText");
        }
    }

    @Override
    void setVisible(Control control, boolean visible) {
        Element element = (Element) control.peer;
        if (control.getControlType() == Control.ControlType.SHELL && control.getParent() != null) {
            element = element.getParentElement();
        }
        element.getStyle().setVisibility(visible ? null : "hidden");
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
    void setRange(Control control, int minimum, int maximum) {
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
    void setSliderProperties(Control control, int thumb, int increment, int pageIncrement) {
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
    void setSelection(Control control, int selection) {
        Element element = ((Element) control.peer);
        switch (control.getControlType()) {
            case BUTTON:
                if (element.getLocalName().equals("label")) {
                    element.getFirstElementChild().setChecked(selection != 0);
                }
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
            default:
                unsupported(control, "setSelection");
        }
    }

    @Override
    void setIndexSelected(Control control, int index, boolean selected) {
        switch (control.getControlType()) {
            case TEXT:
                ((Element) control.peer).getChildren().get(index).setSelected(selected);
                break;
            default:
                unsupported(control, "setSelectedIndex");
        }
    }

    @Override
    void showPopupMenu(Menu menu, int x, int y) {
        throw new RuntimeException("FIXME: GwtDisplay.showPopupMenu");
    }

    void unsupported(Control control, String method) {
        log("GwtDisplay." + method + "() unsupported for type " + control.getControlType().name());
    }

    @Override
    void updateMenuBar(Decorations decorations) {
        throw new RuntimeException("FIXME: GwtDisplay.updateMenuBar");
    }

    @Override
    void updateTab(TabFolder tabFolder, int index, TabItem tabItem) {
        ((GwtTabFolder) tabFolder.peer).updateTab(index, tabItem);
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
        unsupported(control, "getCaretLocation");
        return new Point(0, 0);
    }

    @Override
    int getCaretPosition(Control control) {
        return ((Element) control.peer).getSelectionStart();
    }

    @Override
    int getItemHeight(Control control) {
        unsupported(control, "getItemHeight");
        return 0;
    }

    @Override
    int getLineHeight(Control control) {
        return Math.round(getPx(Window.get().getComputedStyle((Element) control.peer), "lineHeight"));
    }

    @Override
    Point getSelectedRange(Control control) {
        Element element = (Element) control.peer;
        return new Point(element.getSelectionStart(), element.getSelectionEnd());
    }

    @Override
    int getTopPixel(Text control) {
        unsupported(control, "getTopPixel");
        return 0;
    }

    @Override
    void paste(Control control) {
        unsupported(control, "paste");
    }

    @Override
    boolean setDoubleClickEnabled(Text text, boolean doubleClick) {
        return false;
    }

    @Override
    char setEchoChar(Text text, char echo) {
        ((Element) text.peer).setType(echo == 0 ? "text" : "password");
        return echo;
    }

    @Override
    boolean setEditable(Text text, boolean editable) {
        ((Element) text.peer).setReadOnly(!editable);
        return editable;
    }

    @Override
    String setMessage(Text text, String message) {
        return "";
    }

    @Override
    void setOrientation(Control control, int orientation) {
        ((Element) control.peer).setDir(orientation == SWT.RIGHT_TO_LEFT ? "rtl" : "ltr");
    }

    @Override
    boolean setRedraw(Text text, boolean redraw) {
        return true;
    }

    @Override
    int setTextLimit(Control control, int limit) {
        ((Element) control.peer).setMaxlength(limit);
        return limit;
    }

    @Override
    int setTabs(Text text, int tabs) {
        return 0;
    }

    @Override
    void setSelectionRange(Control control, int start, int end) {
        ((Element) control.peer).setSelectionRange(start, end);
    }

    @Override
    int getOrientation(Control control) {
        return ((Element) control.peer).getDir().equals("rtl") ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
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
    void updateMenuItem(MenuItem item) {

    }

    @Override
    void addTableColumn(Table table, TableColumn column) {

    }

    @Override
    void addTableItem(Table table, TableItem item) {

    }

    @Override
    void updateTableColumn(Table table, TableColumn item) {

    }

    @Override
    void updateTableItem(Table table, TableItem item) {

    }

    @Override
    void removeTableColumn(Table table, TableColumn column) {

    }

    @Override
    void removeTableItem(Table table, TableItem item) {

    }

    @Override
    void moveAbove(Control control, Control other) {

    }

    @Override
    void updateTable(Table table) {

    }

    @Override
    ToolBar getToolBar(Shell shell) {
        return null;
    }

    void updateWindowTitle() {
        if (rootShells.size() > 0) {
            Document.get().setTitle(rootShells.get(rootShells.size() - 1).text);
        }
    }

    @Override
    void setImage(Control control, Image image) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case BUTTON:
                if ((control.style & SWT.ARROW) == 0) {
                    Element imgElement = element.querySelector("img");
                    if (imgElement != null) {
                        imgElement.getParentElement().removeChild(imgElement);
                    }
                    if (image != null && (control.style & SWT.ARROW) == 0) {
                        Element img = (Element) image.peer;
                        element.insertBefore(img.cloneNode(false), element.getLastElementChild());
                    }
                }
                break;
            case LABEL:
                if ((control.style & SWT.SEPARATOR) == 0) {
                    if (image == null) {
                        element.setTextContent(((Label) control).getText());
                    } else {
                        element.setTextContent("");
                        Element img = (Element) image.peer;
                        element.appendChild(img.cloneNode(false));
                    }
                }
                break;

            default:
                unsupported(control, "setImage");
        }
    }

    @Override
    void setAlignment(Control button, int alignment) {
        Element element = (Element) button.peer;
        String value = "";
        if ((button.style & SWT.ARROW) != 0) {
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
    boolean isSelected(List list, int i) {
        return ((Element) list.peer).getChildren().get(i).getSelected();
    }

    @Override
    boolean isVisible(Control control) {
        return !"hidden".equals(((Element) control.peer).getStyle().getVisibility());
    }

    @Override
    void setFont(Control control, Font font) {
        log("FIXME: GwtDisplay.setFont");
    }

    @Override
    void setItem(Control control, int index, String string) {
        Element element = findList(control);
        element.getChildren().get(index).setTextContent(string);
    }

    @Override
    Font getFont(Control control) {
        log("FIXME: GwtDisplay.getFont");
        return new Font(this, "Dummy", 12, 0);
    }

    @Override
    int getTopIndex(Control control) {
        return 0;
    }

    @Override
    String getItem(Control control, int index) {
        Element element = findList(control);
        return element.getChildren().get(index).getTextContent();
    }

    @Override
    public int getScrollBarSize(ScrolledComposite scrolledComposite, int i) {
        log("FIXME: GwtDisplay.getScrollBarSize");
        return 0;
    }

    @Override
    int getSelection(Control control) {
        Element element = (Element) control.peer;
        switch (control.getControlType()) {
            case BUTTON:
                Element input = element.querySelector("input");
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
    void redraw(Control control, int i, int i1, int i2, int i3, boolean b) {
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
    void removeItems(Control control, int start, int end) {
        log("FIXME: GwtDisplay.removeItems");
    }

}