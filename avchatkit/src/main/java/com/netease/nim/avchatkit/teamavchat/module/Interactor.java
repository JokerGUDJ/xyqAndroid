package com.netease.nim.avchatkit.teamavchat.module;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.netease.nim.avchatkit.common.log.LogUtil;
import com.netease.nim.uikit.impl.preference.IMCache;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.video.AVChatTextureViewRenderer;

public class Interactor {

    private static final String TAG = "Interactor";

    public interface Status {

        int CAMERA = 0;//照相机界面

        int SHARE_SCREEN = 1;//屏幕共享
    }

    private final String account;

    private AVChatTextureViewRenderer renderer;//画布

    private int capturerType;//当前捕捉器模式

    private Context context;


    public Interactor(String account, Context context, int state) {
        this.account = account;
        this.renderer = new AVChatTextureViewRenderer(context);
        this.capturerType = state;
        this.context = context;

    }

    public String getAccount() {
        return account;
    }


    public AVChatTextureViewRenderer getRenderer() {
        return renderer;
    }


    public int getCapturerType() {
        return capturerType;
    }

    public void setCapturerType(int capturerType) {
        this.capturerType = capturerType;
    }


    public void release() {
        if (renderer.getParent() == null) {
            return;
        }
        removeFromParent();
        try {
            if (TextUtils.equals(account, IMCache.getAccount())) {
                AVChatManager.getInstance().setupLocalVideoRender(null, false,
                                                                  AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            } else {
                AVChatManager.getInstance().setupRemoteVideoRender(account, null, false,
                                                                   AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            }
        } catch (Throwable throwable) {
            renderer.release();
            LogUtil.e(TAG, "release render err : " + throwable.getMessage());
            throwable.printStackTrace();
        }
        this.renderer = new AVChatTextureViewRenderer(context);
    }


    public void removeFromParent() {
        if (renderer.getParent() == null) {
            return;
        }
        ((ViewGroup) renderer.getParent()).removeAllViews();
    }

}
