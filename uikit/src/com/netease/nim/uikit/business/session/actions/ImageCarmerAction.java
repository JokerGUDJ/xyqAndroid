package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageCarmerAction extends PickImageAction {

    public ImageCarmerAction() {
        super(R.drawable.nim_message_plus_photo_pressed, R.string.input_panel_take, true);
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        sendMessage(message);
    }


}

