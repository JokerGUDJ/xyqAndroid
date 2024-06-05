package com.netease.nim.uikit.business.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.impl.cache.FriendDataCache;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity extends UI {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";

    private String account;
    private View layoutNotificationConfig;

    // 基本信息
    private HeadImageView headImageView;
    private TextView nameText;
    private ImageView genderImage;
    private TextView accountText;
    private TextView birthdayText;
    private TextView mobileText;
    private TextView emailText;
    private TextView signatureText;
    private RelativeLayout birthdayLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private RelativeLayout aliasLayout;
    private TextView nickText;

    // 开关
    private ViewGroup toggleLayout;
    private Button addFriendBtn, clear_chat_btn;
    private Button removeFriendBtn;
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch, btn_switch;
    private Map<String, Boolean> toggleStateMap;
    private View clear_message;
    private View layoutNotificationFriend;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        initActionbar();
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toggleLayout != null) {
            toggleLayout.removeAllViews();
        }
        updateUserInfo();
        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    private void registerObserver(boolean register) {
//        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
//        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(muteListChangedNotifyObserver, register);
//    }

    Observer<MuteListChangedNotify> muteListChangedNotifyObserver = new Observer<MuteListChangedNotify>() {
        @Override
        public void onEvent(MuteListChangedNotify notify) {
//            setToggleBtn(noticeSwitch, !notify.isMute());
//            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
//            btn_switch.setCheck(notice);
        }
    };
//
//    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
//        @Override
//        public void onAddedOrUpdatedFriends(List<String> account) {
//            updateUserOperatorView();
//        }
//
//        @Override
//        public void onDeletedFriends(List<String> account) {
//            updateUserOperatorView();
//        }
//
//        @Override
//        public void onAddUserToBlackList(List<String> account) {
//            updateUserOperatorView();
//        }
//
//        @Override
//        public void onRemoveUserFromBlackList(List<String> account) {
//            updateUserOperatorView();
//        }
//    };

    private void findViews() {
        headImageView = findView(R.id.user_head_image);
        nameText = findView(R.id.user_name);
        toggleLayout = findView(R.id.toggle_layout);
        mobileText=findView(R.id.tv_phone_value);
        initNotify();
        RelativeLayout clear_message_p2p = (RelativeLayout) findViewById(R.id.clear_message_p2p);
        clear_message_p2p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyAlertDialogHelper.createOkCancelDiolag(UserProfileActivity.this, null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                        MessageListPanelHelper.getInstance().notifyClearMessages(account, SessionTypeEnum.P2P);
                    }
                }).show();
            }
        });
//        ((TextView) aliasLayout.findViewById(R.id.attribute)).setText(R.string.alias);
//        ((TextView) aliasLayout.findViewById(R.id.attribute)).setVisibility(View.GONE);

//        addFriendBtn.setOnClickListener(onClickListener);
//        removeFriendBtn.setOnClickListener(onClickListener);
//        clear_chat_btn.setOnClickListener(onClickListener);
//        aliasLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserProfileEditItemActivity.startActivity(UserProfileActivity.this, UserConstant.KEY_ALIAS, account);
//            }
//        });
    }

    /**
     * 个人消息设置
     */
    private void initNotify() {
        layoutNotificationConfig = findViewById(R.id.btn_notice);
//        layoutNotificationFriend = findViewById(R.id.btn_friend_message);
//        layoutNotificationFriend.setVisibility(View.GONE);
        ((TextView) layoutNotificationConfig.findViewById(R.id.user_profile_title)).setText("消息免打扰");
//        ((TextView) layoutNotificationFriend.findViewById(R.id.user_profile_title_notice)).setText(R.string.msg_friend_notice);
//        notificationConfigText = (TextView) layoutNotificationConfig.findViewById(R.id.item_detail);
        btn_switch = (SwitchButton) layoutNotificationConfig.findViewById(R.id.user_profile_toggle);
//        noticeSwitch = (SwitchButton) layoutNotificationFriend.findViewById(R.id.user_profile_toggle_nitice);
        btn_switch.setOnChangedListener(ChangedListener);
//        noticeSwitch.setOnChangedListener(friendChangedListener);

        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);//true
        btn_switch.setCheck(notice);

    }

    private SwitchButton.OnChangedListener ChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                return;
            }
            //false 开启  true 关
