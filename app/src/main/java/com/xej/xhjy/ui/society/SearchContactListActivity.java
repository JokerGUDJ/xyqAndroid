package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.society.adapter.SearchContactListAdapter;
import com.xej.xhjy.ui.society.bean.ContactBean;
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
 * @class SearchContactListActivity 通讯录搜索
 * @Createtime 2018/6/22 10:49
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class SearchContactListActivity extends BaseActivity {
    final String TAG = "search_complany";
    @BindView(R.id.recycle_topic_list)
    CommonRecyclerView searchContactList;
    @BindView(R.id.titleview)
    TitleView titleView;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.head_back)
    ImageView mImageView;
    SearchContactListAdapter searchAdapter;
    private List<ContactBean.ContentBean> mList;
    private ClubLoadingDialog mDialog;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPages = 0, mAllPages;
    private String options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        titleView.setTitle("搜索");
        options = getIntent().getStringExtra("content");
        mDialog = new ClubLoadingDialog(this);
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
        searchAdapter = new SearchContactListAdapter(this, mList, options, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent(mActivity, ContactDetailSimpleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User", mList.get(position));//序列化
                intent.putExtras(bundle);//发送数据
                mActivity.startActivityWithAnim(intent);

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
        map.put("commitId", getIntent().getStringExtra("orgId"));
        map.put("options", options);
        String url = NetConstants.ADDRESS_BOOK;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                ContactBean bean = JsonUtils.stringToObject(jsonString, ContactBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<ContactBean.ContentBean> datas = bean.getContent();
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
                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
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
