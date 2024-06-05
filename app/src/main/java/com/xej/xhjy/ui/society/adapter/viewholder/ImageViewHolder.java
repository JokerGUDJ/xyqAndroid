package com.xej.xhjy.ui.society.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import com.xej.xhjy.R;
import com.xej.xhjy.ui.society.widgets.MultiImageView;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.adapter
 * @ClassName: ImageViewHolder
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageViewHolder extends CircleViewHolder {
    /**
     * 图片
     */
    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView) {
        super(itemView, TYPE_IMAGE);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if (multiImageView != null) {
            this.multiImageView = multiImageView;
        }
    }


}