//            Log.d(TAG, "post:" + checkState);
            NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    btn_switch.setCheck(checkState);
                }

                @Override
                public void onFailed(int code) {
//                    Log.d(TAG, "muteTeam failed code:" + code);
                    btn_switch.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {
//                    Log.d(TAG, "onException:" + exception);
                }
            });

        }
    };
    private SwitchButton.OnChangedListener friendChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("Faccid", account);
            map.put("MsgFlag", checkState ? "1" : "0");//true 则为1 false 为0
//            NetWorkUtil.getInstance(baseAt).requestPost(ConstantIM.IP + "PublishMsgFriend.do", 0,
//                    map, new NetWorkUtil.ResultCallBack() {
//
//                        @Override
//                        public void onSuccess(Object response) {
//                            DialogMaker.dismissProgressDialog();
//                            JSONObject jsonObject=null;
//                            try {
//                                jsonObject = new JSONObject(response.toString());
//                                String RejCode = jsonObject.getString("_RejCode");
//                                if ("000000".equals(RejCode)) {
//                                    noticeSwitch.setCheck(checkState);
//                                }
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Object errorMsg) {
////                            Log.d("getUserSeq", "getUserSeq: "+errorMsg);
//                            DialogMaker.dismissProgressDialog();
//                        }
//                    });

        }
    };

    private void initActionbar() {
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


    private void addToggleBtn(boolean black, boolean notice) {
//        blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
//        noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
    }

    private void updateUserInfo() {
//        if (NimUserInfoCache.getInstance()(account)) {
//            updateUserInfoView();
//            return;
//        }

        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallbackWrapper<NimUserInfo>() {
            @Override
            public void onResult(int code, NimUserInfo result, Throwable exception) {
                updateUserInfoView();
            }
        });
    }

    private void updateUserInfoView() {
//        accountText.setText("帐号：" + account);
        headImageView.loadBuddyAvatar(account);

        if (NimUIKit.getAccount().equals(account)) {
            nameText.setText(NimUIKit.getUserInfoProvider().getUserInfo(account).getName());
        }

        final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(account);
        if (userInfo == null) {
            LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
            return;
        }
        mobileText.setText(userInfo.getMobile());
//        accountText.setText("贵州银行");
//        if (userInfo.getGenderEnum() == GenderEnum.MALE) {
//            genderImage.setVisibility(View.VISIBLE);
//            genderImage.setBackgroundResource(R.drawable.nim_male);
//        } else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
//            genderImage.setVisibility(View.VISIBLE);
//            genderImage.setBackgroundResource(R.drawable.nim_female);
//        } else {
//            genderImage.setVisibility(View.GONE);
//        }

//        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
//            birthdayLayout.setVisibility(View.VISIBLE);
//            birthdayText.setText(userInfo.getBirthday());
//        } else {
//            birthdayLayout.setVisibility(View.GONE);
//        }
//
//        if (!TextUtils.isEmpty(userInfo.getMobile())) {
//            phoneLayout.setVisibility(View.VISIBLE);
//            mobileText.setText(userInfo.getMobile());
//        } else {
//            phoneLayout.setVisibility(View.GONE);
//        }
//
//        if (!TextUtils.isEmpty(userInfo.getEmail())) {
//            emailLayout.setVisibility(View.VISIBLE);
//            emailText.setText(userInfo.getEmail());
//        } else {
//            emailLayout.setVisibility(View.GONE);
//        }
//
//        if (!TextUtils.isEmpty(userInfo.getSignature())) {
//            signatureLayout.setVisibility(View.VISIBLE);
//            signatureText.setText(userInfo.getSignature());
//        } else {
//            signatureLayout.setVisibility(View.GONE);
//        }

    }

    private void updateUserOperatorView() {
        if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
//            removeFriendBtn.setVisibility(View.VISIBLE);
//            addFriendBtn.setVisibility(View.GONE);
            updateAlias(true);
        } else {
//            addFriendBtn.setVisibility(View.VISIBLE);
//            removeFriendBtn.setVisibility(View.GONE);
            updateAlias(false);
        }
    }

    private void updateToggleView() {
//        if (IMCache.getAccount() != null && !IMCache.getAccount().equals(account)) {
//            boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
//            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
//            if (blackSwitch == null || noticeSwitch == null) {
//                addToggleBtn(black, notice);
//            } else {
//                setToggleBtn(blackSwitch, black);
////                setToggleBtn(noticeSwitch, notice);
//            }
////            btn_switch.setSelected(notice);
//            Log.i(TAG, "black=" + black + ", notice=" + notice);
            updateUserOperatorView();
//        }
//    }

//    private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
        ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.action_bar_height));
        vp.setLayoutParams(vlp);
