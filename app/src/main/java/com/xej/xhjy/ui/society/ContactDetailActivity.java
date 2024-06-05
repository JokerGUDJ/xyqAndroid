package com.xej.xhjy.ui.society;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.adapter.ContactDetailAdatper;
import com.xej.xhjy.ui.society.bean.PersonalInfoBean;
import com.xej.xhjy.ui.society.bean.PostListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class 个人主页
 * @Createtime 2018/11/23 10:21
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ContactDetailActivity extends BaseActivity {
    ContactDetailAdatper mAdapter;
    View mHeaderView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.iv_personal_follow)
    ImageView iv_personal_follow;
    @BindView(R.id.head_icon)
    ImageView head_icon;
    @BindView(R.id.head_back)
    ImageView head_back;
    @BindView(R.id.tv_personal_follow)
    TextView tv_personal_follow;
    @BindView(R.id.recycle_committee_list)
    CommonRecyclerView recycleContactList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_complany)
    TextView tv_complany;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_follow_huati)
    TextView tv_follow_huati;
    //    @BindView(R.id.titleview)
//    TitleView titleview;
    @BindView(R.id.tv_follow_ren)
    TextView tv_follow_ren;
    @BindView(R.id.tv_mobilephone)
    TextView tv_mobilephone;
    @BindView(R.id.tv_tel)
    TextView tv_tel;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_address)
    TextView tv_address;
    //    @BindView(R.id.titleview_ll)
//    LinearLayout titleviewLl;
    @BindView(R.id.ll_contact_follow)
    LinearLayout ll_contact_follow;
    @BindView(R.id.ll_contact_chat)
    LinearLayout ll_contact_chat;
    int scrollHeight = 0;
    private List<PostListBean.ContentBean> mList;
    private int page = 0;
    private int allPage = 0;
    private String posterId, followId;
    private PersonalInfoBean bean;
    private boolean isFollows, isMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);
        initView();
        personalInfo();
        postData(false);
    }

    private void initView() {
        //由于列表和头部是两个接口，暂时先注释了，有时间修改
//        View committeeEmpty = LayoutInflater.from(this).inflate(R.layout.view_empty, (ViewGroup) recycleContactList.getParent(), false);
//        mHeaderView = LayoutInflater.from(this).inflate(R.layout.view_contact_head, null);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recycleContactList.setLayoutManager(layoutManager);
//        mAdapter.addHeaderView(mHeaderView);


        mList = new ArrayList<>();
        mAdapter = new ContactDetailAdatper(this, "1", mList, null);
        posterId = getIntent().getStringExtra("postId");
        followId = getIntent().getStringExtra("followId");
        //0未关注 1已关注

        isFollows = followId.equals("1") ? true : false;
        if (!TextUtils.isEmpty(posterId)) {
            if (PerferenceUtils.get(AppConstants.User.IM_POST_ID, "").equals(posterId)) {
                isFollows = false;
                isMine = true;
            } else {
                isMine = false;
            }
        }


        if (!isFollows) {
            tv_personal_follow.setTextColor(Color.parseColor("#73787D"));
            tv_personal_follow.setText("关注");
            iv_personal_follow.setImageResource(R.drawable.ic_love_empty);
        } else {
            tv_personal_follow.setTextColor( getResources().getColor(R.color.red));
            tv_personal_follow.setText("已关注");
            iv_personal_follow.setImageResource(R.drawable.ic_love_full);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleContactList.setLayoutManager(layoutManager);
        recycleContactList.setAdapter(mAdapter);
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                postData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                if (page == allPage) {
                    refreshLayout.setNoMoreData(true);
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                postData(true);
            }
        });

        //设置其滑动事件
        int baseHeight = GenalralUtils.dp2px(mActivity, 46) + AppConstants.STATUS_BAR_HEIGHT;
        recycleContactList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                scrollHeight += dy;
                float alpha = 0;
                if (scrollHeight >= baseHeight * 2) {
                    //完全不透明
                    alpha = 1;
                } else if (scrollHeight < baseHeight) {
                    alpha = 0;
                } else {
                    //产生渐变效果
                    alpha = (scrollHeight - baseHeight) / (baseHeight * 1.0f);
                }
//                titleviewLl.setAlpha(alpha);
                //设置其透明度
