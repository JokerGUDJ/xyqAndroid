package com.xej.xhjy.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginFailedEvent;
import com.xej.xhjy.ui.main.BridgeActivity;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.society.MessageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author dazhi
 * @class TitleView 全局公用titleView，统一跳转，可额外设置监听返回
 * @Createtime 2018/6/13 09:57
 * @description 通过自定义属性设置控件的显示隐藏，默认都显示，不显示则设置隐藏即可
 * @Revisetime
 * @Modifier
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {
    private BaseActivity mActivity;
    private ImageView mBack, mZxing, mMessage,mMessageNew,mEdit;
    private TextView mTitle;
    private LinearLayout mLLBg;
    private OnBackClickListener mBackListener;
    private LoginCallBack mCallBack;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.view_all_title, this);
        EventBus.getDefault().register(this);
        mBack = findViewById(R.id.head_back);
        mZxing = findViewById(R.id.img_zxing);
        mMessage = findViewById(R.id.img_message);
        mMessageNew = findViewById(R.id.img_message_new);
        mTitle = findViewById(R.id.head_title);
        mEdit = findViewById(R.id.img_edit);
        mLLBg = findViewById(R.id.ll_title_bg);
        @SuppressLint("CustomViewStyleable")
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Titleview);//TypedArray是一个数组容器
        try {
            boolean backIsShow = array.getBoolean(R.styleable.Titleview_backIsShow, true);
            mBack.setVisibility(backIsShow ? VISIBLE : GONE);
            boolean zxingIsShow = array.getBoolean(R.styleable.Titleview_zxingIsShow, true);
            mZxing.setVisibility(zxingIsShow ? VISIBLE : GONE);
            boolean messIsShow = array.getBoolean(R.styleable.Titleview_messageIsShow, true);
            mMessage.setVisibility(messIsShow ? VISIBLE : INVISIBLE);
            boolean editIsShow = array.getBoolean(R.styleable.Titleview_editIsShow, false);
            mEdit.setVisibility(editIsShow ? VISIBLE : INVISIBLE);
            //决定图标颜色
            boolean lightIcon = array.getBoolean(R.styleable.Titleview_lightIcon, false);
            if(lightIcon){
                mZxing.setImageResource(R.drawable.ic_scan_white);
                mMessage.setImageResource(R.drawable.ic_message_icon_white);
            }
            if (!messIsShow){
                mMessageNew.setVisibility(GONE);
            } else {
                if (AppConstants.HAS_NEW_MESSAGE){
                    mMessageNew.setVisibility(VISIBLE);
                } else {
                    mMessageNew.setVisibility(GONE);
                }
            }
            int bgColor = array.getColor(R.styleable.Titleview_backGround, ContextCompat.getColor(mActivity,R.color.white));
            mLLBg.setBackgroundColor(bgColor);
            String titleString = array.getString(R.styleable.Titleview_titleString);
            if (!TextUtils.isEmpty(titleString)) {
                mTitle.setText(titleString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }
        mBack.setOnClickListener(this);
        mZxing.setOnClickListener(this);
        mMessage.setOnClickListener(this);
    }

    /**
     * 扫码是否显示
     * @param isVisivile
     */
    public void setZxingVisibile(boolean isVisivile){
        mZxing.setVisibility(isVisivile ? VISIBLE : INVISIBLE);
    }


    /**
     * 消息是否显示
     * @param isVisivile
     */
    public void setMessageVisibile(boolean isVisivile){
        mMessage.setVisibility(isVisivile ? VISIBLE : INVISIBLE);
        if (!isVisivile){
            mMessageNew.setVisibility(GONE);
        }
    }

    /**
     * 消息是否显示
     * @param isVisivile
     */
    public void setNewMessageVisibile(boolean isVisivile){
        if (isVisivile){
            if (mMessage.getVisibility() == VISIBLE){//先判断消息按钮是否显示了
                mMessageNew.setVisibility(VISIBLE);
                return;
            }
        }
        mMessageNew.setVisibility(GONE);
    }

    /**
     * 返回按钮是否显示
     * @param isVisivile
     */
    public void setBackVisibile(boolean isVisivile){
        mBack.setVisibility(isVisivile ? VISIBLE : INVISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        unRegiterEventBus();
        super.onDetachedFromWindow();
    }

    /**
     * 收到登录成功的消息，执行之前的跳转代码
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event) {
        if (mCallBack != null && mCallBack.canUse) {
            if (mCallBack.isPass) {
                if ("N".equals(AppConstants.USER_STATE)) {//必须认证
                    mCallBack.loginAfterRun();
                } else {
                    LoginUtils.showCerMessage(mActivity);
                }
            } else {
                mCallBack.loginAfterRun();
            }
        }
        mCallBack = null;
    }
    /**
     * 收到登录失败的消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailedEventMainThread(LoginFailedEvent event) {
        mCallBack = null;
    }

    /**
     * 收到是否有新Message消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
       LogUtils.dazhiLog("收到是否有消息----------"+event.getHasMessage());
       if (event.getHasMessage()){
           if (mMessage.getVisibility() == VISIBLE){//先判断消息按钮是否显示了
               mMessageNew.setVisibility(VISIBLE);
           }
       } else {
           mMessageNew.setVisibility(GONE);
       }
    }

    /**
     * 解除绑定事件
     */
    public void unRegiterEventBus(){
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置背景透明
     */
    public void setBackgroudTransparent(){
        mLLBg.setBackgroundColor(Color.parseColor("#00000000"));
    }
    /**
     * 返回键有特殊需求设置即可
     * @param listener 点击事件
     */
    public void setBackListener(OnBackClickListener listener){
        mBackListener = listener;
    }

    /**
     * 返回键有特殊需求设置即可
     * @param listener 点击事件
     */
    public void setEditListener(OnClickListener listener){
        mEdit.setOnClickListener(listener);
    }

    /**
     * 代码中设置标题
     * @param title 标题String
     */
    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setTitleColor(String color){
        mTitle.setTextColor(Color.parseColor(color));
    }
    /**
     * 代码中设置标题
     * @param titleID 标题资源ID
     */
    public void setTitle(int titleID){
        mTitle.setText(titleID);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_zxing) {
            Intent intent = new Intent(mActivity, BridgeActivity.class);
            intent.putExtra(BridgeActivity.START_ZXING,true);
            mActivity.startActivity(intent);
        } else if(v.getId() == R.id.img_message){
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    Intent intent = new Intent(mActivity, MessageActivity.class);
                    mActivity.startActivityWithAnim(intent);
                }
            };
            mCallBack.isPass = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        } else if(v.getId() == R.id.head_back){
            if (mBackListener != null){
                mBackListener.backClick();
            } else {
                mActivity.finishWithAnim();
            }
        }
    }
}