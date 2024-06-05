package com.xej.xhjy.common.view.spinner;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xej.xhjy.common.R;
import com.xej.xhjy.common.utils.GenalralUtils;

/**
 * @author dazhi
 * @class NiceSpinnerBaseAdapter
 * @Createtime 2018/5/29 15:13
 * @description 下拉选择listview adapter
 * @Revisetime
 * @Modifier
 */
public abstract class NiceSpinnerBaseAdapter<T> extends BaseAdapter {


    private int textColor,paddingLeft,paddingTop;
    private int backgroundSelector;
    int selectedIndex;

    NiceSpinnerBaseAdapter(Context context ,int textColor, int backgroundSelector,
                           int paddingleft) {
        this.paddingLeft = paddingleft;
        this.backgroundSelector = backgroundSelector;
        this.textColor = textColor;
        paddingTop = GenalralUtils.dp2px(context,10);
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        TextView textView;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_list_item, null);
            textView = (TextView) convertView.findViewById(R.id.text_view_spinner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(ContextCompat.getDrawable(context, backgroundSelector));
            }
            textView.setTextColor(textColor);
            textView.setPadding(paddingLeft,paddingTop,0,paddingTop);
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }
        textView.setText(getItem(position).toString());
        return convertView;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public abstract T getItemInDataset(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    static class ViewHolder {
        TextView textView;

        ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
