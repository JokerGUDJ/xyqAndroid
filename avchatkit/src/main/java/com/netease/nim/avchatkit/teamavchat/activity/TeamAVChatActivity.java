package com.netease.nim.avchatkit.teamavchat.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.R;
import com.netease.nim.avchatkit.TeamAVChatProfile;
import com.netease.nim.avchatkit.anim.AnimUtils;
import com.netease.nim.avchatkit.common.activity.UI;
import com.netease.nim.avchatkit.common.permission.MPermission;
import com.netease.nim.avchatkit.common.permission.annotation.OnMPermissionDenied;
import com.netease.nim.avchatkit.common.permission.annotation.OnMPermissionGranted;
import com.netease.nim.avchatkit.common.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nim.avchatkit.common.recyclerview.decoration.SpacingDecoration;
import com.netease.nim.avchatkit.common.util.ScreenUtil;
import com.netease.nim.avchatkit.config.AVChatConfigs;
import com.netease.nim.avchatkit.config.AVPrivatizationConfig;
import com.netease.nim.avchatkit.constant.CustomMsgType;
import com.netease.nim.avchatkit.controll.AVChatSoundPlayer;
import com.netease.nim.avchatkit.service.FloatVideoWindowService;
import com.netease.nim.avchatkit.teamavchat.Receiver.PhoneCallReceiver;
import com.netease.nim.avchatkit.teamavchat.TeamAVChatNotification;
import com.netease.nim.avchatkit.teamavchat.TeamAVChatVoiceMuteDialog;
import com.netease.nim.avchatkit.teamavchat.adapter.TeamAVChatAdapter;
import com.netease.nim.avchatkit.teamavchat.module.SimpleAVChatStateObserver;
import com.netease.nim.avchatkit.teamavchat.module.TeamAVChatItem;

import com.netease.nim.avchatkit.teamavchat.view.MyLayoutManager;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.utils.ImUtils;
import com.netease.nim.avchatkit.teamavchat.view.PagerGridLayoutManager;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItemFilter;
import com.netease.nim.uikit.business.contact.core.model.IContact;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.impl.preference.IMCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatServerRecordMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQualityStrategy;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.video.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.video.AVChatVideoCapturer;
import com.netease.nimlib.sdk.avchat.video.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nrtc.video.render.IVideoRender;
import com.xej.xhjy.common.http.HttpServer;
import com.xej.xhjy.common.http.interfaces.HttpCallBack;
import com.xej.xhjy.common.service.AppService;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.Dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.common.view.marqueeview.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.github.prototypez.appjoint.AppJoint;

import static com.netease.nim.avchatkit.teamavchat.module.TeamAVChatItem.TYPE.TYPE_DATA;

/**
 * 多人音视频界面：包含音视频通话界面和接受拒绝界面
 * Created by huangjun on 2017/5/3.
 * <p>互动直播/多人会议视频通话流程示例
 * <ol>
 * <li>主播或者管理员创建房间 {@link AVChatManager#createRoom(String, String, AVChatCallback)}。 创建房间仅仅是在服务器预留一个房间名，房间未使用时有效期为30天，使用后的房间在所有用户退出后回收。</li>
 * <li>注册音视频模块监听 {@link AVChatManager#observeAVChatState(AVChatStateObserverLite, boolean)} 。</li>
 * <li>开启音视频引擎， {@link AVChatManager#enableRtc()}。 </li>
 * <li>设置互动直播模式，设置互动直播推流地址 [仅限互动直播] {@link AVChatParameters#KEY_SESSION_LIVE_MODE}, {@link AVChatParameters#KEY_SESSION_LIVE_URL}。</li>
 * <li>打开视频模块 {@link AVChatManager#enableVideo()}。</li>
 * <li>设置本地预览画布 {@link AVChatManager#setupLocalVideoRender(IVideoRender, boolean, int)} 。</li>
 * <li>设置视频通话可选参数[可以不设置] {@link AVChatManager#setParameter(AVChatParameters.Key, Object)}, {@link AVChatManager#setParameters(AVChatParameters)}。</li>
 * <li>创建并设置本地视频预览源 {@link AVChatVideoCapturerFactory#createCameraCapturer(boolean)}, {@link AVChatManager#setupVideoCapturer(AVChatVideoCapturer)}</li>
 * <li>打开本地视频预览 {@link AVChatManager#startVideoPreview()}。</li>
 * <li>加入房间 {@link AVChatManager#joinRoom2(String, AVChatType, AVChatCallback)}。</li>
 * <li>开始多人会议或者互动直播，以及各种音视频操作。</li>
 * <li>关闭本地预览 {@link AVChatManager#stopVideoPreview()} 。</li>
 * <li>关闭本地预览 {@link AVChatManager#disableVideo()} ()} 。</li>
 * <li>离开会话 {@link AVChatManager#leaveRoom2(String, AVChatCallback)}。</li>
 * <li>关闭音视频引擎, {@link AVChatManager#disableRtc()}。</li>
 * </ol></p>
 */

public class TeamAVChatActivity extends UI implements PagerGridLayoutManager.PageListener, ModuleProxy{
    private static final String KEY_RECEIVED_CALL = "call";
    private static final String KEY_TEAM_ID = "teamid";
    private static final String KEY_ROOM_ID = "roomid";
    private static final String KEY_ACCOUNTS = "accounts";
    private static final String KEY_TNAME = "teamName";
    private static final String KEY_TNICK = "teamNick";
    private static final String KEY_HEADIMGURL = "headImgUrl";
    private static final String KEY_ISSHOWSCREEN = "sharescreen";
    private static final String KEY_SHARE_ACCID = "shareaccid";
    private static final String KEY_ISFIRSTMEET = "isfirstmeet";
    private static final int AUTO_REJECT_CALL_TIMEOUT = 45 * 1000;
    private static final int CHECK_RECEIVED_CALL_TIMEOUT = 45 * 1000;
    private static final int BASIC_PERMISSION_REQUEST_CODE = 0x100;
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1002;
    private static final int CONTACT_REQUEST_CODE = 0x21;
    private static final int MANAGE_OVERLAY_PERMISSION = 1003;

    // DATA
    private String teamId;
    private String roomId;
    private String shareAccId;
    private long chatId;
    private ArrayList<String> accounts;
    private boolean receivedCall;
    private boolean destroyRTC;
    private boolean bRefuse = false;//是否自己取消音视频通话
    private String teamName, teamNick, headImgUrl;

    // CONTEXT
    private Handler mainHandler;

    // LAYOUT
    private View callLayout;
    private View surfaceLayout;

    // VIEW
    private RecyclerView recyclerView;
    private TeamAVChatAdapter adapter;
    private List<TeamAVChatItem> data;
    private View voiceMuteButton;

