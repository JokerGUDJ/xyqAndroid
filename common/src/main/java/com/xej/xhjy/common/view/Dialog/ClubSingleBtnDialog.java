package com.xej.xhjy.common.view.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xej.xhjy.common.R;
import com.xej.xhjy.common.utils.AppConstants;

/**
 * @author dazhi
 * @class ClubSingleBtnDialog
 * @Createtime 2018/6/11 10:42
 * @description 只有确定按钮的提示框
 * @Revisetime
 * @Modifier
 */
public class ClubSingleBtnDialog extends Dialog {
    private Button mPositiveBtn;//确定按钮
    private TextView mTitleTv;//消息标题文本
    private TextView mMsgTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String mPositiveBtnStr;
    private int mMessageGravit = 0;
    private PositiveListener mPositiveOnclickListener;//确定按钮被点击了的监听器
    private PositiveListener listener;//调用者传过来的回调函数

    public ClubSingleBtnDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    public ClubSingleBtnDialog(Context context, PositiveListener listener) {
        super(context, R.style.CustomDialog);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_club);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setPositiveListener(String str, PositiveListener onYesOnclickListener) {
        if (str != null) {
            mPositiveBtnStr = str;
        }
        if (onYesOnclickListener != null){
            this.mPositiveOnclickListener = onYesOnclickListener;
        }
    }


    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        //if(mPositiveBtn != null)
        {
            mPositiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveOnclickListener != null) {
                        mPositiveOnclickListener.onPositiveClick();
                    }
                    if(listener != null){
                        listener.onPositiveClick();
                    }
                    ClubSingleBtnDialog.this.dismiss();
                }
            });
        }
    }

    public void update(){
        initData();
        initEvent();
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(titleStr)) {
            mTitleTv.setText(titleStr);
        }
        if (!TextUtils.isEmpty(messageStr)) {
            mMsgTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(mPositiveBtnStr)) {
            mPositiveBtn.setText(mPositiveBtnStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mPositiveBtn = (Button) findViewById(R.id.dialog_yes);
        mTitleTv = (TextView) findViewById(R.id.dialog_title);
        mMsgTv = (TextView) findViewById(R.id.dialog_message);
        if (mMessageGravit != 0){
            mMsgTv.setGravity(mMessageGravit);
        }
    }

    public void setMessageGravity(int gravity){
        mMessageGravit = gravity;
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppConstants.IS_DIALOG_SHOW = false;
    }

    @Override
    public void show() {
        if (!AppConstants.IS_DIALOG_SHOW){
//            AppConstants.IS_DIALOG_SHOW = true;
            super.show();
        }
    }
}
