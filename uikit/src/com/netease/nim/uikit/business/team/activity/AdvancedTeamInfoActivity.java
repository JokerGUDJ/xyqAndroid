package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter.TeamMemberItem;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.team.ui.TeamInfoGridView;
import com.netease.nim.uikit.business.team.viewholder.TeamMemberHolder;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.ToastHelper;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.xej.xhjy.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamInfoActivity extends UI implements
        TAdapterDelegate, TeamMemberAdapter.AddMemberCallback, TeamMemberHolder.TeamMemberHolderEventListener {

    private static final int REQUEST_CODE_CONTACT_SELECT = 103;


    private static final String EXTRA_ID = "EXTRA_ID";

    private static final int TEAM_MEMBERS_SHOW_LIMIT = 5;
    public static final String DIAABLE_MEMBER_LIST = "disable_member_list";

    // data
    private TeamMemberAdapter adapter;
    private String teamId;
    private Team team;
    private String creator;
    private List<String> memberAccounts;
    private List<TeamMember> members;
    private List<TeamMemberAdapter.TeamMemberItem> dataSource;
    private List<String> managerList;
    private UserInfoObserver userInfoObserver;
    private TeamInfoGridView gridView;
    private View layoutNotificationConfig;
    private SwitchButton btn_switch;
    private TeamMessageNotifyTypeEnum type;
    private boolean isSelfAdmin = false;
    private boolean isSelfManager = false;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * ************************ TAdapterDelegate **************************
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamMemberHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ***************************** Life cycle *****************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nim_advanced_team_info_activity);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
        parseIntentData();
        findViews();
        initActionbar();
        initAdapter();
        loadTeamInfo();
        requestMembers();
        registerObservers(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTACT_SELECT && resultCode == Activity.RESULT_OK) {
            ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
            ArrayList<String> selectedAccounts = new ArrayList<>();
            if (beans != null && beans.size() > 0) {
                for (ContactGroupBean.ContentBean c : beans) {
                    selectedAccounts.add(c.getAccId());
                }
            }
            if (selectedAccounts != null && !selectedAccounts.isEmpty()) {
                addMembersToTeam(selectedAccounts);
            }
        }

    }

    /**
     * 添加群成员
     */
    private void addMembersToTeam(final ArrayList<String> selected) {
        // add members
        DialogMaker.showProgressDialog(this, getString(R.string.empty), true);
        NIMClient.getService(TeamService.class).addMembers(teamId, selected).setCallback(new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> failedAccounts) {
                DialogMaker.dismissProgressDialog();
                addMember(selected, failedAccounts, false);
                if (failedAccounts != null && !failedAccounts.isEmpty()) {
                    TeamHelper.onMemberTeamNumOverrun(failedAccounts, AdvancedTeamInfoActivity.this);
                } else {
                    ToastHelper.showToast(AdvancedTeamInfoActivity.this, R.string.invite_member_success);
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                ToastHelper.showToast(AdvancedTeamInfoActivity.this, R.string.invite_member_failed);
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    private void addMember(List<String> accounts, List<String> failed, boolean clear) {
        if (accounts == null || accounts.isEmpty()) {
            return;
        }

        if (clear) {
            this.memberAccounts.clear();
        }

        // add
        if (this.memberAccounts.isEmpty()) {
            this.memberAccounts.addAll(accounts);
        } else {
            for (String account : accounts) {
                if (!this.memberAccounts.contains(account) && (failed == null || !failed.contains(account))) {
                    this.memberAccounts.add(account);
                }
            }
        }

        // sort
        Collections.sort(this.memberAccounts, new Comparator<String>() {
            @Override
            public int compare(String l, String r) {
                if (creator == null) {
                    return 0;
                }

                if (creator.equals(l)) {
                    return -1;
                }
                if (creator.equals(r)) {
                    return 1;
                }

                return l.compareTo(r);
            }
        });

        updateDataSource();
    }

    private void updateDataSource() {
        dataSource.clear();

        // member item
        String identity;
        for (String account : memberAccounts) {
            if (creator.equals(account)) {
                identity = TeamMemberHolder.OWNER;
            } else {
                identity = null;
            }
            dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                    .NORMAL, teamId, account, identity));
        }

        // add item
        dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag.ADD, null, null, null));

        // remove item
        if (isSelfAdmin) {
            dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag.DELETE, null, null,
                    null));
        }

        // refresh
        if (adapter.getMode() != TeamMemberAdapter.Mode.DELETE) {
            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        registerObservers(false);

        super.onDestroy();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void findViews() {

        gridView = (TeamInfoGridView) findViewById(R.id.team_member_grid_view);
        gridView.setVisibility(View.GONE);
        findViewById(R.id.clear_message_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyAlertDialogHelper.createOkCancelDiolag(AdvancedTeamInfoActivity.this, null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        NIMClient.getService(MsgService.class).clearChattingHistory(teamId, SessionTypeEnum.Team);
                        MessageListPanelHelper.getInstance().notifyClearMessages(teamId, SessionTypeEnum.Team);
                    }
                }).show();
            }
        });

    }


    private void initActionbar() {
        layoutNotificationConfig = findViewById(R.id.btn_notice);
        ((TextView) layoutNotificationConfig.findViewById(R.id.user_profile_title)).setText("消息免打扰");
        btn_switch = (SwitchButton) layoutNotificationConfig.findViewById(R.id.user_profile_toggle);
        btn_switch.setOnChangedListener(ChangedListener);
        ImageView head_back = (ImageView) findViewById(R.id.head_back);
        TextView toolbar_title = findView(R.id.toolbar_title);
        toolbar_title.setText("聊天设置");
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isNotice;
    private SwitchButton.OnChangedListener ChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(AdvancedTeamInfoActivity.this)) {
                return;
            }
            if (isNotice) {
                type = TeamMessageNotifyTypeEnum.All;
                isNotice = false;
            } else {
                type = TeamMessageNotifyTypeEnum.Mute;
                isNotice = true;
            }
            NIMClient.getService(TeamService.class).muteTeam(teamId, type).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    btn_switch.setCheck(isNotice);
                }

                @Override
                public void onFailed(int code) {
                    btn_switch.setCheck(!isNotice);
                }

                @Override
                public void onException(Throwable exception) {
                    btn_switch.setCheck(!isNotice);
                }
            });

        }
    };

    private void initAdapter() {
        memberAccounts = new ArrayList<>();
        members = new ArrayList<>();
        dataSource = new ArrayList<>();
        managerList = new ArrayList<>();
        adapter = new TeamMemberAdapter(this, dataSource, this, null, this);
        adapter.setEventListener(this);

        gridView.setSelector(R.color.transparent);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        gridView.setAdapter(adapter);
    }

    /**
     * 初始化群组基本信息
     */
    private void loadTeamInfo() {
        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onGetTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onGetTeamInfoFailed() {
        Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param t
     */
    private void updateTeamInfo(final Team t) {
        this.team = t;

        if (team == null) {
            Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            creator = team.getCreator();
            if (creator.equals(NimUIKit.getAccount())) {
                isSelfAdmin = true;
            }

        }
        updateTeamNotifyText(team.getMessageNotifyType());

    }

    /**
     * 更新群成员信息
     *
     * @param m
     */
    private void updateTeamMember(final List<TeamMember> m) {
        if (m != null && m.isEmpty()) {
            return;
        }

        addTeamMembers(m, true);
    }


    /**
     * 添加群成员到列表
     *
     * @param m     群成员列表
     * @param clear 是否清除
     */
    private void addTeamMembers(final List<TeamMember> m, boolean clear) {
        if (m == null || m.isEmpty()) {
            return;
        }

        isSelfManager = false;
        isSelfAdmin = false;

        if (clear) {
            this.members.clear();
            this.memberAccounts.clear();
        }

        // add
        if (this.members.isEmpty()) {
            this.members.addAll(m);
        } else {
            for (TeamMember tm : m) {
                if (!this.memberAccounts.contains(tm.getAccount())) {
                    this.members.add(tm);
                }
            }
        }

        // sort
        Collections.sort(this.members, TeamHelper.teamMemberComparator);

        // accounts, manager, creator
        this.memberAccounts.clear();
        this.managerList.clear();
        for (TeamMember tm : members) {
            if (tm == null) {
                continue;
            }
            if (tm.getType() == TeamMemberType.Manager) {
                managerList.add(tm.getAccount());
            }
            if (tm.getAccount().equals(NimUIKit.getAccount())) {
                if (tm.getType() == TeamMemberType.Manager) {
                    isSelfManager = true;
                } else if (tm.getType() == TeamMemberType.Owner) {
                    isSelfAdmin = true;
                    creator = NimUIKit.getAccount();
                }
            }
            this.memberAccounts.add(tm.getAccount());
        }

        updateTeamMemberDataSource();
    }


    /**
     * 更新成员信息
     */
    private void updateTeamMemberDataSource() {
        if (members.size() > 0) {
            gridView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
            return;
        }

        dataSource.clear();

        // add item
        if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
            dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag.ADD, null, null,
                    null));
        }

        LogUtils.dazhiLog("dataSource=" + dataSource.size());
        // member item
        int count = 0;
        String identity = null;
        for (String account : memberAccounts) {
            int limit = TEAM_MEMBERS_SHOW_LIMIT;
            if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
                limit = TEAM_MEMBERS_SHOW_LIMIT - 1;
            }
            if (count <= memberAccounts.size()) {
                identity = getIdentity(account);
                dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                        .NORMAL, teamId, account, identity));
            }
            count++;
        }

        // refresh
        adapter.notifyDataSetChanged();
    }

    private String getIdentity(String account) {
        String identity;
        if (creator.equals(account)) {
            identity = TeamMemberHolder.OWNER;
        } else if (managerList.contains(account)) {
            identity = TeamMemberHolder.ADMIN;
        } else {
            identity = null;
        }
        return identity;
    }

    /**
     * *************************** 加载&变更数据源 ********************************
     */
    private void requestMembers() {
        NimUIKit.getTeamProvider().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members, int code) {
                if (success && members != null && !members.isEmpty()) {
                    updateTeamMember(members);
                }
            }
        });
    }

    /**
     * ************************** 群信息变更监听 **************************
     */
    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerObservers(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataObserver, register);
        registerUserInfoChangedObserver(register);
    }

    TeamMemberDataChangedObserver teamMemberObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> m) {
            for (TeamMember mm : m) {
                for (TeamMember member : members) {
                    if (mm.getAccount().equals(member.getAccount())) {
                        members.set(members.indexOf(member), mm);
                        break;
                    }
                }
            }
            addTeamMembers(m, false);
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> members) {
            for (TeamMember member : members) {
                removeMember(member.getAccount());
            }
        }
    };

    TeamDataChangedObserver teamDataObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            for (Team team : teams) {
                if (team.getId().equals(teamId)) {
                    updateTeamInfo(team);
                    updateTeamMemberDataSource();
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team.getId().equals(teamId)) {
                AdvancedTeamInfoActivity.this.team = team;
                finish();
            }
        }
    };

    /**
     * ******************************* Action *********************************
     */

    /**
     * 从联系人选择器发起邀请成员
     */
    @Override
    public void onAddMember() {
        ArrayList<String> disableAccounts = new ArrayList<>();
        disableAccounts.addAll(memberAccounts);
        Intent intent = new Intent(AdvancedTeamInfoActivity.this, ContactListSelectAtivity.class);
        intent.putStringArrayListExtra(DIAABLE_MEMBER_LIST, disableAccounts);
        startActivityForResultWithAnim(intent, REQUEST_CODE_CONTACT_SELECT);
    }


    /**
     * ******************************* Event *********************************
     */


    @Override
    public void onHeadImageViewClick(String account) {
        // 打开群成员信息详细页面
//        AdvancedTeamMemberInfoActivity.startActivityForResult(AdvancedTeamInfoActivity.this, account, teamId);
    }


    private void updateTeamNotifyText(TeamMessageNotifyTypeEnum typeEnum) {
        if (typeEnum == TeamMessageNotifyTypeEnum.All) {
            isNotice = false;
        } else if (typeEnum == TeamMessageNotifyTypeEnum.Manager) {
            isNotice = false;
        } else if (typeEnum == TeamMessageNotifyTypeEnum.Mute) {
            isNotice = true;
        }
        btn_switch.setCheck(isNotice);
    }


    /**
     * 移除群成员成功后，删除列表中的群成员
     *
     * @param account 被删除成员帐号
     */
    private void removeMember(String account) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        memberAccounts.remove(account);

        for (TeamMember m : members) {
            if (m.getAccount().equals(account)) {
                members.remove(m);
                break;
            }
        }


        for (TeamMemberItem item : dataSource) {
            if (item.getAccount() != null && item.getAccount().equals(account)) {
                dataSource.remove(item);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void registerUserInfoChangedObserver(boolean register) {
        if (register) {
            if (userInfoObserver == null) {
                userInfoObserver = new UserInfoObserver() {
                    @Override
                    public void onUserInfoChanged(List<String> accounts) {
                        adapter.notifyDataSetChanged();
                    }
                };
            }
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
        } else {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }


}
