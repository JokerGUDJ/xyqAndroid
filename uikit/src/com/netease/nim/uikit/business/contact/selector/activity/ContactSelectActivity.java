package com.netease.nim.uikit.business.contact.selector.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.contact.core.item.ContactItemFilter;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
import com.netease.nim.uikit.business.team.adapter.ContactSelectAvatarAdapter;
import com.netease.nim.uikit.business.team.adapter.ContactSeleteAdapter;
import com.netease.nim.uikit.business.team.suspension.DividerItemDecoration;
import com.netease.nim.uikit.business.team.suspension.IndexBar;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.ToastHelper;
import com.netease.nim.uikit.utils.ImUtils;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.HttpServer;
import com.xej.xhjy.common.http.interfaces.HttpCallBack;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ContactSelectActivity extends BaseActivity implements View.OnClickListener {
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
    public static final String EXTRA_DATA = "EXTRA_DATA"; // 请求数据：Option
    public static final String ALL_LIST = "select_list";
    public static final String FLAG = "flag"; // 返回结果
    private String options;
    private ContactSelectActivity.Option option;
    private ArrayList<String> disableList;

    /**
     * 联系人选择器配置可选项
     */
    public enum ContactSelectType {
        BUDDY,
        TEAM_MEMBER,
        TEAM
    }

    public static class Option {

        /**
         * 联系人选择器中数据源类型：好友（默认）、群、群成员（需要设置teamId）
         */
        public ContactSelectActivity.ContactSelectType type = ContactSelectActivity.ContactSelectType.BUDDY;

        /**
         * 联系人选择器数据源类型为群成员时，需要设置群号
         */
        public String teamId = null;

        /**
         * 联系人选择器标题
         */
        public String title = "联系人选择器";

        /**
         * 联系人单选/多选（默认）
         */
        public boolean multi = true;

        /**
         * 至少选择人数
         */
        public int minSelectNum = 1;

        /**
         * 低于最少选择人数的提示
         */
        public String minSelectedTip = null;

        /**
         * 最大可选人数
         */
        public int maxSelectNum = 2000;

        /**
         * 超过最大可选人数的提示
         */
        public String maxSelectedTip = null;

        /**
         * 是否显示已选头像区域
         */
        public boolean showContactSelectArea = true;

        /**
         * 默认勾选（且可操作）的联系人项
         */
        public ArrayList<String> alreadySelectedAccounts = null;

        /**
         * 需要过滤（不显示）的联系人项
         */
        public ContactItemFilter itemFilter = null;

        /**
         * 需要disable(可见但不可操作）的联系人项
         */
        public ContactItemFilter itemDisableFilter = null;

        /**
         * 是否支持搜索
         */
        public boolean searchVisible = true;

        /**
         * 允许不选任何人点击确定
         */
        public boolean allowSelectEmpty = false;

        /**
         * 是否显示最大数目，结合maxSelectNum,与搜索位置相同
         */
        public boolean maxSelectNumVisible = false;
    }

    public static void startActivityForResult(Context context, ContactSelectActivity.Option option,ArrayList<String> list, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATA, new Gson().toJson(option));
        intent.putStringArrayListExtra(ALL_LIST, list);
        intent.setClass(context, ContactSelectActivity.class);
        ((FragmentActivity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_activity_contacts_select);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
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
        String str = getIntent().getStringExtra(EXTRA_DATA);
        option = new Gson().fromJson(str, Option.class);
//        初始化数据集合
        mList = new ArrayList<>();
        chooseList = new ArrayList<>();
        disableList = new ArrayList<>();
        disableList.addAll(getIntent().getStringArrayListExtra(ALL_LIST));
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
                getDatas(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPages++;
                getDatas(true);
            }
        });

        mAdapter = new ContactSeleteAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                boolean isFlag = mList.get(position).isForPhone();
                if (isFlag) {
                   String accId =  mList.get(position).getAccId();
                   if (!disableList.contains(accId)){
                       chooseList.remove(mList.get(position));
                       mList.get(position).setForPhone(false);
                       mAdapter.notifyItemChanged(position);
                   }else{
                       ToastUtils.shortToast(ContactSelectActivity.this,"已有成员不能取消");
                   }
                } else {
                    chooseList.add(mList.get(position));
                    mList.get(position).setForPhone(true);
                    mAdapter.notifyItemChanged(position);
                }
                mButtonSubmit.setText(getOKBtnText(chooseList.size()));
                notifySelectAreaDataSetChanged();
                LogUtils.dazhiLog("chooseList-->" + chooseList.size());

            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });

        recycleMessageList.addItemDecoration(mDecoration = new SuspensionDecoration(this, mList));
        //如果add两个，那么按照先后顺序，依次渲染。
        recycleMessageList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化索引   有分页和搜索,暂时屏蔽
        mIndexBar.setmPressedShowTextView(letterHit)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(layoutManager)//设置RecyclerView的LayoutManager
                .setSourceDatasAlreadySorted(true);
        recycleMessageList.setEmptyView(emptyView);
        recycleMessageList.setAdapter(mAdapter);
        mAvatarAdapter = new ContactSelectAvatarAdapter(ContactSelectActivity.this, chooseList);
        mGridView.setAdapter(mAvatarAdapter);
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
                        ToastUtils.shortToast(ContactSelectActivity.this, "请输入姓名或机构名称搜索");
                        return false;
                    }
                    //搜索关键字
                    search(options);
                    //关闭键盘去搜索
                    contact_input_content.clearFocus();
                    KeybordUtils.closeKeybord(contact_input_content, ContactSelectActivity.this);
                    return true;
                }
                return false;
            }
        });
        getDatas(false);
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

    private void getDatas(final boolean isLoadMore) {

        NimUIKit.getTeamProvider().fetchTeamMemberList(option.teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members, int code) {
                if (success && members != null && !members.isEmpty()) {
                    List<String> accounts = new ArrayList<>();
                    String owner = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, null);
                    for (TeamMember teamMember : members) {
                        if (!teamMember.getAccount().equals(owner)) {
                            accounts.add(teamMember.getAccount());
                        }
                    }
                    if (!isLoadMore && mList != null && mList.size() > 0) {
                        mList.clear();
                    }
                    List<ContactGroupBean.ContentBean> contentBeans = new ArrayList<>();
                    List<NimUserInfo> userInfos = NimUIKit.getUserInfoProvider().getUserInfo(accounts);
                    for (int i = 0; i < userInfos.size(); i++) {
                        contentBeans.add(new ContactGroupBean.ContentBean(userInfos.get(i)));
                        for(int j = 0; j < disableList.size();j++){
                            String temp = disableList.get(j);
                            if(contentBeans.get(i).getAccId().equals(temp)){
                                contentBeans.get(i).setForPhone(true);
                            }

                        }

                    }

                    mList.addAll(contentBeans);
                    mIndexBar.setmSourceDatas(mList)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(mList);
                    mAdapter.notifyDataSetChanged();
                    mAllPages = 1;
                    refreshLayout.setNoMoreData(true);
                    if (isLoadMore) {
                        refreshLayout.finishLoadMore();
                    } else {
                        refreshLayout.finishRefresh();
                    }

                } else {
                    if (isLoadMore) {
                        refreshLayout.finishLoadMore();
                    } else {
                        refreshLayout.finishRefresh();
                    }
                    ToastHelper.showToast(ContactSelectActivity.this, "获取成员列表失败");
                }
            }
        });
    }

    //搜索
    private void search(String options) {
        if (mList != null && mList.size() > 0) {
            List<ContactGroupBean.ContentBean> mSerachList = new ArrayList<>();
            for (ContactGroupBean.ContentBean item : mList) {
                if (item.getName().contains(options) || item.getOrgName().contains(options)) {
                    mSerachList.add(item);
                }
            }
            mList = mSerachList;
            mAdapter.setDatas(mList);
            mIndexBar.setmSourceDatas(mList)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mList);
            mAdapter.notifyDataSetChanged();
            mAllPages = 1;
            refreshLayout.setNoMoreData(true);
        }
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
                ArrayList<String> accounts = new ArrayList<>();
                ArrayList<String> selectedNames = new ArrayList<>();
                for (ContactGroupBean.ContentBean contentBean : chooseList) {
                    accounts.add(contentBean.getAccId());
                    selectedNames.add(contentBean.getName());
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(RESULT_DATA, accounts);
                intent.putStringArrayListExtra(Extras.RESULT_NAME, selectedNames);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            } else {
                ToastUtils.shortToast(this, R.string.select_contact);

            }

        } else if (i == R.id.iv_head_back) {//直接返回关闭键盘
            KeybordUtils.closeKeybord(contact_input_content, ContactSelectActivity.this);
            finishWithAnim();

        }

    }
}