//
//        TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
//        titleText.setText("消息免打扰");
//
//        SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
//        switchButton.setCheck(initState);
//        switchButton.setOnChangedListener(onChangedListener);
//        switchButton.setTag(key);
//
//        toggleLayout.addView(vp);
//
//        if (toggleStateMap == null) {
//            toggleStateMap = new HashMap<>();
//        }
////        Log.d(TAG, "initState "+initState);
//        toggleStateMap.put(key, initState);
//        return switchButton;
    }

    private void updateAlias(boolean isFriend) {
        if (isFriend) {
//            aliasLayout.setVisibility(View.GONE);
//            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
            Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
            if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
//                nickText.setVisibility(View.VISIBLE);
                nameText.setText(friend.getAlias());
//                nickText.setText("昵称：" + NimUserInfoCache.getInstance().getUserName(account));
            } else {
//                nickText.setVisibility(View.GONE);
                nameText.setText(NimUserInfoCache.getInstance().getUserInfo(account).getName());
            }
        } else {
//            aliasLayout.setVisibility(View.GONE);
//            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
//            nickText.setVisibility(View.GONE);
            nameText.setText(NimUserInfoCache.getInstance().getUserInfo(account).getName());
//            addFriendBtn.setVisibility(View.VISIBLE);
//            removeFriendBtn.setVisibility(View.GONE);

        }
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                if (key.equals(KEY_BLACK_LIST)) {
                    blackSwitch.setCheck(!checkState);
                } else if (key.equals(KEY_MSG_NOTICE)) {
                    noticeSwitch.setCheck(!checkState);
                }
                return;
            }

//                Log.d(TAG, "OnChanged+post "+checkState);
                boolean state=!checkState;
                NIMClient.getService(FriendService.class).setMessageNotify(account,state).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(UserProfileActivity.this, "开启消息免打扰成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "关闭消息免打扰成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        noticeSwitch.setCheck(!checkState);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }

    };

//    private void updateStateMap(boolean checkState, String key) {
//        if (toggleStateMap.containsKey(key)) {
//            toggleStateMap.put(key, checkState);  // update state
////            Log.i(TAG, "toggle " + key + "to " + checkState);
//        }
//    }

//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v == addFriendBtn) {
//                if (FLAG_ADD_FRIEND_DIRECTLY) {
//                    doAddFriend(null, true);  // 直接加为好友
//                } else {
//                    onAddFriendByVerify(); // 发起好友验证请求
//                }
//            } else if (v == removeFriendBtn) {
//                onRemoveFriend();
//            }
////            else if (v == chatBtn) {
////                onChat();
////            }
//        }
//    };

        /**
         * 通过验证方式添加好友
         */
