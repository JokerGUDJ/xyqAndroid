package com.xej.xhjy.ui.society.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import com.xej.xhjy.R;

import cn.jzvd.JzvdStd;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.adapter
 * @ClassName: VideoViewHolder
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VideoViewHolder extends CircleViewHolder {
    public JzvdStd videoPlay;


    public VideoViewHolder(View itemView) {
        super(itemView, TYPE_VIDEO);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }

        viewStub.setLayoutResource(R.layout.viewstub_videobody);
        View subView = viewStub.inflate();
        JzvdStd videoPlay = (JzvdStd) subView.findViewById(R.id.videoplayer);
        if (videoPlay != null) {
            this.videoPlay = videoPlay;
        }

    }


}




