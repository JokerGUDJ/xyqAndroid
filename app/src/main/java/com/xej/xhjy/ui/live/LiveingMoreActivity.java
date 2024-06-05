package com.xej.xhjy.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.api.model.SimpleCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.LiveingAuthBean;
import com.xej.xhjy.bean.LiveingMoreBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.society.adapter.XingLiveingAdapter;
import com.xej.xhjy.ui.web.LiveingWebview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LiveingMoreActivity extends BaseActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private XingLiveingAdapter adapter;
    private int totalPage, pageNum = 0;
    private String liveStatus, source;
    private LinearLayout ll_no_data, ll_back;
    private ImageView img_back;
    private List<LiveingMoreBean.ContentBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liveing_more_activity);
        initView();
        initData();
    }

    private void initView(){
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 0;
                getLiveingMoreInfo(false);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getLiveingMoreInfo(true);
            }
        });
        ll_no_data = findViewById(R.id.ll_no_data);
    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            liveStatus = intent.getStringExtra("liveStatus");
            source = intent.getStringExtra("source");
        }
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new XingLiveingAdapter(LiveingMoreActivity.this, R.layout.item_liveing_more, new SimpleCallback<LiveingMoreBean.ContentBean>() {
            @Override
            public void onResult(boolean success, LiveingMoreBean.ContentBean result, int code) {
                if(success){
                    queryAuthority(result);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        getLiveingMoreInfo(false);
    }

    //查询权限
    private void queryAuthority(LiveingMoreBean.ContentBean contentBean){
        String TAG = "get_authority";
        mActivity.addTag(TAG);
        HashMap<String ,String> params = new HashMap<>();
        params.put("liveId", contentBean.getId());
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_VIEW_AUTHORITY, TAG, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    if(!TextUtils.isEmpty(jsonString)){
                        LiveingAuthBean bean = JsonUtils.stringToObject(jsonString, LiveingAuthBean.class);
                        if(bean != null){
                            if("0".equals(bean.getCode())){
                                LiveingAuthBean.Content content = bean.getContent();
                                if(content != null){
                                    Intent intent = new Intent(LiveingMoreActivity.this, LiveingWebview.class);
                                    if("1".equals(content.getLiveStatus())){
                                        intent.putExtra(LiveingWebview.LOAD_URL, NetConstants.BASE_IP+"xhyjcms/mobile/index.html#/livescreamdetail");
                                    }else{
                                        intent.putExtra(LiveingWebview.LOAD_URL, content.getUrl()+content.getViewUrlPath()+content.getParameters());
                                    }
                                    intent.putExtra("liveId", contentBean.getId());
                                    intent.putExtra("name",contentBean.getName());
                                    intent.putExtra("coverImage", contentBean.getCoverImage());
                                    intent.putExtra("subName", contentBean.getAnnounCement());
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(LiveingMoreActivity.this, errorMsg);
            }
        });
    }

    private void getLiveingMoreInfo(final boolean isLoadMore){
        String TAG = "get_liveing_more_info";
        mActivity.addTag(TAG);
        HashMap<String, String> params = new HashMap<>();
        params.put("liveStatus", liveStatus);
        params.put("pageNum", pageNum+"");
        params.put("pageSize", "10");
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_LIVEING_FOR_PAGE, TAG, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    LiveingMoreBean bean = JsonUtils.stringToObject(jsonString, LiveingMoreBean.class);
                    if(bean != null){
                        if("0".equals(bean.getCode())){
                            LiveingMoreBean.PageInfo pageInfo = bean.getPage();
                            if(pageInfo != null){
                                totalPage = pageInfo.getTotalPages();
                            }
                            if (!isLoadMore) {
                                beanList.clear();
                            }
                            beanList.addAll(bean.getContent());
                            adapter.setDatas(beanList);
                            adapter.notifyDataSetChanged();
                            if (!isLoadMore) {
                                recyclerView.scheduleLayoutAnimation();
                                refreshLayout.finishRefresh();
                            }else{
                                refreshLayout.finishLoadMore();
                            }
                            refreshLayout.setVisibility(View.VISIBLE);
                            if (pageNum == totalPage) {
                                refreshLayout.setNoMoreData(true);
                            } else {
                                refreshLayout.setNoMoreData(false);
                            }
                        }else{
                            refreshLayout.setVisibility(View.GONE);
                            ll_no_data.setVisibility(View.VISIBLE);
                            ToastUtils.shortToast(LiveingMoreActivity.this, bean.getMsg());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                refreshLayout.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                ToastUtils.shortToast(LiveingMoreActivity.this, errorMsg);
            }
        });
    }

    private void goBack(){
        if("external".equals(source)){
            //外部分享进入的直播页面，返回到更多页面
            Intent intent = new Intent(LiveingMoreActivity.this, XingLiveingActivity.class);
            intent.putExtra("source","external");//外部跳转到更多聚合页标识
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
