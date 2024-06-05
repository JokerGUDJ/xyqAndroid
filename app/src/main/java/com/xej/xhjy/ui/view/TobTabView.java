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
 * @class TobTabView 关注和消息上访自定义tab
 * @Createtime 2018/6/13 09:57
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class TobTabView extends RelativeLayout {
    private ImageView mIcon;
    private TextView mText;
    private int mImageCheckID, mImageNormalID;

    public TobTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_top_tab,this);
        mIcon = findViewById(R.id.img_task_icon);
        mText = findViewById(R.id.tv_task_bar);
    }
    public ImageView getIconView() {
        return mIcon;
    }

    public void setIconAndText(int choseResourceID, int normaResourcelID, int txtID) {
        mImageCheckID = choseResourceID;
        mImageNormalID = normaResourcelID;
        mIcon.setImageResource(mImageNormalID);
        mText.setText(txtID);
    }

    public void setIconAndText(int choseResourceID, int normaResourcelID, String txt) {
        mImageCheckID = choseResourceID;
        mImageNormalID = normaResourcelID;
        mIcon.setImageResource(mImageNormalID);
        mText.setText(txt);
    }

    public void setCheck(boolean isCheck) {
        if (isCheck) {
            mIcon.setImageResource(mImageCheckID);
            mText.setTextColor(0xFFE70934);
        } else {
            mIcon.setImageResource(mImageNormalID);
            mText.setTextColor(Color.parseColor("#93a8ac"));
        }
    }
}