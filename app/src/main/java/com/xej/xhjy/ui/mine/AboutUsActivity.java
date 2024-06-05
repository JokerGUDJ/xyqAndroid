package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.DeviceUtil;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.view.TitleView;
import com.xej.xhjy.ui.web.WebOtherPagerActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.mine
 * @ClassName: AboutUsActivity
 * @Description: 关于我们页面
 * @Author: lihy_0203
 * @CreateDate: 2019/1/27 下午9:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/27 下午9:27
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_app_version)
    TextView tv_app_version;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tv_tel)
    TextView tv_tel;
    @BindView(R.id.tv_user_private)
    TextView tv_user_private;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String version = DeviceUtil.getVersionName(AboutUsActivity.this);
        if (!TextUtils.isEmpty(version)) {
            tv_app_version.setText("版本号：(" + version+")");
        } else {
            tv_app_version.setText("");
        }
        tv_tel.setText("025-86775712");
        tv_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:025-86775712");
                intent.setData(data);
                startActivity(intent);
            }
        });

        tv_user_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, WebOtherPagerActivity.class);
                intent.putExtra(WebOtherPagerActivity.LOAD_URL, AppConstants.AGREEMENT_URL);
                intent.putExtra(WebOtherPagerActivity.HEAD_TITLE, "协议");
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        LogUtils.dazhiLog("meet收到是否有消息----------" + event.getHasMessage());
        titleview.setNewMessageVisibile(event.getHasMessage());
    }

}
