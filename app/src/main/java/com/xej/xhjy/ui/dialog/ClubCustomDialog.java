package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;

/**
 * @class ClubCustomDialog
 * @author dazhi
 * @Createtime 2018/6/11 10:43
 * @description 通用的有确定按钮和取消按钮的自定义布局提示框
 * @Revisetime
 * @Modifier
 */
public class ClubCustomDialog extends Dialog {
    private Button mPositiveBtn;//确定按钮
    private Button mNegativeBtn;//取消按钮
    private TextView mTitleTv;//消息标题文本
    private LinearLayout mContainLayout;//消息提示文本
    private String titleStr;//从外界设置的title文本
    //确定文本和取消文本的显示内容
    private String mPositiveBtnStr, mNegativeBtnStr;
    private PositiveListener mPositiveOnclickListener;//确定按钮被点击了的监听器
    private NegativeListener mNegativeOnclickListener;//取消按钮被点击了的监听器
    private View mCustomView;

    /**
     * @param context Activity
     * @param view 自定义布局View实体
     */
    public ClubCustomDialog(Context context,View view) {
        super(context, R.style.ClubDialog);
        mCustomView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
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
     * 初始化界面控件
     */
    private void initView() {
        mPositiveBtn = (Button) findViewById(R.id.dialog_yes);
        mNegativeBtn = (Button) findViewById(R.id.dialog_no);
        mTitleTv = (TextView) findViewById(R.id.dialog_title);
        mContainLayout = (LinearLayout) findViewById(R.id.layout_contain);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        mContainLayout.addView(mCustomView);
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(titleStr)) {
            mTitleTv.setText(titleStr);
        }
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(mPositiveBtnStr)) {
            mPositiveBtn.setText(mPositiveBtnStr);
        }
        if (!TextUtils.isEmpty(mNegativeBtnStr)) {
            mNegativeBtn.setText(mNegativeBtnStr);
        }
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPositiveOnclickListener != null) {
                    mPositiveOnclickListener.onPositiveClick();
                }
                ClubCustomDialog.this.dismiss();
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNegativeOnclickListener != null) {
                    mNegativeOnclickListener.onNegativeClick();
                }
                ClubCustomDialog.this.dismiss();
            }
        });
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
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNegativeListener(String str, NegativeListener onNoOnclickListener) {
        if (str != null) {
            mNegativeBtnStr = str;
        }
        this.mNegativeOnclickListener = onNoOnclickListener;
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
        this.mPositiveOnclickListener = onYesOnclickListener;
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
