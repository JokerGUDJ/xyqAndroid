package com.netease.nim.avchatkit;

import android.text.TextUtils;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.avchatkit.common.Handlers;
import com.netease.nim.avchatkit.common.log.LogUtil;
import com.netease.nim.avchatkit.common.util.TimeUtil;
import com.netease.nim.avchatkit.constant.CustomMsgType;
import com.netease.nim.avchatkit.teamavchat.activity.TeamAVChatActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by hzchenkang on 2017/5/5.
 */

public class TeamAVChatProfile {

    public static final String KEY_ID = "id";
    public static final String KEY_MEMBER = "members";
    public static final String KEY_TID = "teamId";
    public static final String KEY_RID = "room";
    public static final String KEY_TNAME = "teamName";
    public static final String key_OTHER_SHOW = "isShareScreen";

    private static final long OFFLINE_EXPIRY = 45 * 1000;

    private static final int ID = 3;

    private boolean isTeamAVChatting = false;
    private boolean isSyncComplete = true; // 未开始也算同步完成，可能存在不启动同步的情况

    public String buildContent(String roomName, String teamID, List<String> accounts, String teamName) {
        JSONObject json = new JSONObject();
        json.put(KEY_ID, ID);
        JSONArray array = new JSONArray();
        array.add(NimUIKit.getAccount());
        for (String account : accounts) {
            array.add(account);
        }
        json.put(KEY_MEMBER, array);
        json.put(KEY_TID, teamID);
        json.put(KEY_RID, roomName);
        json.put(KEY_TNAME, teamName);
        return json.toString();
    }

    private JSONObject parseContentJson(CustomNotification notification) {
        if (notification != null) {
            String content = notification.getContent();
            return JSONObject.parseObject(content);
        }
        return null;
    }

    private boolean isTeamAVChatInvite(JSONObject json) {
        if (json != null) {
            int id = json.getIntValue(KEY_ID);
            return id == ID;
        }
        return false;
    }

    /**
     * 监听自定义通知消息，id = 3 是群视频邀请
     *
     * @param register
     */

    private Observer<CustomNotification> customNotificationObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification customNotification) {
            try {
                JSONObject jsonObject = parseContentJson(customNotification);
                final Map<String, Object> payload = customNotification.getPushPayload();
                final int msgType = (int) payload.get("msgType");
                // 收到群视频邀请
                if (msgType == CustomMsgType.MSG_INVITE.getValue()) {
                    final String roomName = jsonObject.getString(KEY_RID);
                    final String teamId = jsonObject.getString(KEY_TID);
                    JSONArray accountArray = jsonObject.getJSONArray(KEY_MEMBER);
                    final ArrayList<String> accounts = new ArrayList<>();
                    final String teamName = jsonObject.getString(KEY_TNAME);

                    if (accountArray != null) {
                        for (Object o : accountArray) {
                            accounts.add((String) o);
                        }
                    }

                    // 接收到群视频邀请，启动来点界面
                    LogUtil.ui("receive team video chat notification " + teamId + " room " + roomName);
                    if (isTeamAVChatting || AVChatProfile.getInstance().isAVChatting()) {
                        LogUtil.ui("cancel launch team av chat isTeamAVChatting = " + isTeamAVChatting);
//                        Toast.makeText(AVChatKit.getContext(), "正在进行视频通话", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtil.ui("isSyncComplete = " + isSyncComplete);
                    if (isSyncComplete || !checkOfflineOutTime(customNotification)) {
                        isTeamAVChatting = true;
                        String teamNick = payload.get("teamNick").toString();
                        //呼叫人头像
                        String headUrl = payload.get("headUrl").toString();
                        String str = payload.get("isShareScreen").toString();
                        String shareAccId = payload.get("accId").toString();
                        String meetId = (String) payload.get("meetId");
                        if (!TextUtils.isEmpty(meetId)){
                            PerferenceUtils.put("meetId", meetId);
                        }
                        boolean otherShow;
                        if ("1".equals(str)) {
                            otherShow = true;
                        } else {
                            otherShow = false;
                        }
                        EventBus.getDefault().post(customNotification);
                        LogUtils.dazhiLog("被邀人收到的accid长度====="+accounts.size());
                        launchActivity(teamId, roomName, accounts, teamName, teamNick, headUrl, otherShow, shareAccId);
                    }
                } else if (msgType == CustomMsgType.MSG_PARAMETER.getValue()) {
                    //单独传参数通知
//                    String meetId = (String) payload.get("meetId");
//                    PerferenceUtils.put("meetId", meetId);
//                    LogUtils.dazhiLog("ios发过来的meetId--------->" + meetId);
                }
            } catch (Exception e) {
                isTeamAVChatting = false;
                e.printStackTrace();
            }
        }
    };


    private void launchActivity(final String teamId, final String roomName, final ArrayList<String> accounts, final String teamName,
                                final String teamNick, final String headUrl, final boolean othershow, final String shareAccId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // 欢迎界面正在运行，则等MainActivity启动之后再启动，否则直接启动 TeamAVChatActivity
                if (!AVChatKit.isMainTaskLaunching()) {
                    TeamAVChatActivity.startActivity(AVChatKit.getContext(), true, teamId, roomName, accounts, teamName, teamNick, headUrl, othershow, shareAccId, false);
                } else {
                    LogUtil.ui("launch TeamAVChatActivity delay for WelComeActivity is Launching");
                    launchActivity(teamId, roomName, accounts, teamName, teamNick, headUrl, othershow, shareAccId);
                }
            }
        };

        Handlers.sharedHandler(AVChatKit.getContext()).postDelayed(r, 200);
    }

    private Observer<LoginSyncStatus> loginSyncStatusObserver = new Observer<LoginSyncStatus>() {
        @Override
        public void onEvent(LoginSyncStatus loginSyncStatus) {
            isSyncComplete = (loginSyncStatus == LoginSyncStatus.SYNC_COMPLETED ||
                    loginSyncStatus == LoginSyncStatus.NO_BEGIN);
        }
    };

    public boolean checkOfflineOutTime(CustomNotification notification) {
        // 时间差在45s内，考虑本地时间误差，条件适当放宽
        long time = TimeUtil.currentTimeMillis() - notification.getTime();
        LogUtil.ui("rev offline team AVChat request time = " + time);
        return time > OFFLINE_EXPIRY || time < -OFFLINE_EXPIRY;
    }

    public void setTeamAVChatting(boolean teamAVChatting) {
        isTeamAVChatting = teamAVChatting;
    }

    public boolean isTeamAVChatting() {
        return isTeamAVChatting;
    }

    public void registerObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(loginSyncStatusObserver, register);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotificationObserver, register);
    }

    public static TeamAVChatProfile sharedInstance() {
        return InstanceHolder.teamAVChatProfile;
    }

    private static class InstanceHolder {
        private final static TeamAVChatProfile teamAVChatProfile = new TeamAVChatProfile();
    }

    //回调接口
    public interface ShowScreenInterface {
        void showClick(boolean isshow);
    }

    private ShowScreenInterface itemOnClickInterface;

    public void setItemOnClickInterface(ShowScreenInterface screenInterface) {
        this.itemOnClickInterface = screenInterface;
    }
}
