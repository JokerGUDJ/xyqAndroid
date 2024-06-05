package com.xej.xhjy.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.GenalralUtils;

public class CustomProgressBar extends LinearLayout {

    String text;
    Paint mPaint;
    private Rect textRect;
    private Bitmap bitmap, indicator;
    private ProgressBar progressBar;
    int progress;
    int proWidth, proHeight;
    LinearLayout ll_content;

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText(context);
    }

    public CustomProgressBar(Context context) {
        super(context);
        initText(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText(context);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        progress = progressBar.getProgress();
        this.mPaint.getTextBounds(this.text, 0, this.text.length()-1, textRect);

        proWidth = progressBar.getWidth();
        proHeight = progressBar.getHeight();

        //画指示器
        int bitmapx = (int) (progressBar.getLeft() + proWidth * ((progress * 1f) / progressBar.getMax())) - bitmap.getWidth() / 2;
        int bitmapy = proHeight - bitmap.getWidth()/5;
        if (bitmapx < 0)
            bitmapx = progressBar.getLeft();
//        if(bitmapx > progressBar.getRight()-bitmap.getWidth())
//            bitmapx = progressBar.getRight()-bitmap.getWidth();
        canvas.drawBitmap(bitmap, bitmapx, bitmapy, mPaint);
        canvas.drawBitmap(indicator, bitmapx + indicator.getWidth()*3/4, proHeight + indicator.getHeight()*4/3, mPaint);

        //写字
        int tvx = (int) (progressBar.getLeft() + proWidth * ((progress * 1f) / progressBar.getMax())) - textRect.centerX() - 5;
        int tvy = proHeight - bitmap.getWidth()*1/2;
        if (tvx < 0)
            tvx = progressBar.getLeft();
        if(tvx >= progressBar.getRight()-textRect.width())
            tvx = progressBar.getRight()-textRect.width();
        canvas.drawText(this.text, tvx, tvy + bitmap.getHeight(), this.mPaint);
    }

    //初始化，画笔
    private void initText(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_text_progressbar, this, true);
        progressBar = (ProgressBar) inflate.findViewById(R.id.progressbar);
        progressBar.setVisibility(INVISIBLE);
        setWillNotDraw(false);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.parseColor("#E54844"));
        this.mPaint.setTextSize(28);
        textRect = new Rect();
        text = "0%";
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_progress);
        indicator = getBitmapFromDrawable(context, context.getDrawable(R.drawable.progresesbar_indicator));
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(Context context, @NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(GenalralUtils.dp2px(context,15), GenalralUtils.dp2px(context,15), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }


    public void setText(String str) {
        text = str;
    }
}
