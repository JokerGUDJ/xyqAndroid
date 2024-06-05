package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jaeger.library.StatusBarUtil;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
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

public class SelectMeetingPeopleActivity extends BaseActivity implements View.OnClickListener {
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
    public static final String FLAG = "flag"; // 返回结果
    public static final String DATA_SOURCE = "data_source";
    public static final String SELECTED_MEMBER_LIST = "selected_member_list";
    private String options;
    private ArrayList<String> disableList;
    private boolean isVideo;
    private ContactGroupBean.ContentBean selector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.netease.nim.uikit.R.layout.nim_activity_contacts_select);
        findview();
        init();
    }

    private void findview() {
        bottomPanel = findViewById(com.netease.nim.uikit.R.id.rlCtrl);
        scrollViewSelected = findViewById(com.netease.nim.uikit.R.id.contact_select_area);
        mGridView = findViewById(com.netease.nim.uikit.R.id.contact_select_area_grid);
        mButtonSubmit = findViewById(com.netease.nim.uikit.R.id.button_select);
        letterHit = findViewById(com.netease.nim.uikit.R.id.tvSideBarHint);
        mIndexBar = findViewById(com.netease.nim.uikit.R.id.liv_index);
        head_back = findViewById(com.netease.nim.uikit.R.id.iv_head_back);
        emptyView = findViewById(com.netease.nim.uikit.R.id.meet_empty);
        refreshLayout = findViewById(com.netease.nim.uikit.R.id.refreshLayout);
        recycleMessageList = findViewById(com.netease.nim.uikit.R.id.recycle_message_list);
        contact_input_content = findViewById(com.netease.nim.uikit.R.id.contact_input_content_edit);
        mButtonSubmit.setOnClickListener(this);
        head_back.setOnClickListener(this);
    }

    private void init() {
        //初始化数据集合
        mList = new ArrayList<>();
        chooseList = new ArrayList<>();
        disableList=new ArrayList<>();
        Intent intent = getIntent();
        ArrayList temp = intent.<ContactGroupBean.ContentBean>getParcelableArrayListExtra(DATA_SOURCE);
        if(temp != null){
            mList.addAll(temp);
        }
        disableList.addAll(intent.getStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST));
        selector = intent.getParcelableExtra(SELECTED_MEMBER_LIST);
        if(selector != null){
            initSelector();
            chooseList.add(selector);
        }
        isVideo = intent.getBooleanExtra(ISVIDEO,false);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleMessageList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mActivity, com.netease.nim.uikit.R.anim.layout_animation_fall_down);
        recycleMessageList.setLayoutAnimation(animation);

        mAdapter = new ContactSeleteAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ContactGroupBean.ContentBean item = mList.get(position);
                boolean isFlag = item.isForPhone();
                if (isFlag) {
                    if(isContainContactGroupBean(item)){
                        removeContainContactGroupBean(item);
                        item.setForPhone(false);
                    }
                } else {
                    chooseList.clear();
                    chooseList.add(item);
                    resetContactGroup();
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
        recycleMessageList.addItemDecoration(mDecoration = new SuspensionDecoration(this, new ArrayList<>()));
        //如果add两个，那么按照先后顺序，依次渲染。
        recycleMessageList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化索引   有分页和搜索,暂时屏蔽
//        mIndexBar.setmPressedShowTextView(letterHit)//设置HintTextView
//                .setNeedRealIndex(true)//设置需要真实的索引
//                .setmLayoutManager(layoutManager)//设置RecyclerView的LayoutManager
//                .setSourceDatasAlreadySorted(true);
        recycleMessageList.setEmptyView(emptyView);
        recycleMessageList.setAdapter(mAdapter);
        mAvatarAdapter = new ContactSelectAvatarAdapter(SelectMeetingPeopleActivity.this, chooseList);
        mGridView.setAdapter(mAvatarAdapter);
        //notifySelectAreaDataSetChanged();
//        if (refreshLayout != null) {
//            refreshLayout.autoRefresh();
//        }
    }

    private void initSelector(){
        for(ContactGroupBean.ContentBean bean : mList){
            if(selector.getAccId().equals(bean.getAccId())){
                bean.setForPhone(true);
                break;
            }
        }
    }

    private void resetContactGroup(){
        for(ContactGroupBean.ContentBean bean : mList){
            bean.setForPhone(false);
        }
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

    /**
     * 点击确定开始建群
     *
     * @param count 添加人数
     * @return
     */

    private String getOKBtnText(int count) {
        String caption = getString(com.netease.nim.uikit.R.string.ok);
        int showCount = (count < 1 ? 0 : count);
        StringBuilder sb = new StringBuilder(caption);
        sb.append(" (");
        sb.append(showCount);
        sb.append(")");
        return sb.toString();
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
        if (i == com.netease.nim.uikit.R.id.button_select) {
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
                ToastUtils.shortToast(this, com.netease.nim.uikit.R.string.select_contact);

            }

        } else if (i == com.netease.nim.uikit.R.id.iv_head_back) {//直接返回关闭键盘
            KeybordUtils.closeKeybord(contact_input_content, SelectMeetingPeopleActivity.this);
            finishWithAnim();

        }

    }
}
