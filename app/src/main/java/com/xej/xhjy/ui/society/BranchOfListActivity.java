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
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.society.adapter.BranchOfListAdapter;
import com.xej.xhjy.ui.society.bean.BranchOfBean;
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
 * @class SearchContactListActivity 专委会列表
 * @Createtime 2018/6/22 10:49
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class BranchOfListActivity extends BaseActivity {
    final String TAG = "search_complany";
    @BindView(R.id.recycle_topic_list)
    CommonRecyclerView searchContactList;
    @BindView(R.id.titleview)
    TitleView titleView;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.head_back)
    ImageView mImageView;
    BranchOfListAdapter branchOfListAdapter;
    private List<BranchOfBean.ContentBean> mList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String orgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        titleView.setTitle("通讯录");
        orgId = getIntent().getStringExtra("orgId");
        mList = new ArrayList<>();
        meetEmpty.setVisibility(View.VISIBLE);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mList!=null){
                    mList.clear();
                    getBranchOfListData();
                }
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchContactList.setLayoutManager(layoutManager);
        branchOfListAdapter = new BranchOfListAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent(mActivity, ContactList2Activity.class);
                intent.putExtra("commitId", mList.get(position).getId());
                intent.putExtra("orgId",orgId);
                mActivity.startActivityWithAnim(intent);
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        searchContactList.setEmptyView(meetEmpty);
        searchContactList.setAdapter(branchOfListAdapter);
        refreshLayout.autoRefresh();
    }


    /**
     * 获取专委会列表
     */
    private void getBranchOfListData() {
        String TAG = "barnch_of_list";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        String url = NetConstants.BRANCH_OF_LIST;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                refreshLayout.finishRefresh();
                LogUtils.dazhiLog("通讯录----" + jsonString);
                BranchOfBean bean = JsonUtils.stringToObject(jsonString, BranchOfBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<BranchOfBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            mList.addAll(datas);
                            branchOfListAdapter.notifyDataSetChanged();

                        }

                    } else {
                        ToastUtils.shortToast(mActivity, bean.getMsg());
                    }

                }

            }

            @Override
            public void onError(String errorMsg) {
                refreshLayout.finishRefresh();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
