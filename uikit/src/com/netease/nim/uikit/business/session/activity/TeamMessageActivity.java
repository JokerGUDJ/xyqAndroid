package com.netease.nim.uikit.business.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.business.event.VideoCallEvent;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.fragment.TeamMessageFragment;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.utils.ImUtils;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.xej.xhjy.common.http.HttpServer;
import com.xej.xhjy.common.http.interfaces.HttpCallBack;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群聊界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
public class TeamMessageActivity extends BaseMessageActivity {

    // model
    private Team team;

    private View invalidTeamTipView;

    private TextView invalidTeamTipText;
    private TextView headtitle,video_audio_call_txt;
    private ImageView headback, head_right;
    private RelativeLayout video_audio_view;

    private TeamMessageFragment fragment;

    private Class<? extends Activity> backToClass;
    private final int MSG_INVITE = 0;//音视频邀请
    private final int MSG_HANDUP = 1;//音视频挂断
    private String teamId=null, roomName, teamName,teamNick,headUrl;
    private ArrayList<String> accounts;

    public static void start(Context context, String tid, SessionCustomization customization,
                             Class<? extends Activity> backToClass, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    protected void findViews() {
        head_right = (ImageView) findViewById(R.id.head_right);
        invalidTeamTipView = findView(R.id.invalid_team_tip);
        invalidTeamTipText = findView(R.id.invalid_team_text);
        headback = (ImageView) findViewById(R.id.head_back);
        headtitle = (TextView) findViewById(R.id.head_title);
        head_right.setVisibility(View.VISIBLE);
        head_right.setImageResource(R.drawable.icon_group);
        head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    NimUIKit.startTeamInfo(TeamMessageActivity.this, team.getId());
                }
            }
        });
        headback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (backToClass != null) {
                    Intent intent = new Intent();
                    intent.setClass(TeamMessageActivity.this, backToClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
        video_audio_view = findViewById(R.id.video_audio_view);
        video_audio_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAVList();
            }
        });
       boolean bInvitedMeet =  PerferenceUtils.get("bVideoMeet",false);
       String id = PerferenceUtils.get("teamId","");
       LogUtils.dazhiLog("teamId------"+id);
       video_audio_call_txt = findViewById(R.id.video_audio_call_txt);
       if(bInvitedMeet && sessionId.equals(id)){
           teamNick = PerferenceUtils.get("teamNick", "");
           String accId = PerferenceUtils.get("accId","");
           Team team = NimUIKit.getTeamProvider().getTeamById(id);
           if (team != null) {
               video_audio_call_txt.setText((team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(id) : UserInfoHelper.getUserName(accId)) +"邀请您加入音视频通话，点击此处加入");
               video_audio_view.setVisibility(View.VISIBLE);
           }

       }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
        findViews();

        registerTeamUpdateObserver(true);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = NimUIKit.getTeamProvider().getTeamById(sessionId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(TeamMessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        fragment.setTeam(team);
        invalidTeamTipText.setText(team.getType() == TeamTypeEnum.Normal ? R.string.normal_team_invalid_tip : R.string.team_invalid_tip);
        invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
        if (team!=null){
            headtitle.setEms(12);
            headtitle.setSingleLine();
            headtitle.setGravity(Gravity.CENTER);
            headtitle.setEllipsize(TextUtils.TruncateAt.END);
            headtitle.setText(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + ")");
        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(TeamMessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            fragment.refreshMessageList();
        }
    };

    @Override
    protected MessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
        fragment = new TeamMessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_team_message_activity;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoAudioEvent(CustomNotification customNotification) {
        if(customNotification != null){
            JSONObject jsonObject = JSONObject.parseObject(customNotification.getContent());
            final Map<String, Object> payload = customNotification.getPushPayload();
            int msgType = (int)payload.get("msgType");
            if(msgType == MSG_INVITE){
                video_audio_view.setVisibility(View.VISIBLE);
                video_audio_call_txt.setText("点击此处加入会议");
                roomName = jsonObject.getString("room");
                teamId = jsonObject.getString("teamId");
                if(sessionId.equals(teamId)){
                    JSONArray accountArray = jsonObject.getJSONArray("members");
                    accounts = new ArrayList<>();
                    if (accountArray != null) {
                        for (Object o : accountArray) {
                            accounts.add((String) o);
                        }
                    }
                    teamName = jsonObject.getString("teamName");
                    teamNick = payload.get("teamNick").toString();
                    //呼叫人头像
                    headUrl = payload.get("headUrl").toString();
                    String teamNick = payload.get("teamNick").toString();
                    String accId = payload.get("accId").toString();
                    Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
                    if (team != null) {
                        video_audio_call_txt.setText((team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(teamId) : UserInfoHelper.getUserName(accId)) +"邀请您加入音视频通话，点击此处加入");
                    }else{
                        video_audio_call_txt.setText(teamNick+"邀请你加入音视频通话，点击此处加入");
                    }

                    PerferenceUtils.put("bVideoMeet", true);
                    PerferenceUtils.put("teamId", teamId);
                    PerferenceUtils.put("teamNick", teamNick);
                    PerferenceUtils.put("accId", accId);

                }
            }else if(msgType == MSG_HANDUP){
                teamId = null;
                if(video_audio_view.getVisibility() == View.VISIBLE){
                    video_audio_view.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 获取当前用户被邀请的适配会议列表
     */
    private void getAVList() {
        String TAG = "get_av_list";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("groupId",sessionId);
        final String url = ImUtils.QUERY_AV_LIST;
        HttpServer.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    org.json.JSONObject json = new org.json.JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        org.json.JSONArray array = json.getJSONArray("content");
                        if(array != null && array.length() > 0){
                            accounts = new ArrayList<>();
                            org.json.JSONObject content = (org.json.JSONObject) array.get(0);
                            if(content != null){
                                roomName = content.getString("roomId");
                                teamId = content.getString("tId");
                                teamName = content.getString("tName");
                                String inviteUser = content.getString("inviteUser");
                                UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(inviteUser);
                                teamNick = userInfo.getName();
                                headUrl = userInfo.getAvatar();
                                org.json.JSONArray accIdList = content.getJSONArray("accIdList");
                                for(int i = 0; accIdList != null && i < accIdList.length(); i++){
                                    accounts.add((String) accIdList.get(i));
                                }
                                LogUtils.dazhiLog("teamId------"+teamId);
                                if(TextUtils.isEmpty(teamId)){
                                    ToastUtils.shortToast(mActivity, "您参加的会议已结束");
                                    video_audio_view.setVisibility(View.GONE);
                                    PerferenceUtils.put("bVideoMeet",false);
                                    PerferenceUtils.put("teamId", "");
                                    PerferenceUtils.put("teamNick", "");
                                    PerferenceUtils.put("accId","");
                                }else {
                                    EventBus.getDefault().post(new VideoCallEvent(teamId,roomName,accounts,
                                            teamName,teamNick,headUrl));
                                }
                            }
                        }else{
                            ToastUtils.shortToast(mActivity, "您参加的会议已结束");
                            video_audio_view.setVisibility(View.GONE);
                            PerferenceUtils.put("bVideoMeet",false);
                            PerferenceUtils.put("teamId", "");
                            PerferenceUtils.put("teamNick","");
                            PerferenceUtils.put("accId","");
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
}
