package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xej.xhjy.R;

/**
 * @author dazhi
 * @class ClubSLoadingDialog
 * @Createtime 2018/6/11 10:42
 * @description 等待提示框 可设置等待文本
 * @Revisetime
 * @Modifier
 */
public class ClubLoadingDialog extends Dialog {
    private TextView mLoadingTv;//文本
    private String mLoadingMsg;//从外界设置的文本提示
    private ImageView mPositiveBtn;
    private RotateAnimation animation;
    public ClubLoadingDialog(Context context) {
        super(context, R.style.ClubDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_club);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
//        setCancelable(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(mLoadingMsg)) {
            mLoadingTv.setText(mLoadingMsg);
        } else {
            mLoadingTv.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mPositiveBtn = (ImageView) findViewById(R.id.loading_img);
        mLoadingTv = (TextView) findViewById(R.id.loading_message);
        animation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1000);
        animation.setStartOffset(-1);
        animation.setRepeatMode(RotateAnimation.RESTART);
        animation.setRepeatCount(-1);
    }

    @Override
    public void show() {
        super.show();
        mPositiveBtn.startAnimation(animation);
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     * @param message
     */
    public void setmLoadingMsg(String message) {
        mLoadingMsg = message;
    }

    public void setmChangeMsg(String message) {
        mLoadingTv.setVisibility(View.VISIBLE);
        mLoadingTv.setText(message);
    }
}
