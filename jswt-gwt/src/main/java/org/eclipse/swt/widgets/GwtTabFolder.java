package org.eclipse.swt.widgets;

import org.kobjects.dom.Document;
import org.kobjects.dom.Element;

class GwtTabFolder extends Element {

    protected GwtTabFolder() {

    }

    final Element getTabContainer() {
        return getFirstElementChild();
    }

    final Element getContentContainer() {
        return getLastElementChild();
    }


    public static GwtTabFolder create(Document document) {
        GwtTabFolder tabFolder = (GwtTabFolder) document.createElement("tab-outer");
        tabFolder.appendChild(document.createElement("tab-head"));
        tabFolder.appendChild(document.createElement("tab-body"));
        return tabFolder;
    }


    final void addTab(int index, TabItem tabItem) {
        Element tabContainer = getTabContainer();
        Element newTab = getOwnerDocument().createElement("span");
        newTab.setTextContent("Tab " + index);
        tabContainer.insertBefore(newTab, Elements.getChildElement(tabContainer, index));

        Element contentContainer = getContentContainer();
        Element newContent = getOwnerDocument().createElement("tab-placeholder");
        contentContainer.insertBefore(newContent, Elements.getChildElement(contentContainer, index));

        if (contentContainer.getFirstElementChild() != contentContainer.getLastElementChild()) {
            Elements.setDisplay(newContent, "none");
        }
    }


    final void updateTab(int index, TabItem tabItem) {
        if (tabItem.getText() != null && !tabItem.getText().isEmpty()){
            Element tabContainer = getTabContainer();
            Element tab = Elements.getChildElement(tabContainer, index);
            tab.setTextContent(tabItem.getText());
        }
        if (tabItem.getControl() != null) {
            Element contentContainer = getContentContainer();
            Element newContent = (Element) tabItem.getControl().peer;
            Element oldContent = Elements.getChildElement(contentContainer, index);

            if (oldContent != newContent) {
                Elements.setDisplay(newContent, Elements.getDisplay(oldContent));
                contentContainer.replaceChild(newContent, oldContent);
            }
        }

    }

}
