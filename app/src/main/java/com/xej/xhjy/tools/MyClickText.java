package com.xej.xhjy.tools;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.tools
 * @ClassName: MyClickText
 * @Description: 特殊的textview可点击
 * @Author: lihy_0203
 * @CreateDate: 2019/12/11 上午10:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/11 上午10:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyClickText extends ClickableSpan {

    private int mHighLightColor = Color.BLUE;

    private boolean mUnderLine = false;

    private View.OnClickListener mClickListener;


    public MyClickText(View.OnClickListener listener)

    {

        this.mClickListener = listener;

    }


    public MyClickText(int color, boolean underline, View.OnClickListener listener)

    {

        this.mHighLightColor = color;

        this.mUnderLine = underline;

        this.mClickListener = listener;

    }


    @Override

    public void onClick(View widget)

    {

        if (mClickListener != null)

            mClickListener.onClick(widget);

    }


    @Override

    public void updateDrawState(TextPaint ds)

    {

        ds.setColor(mHighLightColor);

        ds.setUnderlineText(mUnderLine);

    }

}
