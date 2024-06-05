package com.xej.xhjy.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginFailedEvent;
import com.xej.xhjy.ui.start.LogoSplashActivity;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**home
 * @author dazhi
 * @class BridgeActivity
 * @Createtime 2018/6/29 16:45
 * @description 透明的空acitivity，由于每个页面都有扫码和消息按钮，故封装了统一头部进行跳转，
 * 同时为避免每个页面都去处理扫码等返回值接收处理以及额外的二级跳转问题，
 * 这里用一个空的activity来做统一处理，避免了每个页面的重复处理，也避免了在BaseActivity进行处理。
 * @Revisetime
 * @Modifier
 */
public class BridgeActivity extends BaseActivity {
    public static final String START_ZXING = "start_zxing";
    //    public static final String CHECK_MEETID = "check_meetId";
    private final int QRCODE = 98;
    private ClubLoadingDialog mLoadingDialog;
    private LoginCallBack mCallBack;
    private String meetID, tab, meetPlace, banqPlace; //tab默认跳转会议说明, meetPlace;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        boolean isZxing = getIntent().getBooleanExtra(START_ZXING, false);
        if (isZxing) {
            //先申请权限
            new RxPermissions(this)
                    .requestEach(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.name.equals(Manifest.permission.CAMERA)) {
                                //当权限获取成功时，permission.granted=true
                                if (permission.granted) {
                                    BridgeActivity.this.startActivityForResultWithAnim(new Intent(BridgeActivity.this, ScanActivity.class), QRCODE);
                                } else {
                                    //未获得权限直接关闭页面
                                    ToastUtils.shortToast(BridgeActivity.this, "未获得相机使用权限，请在设置中修改！");
                                    finish();
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 收到登录成功的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        if (mCallBack != null && mCallBack.canUse) {
            if ("N".equals(AppConstants.USER_STATE)) {//必须认证
                mCallBack.loginAfterRun();
            } else {
                showNoticeDialog("为了您的权益，请您耐心等待认证审核！");
            }
        }
        mCallBack = null;
    }

    /**
     * 收到登录失败的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailedEventMainThread(LoginFailedEvent event) {
        mCallBack = null;
        showNoticeDialog("扫码需要登录！");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QRCODE) {
                String backStr = data.getStringExtra("barCode");
                LogUtils.dazhiLog("二维码地址---->"+backStr);
//                Intent intent = new Intent(mActivity, WebLearnPlatformActivity.class);
//                intent.putExtra(WebPagerActivity.LOAD_URL, backStr);
//                startActivity(intent);
//                String check_meetId = getIntent().getStringExtra(CHECK_MEETID);
//                LogUtils.dazhiLog("扫码返回数据------" + backStr);
                String[] params = backStr.split("\\?");
                try {
                    if(params.length > 1 && (backStr.indexOf("goldenalliance.com.cn") > -1 || backStr.indexOf("mbank-test.njcb.com.cn") > -1)){
                        final String url;
                        String[] meetids = params[1].split("&");
                        if(meetids.length == 1 && backStr.contains("meetId")){
                            //签到相关处理
                            meetID = meetids[0].split("=")[1];
                            url = NetConstants.QR_SIGN_IN;
                            final Map<String, String> map = new HashMap<>();
                            map.put("meetId", meetID);
                            mCallBack = new LoginCallBack() {
                                @Override
                                public void loginAfterRun() {
                                    doNetPostString(url, map);
                                }
                            };
                            LoginUtils.startCheckLogin(this, mCallBack);
                        }else if(meetids.length == 2 && backStr.contains("MSignUpFirst")){
                            //扫码报名链接
                            meetID = meetids[0].split("=")[1];
                            String mobile = PerferenceUtils.get(AppConstants.User.PHONE, "");
                            querySignupInfo(mobile, meetids[1]);

                        }else if(meetids.length == 3 && backStr.contains("meetId") && backStr.contains("ddindex") || backStr.contains("meetplace") ||
                                backStr.contains("banqplace")){
                            //跳转会议座次/宴会相关处理
                            meetID = meetids[0].split("=")[1];
                            tab = meetids[1].split("=")[1];
                            if(backStr.contains("meetplace")){
                                meetPlace = meetids[2].split("=")[1];
                            }
                            if(backStr.contains("banqplace")){
                                banqPlace = meetids[2].split("=")[1];
                            }
                            url = NetConstants.QR_FOR_SEAT;
                            final Map<String, String> map = new HashMap<>();
                            map.put("meetId", meetID);
                            mCallBack = new LoginCallBack() {
                                @Override
                                public void loginAfterRun() {
                                    doNetPostString(url, map);
                                }
                            };
                            LoginUtils.startCheckLogin(this, mCallBack);
                        }else {
                            showNoticeDialog("请扫描鑫合家园相关二维码!");
                        }
                    }else{
                        showNoticeDialog("请扫描鑫合家园相关二维码!");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void showNoticeDialog(String message) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setMessage(message);
        dialog.setTitle("温馨提示");
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                finish();
            }
        });
        dialog.show();
    }

    private void showNoticeDialog(String message, String meetID) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setMessage(message);
        dialog.setTitle("温馨提示");
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                if("已签到".equals(message)){
                    Intent intent = new Intent(mActivity, WebPagerActivity.class);
                    intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
                    intent.putExtra(WebPagerActivity.MEETTING_ID, meetID);
                    startActivity(intent);
                }
                finish();
            }
        });
        dialog.show();
    }

    /**
     * 扫码签到
     *
     * @param url
     * @param map
     */
    public void doNetPostString(final String url, Map<String, String> map) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ClubLoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        addTag(url);
        RxHttpClient.doPostStringWithUrl(mActivity, url, url, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                try {
                    LogUtils.dazhiLog("签到成功过-----" + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        if(url.equals(NetConstants.QR_FOR_SEAT)){
                            Intent intent = new Intent(mActivity, WebPagerActivity.class);
                            intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail?index="+tab+"&meetplace="+meetPlace+"&banqplace="+banqPlace);
                            intent.putExtra(WebPagerActivity.MEETTING_ID, meetID);
                            startActivity(intent);
                        }else{
                            //跳转到扫码成功页
                            JSONObject content = jsonObject.optJSONObject("content");
                            if (content != null) {
                                gotoMeetDetail(content);
                            }
                        }
                        finish();
                    } else {
                        String message = jsonObject.optString("msg");
                        showNoticeDialog(message, map.get("meetId"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                showNoticeDialog("签到失败！");
            }
        });
    }

    /***
     * 扫码成功页
     * @param content
     * @param
     */
    private void gotoMeetDetail(JSONObject content) throws JSONException {
        Intent intent = new Intent(BridgeActivity.this, QRSuccessActivity.class);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", content.optString("name"));
        jsonObject1.put("address", content.optString("address"));
        jsonObject1.put("beginDate", content.optString("beginDate"));
        jsonObject1.put("feastConfirmFlag", content.optString("feastConfirmFlag"));
        jsonObject1.put("ddindex", tab);
        jsonObject1.put("meetPlace", meetPlace);
        intent.putExtra("name", content.optString("name"));
        intent.putExtra(WebPagerActivity.MEETTING_PARAMS, jsonObject1.toString());
        intent.putExtra("meetID", meetID);
        startActivity(intent);
    }

    /**
     * 查询签到信息
     */
    private void querySignupInfo(String mobile, String meetName){
        if (mLoadingDialog == null) {
            mLoadingDialog = new ClubLoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        final Map<String, String> map = new HashMap<>();
        map.put("meetId", meetID);
        map.put("mobile",mobile);
        addTag(NetConstants.QR_FOR_SIGNUP);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QR_FOR_SIGNUP, NetConstants.QR_FOR_SIGNUP, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        //跳转到扫码成功页
                        JSONObject content = jsonObject.optJSONObject("content");
                        if (content != null && content.length() == 0) {
                            if(LoginUtils.isOrgUser()){
                                gotoSignup(meetID);
                                finish();
                            }else{
                                getOrgManager(meetName);
                            }
                        }else{
                            showNoticeDialog("已报名");
                        }
                    } else {
                        showNoticeDialog(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                showNoticeDialog("请扫描鑫合家园相关二维码!");
            }
        });
    }

    /**
     * 跳转报名页
     */
    private void gotoSignup(String meetId){
        Intent intent = new Intent(this, WebPagerActivity.class);
        intent.putExtra(WebPagerActivity.LOAD_URL, "MSignUpFirst?meetId="+meetId);
        startActivity(intent);
    }
    /***
     * 获取机构联系人,获取到多个时只取第一个
     */
    private void getOrgManager(String meetName){
        String TAG = "query_org_manamger";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("orgId", PerferenceUtils.get(AppConstants.User.ORGID, ""));
        map.put("roleId", "org_manager");
        String url = NetConstants.QUERY_ORG_MANAGER;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONArray content = json.optJSONArray("content");
                        if(content != null && content.length() > 0){
                            showSignUpDialog(meetName, content.getJSONObject(0).optString("userName"));
                        }else{

                            showSignUpDialog(meetName, "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(BridgeActivity.this, errorMsg);
            }
        });
    }

    /**
     * 向机构联系人发短信弹框
     */
    private void showSignUpDialog(String meetName, String orgName){
        ClubDialog dialog = new ClubDialog(this);
        dialog.setMessage("您非贵行机构联系人，请联系贵行机构联系人"+orgName+"报名,如需变更机构联系人请联系会务组");
        dialog.setTitle("温馨提示");
        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                sendSMSToOrgManager(meetName);
            }
        });
        dialog.setNegativeListener("取消", new NegativeListener() {
            @Override
            public void onNegativeClick() {
                finish();
            }
        });
        dialog.show();
    }

    /**
     * 向机构联系人发送短信
     * @param meetName
     */
    private void sendSMSToOrgManager(String meetName){
        String TAG = "send_sms_to_org_manager";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("orgId", PerferenceUtils.get(AppConstants.User.ORGID, ""));
        map.put("mobile", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        map.put("meetName", meetName);
        map.put("department", PerferenceUtils.get(AppConstants.User.DEPARTMENT, ""));
        map.put("userName", PerferenceUtils.get(AppConstants.User.NAME, ""));
        String url = NetConstants.SEND_SMS_TO_ORG_MANAGER;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        ToastUtils.shortToast(BridgeActivity.this, "短信发送成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(BridgeActivity.this, errorMsg);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mCallBack = null;
        super.onDestroy();
    }
}
