package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.web.WebPagerActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society
 * @ClassName: NewsMessageDetailsActivity
 * @Description: :系统消息详情
 * @Author: lihy_0203
 * @CreateDate: 2018/12/17 下午8:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/17 下午8:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NewsMessageDetailsActivity extends BaseActivity {
    @BindView(R.id.head_back)
    ImageView head_back;
    @BindView(R.id.tv_news_message_details_title)
    TextView tv_news_message_details_title;
    @BindView(R.id.tv_news_message_details_content)
    TextView tv_news_message_details_content;
    @BindView(R.id.into_details)
    TextView ll_into_details;
    private Intent intent;
    private String noticeType;
    private ClubLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_details);
        ButterKnife.bind(this);
        mLoadingDialog = new ClubLoadingDialog(mActivity);
        intent = new Intent();
        noticeType = getIntent().getStringExtra("noticeType");
//        String meetId = getIntent().getStringExtra("meetId");
//        String placeName = getIntent().getStringExtra("placeName");
        tv_news_message_details_title.setText(getIntent().getStringExtra("title"));
        tv_news_message_details_content.setText(getIntent().getStringExtra("content"));
        //getTv_news_message_details_time.setText(DateUtils.FormatTime(getIntent().getStringExtra("time")));
        // 0 普通消息  1 宴会  2 座位
        if (!TextUtils.isEmpty(noticeType)) {
            if ("0".equals(noticeType)) {
                ll_into_details.setVisibility(View.GONE);
            } else if ("1".equals(noticeType)) {
                ll_into_details.setVisibility(View.VISIBLE);
            } else if ("2".equals(noticeType)) {
                ll_into_details.setVisibility(View.VISIBLE);
            }else if("3".equals(noticeType)){
                ll_into_details.setVisibility(View.VISIBLE);
            }
        } else {
            ll_into_details.setVisibility(View.GONE);
        }
    }



    @OnClick(R.id.head_back)
    void back() {
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @OnClick(R.id.into_details)
    void gotoDetails() {
        if (!TextUtils.isEmpty(noticeType)) {
            String placeName = getIntent().getStringExtra("placeName");
            String meetId = getIntent().getStringExtra("meetId");
            if ("1".equals(noticeType)) {
                Intent intent = new Intent(mActivity, WebPagerActivity.class);
                intent.putExtra(WebPagerActivity.LOAD_URL, "BanquetSeat");
                intent.putExtra(WebPagerActivity.MISS_MESSAGE, true);
                intent.putExtra(WebPagerActivity.MEETTING_ID,meetId);
                intent.putExtra(WebPagerActivity.MEETTING_PLACENAME,placeName);
                startActivityWithAnim(intent);
            } else if ("2".equals(noticeType)||"3".equals(noticeType)) {
                Intent intent = new Intent(mActivity, WebPagerActivity.class);
                intent.putExtra(WebPagerActivity.LOAD_URL, "MeetSeat");
                intent.putExtra(WebPagerActivity.MISS_MESSAGE, true);
                intent.putExtra(WebPagerActivity.MEETTING_ID,meetId);
                intent.putExtra(WebPagerActivity.MEETTING_PLACENAME,placeName);
                startActivityWithAnim(intent);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK, intent);
        return super.onKeyDown(keyCode, event);
    }
}
