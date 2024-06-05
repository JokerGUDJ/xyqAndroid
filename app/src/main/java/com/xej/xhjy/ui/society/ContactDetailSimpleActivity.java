package com.xej.xhjy.ui.society;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.ui.society.bean.ContactBean;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author dazhi
 * @class MineDetailActivity
 * @Createtime 2018/6/28 16:21
 * @description 个人信息详情页
 * @Revisetime
 * @Modifier
 */
public class ContactDetailSimpleActivity extends BaseActivity {
    @BindView(R.id.tv_mobile_phone)
    TextView tvUserMobilePhone;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_user_job)
    TextView tvUserJob;
    @BindView(R.id.tv_user_tel)
    TextView tvUserTel;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_name)
    TextView tvUsername;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.add_layout)
    LinearLayout addLayout;
    @BindView(R.id.add_canel)
    TextView addCanel;
    @BindView(R.id.call_mobile)
    TextView call_mobile;
    @BindView(R.id.call_phone)
    TextView call_phone;
    @BindView(R.id.btn_call)
    TextView btn_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_simple);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        ButterKnife.bind(this);
        ContactBean.ContentBean bean = (ContactBean.ContentBean) getIntent().getSerializableExtra("User");
        if (bean != null) {
            tvUsername.setText(bean.getName());
            head_title.setText(bean.getName());
            tvUserDepartment.setText(bean.getRoleRole().getUsertype());
            boolean title = TextUtils.isEmpty(bean.getTitle());
            tvUserJob.setText(title ? "/" : bean.getTitle());
            tvUserMobilePhone.setText(bean.getMobile());
            boolean mobile = TextUtils.isEmpty(bean.getMobile());
            boolean email = TextUtils.isEmpty(bean.getMail());
            boolean address = TextUtils.isEmpty(bean.getAddress());
            tvUserTel.setText(mobile ? "/" : bean.getPhone());
            tvUserEmail.setText(email ? "/" : bean.getMail());
            tvUserAddress.setText(address ? "/" : bean.getAddress());
            if (TextUtils.isEmpty(bean.getMobile())) {
                call_phone.setVisibility(View.GONE);
            } else {
                call_phone.setText(bean.getMobile());
            }
            if (TextUtils.isEmpty(bean.getPhone())) {
                call_mobile.setVisibility(View.GONE);
            } else {
                call_mobile.setText(bean.getPhone());
            }
        }


    }

    /**
     * 拨打电话
     */
    @OnClick({R.id.btn_call, R.id.call_phone, R.id.call_mobile, R.id.add_canel})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                showAddView();
                btn_call.setVisibility(View.GONE);
                break;
            case R.id.call_phone:
                callPhone(tvUserMobilePhone.getText().toString().trim());
                break;
            case R.id.call_mobile:
                callPhone(tvUserTel.getText().toString().trim());
                break;
            case R.id.add_canel:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_call.setVisibility(View.VISIBLE);
                    }
                }, 300);

                hideAddView();
                break;
        }

    }

    private void callPhone(String tel) {
        //动态权限管理
        RxPermissions subscribe = new RxPermissions(mActivity);
        subscribe.requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + tel);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            ToastUtils.shortToast(mActivity, "未获取拨打电话权限，请在设置中允许鑫合家园使用拨打电话！");
                        }
                    }
                });

    }

    private void showAddView() {
        addLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_up));
        addLayout.setVisibility(View.VISIBLE);
        btn_call.setVisibility(View.GONE);
    }

    private void hideAddView() {
        addLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_down));
        addLayout.setVisibility(View.GONE);
        btn_call.setVisibility(View.VISIBLE);
    }
}
