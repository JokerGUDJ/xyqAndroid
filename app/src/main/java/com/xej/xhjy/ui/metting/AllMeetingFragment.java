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

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.bean.XHMeeting;
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
import com.xej.xhjy.ui.adapter.RecycleViewAdapter;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.view.EmptyView;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllMeetingFragment extends TFragment {

    private String TAG = "offline_meeting_list";
    private SmartRefreshLayout mRefreshLayout;
    private EmptyView emptyView;
    private int mPages = 0, mAllPages;
    private List<XHMeeting> mList;
    private BaseActivity mActivity;
    private AllMeetingListAdapter mAdapter;
    private CommonSlideRecyclerView mRecyclerView;
    private long mLasttime = 0;
    private String meetId;
    private TextView tv_meeting_apply;

    public AllMeetingFragment(BaseActivity mActivity){
        this.mActivity = mActivity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_meeting, container, false);
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
        mAdapter = new AllMeetingListAdapter(mActivity, mList);

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
        map.put("pageSize","10");
        String url = NetConstants.ALL_MEETTING_LIST;
        //注册后待审核和被拒绝使用特殊的url
        if ("W".equals(AppConstants.USER_STATE)) {
            url = NetConstants.MEETTING_LIST_AUDIT;
        }
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("会议列表----" + jsonString);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                    if(jsonObject != null && "0".equals(jsonObject.optString("code"))){
                        org.json.JSONArray meets = jsonObject.optJSONArray("content");
                        mAllPages = Integer.valueOf(new JSONObject(jsonObject.optString("page")).optString("totalPages"));
                        List<XHMeeting> datas = new ArrayList<>();
                        for(int i = 0; meets != null && i < meets.length(); i++){
                            datas.add(JsonUtils.stringToObject(meets.get(i).toString(), XHMeeting.class));
                        }
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                PerferenceUtils.put(AppConstants.DATA_MEET_LIST, jsonString);
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
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
                    }else {
                        ToastUtils.shortToast(getActivity(), jsonObject.optString("msg"));
                    }
                    if (isLoadMore) {
                        mRefreshLayout.finishLoadMore();
                    } else {
                        mRefreshLayout.finishRefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
        intent.putExtra(WebPagerActivity.MEETTING_ID, str);
        startActivity(intent);
    }

    public void autoRefresh(){
        if(mRefreshLayout != null){
            mRefreshLayout.autoRefresh();
        }
    }
}
