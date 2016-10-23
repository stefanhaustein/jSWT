package org.eclipse.swt.widgets;

import javax.print.Doc;
import org.eclipse.swt.custom.StackLayout;
import org.kobjects.dom.*;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import org.kobjects.dom.Event;

class GwtTabFolder extends Element {


    protected GwtTabFolder() {

    }

    final Element getTabBar() {
        return getFirstElementChild();
    }


    public static GwtTabFolder create(TabFolder tabFolder) {
        tabFolder.setLayout(new GwtTabLayout());
        GwtTabFolder gwtTabFolder = (GwtTabFolder) Document.get().createElement("jswt-tabfolder");
        Element tabBar = Document.get().createElement("jswt-tabbar");
        gwtTabFolder.appendChild(tabBar);
        return gwtTabFolder;
    }

    final void addTab(int index, final TabItem tabItem) {
        boolean isFirst = getFirstElementChild() == getLastElementChild();

        Element tabBar = getTabBar();
        final Element newTab = Document.get().createElement("jswt-tab");
        newTab.setTextContent("Tab " + index);
        newTab.addEventListener("click", new EventListener() {
            @Override
            public void onEvent(Event event) {
                setSelectionImpl(tabItem, newTab);
            }
        });
        tabBar.insertBefore(newTab, tabBar.getChildren().get(index));

        Element newContent = Document.get().createElement("div");
        insertBefore(newContent, this.getChildren().get(index + 1));

        if (isFirst) {
            newTab.setClassName("selected");
        } else {
            newContent.setAttribute("style", "visibility: hidden");
        }
    }

    final int getSelection() {
        int i = 0;
        Element tab = getTabBar().getFirstElementChild();
        while (tab != null) {
            if ("selected".equals(tab.getClassName())) {
                return i;
            }
            i++;
            tab = tab.getNextElementSibling();
        }
        return -1;
    }

    final void updateTab(int index, TabItem tabItem) {
        if (tabItem.getText() != null && !tabItem.getText().isEmpty()){
            Element tabContainer = getTabBar();
            Element tab = tabContainer.getChildren().get(index);
            tab.setTextContent(tabItem.getText());
        }
        if (tabItem.getControl() != null) {
            Element newContent = (Element) tabItem.getControl().peer;
            Element oldContent = this.getChildren().get(index + 1);

            if (oldContent != newContent) {
                newContent.getStyle().setVisibility(oldContent.getStyle().getVisibility());
                replaceChild(newContent, oldContent);
            }
        }
    }

    private final void setSelectionImpl(TabItem selectedTabItem, Element selectedTab) {
        Element tab = getTabBar().getFirstElementChild();
        while (tab != null) {
            tab.setClassName(tab == selectedTab ? "selected" : "");
            tab = tab.getNextElementSibling();
        }

        Element panel = getTabBar().getNextElementSibling();
        while (panel != null) {
            panel.getStyle().setVisibility(panel == selectedTabItem.control.peer ? "visible" : "hidden");
            panel = panel.getNextElementSibling();
        }
    }


    public final PlatformDisplay.Insets getInsets() {
        PlatformDisplay.Insets insets = new PlatformDisplay.Insets();
        insets.top = GwtDisplay.getMinHeight(getTabBar());
        return insets;
    }
}
