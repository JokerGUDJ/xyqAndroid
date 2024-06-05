package com.xej.xhjy.ui.society.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.xej.xhjy.ClubApplication;
import com.xej.xhjy.R;


public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.gray_text;
    /**
     * text颜色
     */
    private int textColor;

    public SpannableClickable() {
        this.textColor = ClubApplication.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
