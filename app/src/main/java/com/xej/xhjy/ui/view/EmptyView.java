package com.xej.xhjy.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xej.xhjy.R;

/**
 * @author dazhi
 * @class EmptyView 空页面view
 * @Createtime 2018/6/13 09:57
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class EmptyView extends RelativeLayout {
    private ImageView mIcon;
    private TextView mText;
    public EmptyView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_empty,this);
        mIcon = findViewById(R.id.img_task_icon);
        mText = findViewById(R.id.tv_task_bar);
    }
    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_empty,this);
        mIcon = findViewById(R.id.img_task_icon);
        mText = findViewById(R.id.tv_task_bar);
    }

    public void setIcon(int resourceID){
        mIcon.setBackgroundResource(resourceID);
    }

    public void setText(int txtID) {
        mText.setText(txtID);
    }

    public void setText(String txt) {
        mText.setText(txt);
    }

}