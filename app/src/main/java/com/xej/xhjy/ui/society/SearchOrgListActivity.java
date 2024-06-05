package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.society.adapter.SearchOrgListAdapter;
import com.xej.xhjy.ui.society.bean.OrgBean;
import com.xej.xhjy.ui.view.EmptyView;
import com.xej.xhjy.ui.view.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dazhi
 * @class SearchContactListActivity 通讯录机构搜索
 * @Createtime 2018/6/22 10:49
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class SearchOrgListActivity extends BaseActivity {
    final String TAG = "search_complany";
    @BindView(R.id.recycle_topic_list)
    CommonRecyclerView searchContactList;
    @BindView(R.id.titleview)
    TitleView titleView;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.head_back)
    ImageView mImageView;
    SearchOrgListAdapter searchAdapter;
    private List<OrgBean.ContentBean> mList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPages = 0, mAllPages;
    private String options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        titleView.setTitle("通讯录");
        options = getIntent().getStringExtra("content");
        mList = new ArrayList<>();
        meetEmpty.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getContactListData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getContactListData(true);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchContactList.setLayoutManager(layoutManager);
        searchAdapter = new SearchOrgListAdapter(this, mList, options, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                LoginCheckUtils.isCertification = true;
                LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                    @Override
                    public void onlogin() {
                        Intent intent = new Intent(mActivity, BranchOfListActivity.class);
                        intent.putExtra("orgId", mList.get(position).getId());
                        mActivity.startActivityWithAnim(intent);
                    }
                }, mActivity);
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        searchContactList.setEmptyView(meetEmpty);
        searchContactList.setAdapter(searchAdapter);
        refreshLayout.autoRefresh();
    }


    /**
     * 获取通讯录
     *
     * @param isLoadMore 是否是加载更多
     */
    private void getContactListData(final boolean isLoadMore) {
        String TAG = "contact_List";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", mPages + "");
        map.put("pageSize", "15");
        map.put("orgName", options);
        String url = NetConstants.ADDRESS_BOOK_ORG;
        LogUtils.dazhiLog("获取通讯录-" + map);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("通讯录----" + jsonString);
                OrgBean bean = JsonUtils.stringToObject(jsonString, OrgBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<OrgBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            searchAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                searchContactList.scheduleLayoutAnimation();
                            }
                        } else {//无数据的情况
                            if (!isLoadMore) {
                                mList.clear();
                                searchAdapter.notifyDataSetChanged();
                            }
                        }
                        if (mPages == mAllPages) {
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    }

                }
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
