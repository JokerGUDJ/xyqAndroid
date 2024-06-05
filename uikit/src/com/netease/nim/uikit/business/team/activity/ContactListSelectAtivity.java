package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.adapter.ContactSelectAvatarAdapter;
import com.netease.nim.uikit.business.team.adapter.ContactSeleteAdapter;
import com.netease.nim.uikit.business.team.suspension.DividerItemDecoration;
import com.netease.nim.uikit.business.team.suspension.IndexBar;
import com.netease.nim.uikit.utils.ImUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.HttpServer;
import com.xej.xhjy.common.http.interfaces.HttpCallBack;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.society
 * @ClassName: ContactListSelectAtivity
 * @Description: 联系人选择器，建群
 * @Author: lihy_0203
 * @CreateDate: 2019/7/1 下午4:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/1 下午4:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ContactListSelectAtivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout bottomPanel;
    HorizontalScrollView scrollViewSelected;
    GridView mGridView;
    private Button mButtonSubmit;
    TextView letterHit;
    IndexBar mIndexBar;
    ImageView head_back;
    View emptyView;
    SmartRefreshLayout refreshLayout;
    CommonRecyclerView recycleMessageList;
    EditText contact_input_content;
    private int mPages = 0, mAllPages;
    private ContactSeleteAdapter mAdapter;
    private List<ContactGroupBean.ContentBean> mList;
    private com.mcxtzhang.indexlib.suspension.SuspensionDecoration mDecoration;
    private ContactSelectAvatarAdapter mAvatarAdapter;
    private List<ContactGroupBean.ContentBean> chooseList;
    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果
    public static final String ISVIDEO = "isVideo";
    public static final String FLAG = "flag";
    public static final String SELECTED_MEMBER_LIST = "selected_member_list";
    private String options;
    private ArrayList<String> disableList;
    private boolean isVideo;
    private int flag = 0;//0,默认，1，选择参会人员，2，选择主持人, 3,选择主讲人


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_activity_contacts_select);
        findview();
        init();
    }

    private void findview() {
        bottomPanel = findViewById(R.id.rlCtrl);
        scrollViewSelected = findViewById(R.id.contact_select_area);
        mGridView = findViewById(R.id.contact_select_area_grid);
        mButtonSubmit = findViewById(R.id.button_select);
        letterHit = findViewById(R.id.tvSideBarHint);
        mIndexBar = findViewById(R.id.liv_index);
        head_back = findViewById(R.id.iv_head_back);
        emptyView = findViewById(R.id.meet_empty);
        refreshLayout = findViewById(R.id.refreshLayout);
        recycleMessageList = findViewById(R.id.recycle_message_list);
        contact_input_content = findViewById(R.id.contact_input_content_edit);
        mButtonSubmit.setOnClickListener(this);
        head_back.setOnClickListener(this);
    }

    private void init() {
        //初始化数据集合
        mList = new ArrayList<>();
        chooseList = new ArrayList<>();
        disableList=new ArrayList<>();
        flag = getIntent().getIntExtra(FLAG,0);
        ArrayList temp = getIntent().<ContactGroupBean.ContentBean>getParcelableArrayListExtra(SELECTED_MEMBER_LIST);
        if(temp != null){
            chooseList.addAll(temp);
            //mButtonSubmit.setText(getOKBtnText(chooseList.size()));
        }
//        //把自己加入进去
        if(flag == 1 || flag == 2){
            //创建一个自己
            ContactGroupBean.ContentBean owner = new ContactGroupBean.ContentBean(PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, ""),PerferenceUtils.get(AppConstants.User.NAME, ""),
                    PerferenceUtils.get(AppConstants.User.COMPLANY, ""));
            if(!isContainContactGroupBean(owner)){
                chooseList.add(owner);
            }
        }

        disableList.addAll(getIntent().getStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST));
        isVideo = getIntent().getBooleanExtra(ISVIDEO,false);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleMessageList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, R.anim.layout_animation_fall_down);
        recycleMessageList.setLayoutAnimation(animation);
        //刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPages = 0;
                contact_input_content.setText("");
                getDatas(false, "");
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getDatas(true, options);
            }
        });

        mAdapter = new ContactSeleteAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ContactGroupBean.ContentBean item = mList.get(position);
                boolean isFlag = item.isForPhone();
                if (isFlag) {
                    String accId =  item.getAccId();
                    if(flag == 2){
                        if(isContainContactGroupBean(item)){
                            //取消已选中
                            removeContainContactGroupBean(item);
                            item.setForPhone(false);
                            mAdapter.notifyItemChanged(position);
                        }
                    }else{
                        if (!disableList.contains(accId)){
                            removeContainContactGroupBean(item);
                            mList.get(position).setForPhone(false);
                            mAdapter.notifyItemChanged(position);
                        }else{
                            ToastUtils.shortToast(ContactListSelectAtivity.this,"已有成员不能取消");
                        }
                    }
                } else {
                    if(flag == 2){
                        //如果是会议，只选择前被其他都清楚
                        resetAllData();
                    }
                    chooseList.add(item);
                    item.setForPhone(true);
                }

                //mButtonSubmit.setText(getOKBtnText(chooseList.size()));
                mAdapter.notifyDataSetChanged();
                //notifySelectAreaDataSetChanged();
                LogUtils.dazhiLog("chooseList-->" + chooseList.size());

            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        mAdapter.notifyDataSetChanged();
        mDecoration = new SuspensionDecoration(this, mList);
        mDecoration.setColorTitleBg(Color.parseColor("#F8F8F8"));
        mDecoration.setColorTitleFont(Color.parseColor("#303030"));
        recycleMessageList.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        recycleMessageList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化索引   有分页和搜索,暂时屏蔽
