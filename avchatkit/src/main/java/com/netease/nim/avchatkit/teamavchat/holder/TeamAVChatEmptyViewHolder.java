package com.netease.nim.avchatkit.teamavchat.holder;


import android.view.View;
import android.view.ViewGroup;

import com.netease.nim.avchatkit.R;
import com.netease.nim.avchatkit.common.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.avchatkit.common.recyclerview.holder.BaseViewHolder;
import com.netease.nim.avchatkit.common.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.avchatkit.teamavchat.module.TeamAVChatItem;
import com.netease.nimlib.sdk.avchat.video.AVChatTextureViewRenderer;
import com.netease.nrtc.video.render.IVideoRender;
import com.xej.xhjy.common.utils.DeviceUtils;

/**
 * Created by huangjun on 2017/5/9.
 */

public class TeamAVChatEmptyViewHolder extends TeamAVChatItemViewHolderBase {
    public TeamAVChatEmptyViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected void inflate(BaseViewHolder holder) {

    }

    @Override
    protected void refresh(TeamAVChatItem data) {

    }


}
