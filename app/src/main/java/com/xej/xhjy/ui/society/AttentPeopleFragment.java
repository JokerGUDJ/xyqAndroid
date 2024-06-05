package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.adapter.AttentPeopleListAdapter;
import com.xej.xhjy.ui.society.bean.followPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dazhi
 * @class AttentPeopleFragment 关注的人页面
 * @Createtime 2018/11/28 15:54
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class AttentPeopleFragment extends BaseFragment {
    @BindView(R.id.recycle_im_list)
    CommonRecyclerView recycleImList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.meet_empty)
    View emptyView;
    AttentPeopleListAdapter mAdapter;
    private int mPages = 0, mAllPages;
    public List<followPerson.ContentBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_attent_people, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleImList.setLayoutManager(layoutManager);
        mAdapter = new AttentPeopleListAdapter(R.layout.item_attent_people, mList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, ContactDetailActivity.class);
                intent.putExtra("postId", mList.get(position).getAttId());
                intent.putExtra("followId", "1");
                mActivity.startActivityWithAnim(intent);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ClubDialog dialog = new ClubDialog(mActivity);
                dialog.setTitle("温馨提示");
                dialog.setMessage("确定取消关注该用户吗？");
                dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                dialog.setPositiveListener("确定", new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        String url = null;
                        String TAG = "follow_add_or_unfollow";
                        mActivity.addTag(TAG);
                        Map<String, String> maps = new HashMap<>();
                        maps.put("id", mList.get(position).getId());
                        url = NetConstants.POST_UNFOLLOW;
                        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack() {
                            @Override
                            public void onSucess(String jsonString) {
                                mList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String errorMsg) {

                            }
                        });

                    }
                });
                dialog.setNegativeListener("取消", new NegativeListener() {
                    @Override
                    public void onNegativeClick() {

                    }
                });
                dialog.show();


            }
        });
        recycleImList.setAdapter(mAdapter);
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                getFollowPersontData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getFollowPersontData(true);
            }
        });
        recycleImList.setEmptyView(emptyView);
        getFollowPersontData(false);
    }

    /**
     * 获取关注的人
     *
     * @param isLoadMore 是否是加载更多
     */
    private void getFollowPersontData(final boolean isLoadMore) {
        String TAG = "follow_person";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("tab", "0");
        map.put("pageNum", mPages + "");
        map.put("pageSize", "15");
        String url = NetConstants.FOLLOW_PERSON;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                followPerson person = JsonUtils.stringToObject(jsonString, followPerson.class);
                if (person != null) {
                    if (person.getCode().equals("0")) {
                        List<followPerson.ContentBean> datas = person.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            mList.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = person.getPage().getTotalPages();
                            if (!isLoadMore) {
                                recycleImList.scheduleLayoutAnimation();
                            }
                        } else {//无数据的情况
                            if (!isLoadMore) {
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        if (mPages == mAllPages) {
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, person.getMsg());
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
    public void onDestroyView() {
        super.onDestroyView();
        //测试是否有内存泄漏，发现没有
//        RefWatcher refWatcher = ClubApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
        unbinder.unbind();
    }
}
