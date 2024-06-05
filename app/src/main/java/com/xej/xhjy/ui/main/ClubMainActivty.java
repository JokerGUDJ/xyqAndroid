package com.xej.xhjy.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.netease.meetinglib.sdk.NECallback;
import com.netease.meetinglib.sdk.NEMeetingError;
import com.netease.meetinglib.sdk.NEMeetingSDK;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.yunxin.nertc.model.ProfileManager;
import com.netease.yunxin.nertc.model.UserModel;
import com.netease.yunxin.nertc.nertcvideocall.model.NERTCVideoCall;
import com.netease.yunxin.nertc.nertcvideocall.model.UIService;
import com.netease.yunxin.nertc.nertcvideocall.model.VideoCallOptions;
import com.netease.yunxin.nertc.ui.NERTCVideoCallActivity;
import com.netease.yunxin.nertc.ui.team.TeamG2Activity;
import com.xej.xhjy.ClubApplication;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.UserBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.http.RxHttpManager;
import com.xej.xhjy.common.safe.SecureManager;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.DeviceUtil;
import com.xej.xhjy.common.utils.DeviceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.receiver.NetWorkStatusEvent;
import com.xej.xhjy.receiver.NetworkChangeReceiver;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.home.HomeFragment;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginFailedEvent;
import com.xej.xhjy.ui.mine.MineFragment;
import com.xej.xhjy.ui.society.MessageActivity;
import com.xej.xhjy.ui.society.SocietyFragment;
import com.xej.xhjy.ui.view.HomeBottomTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * @author dazhi
 * @class ClubMainActivty 全局首页Acitity
 * @Createtime 2018/6/13 10:30
 * @description viewpager 和 framgent布局
 * @Revisetime
 * @Modifier
 */
@SuppressWarnings("unchecked")
public class ClubMainActivty extends BaseActivity implements View.OnClickListener {
    public static final String JPUSH_CICK = "jpush_click_key";
    public static final String MESSAGETYPE = "message_type";
    @BindView(R.id.vp_home)
    ViewPager mViewPager;

    @BindView(R.id.home_tab)
    HomeBottomTabView mHomeTab;

//    @BindView(R.id.metting_tab)
//    HomeBottomTabView mMettingTab;

    @BindView(R.id.course_tab)
    HomeBottomTabView mCourseTab;

