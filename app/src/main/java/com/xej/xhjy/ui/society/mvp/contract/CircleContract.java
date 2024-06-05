package com.xej.xhjy.ui.society.mvp.contract;


import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.ui.society.bean.CommentConfig;
import com.xej.xhjy.ui.society.bean.PostListBean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.mvp.contract
 * @ClassName: CircleContract
 * @Description:
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface CircleContract {

    interface View {

        void update2AddFavorite(int circlePosition, PostListBean.ContentBean.SocialLikeBean bean);

        void update2DeleteFavort(int circlePosition, String favortId);

        void update2AddComment(int circlePosition, List<PostListBean.ContentBean.ReplyListBean> addItem);

        void update2DeleteComment(int circlePosition, String commentId);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

    }

    interface Presenter  {

        void addFavort(BaseActivity activity, final int circlePosition, String FavorId, String messageId);

        void deleteFavort(BaseActivity activity, final int circlePosition, final String favortId);

        void deleteComment(BaseActivity activity, final int circlePosition, final String commentId);

    }


}
