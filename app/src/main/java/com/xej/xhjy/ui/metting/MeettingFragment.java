package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.common.base.BaseFragmentForViewpager;
import com.xej.xhjy.common.service.AppService;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.receiver.NetWorkStatusEvent;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.view.EmptyView;
import com.xej.xhjy.ui.view.TitleView;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.prototypez.appjoint.AppJoint;

/**
 * @author dazhi
 * @class MettingFragment
 * @Createtime 2018/6/12 14:51
 * @description 会议Fragment
 * @Revisetime
 * @Modifier
 */
public class MeettingFragment extends BaseFragmentForViewpager {
    private final String TAG = "meetting_list";
    private CommonRecyclerView mRecyclerView;
    private MeettingListAdapter mAdapter;
    private List<MeettingListBean.ContentBean> mList;
    private SmartRefreshLayout mRefreshLayout;
    private int mPages = 0, mAllPages;
    private EmptyView emptyView;
    private TitleView mTitleView;
    private String meetId;
    private LinearLayout ll_av_meeting;
    private static final int REQUEST_VIDEO_CONTACT = 20213;
    private long mLasttime = 0;

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_metting, null);
        mTitleView = mRootView.findViewById(R.id.titleview);
//        ll_av_meeting = mTitleView.findViewById(R.id.ll_av_meeting);
//        ll_av_meeting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<String> disableAccounts = new ArrayList<>();
//                Intent intent = new Intent(mActivity, ContactListSelectAtivity.class);
//                intent.putExtra(ContactListSelectAtivity.FLAG, "1");
//                intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST, disableAccounts);
//                startActivityForResultWithAnim(intent, REQUEST_VIDEO_CONTACT);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.isClubManager() || AppConstants.isComManager()) {
            //ll_av_meeting.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //ll_av_meeting.setVisibility(View.GONE);
    }

    /**
     * 校验接口
     */
    private void checkQuery(final String str) {
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("meetId", str);
        String url = NetConstants.NEW_MEETTING_CHECKED;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        LogUtils.dazhiLog("会议校验----" + jsonString);
                        toWebViewShow(str);
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

    /**
     * 跳转登录
     *
     * @param str
     */
    private void toWebViewShow(String str) {
        Intent intent = new Intent(mActivity, WebPagerActivity.class);
        intent.putExtra(WebPagerActivity.LOAD_URL, "MeetDetail");
        intent.putExtra(WebPagerActivity.MEETTING_ID, str);
        startActivity(intent);
    }

    /**
     * 初始化要加载的数据
     */
    @Override
    public void initDatas() {
        mRefreshLayout.autoRefresh();
    }


    /**
     * 获取会议列表
     *
     * @param isLoadMore 是否是加载更多
     */
    private void getMeetingListData(final boolean isLoadMore) {
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", mPages + "");
        String url = NetConstants.NEW_MEETTING_LIST;
        //注册后待审核和被拒绝使用特殊的url
        if ("W".equals(AppConstants.USER_STATE)) {
            url = NetConstants.MEETTING_LIST_AUDIT;
        }
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("会议列表----" + jsonString);
                MeettingListBean bean = JsonUtils.stringToObject(jsonString, MeettingListBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<MeettingListBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                PerferenceUtils.put(AppConstants.DATA_MEET_LIST, jsonString);
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                mRecyclerView.scheduleLayoutAnimation();
                            }
                        } else {  //无数据的情况
                            if (!isLoadMore) {
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                                if (AppConstants.IS_LOGIN) {
                                    emptyView.setText("近期无会议安排");
                                } else {
                                    emptyView.setText("请先登录后查看会议信息");
                                }
                                PerferenceUtils.put(AppConstants.DATA_MEET_LIST, "");
                            }
                        }
                        if (mPages == mAllPages) {
                            mRefreshLayout.setNoMoreData(true);
                        } else {
                            mRefreshLayout.setNoMoreData(false);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
                    }

                }
                if (isLoadMore) {
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (isLoadMore) {
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishRefresh();
                }
            }
        });
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        mRefreshLayout.autoRefresh();
    }

    /**
     * 收到登出的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(LoginOutEvent event) {
        mRefreshLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        LogUtils.dazhiLog("meet收到是否有消息----------" + event.getHasMessage());
        //mTitleView.setNewMessageVisibile(event.getHasMessage());
    }

    /**
     * 收到网络状态变化消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkEvent(NetWorkStatusEvent event) {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
