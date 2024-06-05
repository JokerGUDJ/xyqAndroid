package com.xej.xhjy.ui.society.mvp.presenter;

import android.view.View;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.society.bean.CommentConfig;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.contract.CircleContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lihy_0203
 * @class CirclePresenter
 * @Createtime 2018/11/28 15:18
 * @description 社交圈交易层
 * @Revisetime
 * @Modifier
 */
public class CirclePresenter implements CircleContract.Presenter {
    private CircleContract.View view;
    private ClubLoadingDialog mLoadingDialog;

    public CirclePresenter(CircleContract.View view, ClubLoadingDialog dialog) {
        this.view = view;
        this.mLoadingDialog = dialog;
    }


    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(BaseActivity activity, final int circlePosition, String FavorId, String messageId) {
        String TAG = "post_list_favort";
        activity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("userId", FavorId);
        maps.put("messageId", messageId);
        String url = NetConstants.POST_SOCIALLIKE;
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
        RxHttpClient.doPostStringWithUrl(activity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                JSONObject js = null;
                try {
                    js = new JSONObject(jsonString);
                    if (js.optString("code").equals("0")) {
                        JSONObject content = js.getJSONObject("content");
                        PostListBean.ContentBean.SocialLikeBean likeBean = new PostListBean.ContentBean.SocialLikeBean();
                        likeBean.setCommitId(content.optString("commitId"));
                        likeBean.setCreatTime(content.optString("creatTime"));
                        likeBean.setId(content.optString("id"));
                        likeBean.setLikeName(content.optString("likeName"));
                        likeBean.setMessageId(content.optString("messageId"));
                        likeBean.setMessageId(content.optString("mobile"));
                        likeBean.setPeopleId(content.optString("peopleId"));
                        likeBean.setStt(content.optString("stt"));
                        likeBean.setUserId(content.optString("userId"));
                        if (view != null) {
                            view.update2AddFavorite(circlePosition, likeBean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });

    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(BaseActivity activity, final int circlePosition, final String favortId) {
        String TAG = "post_delete_favort";
        activity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("id", favortId);
        String url = NetConstants.POST_SOCIALUNLIKE;
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
        RxHttpClient.doPostStringWithUrl(activity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if (code.equals("0")) {
                        if (view != null) {
                            view.update2DeleteFavort(circlePosition, favortId);
                        }
                    }
                } catch (JSONException js) {

                }


            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });

    }

    /**
     * @param config CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 评论
     * id被回复内容ID  （repleyList 中的id）
     * utterId 被回复人的ID
     * messageId 帖子ID
     * posterId 发帖人ID
     */
    public void addComment(BaseActivity activity, String content, final CommentConfig config) {
        if (config == null) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
        String TAG = "post_add_comment";
        Map<String, String> maps = new HashMap<>();
        maps.put("content", content);
        maps.put("fatherId", config.fatherId); //被回复内容ID  id
        maps.put("replyId", config.replyUser); //被回复人的ID  utterId
        maps.put("messageId", config.messageId); //帖子ID     id
        maps.put("userId", config.posterId);  //发帖人ID    posterId
        String url = NetConstants.POST_COMMENT;
        LogUtils.dazhiLog("评论====" + maps);
        RxHttpClient.doPostStringWithUrl(activity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    JSONArray cont = jsonObject.getJSONArray("content");
                    List<PostListBean.ContentBean.ReplyListBean> beans = new ArrayList<>();
                    PostListBean.ContentBean.ReplyListBean newItem = null;
                    if (code.equals("0")) {
                        for (int i = 0; i < cont.length(); i++) {
                            newItem = new PostListBean.ContentBean.ReplyListBean();
                            newItem.setContent(cont.getJSONObject(i).optString("content"));
                            newItem.setFrom(cont.getJSONObject(i).optString("from"));
                            newItem.setId(cont.getJSONObject(i).optString("id"));
                            newItem.setUtterId(cont.getJSONObject(i).optString("utterId"));
                            newItem.setTo(cont.getJSONObject(i).optString("to"));
                            beans.add(newItem);
                        }
                        if (view != null) {
                            view.update2AddComment(config.circlePosition, beans);
                        }
                    }
                } catch (JSONException js) {

                }


            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });

    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(BaseActivity activity, final int circlePosition, final String commentId) {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
        String TAG = "post_delete_comment";
        Map<String, String> maps = new HashMap<>();
        maps.put("id", commentId);
        String url = NetConstants.POST_DELETE_COMMENT;
        RxHttpClient.doPostStringWithUrl(activity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if (code.equals("0")) {
                        if (view != null) {
                            view.update2DeleteComment(circlePosition, commentId);
                        }
                    }
                } catch (JSONException js) {

                }


            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });

    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        if (view != null) {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
    }


    /**
     * 清除对外部对象的引用，防止内存泄露。
     */
    public void recycle() {
        this.view = null;
        //将遮罩层置空
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
    }
}
