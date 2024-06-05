package com.xej.xhjy.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xej.xhjy.R;

public class TextviewTobTabView extends RelativeLayout {

    private View divider;
    private TextView mText;
    private ImageView redPoint;

    public TextviewTobTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.textview_top_tab,this);
        divider = findViewById(R.id.divier);
        mText = findViewById(R.id.tv_task_bar);
        redPoint = findViewById(R.id.new_message_tip);
    }

    public void setText(String text){
        mText.setText(text);
    }
    public void setRedPointStatus(int visiable){
        if(redPoint != null){
            redPoint.setVisibility(visiable);
        }
    }

    public void setCheck(boolean isCheck) {
        if (isCheck) {
            divider.setVisibility(VISIBLE);
            mText.setTextColor(Color.parseColor("#333333"));
            mText.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            divider.setVisibility(GONE);
            mText.setTextColor(Color.parseColor("#5B5D68"));
            mText.setTypeface(Typeface.DEFAULT);
        }
    }
}
