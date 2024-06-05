package com.xej.xhjy.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.ui.web.WebPagerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @class QRSuccessActivity
 * @author dazhi
 * @Createtime 2018/7/11 18:10
 * @description 扫码签到成功页
 * @Revisetime
 * @Modifier
 */
public class QRSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_success);
        String meetName = getIntent().getStringExtra("name");
        if (!GenalralUtils.isEmpty(meetName)){
            ((TextView)findViewById(R.id.tv_meet_name)).setText(meetName);
        }
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WebPagerActivity.class);
                intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
                intent.putExtra(WebPagerActivity.MEETTING_PARAMS, getIntent().getStringExtra(WebPagerActivity.MEETTING_PARAMS));
                intent.putExtra(WebPagerActivity.MEETTING_ID, getIntent().getStringExtra("meetID"));
                startActivity(intent);
                finishWithAnim();
            }
        });
    }
    @OnClick(R.id.btn_finish)
    void closeActivity(){
        finishWithAnim();
    }
}
