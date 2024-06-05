package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {

    public ImageAction() {
        super(R.drawable.nim_message_plus_photo_normal, R.string.input_panel_photo, true);
    }
    @Override
    public void setIndex(int index) {
        super.setIndex(index);
    }
    @Override
    protected void onPicked(File file) {
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
//            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        } else {
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        }
        sendMessage(message);
    }
}

