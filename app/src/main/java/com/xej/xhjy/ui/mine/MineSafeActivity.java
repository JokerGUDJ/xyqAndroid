package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.session.SessionHelper;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class MineSafeActivity
 * @Createtime 2018/6/28 20:15
 * @description 账户安全页面
 * @Revisetime
 * @Modifier
 */
public class MineSafeActivity extends BaseActivity {

    @BindView(R.id.tv_safe_phone)
    TextView tvSafePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_safe);
        ButterKnife.bind(this);
        tvSafePhone.setText(PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE,""));
    }

    @Override
    protected void onResume() {
        tvSafePhone.setText(PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE,""));
        super.onResume();
    }

    @OnClick(R.id.ll_reset_phone)
    void resetPhone() {
        startActivityForResultWithAnim(new Intent(this, ResetPhoneActivity.class),1908);
    }

    @OnClick(R.id.ll_reset_password)
    void resetPassword() {
        startActivityWithAnim(new Intent(this, ResetPasswordActivity.class));
    }


    @OnClick(R.id.btn_quit_login)
    void logout(){
        String TAG = "login_out";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.LOGIN_OUT, TAG, new HashMap<String, String>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("登出-----》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        EventBus.getDefault().post(new HasMessageEvent(false));
                        LoginUtils.clearLoginInfo();
                        //退出聊天登录
                        NimUIKit.logout();
                        //SessionHelper.cleanSession();
                        //退出时退出云信状态
                        finishWithAnim();
                    } else {
                        ToastUtils.shortToast(MineSafeActivity.this, json.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
    }
}
