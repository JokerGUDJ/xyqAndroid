package com.netease.nim.uikit.business.team.helper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.team.model.Team;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: XHJYMobileClient
 * @ClassName: TeamCreateHelper
 * @Description: 建群\普通群和高级群
 * @Author: lihy_0203
 * @CreateDate: 2019/7/1 下午4:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/1 下午4:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TeamCreateHelper {
    private static final String TAG = TeamCreateHelper.class.getSimpleName();
    private static final int DEFAULT_TEAM_CAPACITY = 200;

    /**
     * 创建普通群
     */
    public static void createNormalTeam(final BaseActivity context, List<ContactGroupBean.ContentBean> iContactList, final boolean isNeedBack, final boolean isNeedChatGroup,
                                        final RequestCallback<CreateTeamResult> callback) {

        List<String> selectedAccounts = new ArrayList<>();
        List<String> disName = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < iContactList.size(); i++){
            ContactGroupBean.ContentBean c = iContactList.get(i);
            selectedAccounts.add(c.getAccId());
            disName.add(c.getName());
            buffer.append(c.getName());
            if(i != iContactList.size() - 1){
                buffer.append("、");
            }
        }
        String teamName = buffer.toString() + "等人";

        DialogMaker.showProgressDialog(context, "", true);
        // 创建群
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<TeamFieldEnum, Serializable>();
        fields.put(TeamFieldEnum.Name, teamName);
        //如果传1则可以添加人员，否则不显示添加
        fields.put(TeamFieldEnum.Extension,"1");
        NIMClient.getService(TeamService.class).createTeam(fields, TeamTypeEnum.Normal, "",
                selectedAccounts).setCallback(
                new RequestCallback<CreateTeamResult>() {
                    @Override
                    public void onSuccess(CreateTeamResult team) {
                        DialogMaker.dismissProgressDialog();
                        if(isNeedChatGroup){
                            ToastUtils.longToast(context, "建群成功");
                            if (isNeedBack) {
                                NimUIKit.startTeamSession(context, team.getTeam().getId()); // 进入创建的群
                            } else {
                                NimUIKit.startTeamSession(context, team.getTeam().getId());
                            }
                            if (callback != null) {
                                callback.onSuccess(null);
                            }
                        }else{
                            //直接进入音视频聊天页面
                            if (callback != null) {
                                callback.onSuccess(team);
                            }
                        }

                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 801) {
                            String tip = context.getString(R.string.over_team_member_capacity, DEFAULT_TEAM_CAPACITY);
                            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.create_team_failed, Toast.LENGTH_SHORT).show();
                        }
                        LogUtils.dazhiLog("建群失败----code=" + code);

                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                }
        );
    }

    /**
     * 创建高级群
     */
    public static void createAdvancedTeam(final Context context, List<ContactGroupBean.ContentBean> iContactList) {

        List<String> selectedAccounts = new ArrayList<>();
        List<String> disName = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        for (ContactGroupBean.ContentBean c : iContactList) {
            selectedAccounts.add(c.getAccId());
            disName.add(c.getName());
            buffer.append(c.getName());
            buffer.append("、");
        }
        String teamName = buffer.toString() + "等人";
        DialogMaker.showProgressDialog(context, "", true);
        // 创建群
        TeamTypeEnum type = TeamTypeEnum.Advanced;
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<>();
        fields.put(TeamFieldEnum.Name, teamName);
        NIMClient.getService(TeamService.class).createTeam(fields, type, "",
                selectedAccounts).setCallback(
                new RequestCallback<CreateTeamResult>() {
                    @Override
                    public void onSuccess(CreateTeamResult t) {
                        onCreateSuccess(context, t.getTeam());
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        String tip;
                        if (code == 801) {
                            tip = context.getString(R.string.over_team_member_capacity,
                                    DEFAULT_TEAM_CAPACITY);
                        } else if (code == 806) {
                            tip = context.getString(R.string.over_team_capacity);
                        } else {
                            tip = context.getString(R.string.create_team_failed);
                        }

                        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                }
        );
    }


    /**
     * 群创建成功回调
     */
    private static void onCreateSuccess(final Context context, final Team team) {
        if (team == null) {
            Log.e(TAG, "onCreateSuccess exception: team is null");
            return;
        }

        DialogMaker.dismissProgressDialog();
        Toast.makeText(context, R.string.create_team_success, Toast.LENGTH_SHORT).show();

        // 演示：向群里插入一条Tip消息，使得该群能立即出现在最近联系人列表（会话列表）中，满足部分开发者需求
        Map<String, Object> content = new HashMap<>(1);
        content.put("content", "成功创建高级群");
        IMMessage msg = MessageBuilder.createTipMessage(team.getId(), SessionTypeEnum.Team);
        msg.setRemoteExtension(content);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        config.enablePushNick = false;
        msg.setConfig(config);
        msg.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);

        // 发送后，稍作延时后跳转
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                NimUIKit.startTeamSession(context, team.getId()); // 进入创建的群
            }
        }, 50);
    }
}
