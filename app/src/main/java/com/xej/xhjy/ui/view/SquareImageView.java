package com.xej.xhjy.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @class SquareImageView  宽高一样的正方形ImageView
 * @author dazhi
 * @Createtime 2018/11/26 15:00
 * @description describe
 * @Revisetime
 * @Modifier
 */
@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
