package com.xej.xhjy.ui.society;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.TeamMemberAitHelper;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.base.BaseFragmentForViewpager;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.datePicker.Util;
import com.xej.xhjy.ui.login.LoginActivity;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.society.adapter.HottopicAdapter;
import com.xej.xhjy.ui.society.bean.HottopicBean;
import com.xej.xhjy.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.netease.nim.uikit.business.recent.RecentContactsFragment.RECENT_TAG_STICKY;

/**
 * @author dazhi
 * @class SocietyFragment 鑫合圈Fragment
 * @Createtime 2018/11/20 16:34
 * @description 群组和话题列表均使用动态添加
 * @Revisetime
 * @Modifier
 */
public class SocietyFragment extends BaseFragmentForViewpager implements View.OnClickListener {
    private LinearLayout ll_sc, ll_ls, ll_my, ll_gs, ll_xw, ll_kj, ll_fx, ll_rl, ll_group_empty, ll_topic_empty, ll_office;
    private TitleView mTitleView;
    private TextView mTopicMore, tv_group, tv_cancel;
    private LayoutInflater mInflater;
    private List<HottopicBean.ContentBean> mList;
    private HottopicAdapter mAdapter;
    private CommonRecyclerView mRecyclerView;
    private RecentContactAdapter adapter;
    private List<RecentContact> items;
    private RecyclerView recycler_view;
    private RecentContactsCallback callback;
    private boolean msgLoaded = false;
    private UserInfoObserver userInfoObserver;
    private static final Handler handler = new Handler();
    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）
    private EditText mSearchOrgName; //搜索机构
    private View edit_cancel;

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mInflater = LayoutInflater.from(mActivity);
        mRootView = mInflater.inflate(R.layout.fragment_society, null);
        mTitleView = mRootView.findViewById(R.id.titleview);
        mRecyclerView = mRootView.findViewById(R.id.recycle_meet_list);
        recycler_view = mRootView.findViewById(R.id.recycler_view);
        ll_group_empty = mRootView.findViewById(R.id.ll_empty);
        tv_group = mRootView.findViewById(R.id.tv_group);
        ll_sc = mRootView.findViewById(R.id.ll_jr);
        ll_ls = mRootView.findViewById(R.id.ll_ls);
        ll_my = mRootView.findViewById(R.id.ll_my);
        ll_gs = mRootView.findViewById(R.id.ll_gs);
        ll_xw = mRootView.findViewById(R.id.ll_xw);
        ll_kj = mRootView.findViewById(R.id.ll_kj);
        ll_fx = mRootView.findViewById(R.id.ll_fx);
        ll_rl = mRootView.findViewById(R.id.ll_rl);
        ll_office = mRootView.findViewById(R.id.ll_office);
        mTopicMore = mRootView.findViewById(R.id.tv_topic_more);
        ll_topic_empty = mRootView.findViewById(R.id.ll_empty_topic);
        mSearchOrgName = mRootView.findViewById(R.id.contact_input_content_edit);
        tv_cancel = mRootView.findViewById(R.id.tv_cancel);
        edit_cancel = mRootView.findViewById(R.id.edit_cancel);
        ll_sc.setOnClickListener(this);
        ll_ls.setOnClickListener(this);
        ll_my.setOnClickListener(this);
        ll_gs.setOnClickListener(this);
        ll_xw.setOnClickListener(this);
        ll_kj.setOnClickListener(this);
        ll_fx.setOnClickListener(this);
        ll_rl.setOnClickListener(this);
        ll_office.setOnClickListener(this);
        ll_group_empty.setOnClickListener(this);
        mTopicMore.setOnClickListener(this);
        if (!AppConstants.IS_LOGIN) {
            tv_group.setText("请登录后查看");
        } else {
            tv_group.setText("暂无群组");
        }
        mRecyclerView.setNestedScrollingEnabled(false);//禁止滑动
        mRecyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getActivity()) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
                if (count > 0) {
                    int realHeight = 0;
                    int realWidth = 0;
                    for (int i = 0; i < count; i++) {
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });

        mList = new ArrayList<>();
        mAdapter = new HottopicAdapter(getActivity(), mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //校验登录，登录完成后在进入
                LoginCheckUtils.isCertification = true;
                LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                    @Override
                    public void onlogin() {
                        Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                        intent.putExtra("topicId", mList.get(position).getId());
                        intent.putExtra("title", "#" + mList.get(position).getTitle());
                        intent.putExtra("content", mList.get(position).getContent());
                        intent.putExtra("accessoryId", mList.get(position).getAccessoryId());
                        EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                                "eventHotTopic", "topicId="+mList.get(position).getId());
                        startActivity(intent);
                    }
                }, mActivity);

            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //初始化聊天列表
        initMessageList();
        requestSystemMessageUnreadCount();
        searchOrgName();
    }


    /**
     * 搜索机构
     */
    private void searchOrgName() {
        mSearchOrgName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //开始搜索
                    String options = mSearchOrgName.getText().toString().trim();
                    if (TextUtils.isEmpty(options)) {
                        ToastUtils.shortToast(mActivity, "请输入搜索内容");
                        return false;
                    }
                    Intent intent = new Intent(mActivity, SearchOrgListActivity.class);
                    intent.putExtra("content", options);
                    mActivity.startActivityWithAnim(intent);
                    KeybordUtils.closeKeybord(mSearchOrgName, mActivity);
                    return true;
                }
                return false;
            }
        });
        mSearchOrgName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSearchOrgName
                        .getLayoutParams();
                if(TextUtils.isEmpty(mSearchOrgName.getText().toString())){
                    params.rightMargin = Util.dpToPx(getContext(),15);
                    tv_cancel.setVisibility(View.GONE);
                    edit_cancel.setVisibility(View.GONE);
                }else{
                    params.rightMargin = Util.dpToPx(getContext(),55);
                    tv_cancel.setVisibility(View.VISIBLE);
                    edit_cancel.setVisibility(View.VISIBLE);
                }
                mSearchOrgName.setLayoutParams(params);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchOrgName.setText("");
            }
        });
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchOrgName.setText("");
            }
        });
    }

    public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
        public RecyclerViewNoBugLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                //try catch一下
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 初始化要加载的数据
     */
    @Override
    public void initDatas() {
        //初始化数据，懒加载
        if (AppConstants.IS_LOGIN) {
            requestMessages(true);
        }
        //注册监听
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
        addTopicItem();

    }

    @Override
    public void onResume() {
        super.onResume();
        //清除搜索
        if (mSearchOrgName != null) {
            mSearchOrgName.setText("");
            KeybordUtils.closeKeybord(mSearchOrgName, mActivity);
        }
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);
        adapter = new RecentContactAdapter(recycler_view, items);
        initCallBack();
        adapter.setCallback(callback);

        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setNestedScrollingEnabled(false);//禁止滑动
        recycler_view.addOnItemTouchListener(touchListener);

        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });

    }


    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };
    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {

                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }
            //监听会话变化
            requestSystemMessageUnreadCount();
            onRecentContactChanged(recentContacts);
        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };
    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点消失，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };
    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    private void onRecentContactChanged(List<RecentContact> recentContacts) {

        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                items.remove(index);
            }
            Team t = NimUIKit.getTeamProvider().getTeamById(r.getContactId());
            //注意  过滤掉单聊，增加群聊 然后排序
            if (t != null) {
                if (r.getSessionType() == SessionTypeEnum.Team && t.isMyTeam()) {
                    items.add(r);
                }
                if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                    TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
                }
            }
        }

        cacheMessages.clear();

        refreshMessages(true);


    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {

        }
    };
    private List<RecentContact> loadedRecents = new ArrayList<>();

    /**
     * 获取聊天消息数据
     *
     * @param delay
     */

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        if (loadedRecents == null) {
                            loadedRecents = new ArrayList<>();
                        }
                        // 初次加载，更新离线的消息中是否有@我的消息
                        for (RecentContact loadedRecent : recents) {
                            Team t = NimUIKit.getTeamProvider().getTeamById(loadedRecent.getContactId());
                            if (loadedRecent.getSessionType() == SessionTypeEnum.Team && t != null && t.isMyTeam()) {
                                if (loadedRecents != null && loadedRecents.size() > 2) {
                                    break;
                                }

                                loadedRecents.add(loadedRecent);
                                updateOfflineContactAited(loadedRecent);
                            }

                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true;
                        if (msgLoaded) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        if (items != null) {
            items.clear();
        }
        if (loadedRecents != null) {
            if (items == null) {
                items = new ArrayList<>();
            }
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }

    }

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {
                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }

                    // 更新并展示
                    if (messages != null) {
                        TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                            "eventChatGourps", "chatId="+recent.getContactId());
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                }
//                else if (recent.getSessionType() == SessionTypeEnum.P2P) {
//                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
//                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }


    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    private void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        boolean empty = items.isEmpty() && msgLoaded;
        ll_group_empty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）

            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }

            Badger.updateBadgerCount(unreadNum);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
        //每次增加群长度减1
        if (items != null && items.size() > 3) {
            items.remove(items.size() - 1);
        }
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };


    /*****************************************消息未读数监听*******************************************/

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        updateUnreadNum(unread);
    }


    /**
     * @param unreadCount 消息未读数获取
     */
    private void updateUnreadNum(int unreadCount) {
        if (unreadCount > 0) {
            LogUtils.dazhiLog("云信消息未读数=" + unreadCount);
            EventBus.getDefault().post(new HasMessageEvent(true));
        } else {
            LogUtils.dazhiLog("云信消息未读数=" + unreadCount);
        }
    }

    /*************************************以下是业务交易*********************************************/

    /**
     * 获取话题
     */

    private void addTopicItem() {
        String TAG = "society_hot_topic";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", 0 + "");//页数
        map.put("pageSize", "3"); //条数
        String url = NetConstants.HOT_TOPIC;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                HottopicBean bean = JsonUtils.stringToObject(jsonString, HottopicBean.class);
                if ("0".equals(bean.getCode()) && bean.getContent() != null) {
                    if (bean.getContent().size() > 0) {
                        ll_topic_empty.setVisibility(View.GONE);
                        mTopicMore.setVisibility(View.VISIBLE);
                    } else {
                        ll_topic_empty.setVisibility(View.VISIBLE);
                        mTopicMore.setVisibility(View.GONE);
                    }
                    mList.addAll(bean.getContent());
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    private long mLasttime = 0;

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
            return;
        mLasttime = System.currentTimeMillis();
        if (v == mTopicMore) {
            mActivity.startActivityWithAnim(new Intent(mActivity, TopicListActivity.class));
        } else if (v == ll_group_empty) {
            if (!AppConstants.IS_LOGIN) {
                mActivity.startActivityWithAnim(new Intent(mActivity, LoginActivity.class));
            }
        } else if (v == ll_sc) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906454", "金融市场", "eventJrsc");
                }
            }, mActivity);

        } else if (v == ll_ls) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906455", "零售金融", "eventLsjr");
                }
            }, mActivity);

        } else if (v == ll_my) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906456", "贸易金融", "eventMyjr");
                }
            }, mActivity);

        } else if (v == ll_gs) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906457", "公司金融", "eventGsjr");
                }
            }, mActivity);

        } else if (v == ll_xw) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906458", "小微金融", "eventXwjr");
                }
            }, mActivity);

        } else if (v == ll_kj) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906459", "金融科技", "eventKjjr");
                }
            }, mActivity);

        } else if (v == ll_fx) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906460", "风险管理", "eventFxgl");
                }
            }, mActivity);

        } else if (v == ll_rl) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906461", "人力资源", "eventRlzy");
                }
            }, mActivity);

        } else if (v == ll_office) {
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoCommitteeActivity("586914089029906463", "办公室", "eventBgs");
                }
            }, mActivity);
        }
    }

    private void gotoCommitteeActivity(String orgId, String title, String eventName){
        String commitId = PerferenceUtils.get(AppConstants.User.COMMIT_ID, "");
        if(!TextUtils.isEmpty(commitId) && commitId.equals(orgId)){
            if ("N".equals(AppConstants.USER_STATE)){
                Intent intent = new Intent(mActivity, CommitteeActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("orgId", orgId);
                EventTrackingUtil.EventTrackSubmit(mActivity, "pageCircle", "channel=android",
                        eventName, "");
                mActivity.startActivityWithAnim(intent);
            }else{
                ToastUtils.shortToast(mActivity, "您账号暂未认证，暂无发帖权限!");
            }
        }else{
            ToastUtils.shortToast(mActivity, "您不属于本专委会，暂无访问权限!");
        }
    }

    /**
     * 获取专委会ID
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        mTitleView.setNewMessageVisibile(event.getHasMessage());
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        tv_group.setText("暂无群组");
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (AppConstants.IS_LOGIN) {
                    msgLoaded = false;
                    requestMessages(true);
                }
            }
        }, 500);
    }

    /**
     * 收到登出的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(LoginOutEvent event) {
        NimUIKit.logout();
        tv_group.setText("请登录后查看");
        if (items != null) {
            items.clear();
            msgLoaded = true;
            notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
        if (items != null) {
            items = null;
        }
        super.onDestroy();
    }


}
