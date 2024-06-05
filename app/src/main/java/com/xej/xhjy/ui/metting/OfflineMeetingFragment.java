package com.xej.xhjy.ui.metting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.netease.nim.uikit.common.fragment.TFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonSlideRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.view.EmptyView;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflineMeetingFragment extends TFragment {

    private String TAG = "offline_meeting_list";
    private SmartRefreshLayout mRefreshLayout;
    private EmptyView emptyView;
    private int mPages = 0, mAllPages;
    private List<MeettingListBean.ContentBean> mList;
    private BaseActivity mActivity;
    private MeettingListAdapter mAdapter;
    private CommonSlideRecyclerView mRecyclerView;
    private long mLasttime = 0;
    private String meetId;
    private TextView tv_meeting_apply;
    private int type = 0;//0, 鑫合会议，1，我的会议

    public OfflineMeetingFragment(BaseActivity mActivity, int type){
        this.mActivity = mActivity;
        this.type = type;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_meeting, container, false);
    }

    private void findViews(){
        mRefreshLayout = findView(R.id.refreshLayout);
        emptyView = findView(R.id.meet_empty);
        //刷新数据
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getMeetingListData(false);
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getMeetingListData(true);
            }
        });
        mRecyclerView = findView(R.id.recycle_meet_list);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, R.anim.layout_animation_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mList = new ArrayList<>();
        mAdapter = new MeettingListAdapter(mActivity, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {//item单击
                if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                    return;
                mLasttime = System.currentTimeMillis();
                //点击是校验权限
                meetId = mList.get(position).getId();
                if (meetId != null) {
                    LoginCheckUtils.isCertification = true;
                    LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                        @Override
                        public void onlogin() {
                            checkQuery(meetId, mList.get(position).getWhetherJoin());
                        }
                    }, mActivity);
                } else {
                    ToastUtils.shortToast(mActivity, "会议Id为空");
                }
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        }, type);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(emptyView);
        getMeetingListData(false);
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
        String url = type == AppConstants.XH_MEETING?NetConstants.NEW_MEETTING_LIST:NetConstants.MY_OFFLINE_MEETING;
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
                        ToastUtils.shortToast(getActivity(), bean.getMsg());
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
     * 校验接口
     */
    private void checkQuery(final String str, String whetherJoin) {
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("meetId", str);
        String url = NetConstants.NEW_MEETTING_CHECKED;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                            "eventViewMeet", "meetId="+str);
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        LogUtils.dazhiLog("会议校验----" + jsonString);
                        toWebViewShow(str, whetherJoin);
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
    private void toWebViewShow(String str, String whetherJoin) {
        Intent intent = new Intent(mActivity, WebPagerActivity.class);
        if(type == 0){
            if("Y".equals(whetherJoin)){
                intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
            }else{
                intent.putExtra(WebPagerActivity.LOAD_URL, "MeetDetail");
            }
        }else{
            intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
        }
        intent.putExtra(WebPagerActivity.MEETTING_ID, str);
        startActivity(intent);
    }

    public void autoRefresh(){
        if(mRefreshLayout != null){
            mRefreshLayout.autoRefresh();
        }
    }
}
