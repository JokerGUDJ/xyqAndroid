package com.xej.xhjy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.meetinglib.sdk.NECallback;
import com.netease.meetinglib.sdk.NEMeetingError;
import com.netease.meetinglib.sdk.NEMeetingSDK;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.IMAccountBean;
import com.xej.xhjy.bean.UserBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.safe.ActivityPrevent;
import com.xej.xhjy.common.safe.SecureManager;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.countdown.CountdownView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.session.SessionHelper;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.NormalUtils;
import com.xej.xhjy.tools.PassGuardUtils;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.BackToHomeEvent;
import com.xej.xhjy.ui.mine.checkBranchOfEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.passguard.PassGuardEdit;

/**
 * @author dazhi
 * @class LoginActivity  登录页面
 * @Createtime 2018/6/25 09:35
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_msg_number)
    EditText msg_number;
    @BindView(R.id.edt_password)
    PassGuardEdit edtPassword;
//    @BindView(R.id.titleview)
//    TitleView titleview;
    @BindView(R.id.tv_sms)
    CountdownView tvSms;
    @BindView(R.id.tv_resetlogin)
    TextView tvResetlogin;
    @BindView(R.id.img_close)
    ImageView img_close;
    private long mLasttime = 0;
    private boolean isShowPhone;
    private ClubLoadingDialog mLoadingDialog;
    private String currTime;

    //是否验证码登录
    private boolean isAuthCodeLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        ButterKnife.bind(this);
        PassGuardUtils.initialize(edtPassword);
        initData();
        EventBus.getDefault().register(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    private void initData() {
        mLoadingDialog = new ClubLoadingDialog(this);
        String phone = PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE, "");
        if (!GenalralUtils.isEmpty(phone)) {
            edtUsername.setText(phone);
            edtUsername.setSelection(edtUsername.getText().length());
            isShowPhone = true;
        }

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String editPhone = s.toString();
                if(TextUtils.isEmpty(editPhone) || !editPhone.equals(phone)){
                    //手机号码为空，或者手机号码不等于之前保存的，从edittext里取
                    isShowPhone = false;
                }else{
                    isShowPhone = true;
                }
            }
        });
//        titleview.setBackListener(new OnBackClickListener() {
//            @Override
//            public void backClick() {
//                EventBus.getDefault().post(new LoginFailedEvent("登录失败"));
//                finishWithAnim();
//            }
//        });
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_sms)
    void getCode() {
        String phone = getSaveMobilePhone(isShowPhone);
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(this, "请输入手机号！");
            return;
        }
        mLoadingDialog.show();
        String TAG = "do_get_login_code";
        addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("mobilephone", phone);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_LOGIN_CODE, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("获取登录code----》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        tvSms.startCountdown();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mLoadingDialog.dismiss();
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                ToastUtils.shortToast(LoginActivity.this, "短信发送失败，请重新尝试或使用密码登录");
                LogUtils.dazhiLog("获取登录cod失败----》" + errorMsg);
            }
        });
    }

    /**
     * 登录先向服务器获取时间戳
     */
    @OnClick(R.id.btn_login)
    void getTimeMills() {
        if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
            return;
        mLasttime = System.currentTimeMillis();
        String phone = getSaveMobilePhone(isShowPhone);
        String msg = msg_number.getText().toString().trim();
        int len = edtPassword.getLength();
        // 密码控件校验

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(this, "请输入手机号！");
            return;
        }
        if (!isAuthCodeLogin) {
            if (TextUtils.isEmpty(msg)) {
                ToastUtils.shortToast(this, "请输入验证码！");
                return;
            }
        } else {
            if (len == 0) {
                ToastUtils.shortToast(this, "请输入密码！");
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        String TAG = "getTimeMills";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.PWD_TIME_MILLIS, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("getTimeMills---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.getJSONObject("content");
                        currTime = content.optString("currTime");
                        String safe = content.optString("safe");
                        String sKey = content.optString("sKey");
                        String affineX = content.optString("affineX");
                        String affineY = content.optString("affineY");
                        LogUtils.dazhiLog("sKey=" + sKey);
                        if (!isAuthCodeLogin) {
                            doLogin();
                        } else {
                            if (!TextUtils.isEmpty(safe)) {
                                if ("0".equals(safe)) {
                                    ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(LoginActivity.this);
                                    dialog.setMessage("系统安全升级，请重置密码后重新登录");
                                    dialog.setPositiveListener("确定", new PositiveListener() {
                                        @Override
                                        public void onPositiveClick() {
                                            startActivityWithAnim(new Intent(LoginActivity.this, FindPasswordActivity.class));
                                        }
                                    });
                                    dialog.show();
                                } else if ("1".equals(safe)) {
                                    if (sKey != null) {
                                        // 返回时间戳放入密码控件生成密文
                                        edtPassword.setCipherKey(sKey);
                                        edtPassword.setEccKey(affineX + "|" + affineY);
                                    }
                                    if (!GenalralUtils.isEmpty(sKey)) {
                                        doLogin();
                                    } else {
                                        ToastUtils.shortToast(mActivity, "登录失败！");
                                        mLoadingDialog.dismiss();
                                    }
                                } else {
                                    ToastUtils.shortToast(mActivity, "状态异常！");
                                }
                            }
                        }

                    } else {
                        ToastUtils.shortToast(mActivity, "登录失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "登录失败！");
            }
        });


    }


    void doLogin() {
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageLogin", "channel=android", "eventLogin", "mobilephone="+PerferenceUtils.get(AppConstants.User.PHONE,""));
        String TAG = "do_login";
        addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("mobilephone", getSaveMobilePhone(isShowPhone));
        String url = NetConstants.LOGIN_USER;
        if (!isAuthCodeLogin) {
            url = NetConstants.LOGIN_BY_CODE;
            map.put("phoneToken", msg_number.getText().toString().trim());
        } else {
            map.put("password", edtPassword.getSM2SM4Ciphertext());
        }
        map.put("timeToken", currTime);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("登录成功----》" + jsonString);
                EventTrackingUtil.EventTrackSubmit(mActivity, "pageLoginSucc", "channel=android", "eventLoginSucc",
                        "mobilephone="+PerferenceUtils.get(AppConstants.User.PHONE,""));
                try {
                    JSONObject js = new JSONObject(jsonString);
                    if (js.optString("code").equals("0")) {
                        JSONObject json = js.optJSONObject("content");
                        if (json != null) {
                            AppConstants.USER_ID = json.optString("userId");
                            AppConstants.USER_ROLES = json.optString("userRoles");
                            AppConstants.USER_STATE = json.optString("userState");
                            PerferenceUtils.put(AppConstants.User.ID, AppConstants.USER_ID);
                            PerferenceUtils.put(AppConstants.User.ROLES, AppConstants.USER_ROLES);
                            PerferenceUtils.put(AppConstants.User.STATE, AppConstants.USER_STATE);
                        }
                        AppConstants.IS_LOGIN = true;
                        //重新登录后要重新初始化
                        SessionHelper.init();
                        NIMClient.getService(AuthService.class).logout();
                        if (!TextUtils.isEmpty(AppConstants.USER_ID)) {
                            getUserInfo();
                        } else {
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(LoginActivity.this, "登录失败！请检查网络后重新尝试");
                LogUtils.dazhiLog("登录失败----》" + errorMsg);
            }
        });
    }

    /**
     * 获取手机号方法
     */
    public String getSaveMobilePhone(boolean isShowPhone) {
        String phone = "";
        if (isShowPhone) {
            phone = PerferenceUtils.get(AppConstants.User.PHONE, "");
        } else {
            phone = edtUsername.getText().toString().trim();
        }
        return phone;
    }


    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        String TAG1 = "get_userinfo";
        addTag(TAG1);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_USER_INFO, TAG1, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("个人信息-------" + jsonString);
                UserBean bean = JsonUtils.stringToObject(jsonString, UserBean.class);
                if (bean != null && "0".equals(bean.getCode()) && bean.getContent() != null) {
                    PerferenceUtils.put(AppConstants.User.NAME, bean.getContent().getUserName());
                    PerferenceUtils.put(AppConstants.User.COMPLANY, bean.getContent().getOrgInfo().getOrgName());
                    PerferenceUtils.put(AppConstants.User.PHONE, bean.getContent().getMobilephone());
                    //保存脱敏手机号码
                    PerferenceUtils.put(AppConstants.User.HIDDEN_PHONE, NormalUtils.mobileEncrypt(bean.getContent().getMobilephone()));
                    PerferenceUtils.put(AppConstants.User.TEL, bean.getContent().getPhone());
                    PerferenceUtils.put(AppConstants.User.GENDER, bean.getContent().getGender());
                    PerferenceUtils.put(AppConstants.User.ORGID, bean.getContent().getOrgId());
                    PerferenceUtils.put(AppConstants.User.JOB, bean.getContent().getJobTitle().getJobName());
                    PerferenceUtils.put(AppConstants.User.EMAIL, bean.getContent().getEmail());
                    PerferenceUtils.put(AppConstants.User.ADDRESS, bean.getContent().getAddr());
                    PerferenceUtils.put(AppConstants.User.DEPARTMENT, bean.getContent().getDivInfo().getDivName().trim());
                    PerferenceUtils.put(AppConstants.User.BRANCH_OF, bean.getContent().getCommitList().toString());
                    queryIMAccount();
                    if (!TextUtils.isEmpty(bean.getContent().getCommitId())) {
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_NAME, bean.getContent().getCommit().getName());
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_ID, bean.getContent().getCommit().getId());
                        PerferenceUtils.put(AppConstants.User.COMMIT_ID, bean.getContent().getCommitId());
                    } else {
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_NAME, "其他");
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_ID, "586914089029906462");
                        String ignore = bean.getContent().getIgnore();
                        PerferenceUtils.put(AppConstants.User.COMMIT_ID, "586914089029906462");
                        if (!TextUtils.isEmpty(ignore)) {
                            if (!"1".equals(ignore)) {
                                EventBus.getDefault().post(new checkBranchOfEvent("选择专委会"));
                            }
                        } else {
                            EventBus.getDefault().post(new checkBranchOfEvent("选择专委会"));
                        }
                    }

                } else {
                    ToastUtils.shortToast(mActivity, "获取个人信息失败！");
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "获取个人信息失败！");
                LogUtils.dazhiLog("用户信息失败----》" + errorMsg);
            }
        });
    }

    /**
     * 查询云信账号
     */
    private void queryIMAccount() {
        String TAG1 = "query_im_account";
        addTag(TAG1);
        Map<String, String> map = new HashMap<>();
        map.put("phone", getSaveMobilePhone(isShowPhone));
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_PHONE_IM_ACCOUND, TAG1, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("App登录获取云信账号------" + jsonString);
                IMAccountBean bean = JsonUtils.stringToObject(jsonString, IMAccountBean.class);
                if (bean != null && "0".equals(bean.getCode()) && bean.getContent() != null) {
                    PerferenceUtils.put(AppConstants.User.IM_CHAT_ACCOUNT, bean.getContent().getAccId());

                    PerferenceUtils.put(AppConstants.User.IM_POST_ID, bean.getContent().getId());
                    PerferenceUtils.put(AppConstants.User.IM_CHAT_TOKEN, bean.getContent().getToken());
                    //保存会议账号
                    PerferenceUtils.put(AppConstants.User.MEETING_ACCOUNT_ID, bean.getContent().getAccountId());
                    PerferenceUtils.put(AppConstants.User.MEETING_ACCOUNT_TOKEN, bean.getContent().getAccountToken());
                    meetingSDKLogin(bean.getContent().getAccountId(), bean.getContent().getAccountToken());
                    //获取聊天账号，发消息自动登录，如果鑫合家园已经登录，则进入app时云信自动登录。
                    finishWithPostEvent();
                } else {
                    ToastUtils.shortToast(mActivity, "获取云信账号失败");
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "获取云信账号失败！");
                LogUtils.dazhiLog("获取云信账号失败----》" + errorMsg);
            }
        });
    }

    private void meetingSDKLogin(String accountId, String accountToken){
        NEMeetingSDK.getInstance().login(accountId, accountToken, new NECallback<Void>() {
            @Override
            public void onResult(int resultCode, String resultMsg, Void result) {
                if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                    //登录成功
                    Log.d("lirong", "meeting sdk login succ");
                } else {
                    //登录失败
                    Log.d("lirong", "meeting sdk login fail");
                }
            }
        });
    }

    /**
     * 登录成功要关闭时调用该方法进行全局通知
     */
    private void finishWithPostEvent() {
        EventBus.getDefault().post(new LoginEvent("登录成功！！！"));
        LoginCheckUtils.isLogin();
        finishWithAnim();
    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_register)
    void reginster() {
        Intent intent = new Intent(mActivity, RegisterStartActivity.class);
        startActivityWithAnim(intent);
    }

    /**
     * 找回密码
     */
    @OnClick(R.id.tv_forget_password)
    void findPassword() {
        startActivityWithAnim(new Intent(this, FindPasswordActivity.class));
    }


    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_resetlogin)
    void changeLogin() {
        if (isAuthCodeLogin) {
            isAuthCodeLogin = false;
            msg_number.setVisibility(View.VISIBLE);
            edtPassword.setVisibility(View.GONE);
            tvSms.setVisibility(View.VISIBLE);
            tvResetlogin.setText("密码登录");
            msg_number.setHint(R.string.login_input_msg);
            edtPassword.setText("");
            msg_number.setText("");
        } else {
            isAuthCodeLogin = true;
            msg_number.setVisibility(View.GONE);
            edtPassword.setVisibility(View.VISIBLE);
            tvSms.setVisibility(View.GONE);
            tvResetlogin.setText("短信登录");
            edtPassword.setHint(R.string.login_input_pwd);
            edtPassword.setText("");
            msg_number.setText("");
        }
    }

    @OnClick(R.id.img_close)
    void closeLogin(){
        finishWithAnim();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new LoginFailedEvent("登录失败"));
        finishWithAnim();
    }

    /**
     * 收到返回首页的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(BackToHomeEvent event) {
        finishWithAnim();
    }

    @Override
    protected void onDestroy() {
        if (edtPassword != null) {
            edtPassword = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //解决登录后台下情况密码清除
        SecureManager.monitorRunningTask(this, true, new ActivityPrevent.TaskStatusListener() {
            @Override
            public void taskStatusChanged(boolean isBackground) {
                if (edtPassword != null) {
                    //在后台清理密码
                    edtPassword.setText("");
                    //防止系统键盘弹出，固失去焦点。
                    edtPassword.clearFocus();
                }

            }
        });
    }


}