//                LogUtils.dazhiLog("scrollHeight------>" + scrollHeight);

                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    /**
     * 初始化数据
     */
    private void postData(final boolean isMore) {
        String TAG = "committee_post_list";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("posterId", posterId);
        maps.put("pageNum", Integer.toString(page));
        maps.put("pageSize", "10");
        String url = NetConstants.POST_PERSONAL_LIST;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                PostListBean bean = JsonUtils.stringToObject(jsonString, PostListBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<PostListBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            allPage = bean.getPage().getTotalPages();
                            if (!isMore) {
                                recycleContactList.scheduleLayoutAnimation();
                            }
                        } else {//无数据的情况
                        }
                        if (page == allPage) {
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
                    }

                }
                if (isMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }

            }

            @Override
            public void onError(String errorMsg) {
            }
        });


    }

    private void personalInfo() {
        String TAG = "post_personal_info";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("userId", posterId);
//        maps.put("commitId",)
        String url = NetConstants.POST_PERSONAL_INFO;
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                bean = JsonUtils.stringToObject(jsonString, PersonalInfoBean.class);
                if (bean != null && bean.getCode().equals("0")) {
                    tv_name.setText(bean.getContent().getUserName());
                    tv_complany.setText(bean.getContent().getOrgAllName());
                    tv_job.setText(bean.getContent().getDepartment() + "  " + bean.getContent().getJob());
                    tv_follow_ren.setText(bean.getContent().getConPeople()+"");
                    tv_follow_huati.setText( bean.getContent().getConTopic()+"");
                    String phone = bean.getContent().getPhone();
                    String telPhone = bean.getContent().getTelPhone();
                    String email = bean.getContent().getEMail();
                    String address = bean.getContent().getAddress();
                    boolean mobilePhone = TextUtils.isEmpty(phone);
                    boolean tel = TextUtils.isEmpty(telPhone);
                    boolean isEmail =TextUtils.isEmpty(email);
                    boolean isAddress = TextUtils.isEmpty(address);
                    tv_mobilephone.setText(mobilePhone ? "/" : phone);
                    tv_tel.setText(tel ? "/" : telPhone);
                    tv_email.setText(isEmail ? "/" :email);
                    tv_address.setText(isAddress ? "/" :address);
                    String url = NetConstants.HEAD_IMAG_URL + bean.getContent().getUserId() + ".jpg";
                    ImageLoadUtils.showHttpImageCycleNoCache(ContactDetailActivity.this, url, head_icon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
                }
            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    /**
     * 返回上一页
     */
    @OnClick(R.id.head_back)
    void goBack() {
        finishWithAnim();
    }

    @OnClick(R.id.ll_contact_follow)
    void goFollow() {
        if (isFollows) {
            ClubDialog dialog = new ClubDialog(ContactDetailActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("确定取消关注吗？");
            dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
            dialog.setPositiveListener("确定", new PositiveListener() {
                @Override
                public void onPositiveClick() {
                    followPost();
                }
            });
            dialog.setNegativeListener("取消", new NegativeListener() {
                @Override
                public void onNegativeClick() {

                }
            });
            dialog.show();
        } else {
            if (!isMine) {
                followPost();
            } else {
                ToastUtils.shortToast(this, "自己不能关注自己");
            }

        }

    }

    @OnClick(R.id.ll_contact_chat)
    void goChat() {
        String TAG = "query_phone_im_account";
        Map<String, String> maps = new HashMap<>();
        maps.put("phone", bean.getContent().getPhone());
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_PHONE_IM_ACCOUNT, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if ("0".equals(code)) {
                        String accId = jsonObject.getJSONObject("content").optString("accId");
                        if (!TextUtils.isEmpty(accId)) {
                            NimUIKit.startP2PSession(mActivity, accId);
                        } else {
                            ToastUtils.shortToast(mActivity, "账号为空");
                        }

                    } else {
                        ToastUtils.shortToast(mActivity, "账号查询失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "账号查询失败");
            }
        });

    }

    /**
     * 关注或取消关注
     * 0关注人  1关注话题
     */
    private void followPost() {
        String url = null;
        String TAG = "post_add_or_delete";
        mActivity.addTag(TAG);
        Map<String, String> maps = new HashMap<>();
        maps.put("attId", posterId);
        maps.put("tab", "0");
        if (isFollows) {
            url = NetConstants.POST_UNFOLLOW;
        } else {
            url = NetConstants.POST_FOLLOW;
        }
        LogUtils.dazhiLog("关注" + maps);
        RxHttpClient.doPostStringWithUrl(this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String code = jsonObject.optString("code");
                    if ("0".equals(code)) {
                        if (isFollows) {
                            tv_personal_follow.setTextColor(Color.parseColor("#73787D"));
                            tv_personal_follow.setText("关注");
                            iv_personal_follow.setImageResource(R.drawable.ic_love_empty);
                            isFollows = false;
                        } else {
                            tv_personal_follow.setTextColor( getResources().getColor(R.color.red));
                            tv_personal_follow.setText("已关注");
                            iv_personal_follow.setImageResource(R.drawable.ic_love_full);
                            isFollows = true;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

}
