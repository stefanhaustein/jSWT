package org.eclipse.swt.widgets;

import org.kobjects.dom.Document;
import org.kobjects.dom.Element;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

class GwtTabFolder extends Element {

    private static int tabIdCounter = 0;

    protected GwtTabFolder() {

    }

    final Element getTabBar() {
        return getFirstElementChild();
    }


    public static GwtTabFolder create(TabFolder tabFolder, Document document) {
        tabFolder.setLayout(new GwtTabLayout());
        GwtTabFolder gwtTabFolder = (GwtTabFolder) Elements.createMdlElement(document, "div", "mdl-tabs mdl-js-tabs mdl-js-ripple-effect");
        Element tabBar = Elements.createMdlElement(document, "div", "mdl-tabs__tab-bar"); gwtTabFolder.appendChild(tabBar);

        return gwtTabFolder;
    }


    final void addTab(int index, TabItem tabItem) {
        String activeSuffix = getFirstElementChild() == getLastElementChild() ? " is-active" : "";

        Element tabBar = getTabBar();
        Element newTab = Elements.createMdlElement(getOwnerDocument(), "a", "mdl-tabs__tab" + activeSuffix);
        newTab.setTextContent("Tab " + index);
        String tabId = "tab-" + tabIdCounter++;
        newTab.setAttribute("href", "#" + tabId);
        tabBar.insertBefore(newTab, Elements.getChildElement(tabBar, index));

        Element newContent = Elements.createMdlElement(getOwnerDocument(), "div", "mdl-tabs__panel" + activeSuffix);
        newContent.setAttribute("id", tabId);
        insertBefore(newContent, Elements.getChildElement(this, index + 1));
    }


    final void updateTab(int index, TabItem tabItem) {
        if (tabItem.getText() != null && !tabItem.getText().isEmpty()){
            Element tabContainer = getTabBar();
            Element tab = Elements.getChildElement(tabContainer, index);
            tab.setTextContent(tabItem.getText());
        }
        if (tabItem.getControl() != null) {
            Element newContent = (Element) tabItem.getControl().peer;
            Element oldContent = Elements.getChildElement(this, index + 1);

            if (oldContent != newContent) {
                newContent.setAttribute("id", oldContent.getAttribute("id"));
                newContent.setClassName(oldContent.getClassName());
                replaceChild(newContent, oldContent);
            }
        }

        Elements.initTabs(this);
    }

    public final PlatformDisplay.Insets getInsets() {
        PlatformDisplay.Insets insets = new PlatformDisplay.Insets();
        insets.top = Elements.getMinHeight(getTabBar());
        return insets;
    }
}