//        private void onAddFriendByVerify () {
//        final EasyEditDialog requestDialog = new EasyEditDialog(this);
//        requestDialog.setEditTextMaxLength(32);
//        requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
//        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestDialog.dismiss();
//            }
//        });
//        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestDialog.dismiss();
//                String msg = requestDialog.getEditMessage();
//                doAddFriend(msg, false);
//            }
//        });
//        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
//        requestDialog.show();
//        Map<String,String> map=new HashMap<>();
//        map.put("UserSeqFaccid",account);
//        DialogMaker.showProgressDialog(this, "", false);
//        NetWorkUtil.getInstance(baseAt).requestPost(ConstantIM.IP + "QueryApplyFriendsFlag.do", 0,
//                map, new NetWorkUtil.ResultCallBack() {
//
//                    @Override
//                    public void onSuccess(Object response) {
//                        DialogMaker.dismissProgressDialog();
////                        Log.d("getUserSeq", "getUserSeq: "+response);
//                        JSONObject jsonObject=null;
//                        try {
//                            jsonObject = new JSONObject(response.toString());
//                            String RejCode = jsonObject.getString("_RejCode");
//                            if ("000000".equals(RejCode)) {
//                                boolean flag=jsonObject.optBoolean("ApplyFlag");
//                                if (!flag) return;
//                                doAddFriend("",false);
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//
//
//                    }
//
//                    @Override
//                    public void onError(Object errorMsg) {
//                        DialogMaker.dismissProgressDialog();
//                    }
//                });
//        }

//    private void doAddFriend(String msg, boolean addDirectly) {
//        if (!NetworkUtil.isNetAvailable(this)) {
//            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!TextUtils.isEmpty(account) && account.equals(IMCache.getAccount())) {
//            Toast.makeText(UserProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
//        DialogMaker.showProgressDialog(this, "", true);
//        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
//                .setCallback(new RequestCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void param) {
//                        DialogMaker.dismissProgressDialog();
//                        updateUserOperatorView();
//                        if (VerifyType.DIRECT_ADD == verifyType) {
//                            Toast.makeText(UserProfileActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(UserProfileActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(int code) {
//                        DialogMaker.dismissProgressDialog();
//                        if (code == 408) {
//                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast
//                                    .LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast
//                                    .LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onException(Throwable exception) {
//                        DialogMaker.dismissProgressDialog();
//                    }
//                });
//
////        Log.i(TAG, "onAddFriendByVerify");
//    }

//    private void onRemoveFriend() {
////        Log.i(TAG, "onRemoveFriend");
//        if (!NetworkUtil.isNetAvailable(this)) {
//            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend),
//                getString(R.string.remove_friend_tip), true,
//                new EasyAlertDialogHelper.OnDialogActionListener() {
//
//                    @Override
//                    public void doCancelAction() {
//
//                    }
//
//                    @Override
//                    public void doOkAction() {
//                        DialogMaker.showProgressDialog(UserProfileActivity.this, "", true);
//                        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
//                            @Override
//                            public void onSuccess(Void param) {
//                                NoticeFollow();
//                            }
//
//                            @Override
//                            public void onFailed(int code) {
//                                DialogMaker.dismissProgressDialog();
//                                if (code == 408) {
//                                    Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onException(Throwable exception) {
//                                DialogMaker.dismissProgressDialog();
//                            }
//                        });
//                    }
//                });
//        if (!isFinishing() && !isDestroyedCompatible()) {
//            dialog.show();
//        }
//    }
        //通知鑫E家将好于关系解除
//    private void NoticeFollow(){
//        Map<String,String> map=new HashMap<>();
//        map.put("UserSeqFaccid",account);
//        NetWorkUtil.getInstance(baseAt).requestPost(ConstantIM.IP + "DeleteFriendsMsg.do", 0,
//                map, new NetWorkUtil.ResultCallBack() {
//
//                    @Override
//                    public void onSuccess(Object response) {
//                        DialogMaker.dismissProgressDialog();
////                        Log.d("getUserSeq", "getUserSeq: "+response);
//                        JSONObject jsonObject=null;
//                        try {
//                            jsonObject = new JSONObject(response.toString());
//                            String RejCode = jsonObject.getString("_RejCode");
//                            if ("000000".equals(RejCode)) {
//                                addFriendBtn.setVisibility(View.VISIBLE);
//                                removeFriendBtn.setVisibility(View.GONE);
//                                Toast.makeText(UserProfileActivity.this, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//
//
//                    }
//
//                    @Override
//                    public void onError(Object errorMsg) {
////                        Log.d("getUserSeq", "getUserSeq: "+errorMsg);
//                        DialogMaker.dismissProgressDialog();
//                    }
//                });
//
//
//    }
//
//    private void onChat() {
////        Log.i(TAG, "onChat");
//        SessionHelper.startP2PSession(this, account);
//    }

}
