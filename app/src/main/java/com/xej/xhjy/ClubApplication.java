package com.xej.xhjy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.mob.MobSDK;
import com.mob.moblink.MobLink;
import com.mob.moblink.RestoreSceneListener;
import com.mob.moblink.Scene;
import com.netease.meetinglib.sdk.NECallback;
import com.netease.meetinglib.sdk.NEMeetingError;
import com.netease.meetinglib.sdk.NEMeetingSDK;
import com.netease.meetinglib.sdk.NEMeetingSDKConfig;
import com.netease.meetinglib.sdk.config.NEForegroundServiceConfig;
import com.netease.nim.uikit.impl.preference.IMCache;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xej.xhjy.common.base.BaseActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.smtt.sdk.QbSdk;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.im.NimSDKOptionConfig;
import com.xej.xhjy.im.PushContentProvider;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.session.SessionHelper;
import com.xej.xhjy.sharesdk.RestoreSenceActivity;
import com.xej.xhjy.ui.login.LoginActivity;

import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class ClubApplication extends Application {
    private ClubApplication mApplication;
    private static Context context;
//    private RefWatcher refWatcher;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        context = getApplicationContext();
        //内存溢出检查
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
        LogUtils.isShowLog = AppConstants.IS_DEBUG;
        init();
    }

    //    /**
//     * 检查代码是否有没存泄漏方法
//     *
//     * @param context
//     * @return
//     */
//    public static RefWatcher getRefWatcher(Context context) {
//        ClubApplication application = (ClubApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
    private void init() {
        try{
            AppConstants.STATUS_BAR_HEIGHT = GenalralUtils.getStatusBarHeight(mApplication);
            PerferenceUtils.init(mApplication);
            //登录信息
            AppConstants.USER_ID = PerferenceUtils.get(AppConstants.User.ID, "");
            AppConstants.IS_LOGIN = !TextUtils.isEmpty(AppConstants.USER_ID);
            AppConstants.USER_STATE = PerferenceUtils.get(AppConstants.User.STATE, "");
            AppConstants.USER_ROLES = PerferenceUtils.get(AppConstants.User.ROLES, "");
            initTBS();
            initRxHttpClint();
            initIM();
            initMeetingSDK();
            //极光推送
            JPushInterface.setDebugMode(AppConstants.IS_DEBUG);
            JPushInterface.init(mApplication);

            //注册到微信
            initWX();
            //注册sharesdk
            MobSDK.init(this, AppConstants.SSDK_APP_ID, AppConstants.SSDK_APP_SECRET);
            MobLink.setRestoreSceneListener(new SceneListener());
            CrashReport.initCrashReport(getApplicationContext(), "132058b33e", false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 初始化网络请求配置
     */
    private void initRxHttpClint() {
        try{
            RxHttpClient.initHttpClient(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 初始化TBS浏览服务X5内核
     */
    private void initTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.setDownloadWithoutWifi(true);//非wifi条件下允许下载X5内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean isSuccess) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                if (AppConstants.IS_DEBUG) {
//                    if (isSuccess) {
//                        ToastUtils.shortToast(getApplicationContext(), "X5内核初始化成功！");
//                    } else {
//                        ToastUtils.shortToast(getApplicationContext(), "X5内核初始化失败！");
//                    }
//                }
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化云信相关信息
     */
    private void initIM() {
        // 初始化云信SDK
        LoginInfo loginInfo = getLoginInfo();
        IMCache.setContext(this);
        NIMClient.init(this, loginInfo, NimSDKOptionConfig.getSDKOptions(this));
        //必须主线程中初始化
        if (NIMUtil.isMainProcess(this)) {
            NimUIKit.init(this);
            NimUIKit.setCustomPushContentProvider(new PushContentProvider());
            SessionHelper.init();
            //initAVChatKit();
        }
    }

    private void initMeetingSDK(){
        NEMeetingSDKConfig config = new NEMeetingSDKConfig();
        //会议AppKey
        config.appKey = AppConstants.MEETING_APP_KEY;
        config.appName = context.getString(R.string.app_name);
        config.reuseNIM = true;
        //配置会议时显示前台服务
        NEForegroundServiceConfig foregroundServiceConfig = new NEForegroundServiceConfig();
        foregroundServiceConfig.contentTitle = context.getString(R.string.app_name);
        config.foregroundServiceConfig = foregroundServiceConfig;
        NEMeetingSDK.getInstance().initialize(this, config, new NECallback<Void>() {
            @Override
            public void onResult(int resultCode, String resultMsg, Void resultData) {
                if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                    //TODO when initialize success
                    Log.d("lirong", "succ");
                    String account_id = PerferenceUtils.get(AppConstants.User.MEETING_ACCOUNT_ID, "");
                    String account_token = PerferenceUtils.get(AppConstants.User.MEETING_ACCOUNT_TOKEN, "");
                    if(!TextUtils.isEmpty(account_id) && !TextUtils.isEmpty(account_token)){
                        meetingSDKLogin(account_id, account_token);
                    }
                } else {
                    //TODO when initialize fail
                    Log.d("lirong", "fail");
                }
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
     * 获取云信账号信息
     *
     * @return
     */
    public static LoginInfo getLoginInfo() {
        String accId = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
        String token = PerferenceUtils.get(AppConstants.User.IM_CHAT_TOKEN, "");
        NimUIKit.setAccount(accId);
        IMCache.setAccount(accId.toLowerCase());
        LogUtils.dazhiLog("自动登录获取云信accId-------->" + accId);
        if (!TextUtils.isEmpty(accId) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(accId, token);
        } else {
            return null;
        }
    }

    private void initWX(){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        IWXAPI api = WXAPIFactory.createWXAPI(this, AppConstants.APPID, true);

        // 将应用的appId注册到微信
        api.registerApp(AppConstants.APPID);
    }

    class SceneListener extends Object implements RestoreSceneListener {

        @Override
        public Class<? extends Activity> willRestoreScene(Scene scene) {
            return RestoreSenceActivity.class;
        }

        @Override
        public void completeRestore(Scene scene) {
        }

        @Override
        public void notFoundScene(Scene scene) {
        }
    }
}