    @BindView(R.id.mine_tab)
    HomeBottomTabView mMineTab;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<HomeBottomTabView> mTabs = new ArrayList<>();
    private LoginCallBack mCallBack;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //权限获取之后才能拿到值，所以在此初始化
        AppConstants.DEVICEID = DeviceUtil.getIMEI(mActivity);
        AppConstants.APP_VERSION = UpdateUtils.getVerName(mActivity);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_club_main);
        //不保留后台活动，从厂商推送进聊天页面，会无法退出聊天页面
        if (savedInstanceState == null && parseIntent()) {
            return;
        }
        if (!GenalralUtils.isNetWorkConnected(mActivity)) {
            AppConstants.IS_NETWORK = false;
            ToastUtils.longToast(mActivity, "网络连接失败，请检查网络后再尝试！");
        }

        //动态注册监听网络状态变化广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        if (AppConstants.IS_LOGIN) {
            getUserInfo();
        }
        initG2();
        initView();
        getJobList();
        setNextStep(getIntent());
        if (AppConstants.IS_LOGIN) {
            MainDialogShowUtils.ShowDialog(this);
        }
        //校验更新版本
        if (!AppConstants.IS_DEBUG) {
            if (SecureManager.isEmulator(ClubMainActivty.this)) {
                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
                dialog.setMessage("您正在使用模拟器运行，为您的数据安全请使用真机运行");
                dialog.setPositiveListener("确定", new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                });
                dialog.show();
            }

            if (DeviceUtils.isRoot()) {
                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(ClubMainActivty.this);
                dialog.setMessage("您的手机已经Root，存在安全隐患，请谨慎使用！");
                dialog.show();
            }

            LoginUtils.check(mActivity);
            UpdateUtils.checkUpdate(ClubMainActivty.this);
        }
    }

    private void initG2(){
        try{
            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
                @Override
                public void onEvent(StatusCode statusCode) {
                    if (statusCode == StatusCode.LOGINED) {
                        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(this, false);

                        // TODO G2 用户根据实际配置方式获取
                        LoginInfo loginInfo = ClubApplication.getLoginInfo();
                        if (loginInfo == null) {
                            return;
                        }

                        String imAccount = loginInfo.getAccount();
                        String imToken = loginInfo.getToken();

                        ApplicationInfo appInfo = null;
                        try {
                            // TODO G2 用户根据实际配置方式获取
                            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                            String appKey = appInfo.metaData.getString("com.netease.nim.appKey");

                            NERTCVideoCall.sharedInstance().setupAppKey(getApplicationContext(), appKey, new VideoCallOptions(null, new UIService() {
                                @Override
                                public Class getOneToOneAudioChat() {
                                    return NERTCVideoCallActivity.class;
                                }

                                @Override
                                public Class getOneToOneVideoChat() {
                                    return NERTCVideoCallActivity.class;
                                }

                                @Override
                                public Class getGroupVideoChat() {
                                    return TeamG2Activity.class;
                                }

                                @Override
                                public int getNotificationIcon() {
                                    return R.drawable.ic_launcher;
                                }

                                @Override
                                public int getNotificationSmallIcon() {
                                    return R.drawable.ic_launcher;
                                }

                                @Override
                                public void startContactSelector(Context context, String teamId, List<String> excludeUserList, int requestCode) {

                                }
                            }, ProfileManager.getInstance()));

                            NERTCVideoCall.sharedInstance().login(imAccount, imToken, new RequestCallback<LoginInfo>() {
                                @Override
                                public void onSuccess(LoginInfo param) {

                                }

                                @Override
                                public void onFailed(int code) {

                                }

                                @Override
                                public void onException(Throwable exception) {

                                }
                            });

                            //注册获取token的服务
                            //在线上环境中，token的获取需要放到您的应用服务端完成，然后由服务器通过安全通道把token传递给客户端
                            //Demo中使用的URL仅仅是demoserver，不要在您的应用中使用
                            //详细请参考: http://dev.netease.im/docs?doc=server
                            NERTCVideoCall.sharedInstance().setTokenService((uid, callback) -> {
                                String demoServer = "https://nrtc.netease.im/demo/getChecksum.action";
                                new Thread(() -> {
                                    try {
                                        String queryString = demoServer + "?uid=" +
                                                uid + "&appkey=" + appKey;
                                        URL requestedUrl = new URL(queryString);
                                        HttpURLConnection connection = (HttpURLConnection) requestedUrl.openConnection();
                                        connection.setRequestMethod("POST");
                                        connection.setConnectTimeout(6000);
                                        connection.setReadTimeout(6000);
                                        if (connection.getResponseCode() == 200) {
                                            String result = readFully(connection.getInputStream());
                                            if (!TextUtils.isEmpty(result)) {
                                                org.json.JSONObject object = new org.json.JSONObject(result);
                                                int code = object.getInt("code");
                                                if (code == 200) {
                                                    String token = object.getString("checksum");
                                                    if (!TextUtils.isEmpty(token)) {
                                                        new Handler(getMainLooper()).post(() -> {
                                                            callback.onSuccess(token);
                                                        });
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    new Handler(getMainLooper()).post(() -> {
                                        //fixme 此处因为demo可以走非安全模式所以返回null，线上环境请在此处走 onFailed 逻辑
                                        callback.onSuccess(null);
//                                    callback.onFailed(-1);
                                    });
                                }).start();
                            });

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, true);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String readFully(InputStream inputStream) throws IOException {

        if (inputStream == null) {
            return "";
        }

        ByteArrayOutputStream byteArrayOutputStream;

        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();

            final byte[] buffer = new byte[1024];
            int available;

            while ((available = bufferedInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer, 0, available);
            }

            return byteArrayOutputStream.toString();

        } finally {
            bufferedInputStream.close();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
        setNextStep(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstants.IS_LOGIN) {
            hasNewMessage();
        }
    }

    private void initView() {
        mTabs.add(mHomeTab);
        //mTabs.add(mMettingTab);
        mTabs.add(mCourseTab);
        mTabs.add(mMineTab);
        for (HomeBottomTabView tabView : mTabs) {
            tabView.setOnClickListener(this);
        }
        mHomeTab.setIconAndText(R.drawable.ic_tab_home_check, R.drawable.ic_tab_home_normal, "首页");
        mHomeTab.setCheck(true);
        //mMettingTab.setIconAndText(R.drawable.ic_tab_metting_check, R.drawable.ic_tab_metting_normal, "鑫合会议");
        mCourseTab.setIconAndText(R.drawable.ic_tab_course_check, R.drawable.ic_tab_course_normal, "鑫合圈");
        mMineTab.setIconAndText(R.drawable.ic_tab_mine_check, R.drawable.ic_tab_mine_normal, "我的");
        HomeFragment mHomeFramgent = new HomeFragment();
        SocietyFragment mCourseFramgent = new SocietyFragment();
        MineFragment mMineFramgent = new MineFragment();
        mFragments.add(mHomeFramgent);
        //mFragments.add(mMettingFramgent);
        mFragments.add(mCourseFramgent);
        mFragments.add(mMineFramgent);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabs.size(); i++) {
                    if (position == i) {
                        mTabs.get(i).setCheck(true);
                    } else {
                        mTabs.get(i).setCheck(false);
                    }
                    if(position == 0){
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }else{
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    if(i == 0){
                        //首页埋点
                        EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                                "eventTabHome", "");
                    }else if(i == 1){
                        //鑫合圈埋点
                        EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                                "eventTabCircle", "");
                    }else if(i == 2){
                        //我的埋点
                        EventTrackingUtil.EventTrackSubmit(mActivity, "pageMy", "channel=android",
                                "eventTabMy", "");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //页面切换动画
        mViewPager.setPageTransformer(true, new AccordionTransformer());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    private void setNextStep(Intent intent) {
        int step = intent.getIntExtra(JPUSH_CICK, -1);
        if (step == AppConstants.JpushClick.GO_MESSAGE) {//跳到消息
//            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
//                @Override
//                public void onlogin() {
//                    //推送过来的消息，直接切到平台消息页签
//                    Intent intent = new Intent(mActivity, MessageActivity.class);
//                    intent.putExtra(MESSAGETYPE, true);
//                    mActivity.startActivityWithAnim(intent);
//                }
//            }, mActivity);
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    //推送过来的消息，直接切到平台消息页签
                    Intent intent = new Intent(mActivity, MessageActivity.class);
                    intent.putExtra(MESSAGETYPE, true);
                    mActivity.startActivityWithAnim(intent);
                }
            };
            mCallBack.isPass = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        }
    }

    /**
     * 职位列表
     */
    private void getJobList() {
        String TAG = "JOB_LIST";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.JOB_LIST, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
//                LogUtils.dazhiLog("职务列表---》" + jsonString);
                PerferenceUtils.put(AppConstants.DATA_JOB_LIS_KEY, jsonString);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    /**
     * 是否有新消息
     */
    private void hasNewMessage() {
        String TAG = "main_new_message";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.POST_NEW_MESSAGE, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        JSONArray jsonArray = jsonObject.optJSONArray("content");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            EventBus.getDefault().post(new HasMessageEvent(true));
                            AppConstants.HAS_NEW_MESSAGE = true;
                        } else {
                            EventBus.getDefault().post(new HasMessageEvent(false));
                            AppConstants.HAS_NEW_MESSAGE = false;
                        }
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


    @Override
    public void onClick(View v) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (v == mTabs.get(i)) {
                mTabs.get(i).setCheck(true);
                mViewPager.setCurrentItem(i);
            } else {
                mTabs.get(i).setCheck(false);
            }

        }
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
                LogUtils.dazhiLog("个人信息----" + jsonString);
                UserBean bean = JsonUtils.stringToObject(jsonString, UserBean.class);
                if (bean != null && "0".equals(bean.getCode()) && bean.getContent() != null) {
                    PerferenceUtils.put(AppConstants.User.NAME, bean.getContent().getUserName());
                    PerferenceUtils.put(AppConstants.User.COMPLANY, bean.getContent().getOrgInfo().getOrgName());
                    PerferenceUtils.put(AppConstants.User.PHONE, bean.getContent().getMobilephone());
                    PerferenceUtils.put(AppConstants.User.GENDER, bean.getContent().getGender());
                    PerferenceUtils.put(AppConstants.User.TEL, bean.getContent().getPhone());
                    PerferenceUtils.put(AppConstants.User.JOB, bean.getContent().getJobTitle().getJobName());
                    PerferenceUtils.put(AppConstants.User.EMAIL, bean.getContent().getEmail());
                    PerferenceUtils.put(AppConstants.User.ORGID, bean.getContent().getOrgId());
                    PerferenceUtils.put(AppConstants.User.ADDRESS, bean.getContent().getAddr());
                    PerferenceUtils.put(AppConstants.User.DEPARTMENT, bean.getContent().getDivInfo().getDivName());
                    AppConstants.USER_STATE = bean.getContent().getUserState();
                    PerferenceUtils.put(AppConstants.User.STATE, AppConstants.USER_STATE);
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("用户信息失败----》" + errorMsg);
            }
        });
    }

    /**
     * 注册Jpush用户信息
     */
    private void jpushRigsiter() {
        String tag33 = "JPUSH_REGITST";
        addTag(tag33);
        Map<String, String> map = new HashMap<>();
        LogUtils.dazhiLog("pushid-----------" + JPushInterface.getRegistrationID(getApplicationContext()));
        map.put("registrationId", JPushInterface.getRegistrationID(getApplicationContext()));
        map.put("module", "1");
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.JPUSH_REGITST, tag33, map, new HttpCallBack() {

            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("jpush成功----》" + jsonString);
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("用户信息失败----》" + errorMsg);
            }
        });
    }


    /**
     * 获取登录云信，只登陆一次
     */
    public static AbortableFuture<LoginInfo> loginRequest;

    public void doLogin() {
        String accId = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
        String token = PerferenceUtils.get(AppConstants.User.IM_CHAT_TOKEN, "");
        LogUtils.dazhiLog("ClubMain获取云信账号------" + accId);
        doLoginForIM(accId, token);
        //doLoginForMeeting(accId, token);
    }

    /**
     *  IM登录鉴权
     */
    private  void doLoginForIM(String accId, String token){
        loginRequest = NimUIKit.login(new LoginInfo(accId, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtils.dazhiLog("聊天登录成功---账号=" + param.getAccount());
                NimUIKit.setAccount(accId);
                ProfileManager.getInstance().setLogin(true);
                NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
                    @Override
                    public void onEvent(LoginSyncStatus loginSyncStatus) {
                        if(loginSyncStatus == LoginSyncStatus.SYNC_COMPLETED){
                            UserModel userModel = new UserModel();
                            userModel.imAccid = accId;
                            userModel.imToken = token;
                            NimUserInfo userInfo = NIMClient.getService(UserService.class).getUserInfo(accId);
                            userModel.nickname = userInfo == null?"":userInfo.getName();
                            LogUtils.dazhiLog("nickname" + userModel.nickname );
                            ProfileManager.getInstance().setUserModel(userModel);
                        }
                    }
                }, true);
            }

            @Override
            public void onFailed(int code) {
                if (code == 302) {
                    ToastUtils.shortToast(mActivity, "聊天登录失败");
                }
                LogUtils.dazhiLog("聊天登录失败----onFailed" + code);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.dazhiLog("聊天登录失败---exception" + exception);
            }
        });
    }
    /**
     *  会议sdk登录鉴权
     */
    private  void doLoginForMeeting(String accId, String token){
        NEMeetingSDK.getInstance().login(accId, token, new NECallback<Void>() {
            @Override
            public void onResult(int resultCode, String resultMsg, Void result) {
                if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                    //登录成功
                } else {
                    //登录失败
                }
            }
        });
    }

    private long mexit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mexit) > 2000) {
                mexit = System.currentTimeMillis();
                ToastUtils.shortToast(this, "再按一次退出应用程序");
            } else {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 收到返回首页的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(BackToHomeEvent event) {
        mViewPager.setCurrentItem(0);
    }

    /**
     * 收到网络状态变化消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkEvent(NetWorkStatusEvent event) {
        getJobList();
        if (AppConstants.IS_LOGIN) {
            getUserInfo();
            hasNewMessage();
        }
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        LogUtils.dazhiLog("收到登录消息==========");
        jpushRigsiter();
        doLogin();
        //MainDialogShowUtils.ShowDialog(ClubMainActivty.this);
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
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailedEventMainThread(LoginFailedEvent event) {
        mCallBack = null;
    }


    /**
     * 收到JPUSH消息后查询平台消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailedEventMainThread(RecivedJpusheEvent event) {
        LogUtils.dazhiLog("收到jpush消息做请求");
        hasNewMessage();
    }


    /**
     * 通知跳转到群聊或单聊
     */

    private boolean parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            LogUtils.dazhiLog("NimIntent---" + NimIntent.EXTRA_NOTIFY_CONTENT);
            ArrayList<IMMessage> list = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            //云信通知栏消息处理
            if (list != null && list.size() > 0) {
                IMMessage message = list.get(0);
                switch (message.getSessionType()) {
                    case P2P:
                        NimUIKit.startP2PSession(this, message.getSessionId());
                        break;
                    case Team:
                        NimUIKit.startTeamSession(this, message.getSessionId());
                        break;
                    default:
                        break;
                }
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkChangeReceiver);
        EventBus.getDefault().unregister(this);
        LogUtils.dazhiLog("ClubMainActivty已经注销EventBus");
        //清除所有网络请求
        RxHttpManager.cancelAll();
        super.onDestroy();
    }
}