    // TIMER
    private Timer timer;
    private int seconds;
    private TextView timerText;
    private Runnable autoRejectTask;

    // CONTROL STATE
    boolean videoMute = true;
    boolean microphoneMute = false;
    boolean speakerMode = true;
    boolean shareMode = false;//开启共享屏幕
    boolean isShare = false;//有共享
    boolean otherShare = false;


    // AVCAHT OBSERVER
    private AVChatStateObserver stateObserver;
    private Observer<AVChatControlEvent> notificationObserver;
    private AVChatCameraCapturer mVideoCapturer;
    private AVChatVideoCapturer videoCapturer;
    private static boolean needFinish = true;

    private TeamAVChatNotification notifier;
    private ImageView zoom;
    private ImageView invite;
    private PageIndicatorView pageIndicatorView;
    private ServiceConnection mVideoServiceConnection;
    private String meetId;

    /**
     * 点击屏幕显示隐藏功能按钮
     */
    private RelativeLayout avchat_setting_top_layout;
    private ConstraintLayout avchat_setting_layout;
    private TextView timer_text;
    private MyLayoutManager mGridLayoutManager;
    private int mVisiable = View.INVISIBLE;
    private boolean isFirstMeet;
    /**
     * 来电响铃的监听
     */
    private PhoneCallReceiver phoneCallReceiver;
    /**
     * 来电响铃监听是否已注册
     */
    private boolean hasRegisterPhoneCall = false;


    public static void startActivity(Context context, boolean receivedCall, String teamId, String roomId,
                                     ArrayList<String> accounts, String teamName, String teamNick, String headImgUrl, boolean isshow, String shareAccId, boolean isFirstMeet) {
        needFinish = false;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(context, TeamAVChatActivity.class);
        intent.putExtra(KEY_RECEIVED_CALL, receivedCall);
        intent.putExtra(KEY_ROOM_ID, roomId);
        intent.putExtra(KEY_TEAM_ID, teamId);
        intent.putExtra(KEY_ACCOUNTS, accounts);
        intent.putExtra(KEY_TNAME, teamName);
        intent.putExtra(KEY_TNICK, teamNick);
        intent.putExtra(KEY_HEADIMGURL, headImgUrl);
        intent.putExtra(KEY_ISSHOWSCREEN, isshow);
        intent.putExtra(KEY_SHARE_ACCID, shareAccId);
        intent.putExtra(KEY_ISFIRSTMEET, isFirstMeet);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (needFinish) {
            finish();
            return;
        }

        dismissKeyguard();
        setContentView(R.layout.team_avchat_activity);
        onInit();
        onIntent();
        initNotification();
        findLayouts();
        showViews();
        setChatting(true);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotificationObserver, true);
        //注册电话监听接口
//        PhoneCallReceiver.registerNotify("TeamAVChatActivity", TeamAVChatActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 取消通知栏
        activeCallingNotifier(false);
        // 禁止自动锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        activeCallingNotifier(true);
//        registerPhoneCallReceiver(false);
    }

//    /**
//     * 监听来电响铃状态
//     */
//    public void registerPhoneCallReceiver(boolean register) {
//        if (register) {
//            if (phoneCallReceiver == null) {
//                LogUtils.dazhiLog("phoneCallReceiver为空");
//                phoneCallReceiver = new PhoneCallReceiver();
//                phoneCallReceiver.setOnOnPhoneCallListener(TeamAVChatActivity.this);
//
//            }
//            if (!hasRegisterPhoneCall) {
//                hasRegisterPhoneCall = true;
//                TeamAVChatActivity.this.registerReceiver(phoneCallReceiver, new IntentFilter());
//            }
//        } else {
//            if (phoneCallReceiver != null && hasRegisterPhoneCall) {
//                hasRegisterPhoneCall = false;
//                TeamAVChatActivity.this.unregisterReceiver(phoneCallReceiver);
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareMode = false;
        needFinish = true;
        if (timer != null) {
            timer.cancel();
        }

        if (stateObserver != null) {
            AVChatManager.getInstance().observeAVChatState(stateObserver, false);
        }

        if (notificationObserver != null) {
            AVChatManager.getInstance().observeControlNotification(notificationObserver, false);
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        hangup(); // 页面销毁的时候要保证离开房间，rtc释放。
        activeCallingNotifier(false);
        setChatting(false);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotificationObserver, false);
