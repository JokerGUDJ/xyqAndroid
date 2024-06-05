package com.xej.xhjy.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.xej.xhjy.common.R;
import com.xej.xhjy.common.http.http.RxHttpManager;
import com.xej.xhjy.common.safe.ActivityPrevent;
import com.xej.xhjy.common.safe.SecureManager;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dazhi
 * @class BaseActivity
 * @Createtime 2018/6/5 20:39
 * @description 基类Activity，负责设置统一状态栏颜色，权限管理，网络请求生命周期管理
 * @Revisetime
 * @Modifier
 */
public class BaseActivity extends AppCompatActivity {
    private List<String> tags = new ArrayList<>();
    public BaseActivity mActivity;
    public static ClubSingleBtnDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        //禁止截屏
//        if (!LogUtils.isShowLog){
//            SecureManager.preventScreenShot(this);
//        }
        //沉浸式状态栏
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    /**带动画启动*/
    public void startActivityWithAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**带动画启动*/
    public void startActivityForResultWithAnim(Intent intent,int code) {
        startActivityForResult(intent,code);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**带动画关闭*/
    public void finishWithAnim(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void addTag(String tag){
        tags.add(tag);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //页面防劫持提醒用户
        SecureManager.monitorRunningTask(this,true, new ActivityPrevent.TaskStatusListener() {
            @Override
            public void taskStatusChanged(boolean isBackground) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if(dialog != null && dialog.isShowing()){
            dialog.hide();
        }
        dialog = null;
        //销毁时移除网络请求，避免内存泄漏
        if (tags.size() > 0) {
            for (String tag : tags) {
                RxHttpManager.removeDisposable(tag);
            }
        }
        //移除EventBus监听
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 带动画退出
     */
    @Override
    public void onBackPressed() {
        if(dialog != null && dialog.isShowing()){
            dialog.hide();
        }
        dialog = null;
        finishWithAnim();
    }
}
