package org.eclipse.swt.widgets;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.ArrayList;

class AndroidTabFolder extends LinearLayout {
    TabLayout tabLayout;
    FrameLayout tabContent;

    AndroidTabFolder(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        tabLayout = new TabLayout(context);
        addView(tabLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Create the FrameLayout (the content area)
        tabContent = new FrameLayout(context);
        addView(tabContent, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

//        int px = px(context, 4);
//        frameLayoutParams.setMargins(px, 4 * px, px, 2 * px);

        // Make sure the total size accounts for the largest tab.
        tabContent.setMeasureAllChildren(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabContent.getChildAt(tab.getPosition()).setVisibility(VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab != null) {
                    tabContent.getChildAt(tab.getPosition()).setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabContent.getChildAt(tab.getPosition()).setVisibility(VISIBLE);
            }
        });

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    void updateTab(int index, TabItem tabItem) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        if (tabItem.getText() != null && !tabItem.getText().isEmpty()) {
            String title = tabItem.getText();
            tab.setText(title);
            // tabLayout.invalidate();
        }
        if (tabItem.getControl() != null) {
            tabContent.removeView(tabContent.getChildAt(index));
            View view = (View) tabItem.getControl().peer;
            tabContent.addView(view);
            view.setVisibility(index == tabLayout.getSelectedTabPosition() ? VISIBLE : GONE);
        }
    }

    void addTab(int index, TabItem tabItem) {
        tabContent.addView(new TextView(getContext()), index);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("Tab " + String.valueOf(index));
        tabLayout.addTab(tab, index);
    }


}