//        PhoneCallReceiver.removeNotify("TeamAVChatActivity");
    }


    /**
     * 收到共享、取消共享、邀请、传参等通知
     */

    private String accId;
    int p = 0;
    private Observer<CustomNotification> customNotificationObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification customNotification) {
            try {
                Map<String, Object> payload = customNotification.getPushPayload();
                int msgType = (int) payload.get("msgType");
                //有人发起屏幕共享
                if (msgType == CustomMsgType.MSG_START_SHARE_SCREEM.getValue()) {
                    //标记开启共享
                    accId = (String) payload.get("accId");
                    LogUtils.dazhiLog("共享通知accId=========" + accId);
                    isShare = true;
                    LogUtils.dazhiLog("收到共享人员总数======" + data.size());
                    for (int i = 0; i < data.size(); i++) {
                        //将shareFull状态设为true
                        if (data.get(i).account.equals(accId)) {
                            data.get(i).shareFull = true;
                            p = i;
                        }
//                        else {
//                            data.get(i).shareFull = false;
//                        }
                    }
                    adapter.notifyDataItemChanged(p);
                    LogUtils.dazhiLog("共享通知定位具体位置1--p=" + p);
                    LogUtils.dazhiLog("共享通知定位具体位置1=" + data.get(p).account);
//                    //解决定位问题
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mGridLayoutManager.scrollToPositionWithOffset(p, 0);
                        }
                    }, 700);
                    //解决静止滑动问题
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //静止滑动
                            mGridLayoutManager.setScrollEnabled(false);
                        }
                    }, 500);

                } else if (msgType == CustomMsgType.MSG_END_SHARE_SCREEM.getValue()) {
                    accId = (String) payload.get("accId");
                    //关闭本地视频预览
//                    if (videoMute){
//                        AVChatManager.getInstance().stopVideoPreview();
//                    }else{
//                        AVChatManager.getInstance().startVideoPreview();
//                    }
                    //关闭屏幕共享标记
                    isShare = false;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).account.equals(accId)) {
                            p = i;
                            data.get(i).shareFull = false;
                        }
                    }
                    adapter.notifyDataItemChanged(p);
                    //解除滑动
                    mGridLayoutManager.setScrollEnabled(true);
                } else if (msgType == CustomMsgType.MSG_ADDUSER.getValue()) {
                    Object listAccounts = payload.get("listAccounts");
                    ArrayList<String> list = (ArrayList<String>) listAccounts;
                    for (int i = 0; i < data.size(); i++) {
                        if (list.contains(data.get(i).account)) {
                            list.remove(data.get(i).account);
                        }
                    }
                    if (accounts != null) {
                        accounts.addAll(list);
                    }
                    if (list.size() > 0) {
                        for (String str : list) {
                            if (str.equals(AVChatKit.getAccount())) {
                                continue;
                            }
                            data.add(new TeamAVChatItem(TYPE_DATA, teamId, str, false));
                        }
                        adapter.notifyDataSetChanged();

                    }

                    LogUtils.dazhiLog("收到邀请人员总数======" + data.size());

                } else if (msgType == CustomMsgType.MSG_PARAMETER.getValue()) {
                    //单独传参数通知
                    String id = (String) payload.get("meetId");
                    if (!TextUtils.isEmpty(id)) {
                        meetId = id;
                        LogUtils.dazhiLog("meetId--------->" + meetId);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * ************************************ 初始化 ***************************************
     */

    // 设置窗口flag，亮屏并且解锁/覆盖在锁屏界面上
    private void dismissKeyguard() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
    }

    private void onInit() {
        mainHandler = new Handler(this.getMainLooper());
    }

    private void onIntent() {
        Intent intent = getIntent();
        receivedCall = intent.getBooleanExtra(KEY_RECEIVED_CALL, false);
        roomId = intent.getStringExtra(KEY_ROOM_ID);
        teamId = intent.getStringExtra(KEY_TEAM_ID);
        accounts = (ArrayList<String>) intent.getSerializableExtra(KEY_ACCOUNTS);
        teamName = intent.getStringExtra(KEY_TNAME);
        teamNick = intent.getStringExtra(KEY_TNICK);
        headImgUrl = intent.getStringExtra(KEY_HEADIMGURL);
        otherShare = intent.getBooleanExtra(KEY_ISSHOWSCREEN, false);//是否有屏幕共享
        shareAccId = intent.getStringExtra(KEY_SHARE_ACCID);//发起屏幕共享的accId
        isFirstMeet = intent.getBooleanExtra(KEY_ISFIRSTMEET, false);//是否为快速会议

    }

    private void findLayouts() {
        callLayout = findView(R.id.team_avchat_call_layout);
        surfaceLayout = findView(R.id.team_avchat_surface_layout);
        voiceMuteButton = findView(R.id.avchat_shield_user);
        avchat_setting_top_layout = findViewById(R.id.avchat_setting_top_layout);
        avchat_setting_layout = findViewById(R.id.avchat_setting_layout);
        timer_text = findViewById(R.id.timer_text);
        pageIndicatorView = findViewById(R.id.indicator);
        ImageView head = findView(R.id.img_head);
        AppService appService = AppJoint.service(AppService.class);
        appService.showHeadImagUrl(this, head, headImgUrl);
        TextView callee_name = findView(R.id.callee_name);
        //如果是高级群则显示群名称，如果是自建群则显示具体某个人
        Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (team != null) {
            LogUtils.dazhiLog("shareAccId====" + shareAccId);
            callee_name.setText(team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(teamId) : UserInfoHelper.getUserName(shareAccId));
        } else {
            callee_name.setText(teamNick);
        }
        //缩放
        zoom = findViewById(R.id.iv_avchat_zoom);
        //邀请
        invite = findViewById(R.id.iv_avchat_invitation);

        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收齐会议
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(TeamAVChatActivity.this)) {
                    //没有权限，需要申请权限，因为是打开一个授权页面，所以拿不到返回状态的，所以建议是在onResume方法中从新执行一次校验
                    ToastUtils.shortToast(TeamAVChatActivity.this, "需要开启显示悬浮窗权限");
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, MANAGE_OVERLAY_PERMISSION);
                } else {
                    startFloatVideoWindowService();
                }
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //邀请成员加入
                LogUtils.dazhiLog("teamId---------" + teamId);
                NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                    @Override
                    public void onResult(boolean success, Team team, int code) {
                        //扩展字段，为1则是快速会议创建的,ios传值为Extension
                        if (team != null && team.getExtServer().equals("1") || team.getExtension().equals("1")) {
                            Intent intent = new Intent(TeamAVChatActivity.this, com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.class);
                            intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, "1");
                            intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST, accounts);
                            intent.putExtra(ContactListSelectAtivity.ISVIDEO, true);
                            startActivityForResultWithAnim(intent, CONTACT_REQUEST_CODE);
                        } else {
                            ContactSelectActivity.startActivityForResult(TeamAVChatActivity.this, getContactSelectOption(teamId), accounts, CONTACT_REQUEST_CODE);
                        }

                    }
                });

            }
        });


    }

    private void startFloatVideoWindowService() {
        TeamAVChatActivity.super.moveTaskToBack(true);
        mVideoServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // 获取服务的操作对象
                FloatVideoWindowService.MyBinder binder = (FloatVideoWindowService.MyBinder) service;
                binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(TeamAVChatActivity.this, FloatVideoWindowService.class);//开启服务显示悬浮框
        intent.putExtra("seconds", seconds);
        bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
    }

    //
    private ContactSelectActivity.Option getContactSelectOption(String teamId) {
        ContactSelectActivity.Option option = new ContactSelectActivity.Option();
        option.type = ContactSelectActivity.ContactSelectType.TEAM_MEMBER;
        option.teamId = teamId;
        option.maxSelectNum = accounts.size();
        option.maxSelectNumVisible = true;
        option.title = TeamAVChatActivity.this.getString(com.netease.nim.uikit.R.string.invite_member);
        option.maxSelectedTip = TeamAVChatActivity.this.getString(com.netease.nim.uikit.R.string.reach_capacity);
        option.itemFilter = new ContactItemFilter() {
            @Override
            public boolean filter(AbsContactItem item) {
                IContact contact = ((ContactItem) item).getContact();
                // 过滤掉自己
                return contact.getContactId().equals(IMCache.getAccount());
            }
        };
        return option;
    }

    private void initNotification() {
        notifier = new TeamAVChatNotification(this);
        notifier.init(teamId, teamName);
    }

    /**
     * ************************************ 主流程 ***************************************
     */

    private void showViews() {
        if (receivedCall) {
            showReceivedCallLayout();
        } else {
            showSurfaceLayout();
        }
    }

    /*
     * 设置通话状态
     */
    private void setChatting(boolean isChatting) {
        TeamAVChatProfile.sharedInstance().setTeamAVChatting(isChatting);
    }

    /*
     * 接听界面
     */
    private void showReceivedCallLayout() {
        callLayout.setVisibility(View.VISIBLE);
        // 播放铃声
        AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.RING);

        // 拒绝
        callLayout.findViewById(R.id.refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bRefuse = true;
                AVChatSoundPlayer.instance().stop();
                cancelAutoRejectTask();
                finish();
            }
        });

        // 接听
        callLayout.findViewById(R.id.receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVChatSoundPlayer.instance().stop();
                cancelAutoRejectTask();
                callLayout.setVisibility(View.GONE);
                showSurfaceLayout();
            }
        });

        startAutoRejectTask();
    }

    /*
     * 通话界面
     */
    private void showSurfaceLayout() {
        // 列表
        surfaceLayout.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) surfaceLayout.findViewById(R.id.recycler_view);
        initRecyclerView();
        // 通话计时
        timerText = (TextView) surfaceLayout.findViewById(R.id.timer_text);
        // 控制按钮
        ViewGroup settingLayout = (ViewGroup) surfaceLayout.findViewById(R.id.avchat_setting_layout);
        for (int i = 0; i < settingLayout.getChildCount(); i++) {
            View v = settingLayout.getChildAt(i);
            if (v instanceof RelativeLayout) {
                ViewGroup vp = (ViewGroup) v;
                if (vp.getChildCount() == 2) {
                    vp.getChildAt(0).setOnClickListener(settingBtnClickListener);
                }
            }
        }
        /**
         * 挂断---由于挂断按钮的特殊性，直接在这里处理了
         */
        surfaceLayout.findViewById(R.id.hangup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hangup();
                finish();
            }
        });
        // 音视频权限检查
        checkPermission();
    }

    /**
     * ************************************ 点击事件 ***************************************
     */
    private View mView;
    private View.OnClickListener settingBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int i = v.getId();
            if (i == R.id.avchat_switch_camera) {// 切换前后摄像头（暂无）
                mVideoCapturer.switchCamera();

            } else if (i == R.id.avchat_enable_video) {// 开启视频
                if (videoMute) {
                    AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.PreferFrameRate);
                    AVChatManager.getInstance().startVideoPreview();
                } else {
                    AVChatManager.getInstance().stopVideoPreview();
                }
                AVChatManager.getInstance().muteLocalVideo(videoMute = !videoMute);
                // 发送控制指令
                byte command = videoMute ? AVChatControlCommand.NOTIFY_VIDEO_OFF : AVChatControlCommand.NOTIFY_VIDEO_ON;
                AVChatManager.getInstance().sendControlCommand(chatId, command, null);
                v.setBackgroundResource(videoMute ? R.drawable.t_avchat_camera_mute_selector : R.drawable.t_avchat_camera_selector);
                updateSelfItemVideoState(!videoMute);
            } else if (i == R.id.avchat_volume) {// 免提
                AVChatManager.getInstance().muteLocalAudio(microphoneMute = !microphoneMute);
                v.setBackgroundResource(microphoneMute ? R.drawable.t_avchat_microphone_mute_selector : R.drawable.t_avchat_microphone_selector);

            } else if (i == R.id.avchat_enable_audio) {// 静音
                AVChatManager.getInstance().setSpeaker(speakerMode = !speakerMode);
                v.setBackgroundResource(speakerMode ? R.drawable.t_avchat_speaker_selector : R.drawable.t_avchat_speaker_mute_selector);

            } else if (i == R.id.avchat_shield_user) {// 屏幕共享
                if (isShare) {
                    Toast.makeText(TeamAVChatActivity.this, "有人共享了屏幕", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!shareMode) {
                    ClubDialog dialog = new ClubDialog(TeamAVChatActivity.this);
                    dialog.setMessage("您屏幕上包含的通知在内的全部内容，均将被录制");
                    dialog.setPositiveListener("开始共享", new PositiveListener() {

                        @Override
                        public void onPositiveClick() {
                            shareScreen();
                            mView = v;
                        }
                    });
                    dialog.setNegativeListener("取消共享", new NegativeListener() {

                        @Override
                        public void onNegativeClick() {
                        }
                    });
                    dialog.show();

                } else {
                    ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(TeamAVChatActivity.this);
                    dialog.setMessage("共享时间:" + timerText.getText().toString());
                    dialog.setPositiveListener("结束共享", new PositiveListener() {

                        @Override
                        public void onPositiveClick() {
                            if (AVChatManager.getInstance().isLocalVideoMuted()) {
                                Toast.makeText(TeamAVChatActivity.this, "请先开启发送视频", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            sendCustomNotification(CustomMsgType.MSG_END_SHARE_SCREEM.getValue(), accounts);
                            AVChatManager.getInstance().stopVideoPreview();
                            AVChatManager.getInstance().muteLocalVideo(true);
                            LogUtils.dazhiLog("videoMute=" + videoMute);
//                            AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.PreferImageQuality);
                            if (mVideoCapturer == null) {
                                mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer(true);
                            }
                            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
                            AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.PreferFrameRate);
                            AVChatManager.getInstance().startVideoPreview();
                            shareMode = false;
                            videoCapturer = null;
                            v.setBackgroundResource(shareMode ? R.drawable.t_avchat_share_screen_selector : R.drawable.t_avchat_share_screen_mute_selector);


                        }
                    });
                    dialog.show();

                }

            }
        }
    };


    private void onPermissionChecked() {
        startRtc(); // 启动音视频
    }

    /**
     * ************************************ 音视频事件 ***************************************
     */

    private void startRtc() {
        // rtc init
        AVChatManager.getInstance().enableRtc(AVPrivatizationConfig.getServerAddresses(this));
        AVChatManager.getInstance().enableVideo();
        mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer(true);
        AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);


        // state observer
        if (stateObserver != null) {
            AVChatManager.getInstance().observeAVChatState(stateObserver, false);
        }
        stateObserver = new SimpleAVChatStateObserver() {
            @Override
            public void onJoinedChannel(int code, String audioFile, String videoFile, int i) {
                //返回录制的音视频地址,服务器返回成功
                if (code == 200) {
                    LogUtils.dazhiLog("audioFile" + audioFile + "  audioFile" + videoFile + "i" + i);
                    onJoinRoomSuccess();
                } else {
                    onJoinRoomFailed(code, null);
                }
            }

            @Override
            public void onUserJoined(String account) {
                LogUtils.dazhiLog("onAVChatUserJoined加入时的长度======" + data.size());
                onAVChatUserJoined(account);
                //透传  如果有共享，发起共享通知
                if (shareMode) {
                    ArrayList<String> str = new ArrayList<>();
                    str.add(account);
                    sendCustomNotification(CustomMsgType.MSG_START_SHARE_SCREEM.getValue(), str);
                }
                //传参meetId通知
                if (!TextUtils.isEmpty(meetId)) {
                    sendMeetIdNotification();
                }

            }

            @Override
            public void onUserLeave(String account, int event) {
                onAVChatUserLeave(account);
                if (shareMode) {
                    ArrayList<String> str = new ArrayList<>();
                    str.add(account);
                    sendCustomNotification(CustomMsgType.MSG_END_SHARE_SCREEM.getValue(), str);
                }
            }

            @Override
            public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {
                onAudioVolume(speakers);
            }
            @Override
            public void onFirstVideoFrameAvailable(String account) {
                super.onFirstVideoFrameAvailable(account);
                AVChatManager.getInstance().startAVRecording(account);
            }
        };
        AVChatManager.getInstance().observeAVChatState(stateObserver, true);

        // notification observer
        if (notificationObserver != null) {
            AVChatManager.getInstance().observeControlNotification(notificationObserver, false);
        }
        notificationObserver = new Observer<AVChatControlEvent>() {

            @Override
            public void onEvent(AVChatControlEvent event) {
                final String account = event.getAccount();
                if (AVChatControlCommand.NOTIFY_VIDEO_ON == event.getControlCommand()) {
                    onVideoLive(account);
                } else if (AVChatControlCommand.NOTIFY_VIDEO_OFF == event.getControlCommand()) {
                    onVideoLiveEnd(account);
                }
            }
        };
        AVChatManager.getInstance().observeControlNotification(notificationObserver, true);

        // join
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_AUDIO_REPORT_SPEAKER, true);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_1_1);

        AVChatConfigs avChatConfigs = new AVChatConfigs(this);
        //服务端录制开关/分辨率等设置
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SERVER_AUDIO_RECORD, avChatConfigs.isServerRecordAudio());
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SERVER_VIDEO_RECORD, avChatConfigs.isServerRecordVideo());
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SERVER_RECORD_MODE, AVChatServerRecordMode.MIX);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SERVER_RECORD_SPEAKER, true);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_QUALITY, avChatConfigs.getAvChatParameters().getInteger(AVChatParameters.KEY_VIDEO_QUALITY));
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, avChatConfigs.getAvChatParameters().getInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO));
//        AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.ScreenSharing);

        AVChatManager.getInstance().joinRoom2(roomId, AVChatType.AUDIO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                chatId = data.getChatId();
                String inviteAccid = PerferenceUtils.get("inviteAccid", "");
                if (NimUIKit.getAccount().equals(inviteAccid)) {
                    LogUtils.dazhiLog("channelId======" + chatId);
                    PerferenceUtils.put("inviteAccid", "");
                    uploadCallInfo(roomId, chatId, teamId, accounts);
                }
            }

            @Override
            public void onFailed(int code) {
                onJoinRoomFailed(code, null);
                LogUtils.dazhiLog("join room failed, code=" + code + ", roomId=" + roomId);
            }

            @Override
            public void onException(Throwable exception) {
                onJoinRoomFailed(-1, exception);
                LogUtils.dazhiLog("join room failed, e=" + exception.getMessage() + ", roomId=" + roomId);
            }
        });
        LogUtils.dazhiLog("start join room, roomId=" + roomId);
    }

    private void onJoinRoomSuccess() {
        startTimer();
        startLocalPreview();
        startTimerForCheckReceivedCall();
        LogUtils.dazhiLog("team avchat running..." + ", roomId=" + roomId);
    }

    private void onJoinRoomFailed(int code, Throwable e) {
        if (code == ResponseCode.RES_ENONEXIST) {
            ToastUtils.shortToast(TeamAVChatActivity.this, getString(R.string.t_avchat_join_fail_not_exist));
            PerferenceUtils.put("bVideoMeet", false);
            hideVideoEntry();
            finish();
        } else {
            ToastUtils.shortToast(TeamAVChatActivity.this, "join room failed, code=" + code + ", e=" + (e == null ? "" : e.getMessage()));
        }
    }

    public void onAVChatUserJoined(String account) {
        int index = getItemIndex(account);
        if (index >= 0) {
            TeamAVChatItem item = data.get(index);
            IVideoRender surfaceView = adapter.getViewHolderSurfaceView(item);
            if (surfaceView != null) {
                item.state = TeamAVChatItem.STATE.STATE_PLAYING;
                item.videoLive = false;
                adapter.notifyDataItemChanged(index);
                AVChatManager.getInstance().setupRemoteVideoRender(account, surfaceView, false, AVChatVideoScalingType.SCALE_ASPECT_FIT);
            }
        }
//        updateAudioMuteButtonState();

    }

    public void onAVChatUserLeave(String account) {
        int index = getItemIndex(account);
        if (index >= 0) {
            TeamAVChatItem item = data.get(index);
            item.state = TeamAVChatItem.STATE.STATE_HANGUP;
            item.volume = 0;
            item.videoLive = false;
            adapter.notifyDataItemChanged(index);
        }
//        updateAudioMuteButtonState();

    }

    private void startLocalPreview() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).account.equals(NimUIKit.getAccount())) {
                IVideoRender surfaceView = adapter.getViewHolderSurfaceView(data.get(i));
                if (surfaceView != null) {
                    AVChatManager.getInstance().setupLocalVideoRender(surfaceView, false, AVChatVideoScalingType.SCALE_ASPECT_FIT);
                    AVChatManager.getInstance().stopVideoPreview();
                    data.get(i).state = TeamAVChatItem.STATE.STATE_PLAYING;
                    data.get(i).videoLive = false;
                    adapter.notifyDataItemChanged(i);
                }
            }

        }

    }


    /**
     * ************************************ 保存到自己服务端 ***************************************
     */

    //上传会议信息
    private void uploadCallInfo(String roomId, long channelId, String gourpId, final List<String> memberList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Map<String, String> maps = new HashMap<>();
        maps.put("roomId", roomId);
        maps.put("channelId", String.valueOf(channelId));
        maps.put("groupId", gourpId);
        maps.put("groupName", teamName);
        maps.put("startTime", formatter.format(curDate));
        final JSONArray array = new JSONArray();
        for (String accId : memberList) {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("accId", accId);
            array.add(jsonObject);
        }
        maps.put("memberList", array.toJSONString());
        String TAG = "upload_call_info";
        mActivity.addTag(TAG);
        String url = ImUtils.UPLOAD_CALL_INFO;
        LogUtils.dazhiLog("uploadCallInfo" + maps);
        HttpServer.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("保存会议信息成功" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.getJSONObject("content");
                        meetId = content.getString("id");
                        LogUtils.dazhiLog("返回的meetId====" + meetId);
                        //保存会议id
                        if (!TextUtils.isEmpty(meetId)) {
                            PerferenceUtils.put("meetId", meetId);
                            uploadUser(meetId, array.toJSONString());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("保存会议信息失败" + errorMsg);
            }
        });
    }

    //上传邀请的参会人
    private void uploadUser(final String meetId, String accIdArray) {
        Map<String, String> maps = new HashMap<>();
        maps.put("meetId", meetId);
        maps.put("accIdArray", accIdArray);
        String TAG = "upload_user";
        mActivity.addTag(TAG);
        LogUtils.dazhiLog("上传accId====" + accIdArray);
        String url = ImUtils.UPLOAD_USER_INFO;
        HttpServer.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                ToastUtils.longToast(TeamAVChatActivity.this, "邀请成功");
                sendMeetIdNotification();
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.longToast(TeamAVChatActivity.this, "邀请失败");
            }
        });
    }

    /**
     * 获取当前用户被邀请的会议列表
     */
    private void updateMeetingStatus() {
        String TAG = "update_meeting_status";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("id", PerferenceUtils.get("meetId", ""));
        map.put("duration", String.valueOf(seconds));
        map.put("meetStatus", "S");
        final String url = ImUtils.UPDATE_MEETING_STATUA;
        HttpServer.doPostStringWithUrl(mActivity, url, TAG, map, new com.xej.xhjy.common.http.interfaces.HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }


    /**
     * ************************************ 音视频状态 ***************************************
     */

    private void onVideoLive(String account) {
        if (account.equals(NimUIKit.getAccount())) {
            return;
        }

        notifyVideoLiveChanged(account, true);
    }

    private void onVideoLiveEnd(String account) {
        if (account.equals(NimUIKit.getAccount())) {
            return;
        }

        notifyVideoLiveChanged(account, false);
    }

    private void notifyVideoLiveChanged(String account, boolean live) {
        int index = getItemIndex(account);
        if (index >= 0) {
            TeamAVChatItem item = data.get(index);
            item.videoLive = live;
            adapter.notifyDataItemChanged(index);
        }
    }

    private void onAudioVolume(Map<String, Integer> speakers) {
        for (TeamAVChatItem item : data) {
            if (speakers.containsKey(item.account)) {
                item.volume = speakers.get(item.account);
                adapter.updateVolumeBar(item);
            }
        }
    }

    private void updateSelfItemVideoState(boolean live) {
        LogUtils.dazhiLog("刷新self==" + live);
        int index = getItemIndex(NimUIKit.getAccount());
        if (index >= 0) {
            TeamAVChatItem item = data.get(index);
            item.videoLive = live;
            adapter.notifyDataItemChanged(index);
        }
    }


    private void hangup() {

        try {
            if (!bRefuse) {
                //发送会议结束的消息
                if (shareMode) {
                    sendCustomNotification(CustomMsgType.MSG_END_SHARE_SCREEM.getValue(), accounts);
                }
                sendCustomNotification(CustomMsgType.MSG_HANDUP.getValue(), accounts);
            }
            bRefuse = false;

            if (destroyRTC) {
                return;
            }
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().muteLocalVideo(true);
            AVChatManager.getInstance().disableVideo();
            AVChatManager.getInstance().leaveRoom2(roomId, null);
            AVChatManager.getInstance().disableRtc();
            if (getCountInMeeting() == 1) {
                updateMeetingStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        destroyRTC = true;
    }


    /**
     * 获取会议中正在通话人员数量
     */
    private int getCountInMeeting() {
        int count = 0;
        if (data != null && data.size() > 0) {
            for (TeamAVChatItem item : data) {
                if (item.state == TeamAVChatItem.STATE.STATE_PLAYING) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * ************************************ 定时任务 ***************************************
     */

    private void startTimer() {
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
        timerText.setText("00:00");
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            seconds++;
            int m = seconds / 60;
            int s = seconds % 60;
            final String time = String.format(Locale.CHINA, "%02d:%02d", m, s);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerText.setText(time);
                }
            });
        }
    };

    private void startTimerForCheckReceivedCall() {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                for (TeamAVChatItem item : data) {
                    if (item.type == TYPE_DATA && item.state == TeamAVChatItem.STATE.STATE_WAITING) {
                        item.state = TeamAVChatItem.STATE.STATE_END;
                        adapter.notifyDataSetChanged();
                    }
                    index++;
                }
                checkAllHangUp();
            }
        }, CHECK_RECEIVED_CALL_TIMEOUT);
    }

    private void startAutoRejectTask() {
        if (autoRejectTask == null) {
            autoRejectTask = new Runnable() {
                @Override
                public void run() {
                    AVChatSoundPlayer.instance().stop();
                    finish();
                }
            };
        }

        mainHandler.postDelayed(autoRejectTask, AUTO_REJECT_CALL_TIMEOUT);
    }

    private void cancelAutoRejectTask() {
        if (autoRejectTask != null) {
            mainHandler.removeCallbacks(autoRejectTask);
        }
    }

    /*
     * 除了所有人都没接通，其他情况不做自动挂断
     */
    private void checkAllHangUp() {
        for (TeamAVChatItem item : data) {
            if (item.account != null &&
                    !item.account.equals(AVChatKit.getAccount()) &&
                    item.state != TeamAVChatItem.STATE.STATE_END) {
                return;
            }
        }
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hangup();
                finish();
            }
        }, 200);
    }

    /**
     * 通知栏
     */
    private void activeCallingNotifier(boolean active) {
        if (notifier != null) {
            if (destroyRTC) {
                notifier.activeCallingNotification(false);
            } else {
                notifier.activeCallingNotification(active);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mVideoServiceConnection != null) {
            unbindService(mVideoServiceConnection);//不显示悬浮框
            mVideoServiceConnection = null;
        }
    }


    private void hideVideoEntry() {
        CustomNotification command = new CustomNotification();
        command.setSessionId(NimUIKit.getAccount());
        command.setSessionType(SessionTypeEnum.P2P);
        Map<String, Object> payload = new HashMap<>();
        payload.put("msgType", CustomMsgType.MSG_INVITE.getValue());
        command.setPushPayload(payload);
        command.setSendToOnlineUserOnly(false);
        EventBus.getDefault().post(command);
    }


    /*******************************************屏幕共享*********************************************/


    private void shareScreen() {
        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().muteLocalVideo(false);
        if (AVChatManager.getInstance().isLocalVideoMuted()) {
            ToastUtils.shortToast(this, "请先开启视频");
            return;
        }
        // 如果之前没有人在屏幕共享，则开启屏幕共享
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ToastUtils.shortToast(this, "手机版本过低，不支持屏幕共享");
            return;
        }
        tryStartScreenCapturer();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void tryStartScreenCapturer() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) TeamAVChatActivity.this.getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, CAPTURE_PERMISSION_REQUEST_CODE);
    }


    private void updateAudioMuteButtonState() {
        boolean enable = false;
        for (TeamAVChatItem item : data) {
            if (item.state == TeamAVChatItem.STATE.STATE_PLAYING &&
                    !AVChatKit.getAccount().equals(item.account)) {
                enable = true;
                break;
            }
        }
        voiceMuteButton.setEnabled(enable);
        voiceMuteButton.invalidate();
    }

    /**
     * 屏蔽用户
     */
    private void disableUserAudio() {
        List<Pair<String, Boolean>> voiceMutes = new ArrayList<>();
        for (TeamAVChatItem item : data) {
            if (item.state == TeamAVChatItem.STATE.STATE_PLAYING &&
                    !AVChatKit.getAccount().equals(item.account)) {
                voiceMutes.add(new Pair<>(item.account, AVChatManager.getInstance().isRemoteAudioMuted(item.account)));
            }
        }
        TeamAVChatVoiceMuteDialog dialog = new TeamAVChatVoiceMuteDialog(this, teamId, voiceMutes);
        dialog.setTeamVoiceMuteListener(new TeamAVChatVoiceMuteDialog.TeamVoiceMuteListener() {
            @Override
            public void onVoiceMuteChange(List<Pair<String, Boolean>> voiceMuteAccounts) {
                if (voiceMuteAccounts != null) {
                    for (Pair<String, Boolean> voiceMuteAccount : voiceMuteAccounts) {
                        AVChatManager.getInstance().muteRemoteAudio(voiceMuteAccount.first, voiceMuteAccount.second);
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // 屏蔽BACK
    }

    /**
     * ************************************ 数据源 ***************************************
     */
    private float lastDownX, lastDownY;

    private void initRecyclerView() {
        //去一次重复
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(accounts);
        accounts = new ArrayList<>(hashSet);
        // 确认数据源,自己放在首位
        data = new ArrayList<>(accounts.size());
        isShare = otherShare;
        for (String account : accounts) {
            if (account.equals(NimUIKit.getAccount())) {
                continue;
            }
            data.add(new TeamAVChatItem(TYPE_DATA, teamId, account, false));
        }
        TeamAVChatItem selfItem = new TeamAVChatItem(TYPE_DATA, teamId, NimUIKit.getAccount(), false);
        selfItem.state = TeamAVChatItem.STATE.STATE_PLAYING; // 自己直接采集摄像头画面
        data.add(0, selfItem);
        adapter = new TeamAVChatAdapter(recyclerView, data);
        recyclerView.setAdapter(adapter);
        mGridLayoutManager = new MyLayoutManager(this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        mGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //ios传值，弃用
//        meetId = PerferenceUtils.get("meetId", "");
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (data.get(position).shareFull) {
                    return 2;
                } else {
                    return data.size() > 2 ? 1 : 2;

                }
            }
        });
        recyclerView.addItemDecoration(new SpacingDecoration(ScreenUtil.dip2px(1), ScreenUtil.dip2px(1), true));

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) { // 判断拦截的时机
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastDownX = event.getRawX();
                        lastDownY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //隐藏显示
                        onClickMySelf();
                        new Thread() {
                            public void run() {
                                SystemClock.sleep(5000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onClickMySelf();
                                    }
                                });
                            }
                        }.start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        float offsetX = event.getRawX() - lastDownX;
                        float offsetY = event.getRawY() - lastDownY;
                        if (offsetX > 5 && offsetX > Math.abs(offsetY)) {
                            LogUtils.dazhiLog("点击滑动了");
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                LogUtils.dazhiLog("MotionEvent" + e);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                LogUtils.dazhiLog("disallowIntercept=" + disallowIntercept);

            }
        });
    }

    /**********************************************隐藏显示动画********************************************/

    private void onClickMySelf() {
        setVisiable();
        setContainerVisiable();
        if (mVisiable == View.INVISIBLE) {
            setOutAnim();
        } else {
            setInAnim();
        }
    }

    private void setVisiable() {
        mVisiable = mVisiable == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE;
    }

    private void setContainerVisiable() {
        avchat_setting_top_layout.setVisibility(mVisiable);
        avchat_setting_layout.setVisibility(mVisiable);
    }

    private void setOutAnim() {
        avchat_setting_top_layout.setAnimation(AnimUtils.getTopOutAnim(TeamAVChatActivity.this));
        avchat_setting_layout.setAnimation(AnimUtils.getBottomOutAnim(TeamAVChatActivity.this));
    }

    private void setInAnim() {
        avchat_setting_top_layout.setAnimation(AnimUtils.getTopInAnim(TeamAVChatActivity.this));
        avchat_setting_layout.setAnimation(AnimUtils.getBottomInAnim(TeamAVChatActivity.this));
    }


    private int getItemIndex(final String account) {
        int index = 0;
        boolean find = false;
        for (TeamAVChatItem i : data) {
            if (i.account.equals(account)) {
                find = true;
                break;
            }
            index++;
        }

        return find ? index : -1;
    }

    @Override
    public void onPageSizeChanged(int pageSize) {
        LogUtils.dazhiLog("总页数 = " + pageSize);
        pageIndicatorView.initIndicator(pageSize);
        if (pageSize > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
        } else {
            pageIndicatorView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageSelect(int pageIndex) {
        pageIndicatorView.setSelectedPage(pageIndex);
        LogUtils.dazhiLog("选中页码 = " + pageIndex);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * ************************************ 权限检查 ***************************************
     */

    private void checkPermission() {
        List<String> lackPermissions = AVChatManager.getInstance().checkPermission(TeamAVChatActivity.this);
        if (lackPermissions.isEmpty()) {
            onBasicPermissionSuccess();
        } else {
            String[] permissions = new String[lackPermissions.size()];
            for (int i = 0; i < lackPermissions.size(); i++) {
                permissions[i] = lackPermissions.get(i);
            }
            MPermission.with(TeamAVChatActivity.this)
                    .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                    .permissions(permissions)
                    .request();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        onPermissionChecked();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        ToastUtils.shortToast(this, "音视频通话所需权限未全部授权，部分功能可能无法正常运行！");
        onPermissionChecked();
    }


    /**
     * 在线状态观察者
     */
    private Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                AVChatSoundPlayer.instance().stop();
                hangup();
                finish();
            }
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == CAPTURE_PERMISSION_REQUEST_CODE) {
            //只有系统提示确认了再去改变状态。
            shareMode = true;
            if (mView != null) {
                mView.setBackgroundResource(shareMode ? R.drawable.t_avchat_share_screen_selector : R.drawable.t_avchat_share_screen_mute_selector);
            }
            sendCustomNotification(CustomMsgType.MSG_START_SHARE_SCREEM.getValue(), accounts);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                videoCapturer = AVChatVideoCapturerFactory.createScreenVideoCapturer(data,
                        new MediaProjection.Callback() {
                            @Override
                            public void onStop() {
                                super.onStop();
                            }
                        });
            }
            //屏幕共享
            AVChatManager.getInstance().setupVideoCapturer(videoCapturer);
            AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.ScreenSharing);
            AVChatManager.getInstance().startVideoPreview();
        } else if (requestCode == CONTACT_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            //邀请成员加入
            ArrayList<String> selectedAccounts = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
            LogUtils.dazhiLog("selectedAccounts" + selectedAccounts.size());
            if (!TextUtils.isEmpty(meetId)) {
                final JSONArray array = new JSONArray();
                for (String accId : selectedAccounts) {
                    com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("accId", accId);
                    array.add(jsonObject);
                }

                LogUtils.dazhiLog("上传的meetId===" + meetId);
                uploadUser(meetId, array.toJSONString());
            }
            onCreateRoomSuccess(roomId, selectedAccounts);
            adapter.notifyDataSetChanged();
        } else if (requestCode == MANAGE_OVERLAY_PERMISSION) {
            Intent intent = new Intent(getApplicationContext(), TeamAVChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

    }


    /**
     * 再次发起音视频成员加入
     *
     * @param roomName
     * @param list
     */

    public void onCreateRoomSuccess(String roomName, List<String> list) {
        for (String accId : list) {
            data.add(new TeamAVChatItem(TYPE_DATA, teamId, accId, false));
        }
        accounts.addAll(list);
        String teamID = teamId;
        // 在群里发送tip消息
        IMMessage message = MessageBuilder.createTextMessage(teamID, SessionTypeEnum.Team, "");
        CustomMessageConfig tipConfig = new CustomMessageConfig();
        tipConfig.enableHistory = false;
        tipConfig.enableRoaming = false;
        tipConfig.enablePush = false;
        String teamNick = TeamHelper.getDisplayNameWithoutMe(teamID, NimUIKit.getAccount());
        message.setContent(teamNick + this.getString(R.string.t_avchat_start));
        message.setConfig(tipConfig);
        sendMessage(message);
        // 对各个成员发送点对点自定义通知
        String teamName = TeamHelper.getTeamName(teamID);
        String content = TeamAVChatProfile.sharedInstance().buildContent(roomName, teamID, accounts, teamName);
        CustomNotificationConfig config = new CustomNotificationConfig();
        config.enablePush = true;
        config.enablePushNick = false;
        config.enableUnreadCount = true;
        // accounts 循环ios会再次接受到邀请

        for (String account : list) {
            CustomNotification command = new CustomNotification();
            command.setSessionId(account);
            command.setSessionType(SessionTypeEnum.P2P);
            command.setConfig(config);
            command.setContent(content);
            command.setApnsText(teamNick + this.getString(R.string.t_avchat_push_content));
            Map<String, Object> payload = new HashMap<>();
            payload.put("msgType", CustomMsgType.MSG_INVITE.getValue());
            payload.put("teamNick", teamNick);
            payload.put("listAccounts", list);
            //是否在共享
            if (isShare || shareMode) {
                payload.put("isShareScreen", "1");
            } else {
                payload.put("isShareScreen", "0");
            }
            //第三人收到共享时候
            if (isShare) {
                payload.put("accId", accId);
            } else {
                payload.put("accId", NimUIKit.getAccount());
            }
            //发起邀请人的头像
            payload.put("headUrl", AppJoint.service(AppService.class).getHeadImgUrl());
            command.setPushPayload(payload);
            command.setSendToOnlineUserOnly(false);
            //发送消息通知被邀请的用户参加会议
            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }


        for (String account : accounts) {
            if (!account.equals(NimUIKit.getAccount())) {
                CustomNotification command = new CustomNotification();
                command.setSessionId(account);
                command.setSessionType(SessionTypeEnum.P2P);
                command.setConfig(config);
                command.setContent(content);
                command.setApnsText(teamNick + this.getString(R.string.t_avchat_push_content));
                Map<String, Object> payload = new HashMap<>();
                payload.put("msgType", CustomMsgType.MSG_ADDUSER.getValue());
                payload.put("teamNick", teamNick);
                payload.put("listAccounts", list);
                //发起邀请人的头像
                payload.put("headUrl", AppJoint.service(AppService.class).getHeadImgUrl());
                command.setPushPayload(payload);
                command.setSendToOnlineUserOnly(false);
                //发送消息通知被邀请的用户参加会议
                NIMClient.getService(MsgService.class).sendCustomNotification(command);
            }
        }
    }


    /**
     * 发送共享通知
     *
     * @param type
     */

    private void sendCustomNotification(int type, ArrayList<String> selectedAccounts) {
        LogUtils.dazhiLog("发送共享排除自己====" + NimUIKit.getAccount());
        for (String account : selectedAccounts) {
            if (!account.equals(NimUIKit.getAccount())) {
                LogUtils.dazhiLog("发送给account共享====" + account);
                CustomNotification command = new CustomNotification();
                command.setSessionId(account);
                command.setSessionType(SessionTypeEnum.P2P);
                Map<String, Object> payload = new HashMap<>();
                payload.put("msgType", type);
                payload.put("listAccounts", accounts);
                if (shareMode) {
                    payload.put("isShareScreen", shareMode ? "1" : "0");
                    payload.put("accId", NimUIKit.getAccount());
                    //ios使用
                    payload.put("ShareScreen", type == 2 ? 1 : 0);
                    payload.put("ShareScreenUid", NimUIKit.getAccount());
                    LogUtils.dazhiLog("shareMode-sharedAccId" + NimUIKit.getAccount());
                }
                if (isShare) {
                    payload.put("isShareScreen", isShare ? "1" : "0");
                    payload.put("accId", accId);
                    payload.put("ShareScreenUid", accId);

                }
                command.setPushPayload(payload);
                command.setSendToOnlineUserOnly(false);
                NIMClient.getService(MsgService.class).sendCustomNotification(command);
            }
        }
    }


    private void sendMeetIdNotification() {
        for (String account : accounts) {
            CustomNotification command = new CustomNotification();
            command.setSessionId(account);
            command.setSessionType(SessionTypeEnum.P2P);
            Map<String, Object> payload = new HashMap<>();
            payload.put("msgType", CustomMsgType.MSG_PARAMETER.getValue());
            payload.put("meetId", meetId);
            //发起邀请人的头像
            command.setPushPayload(payload);
            //发送消息通知被邀请的用户参加会议
            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }


    @Override
    public boolean sendMessage(IMMessage message) {
        message.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(message, true);
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {

    }


//    @Override
//    public void onAnswerThePhone() {
//        LogUtils.dazhiLog("回调了");
//        hangup();
//        finish();
//    }
}