//        mIndexBar.setmPressedShowTextView(letterHit)//设置HintTextView
//                .setNeedRealIndex(true)//设置需要真实的索引
//                .setmLayoutManager(layoutManager)//设置RecyclerView的LayoutManager
//                .setSourceDatasAlreadySorted(true);
        recycleMessageList.setEmptyView(emptyView);
        recycleMessageList.setAdapter(mAdapter);
        mAvatarAdapter = new ContactSelectAvatarAdapter(ContactListSelectAtivity.this, chooseList);
        mGridView.setAdapter(mAvatarAdapter);
        //notifySelectAreaDataSetChanged();
//        if (refreshLayout != null) {
//            refreshLayout.autoRefresh();
//        }
        contact_input_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //开始搜索
                    options = contact_input_content.getText().toString().trim();
                    if (TextUtils.isEmpty(options)) {
                        ToastUtils.shortToast(ContactListSelectAtivity.this, "请输入搜索内容");
                        return false;
                    }
                    //搜索关键字
                    getDatas(false, options);
                    //关闭键盘去搜索
                    contact_input_content.clearFocus();
                    KeybordUtils.closeKeybord(contact_input_content, ContactListSelectAtivity.this);
                    return true;
                }
                return false;
            }
        });
        getDatas(false, options);
    }

    private boolean isContainContactGroupBean(ContactGroupBean.ContentBean item){
        for(ContactGroupBean.ContentBean bean : chooseList){
            if(bean.getAccId().equals(item.getAccId())){
                return true;
            }
        }
        return false;
    }

    private void removeContainContactGroupBean(ContactGroupBean.ContentBean item){
        for(ContactGroupBean.ContentBean bean : chooseList){
            if(bean.getAccId().equals(item.getAccId())){
                chooseList.remove(bean);
                return;
            }
        }
        return;
    }

    private void resetAllData(){
        for(ContactGroupBean.ContentBean bean : mList){
            bean.setForPhone(false);
        }
        for(ContactGroupBean.ContentBean bean1 : chooseList){
            bean1.setForPhone(false);
        }
        chooseList.clear();
    }

    /**
     * 点击确定开始建群
     *
     * @param count 添加人数
     * @return
     */

    private String getOKBtnText(int count) {
        String caption = getString(R.string.ok);
        int showCount = (count < 1 ? 0 : count);
        StringBuilder sb = new StringBuilder(caption);
        sb.append(" (");
        sb.append(showCount);
        sb.append(")");
        return sb.toString();
    }

    /**
     * 数据请求
     */

    private void getDatas(final boolean isLoadMore, String options) {
        Map<String, String> maps = new HashMap<>();
        maps.put("option", options);
        maps.put("pageNum", mPages+"");
        maps.put("pageSize", "200");
        String TAG = "contact_list_select";
        mActivity.addTag(TAG);
        String url = ImUtils.CONTACT_LIST;
        HttpServer.doPostStringWithUrl(ContactListSelectAtivity.this, url, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                ContactGroupBean bean = JsonUtils.stringToObject(jsonString, ContactGroupBean.class);
                if (bean != null) {
                    if (bean.getCode().equals("0")) {
                        List<ContactGroupBean.ContentBean> datas = bean.getContent();
                        if (datas != null && datas.size() > 0) {
                            if (!isLoadMore) {
                                mList.clear();
                            }
                            for(int i = 0; i < datas.size();i++){
                                ContactGroupBean.ContentBean beans = datas.get(i);
                                String value=beans.getAccId();
                                for(int j = 0; j < disableList.size();j++){
                                    String temp = disableList.get(j);
                                    if(value.equals(temp)){
                                        datas.get(i).setForPhone(true);
                                    }
                                }
                                for(int k = 0; chooseList != null && k < chooseList.size();k++){
                                    ContactGroupBean.ContentBean temp = chooseList.get(k);
                                    if(value.equals(temp.getAccId())){
                                        datas.get(i).setForPhone(true);
                                    }
                                }
                            }

                            mList.addAll(datas);
                            mIndexBar.setmSourceDatas(mList)//设置数据
                                    .invalidate();
                            mDecoration.setmDatas(mList);
                            mAdapter.notifyDataSetChanged();
                            mAllPages = bean.getPage().getTotalPages();
                            if (!isLoadMore) {
                                recycleMessageList.scheduleLayoutAnimation();
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
                LogUtils.dazhiLog("json  errorMsg---------" + errorMsg);
            }
        });

    }

    //刷新数据以及页面
    private void notifySelectAreaDataSetChanged() {
        //计算mGridView宽高
        int converViewWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, this.getResources()
                .getDisplayMetrics()));
        ViewGroup.LayoutParams layoutParams = mGridView.getLayoutParams();
        layoutParams.width = converViewWidth * mAvatarAdapter.getCount();
        layoutParams.height = converViewWidth;
        mGridView.setLayoutParams(layoutParams);
        mGridView.setNumColumns(mAvatarAdapter.getCount());

        try {
            final int x = layoutParams.width;
            final int y = layoutParams.height;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollViewSelected.scrollTo(x, y);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAvatarAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_select) {
            if (chooseList.size() > 0) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (isVideo){
                    ArrayList<String> accounts = new ArrayList<>();
                    for (ContactGroupBean.ContentBean contentBean : chooseList) {
                        accounts.add(contentBean.getAccId());
                    }
                    intent.putStringArrayListExtra(RESULT_DATA, accounts);
                }else{
                    intent.putParcelableArrayListExtra(RESULT_DATA, (ArrayList<? extends Parcelable>) chooseList);
                    intent.putExtras(bundle);
                }
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            } else {
                ToastUtils.shortToast(this, R.string.select_contact);

            }

        } else if (i == R.id.iv_head_back) {//直接返回关闭键盘
            KeybordUtils.closeKeybord(contact_input_content, ContactListSelectAtivity.this);
            finishWithAnim();

        }

    }
}


