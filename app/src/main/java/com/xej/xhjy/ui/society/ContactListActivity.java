package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.netease.nim.uikit.business.team.suspension.DividerItemDecoration;
import com.netease.nim.uikit.business.team.suspension.IndexBar;
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
import com.xej.xhjy.ui.datePicker.Util;
import com.xej.xhjy.ui.society.adapter.ContactListAdapter;
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
 * @class ContactListActivity 通讯录页面
 * @Createtime 2018/11/21 10:15
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ContactListActivity extends BaseActivity {
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.recycle_contact_list)
    CommonRecyclerView recycleContactList;
    @BindView(R.id.meet_empty)
    EmptyView meetEmpty;
    @BindView(R.id.contact_input_content_edit)
    EditText contact_input_content;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.liv_index)
    IndexBar mIndexBar;
    private String options;
    private int mPages = 0, mAllPages;
    private ContactListAdapter mAdapter;
    private List<ContactBean.ContentBean> mList = new ArrayList<>();
    private com.mcxtzhang.indexlib.suspension.SuspensionDecoration mDecoration;
    private View edit_cancel;
    private TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ButterKnife.bind(this);
        meetEmpty.setText("暂无联系人");
        //刷新数据
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
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleContactList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, R.anim.layout_animation_fall_down);
        recycleContactList.setLayoutAnimation(animation);
        mDecoration = new SuspensionDecoration(this, mList);
        mDecoration.setColorTitleBg(Color.parseColor("#F8F8F8"));
        mDecoration.setColorTitleFont(Color.parseColor("#303030"));
        recycleContactList.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        //recycleContactList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        meetEmpty.setVisibility(View.VISIBLE);
        recycleContactList.setEmptyView(meetEmpty);
        mAdapter = new ContactListAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
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
        recycleContactList.setAdapter(mAdapter);
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }

        tv_cancel = findViewById(R.id.tv_cancel);
        edit_cancel = findViewById(R.id.edit_cancel);
        contact_input_content.setHint("搜索姓名或机构");
        contact_input_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //开始搜索
                    options = contact_input_content.getText().toString().trim();
                    if (TextUtils.isEmpty(options)) {
                        ToastUtils.shortToast(ContactListActivity.this, "请输入搜索内容");
                        return false;
                    }
                    Intent intent = new Intent(ContactListActivity.this, SearchContactListActivity.class);
                    intent.putExtra("content", options);
                    intent.putExtra("orgId", getIntent().getStringExtra("orgId"));
                    startActivityWithAnim(intent);
                    KeybordUtils.closeKeybord(contact_input_content, ContactListActivity.this);
                    return true;
                }
                return false;
            }
        });
        contact_input_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contact_input_content
                        .getLayoutParams();
                if(TextUtils.isEmpty(contact_input_content.getText().toString())){
                    params.width = Util.dpToPx(ContactListActivity.this,375);
                    tv_cancel.setVisibility(View.GONE);
                    edit_cancel.setVisibility(View.GONE);
                }else{
                    params.width = Util.dpToPx(ContactListActivity.this,298);
                    tv_cancel.setVisibility(View.VISIBLE);
                    edit_cancel.setVisibility(View.VISIBLE);
                }
                contact_input_content.setLayoutParams(params);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_input_content.setText("");
            }
        });
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_input_content.setText("");
            }
        });

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
                            mIndexBar.setmSourceDatas(mList)//设置数据
                                    .invalidate();
                            mDecoration.setmDatas(mList);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                recycleContactList.scheduleLayoutAnimation();
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


}
