package org.eclipse.swt.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class AndroidTableView extends RecyclerView {
    Table table;
    int columnOffset = 1;
    int selectedIndex = -1;
    ItemDecoration itemDecoration;

    public AndroidTableView(Context context, Table table) {
        super(context);
        this.table = table;
        setAdapter(new TableItemAdapter());
        setLayoutManager(new LinearLayoutManager(context));
        columnOffset = (table.style & (SWT.SINGLE | SWT.MULTI | SWT.CHECK)) != 0 ? 1 : 0;
        updateStyle();
    }

    void updateStyle() {
        if (table.getLinesVisible()) {
            if (itemDecoration == null) {
                itemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
                addItemDecoration(itemDecoration);
            }
        } else {
            if (itemDecoration != null) {
                removeItemDecoration(itemDecoration);
                itemDecoration = null;
            }
        }
    }

    void select(int index) {
        if (index != selectedIndex) {
            int oldSelectedIndex = selectedIndex;
            selectedIndex = index;
            getAdapter().notifyItemChanged(oldSelectedIndex);
            getAdapter().notifyItemChanged(selectedIndex);
        }
    }


    class TableItemAdapter extends RecyclerView.Adapter<TableViewHolder> {

        @Override
        public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TableViewHolder(new LinearLayout(getContext()));
        }

        @Override
        public void onBindViewHolder(TableViewHolder holder, int position) {
            holder.index = position;
            int columnCount = Math.max(table.getColumnCount(), 1) + columnOffset;
            while (holder.linearLayout.getChildCount() > columnCount) {
                holder.linearLayout.removeViewAt(holder.linearLayout.getChildCount() - 1);
            }
            TableItem tableItem = table.getItem(position);
            for (int i = holder.linearLayout.getChildCount(); i < columnCount; i++) {
                TextView view = new TextView(getContext(), null, android.R.attr.textAppearanceListItemSmall);
                holder.linearLayout.addView(view);
                LinearLayout.LayoutParams params = ((LinearLayout.LayoutParams) view.getLayoutParams());
                params.weight = 1;
                params.width = 0;
            }
            for (int i = columnOffset; i < columnCount; i++) {
                TextView textView = ((TextView) holder.linearLayout.getChildAt(i));
                textView.setText(tableItem.getText(i - columnOffset));
                Image image = tableItem.getImage(i - columnOffset);
                BitmapDrawable icon = image == null ? null : new BitmapDrawable(getResources(), (Bitmap) image.peer);
                textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                ((LinearLayout.LayoutParams) textView.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
            }
            if (holder.radioButton != null) {
                holder.radioButton.setChecked(position == selectedIndex);
            } else if (holder.checkBox != null) {
                holder.checkBox.setChecked(tableItem.getChecked());
            }
        }

        @Override
        public int getItemCount() {
            return table.getItemCount();
        }
    }

    class TableViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        CheckBox checkBox;
        RadioButton radioButton;
        LinearLayout linearLayout;
        int index;

        public TableViewHolder(View itemView) {
            super(itemView);
            this.linearLayout = (LinearLayout) itemView;
            this.linearLayout.setLayoutParams(
                    new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if ((table.style & SWT.CHECK) != 0) {
                checkBox = new CheckBox(getContext());
                checkBox.setOnClickListener(this);
                linearLayout.addView(checkBox);
                ((LinearLayout.LayoutParams) checkBox.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
            } else if ((table.style & (SWT.SINGLE | SWT.MULTI)) != 0) {
                radioButton = new RadioButton(getContext());
                radioButton.setOnClickListener(this);
                linearLayout.addView(radioButton);
                ((LinearLayout.LayoutParams) radioButton.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
            }
            linearLayout.setOnClickListener(this);
            linearLayout.setMinimumHeight(Math.round(((AndroidDisplay) table.display).pixelPerDp * 48));
        }

        @Override
        public void onClick(View view) {
            if (checkBox != null) {
                table.getItem(index).setChecked(!table.getItem(index).getChecked());
            } else {
                select(index);
            }
        }
    }



}
