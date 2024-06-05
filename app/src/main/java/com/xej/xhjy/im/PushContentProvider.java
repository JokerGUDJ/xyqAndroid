package com.xej.xhjy.im;

import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.im
 * @ClassName: PushContentProvider
 * @Description: 1.自定义的推送文案
 * 2.自定义推送 payload 实现特定的点击通知栏跳转行为
 * 如果自定义文案和payload，请开发者在各端发送消息时保持一致。
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class PushContentProvider implements CustomPushContentProvider {

    @Override
    public String getPushContent(IMMessage message) {
        return getContent(message);
    }

    @Override
    public Map<String, Object> getPushPayload(IMMessage message) {
        return getPayload(message);
    }

    private String getContent(IMMessage message) {
        if (message == null) {
            return null;
        }
        NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(NimUIKit.getAccount());
        if (userInfo == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(NimUIKit.getAccount(), null);
        }
        String nick = userInfo == null ? "" : userInfo.getName();
        if (message.getSessionType() == SessionTypeEnum.Team) {
            Team team = NIMClient.getService(TeamService.class).queryTeamBlock(message.getSessionId());
//            String teamName = team == null ? "" : team.getName();
//            return String.format("(群：%s) ", teamName) + createDefalutContent(nick, message);
            return createDefalutContent(nick, message);
        } else {
            return createDefalutContent(nick, message);
        }
    }

    private Map<String, Object> getPayload(IMMessage message) {
        if (message == null) {
            return null;
        }
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("sessionType", message.getSessionType().getValue());
        if (message.getSessionType() == SessionTypeEnum.Team) {
            payload.put("sessionID", message.getSessionId());
        } else if (message.getSessionType() == SessionTypeEnum.P2P) {
            payload.put("sessionID", message.getFromAccount());
        }

        return payload;
    }

    private String createDefalutContent(String nick, IMMessage message) {
        if (message == null) {
            return null;
        }
        if (message.getMsgType() == MsgTypeEnum.text || !TextUtils.isEmpty(message.getContent())) {
            if (message.getSessionType() == SessionTypeEnum.Team) {
                return "收到了一条来自群的消息";
            } else {
                return "收到了一条来自" + message.getFromNick() + "的消息";
            }
        }

        NimStrings strings = new NimStrings();
        switch (message.getMsgType()) {
            case image:
                return String.format(strings.status_bar_image_message, nick);
            case audio:
                return String.format(strings.status_bar_audio_message, nick);
            case video:
                return String.format(strings.status_bar_video_message, nick);
            case file:
                return String.format(strings.status_bar_file_message, nick);
            case location:
                return String.format(strings.status_bar_location_message, nick);
            case custom:
                return String.format(strings.status_bar_custom_message, nick);
            default:
                return String.format(strings.status_bar_unsupported_message, nick);
        }
    }
}
