package com.xej.xhjy.ui.society;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.MyCallback;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.main.ClubMainActivty;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.view.TextviewTobTabView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class MessageActivity 消息列表,俩fragment
 * @Createtime 2018/11/28 15:18
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class MessageActivity extends BaseActivity {

    @BindView(R.id.rb_chat_message)
    TextviewTobTabView mRadioButton_chat;
    @BindView(R.id.rb_platform_message)
    TextviewTobTabView mRadioButton_platfrom_message;
    @BindView(R.id.image_back)
    ImageView mTitleView;
    @BindView(R.id.lin_right)
    LinearLayout mTitleRight;
    private RecentContactsFragment imMessageFragment;
    private NewsMessageFragment newsMessageFragment;
    private static final int REQUEST_CODE_ADVANCED = 20212;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        hasNewMessage();
    }

    private void init() {

        mRadioButton_chat.setText("聊天消息");
        mRadioButton_chat.setCheck(false);
        mRadioButton_platfrom_message.setText("系统消息");
        mRadioButton_platfrom_message.setCheck(true);
        //如果是平台消息则切换至平台消息页签
        boolean isPlatformessage = getIntent().getBooleanExtra(ClubMainActivty.MESSAGETYPE, true);
        if (isPlatformessage) {
            select(0);
        } else {
            select(1);
        }
        mRadioButton_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(1);
            }
        });
        mRadioButton_platfrom_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(0);
            }
        });
    }


    /**
     * 创建群聊
     */
    @OnClick(R.id.lin_right)
    void goPostEdit() {
        ArrayList<String> disableAccounts = new ArrayList<>();
        Intent intent = new Intent(MessageActivity.this, com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, "1");
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
        startActivityForResultWithAnim(intent, REQUEST_CODE_ADVANCED);

    }


    @OnClick(R.id.image_back)
    void getback() {
        finishWithAnim();
    }


    private void select(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hidtFragment(ft);
        switch (i) {
            case 0:
                mRadioButton_chat.setCheck(false);
                mRadioButton_platfrom_message.setCheck(true);
                if (newsMessageFragment == null) {
                    newsMessageFragment = new NewsMessageFragment();
                    ft.add(R.id.container_fg, newsMessageFragment);
                } else {
                    ft.show(newsMessageFragment);
                }
                break;
            case 1:

                mRadioButton_platfrom_message.setCheck(false);
                mRadioButton_chat.setCheck(true);
                if (imMessageFragment == null) {
                    imMessageFragment = new RecentContactsFragment();
                    imMessageFragment.setMyCallback(new MyCallback() {
                        @Override
                        public void callback(String param) {
                            EventTrackingUtil.EventTrackSubmit(mActivity, "pageRightChatIcon", "channel=android",
                                    "eventChat", "chatId="+param);
                        }
                    });
                    ft.add(R.id.container_fg, imMessageFragment);
                } else {
                    ft.show(imMessageFragment);
                }

                break;

        }
        //提交事务
        ft.commit();
    }

    //隐藏所有Fragment
    private void hidtFragment(FragmentTransaction fragmentTransaction) {
        if (imMessageFragment != null) {
            fragmentTransaction.hide(imMessageFragment);
        }
        if (newsMessageFragment != null) {
            fragmentTransaction.hide(newsMessageFragment);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
                LogUtils.dazhiLog("创建群长度------" + beans.size());
                if (beans != null && !beans.isEmpty()) {
                    //创建高级群
//                    TeamCreateHelper.createAdvancedTeam(MessageActivity.this, beans);
                    //创建普通群
                    com.netease.nim.uikit.business.team.helper.TeamCreateHelper.createNormalTeam(MessageActivity.this, beans, true,true, new RequestCallback<CreateTeamResult>() {
                        @Override
                        public void onSuccess(CreateTeamResult param) {

                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    ToastUtils.shortToast(MessageActivity.this, R.string.select_contact);
                }


            }
        }

    }

    /**
     * 是否有新消息
     */
    private void hasNewMessage() {
        String TAG = "main_new_message";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.POST_NEW_MESSAGE, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        JSONArray jsonArray = jsonObject.optJSONArray("content");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            EventBus.getDefault().post(new HasMessageEvent(true));
                            AppConstants.HAS_NEW_MESSAGE = true;
                            mRadioButton_platfrom_message.setRedPointStatus(View.VISIBLE);

                        } else {
                            EventBus.getDefault().post(new HasMessageEvent(false));
                            AppConstants.HAS_NEW_MESSAGE = false;
                            mRadioButton_platfrom_message.setRedPointStatus(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

}
