package com.xej.xhjy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.CardScanResultBean;
import com.xej.xhjy.bean.JobBean;
import com.xej.xhjy.bean.SearchComplanyBean;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.spinner.NiceSpinner;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author dazhi
 * @class RegisterFragment1 找回密码第一个页面
 * @Createtime 2018/6/21 16:43
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class RegisterFragment1 extends BaseFragment {
    private final String TAG1 = "job_list";
    public final int SEARCH_COMPLANY_CODE = 33;
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.spinner_gender)
    NiceSpinner mGenderSpinner;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.tv_complany)
    TextView tvComplany;
    @BindView(R.id.edt_department)
    EditText edtDepartment;
    @BindView(R.id.spinner_job)
    NiceSpinner spinnerJob;
    @BindView(R.id.edt_tel)
    EditText edtTel;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_address)
    EditText edtAddress;

    Unbinder mUnbinder;
    String orgName, orgID, jobName, jobID;
    List<JobBean.ContentBean> jobList;
    int genderPosition = 0;
    private ClubLoadingDialog mDialog;
    private CardScanResultBean cardBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Object object = bundle.getSerializable("scan_result");
            if (object != null) {
                cardBean = (CardScanResultBean) object;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register1, null);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        initDatas();
        return view;
    }

    public void initView() {
        mDialog = new ClubLoadingDialog(mActivity);
        List<String> dataList = new ArrayList<>();
        dataList.add("男");
        dataList.add("女");
        mGenderSpinner.attachDataSource(dataList);
        mGenderSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                genderPosition = position;
            }
        });
    }

    public void initDatas() {
        //职务列表先从缓存中拿
        String job_list = PerferenceUtils.get(AppConstants.DATA_JOB_LIS_KEY, "");
        if (!TextUtils.isEmpty(job_list)) {
            setJoblist(job_list);
        }
        if (cardBean != null) {//有名片扫描的数据
            //姓名
            String name = cardBean.getContent().getJson().get(0).getName();
            if (!GenalralUtils.isEmpty(name)) {
                mEdtName.setText(name);
                mEdtName.setSelection(mEdtName.getText().length());
            }
            //手机号
            List<String> phones = cardBean.getContent().getJson().get(0).getTel_cell();
            if (phones != null && phones.size() > 0) {
                edtPhone.setText(phones.get(0));
                edtPhone.setSelection(edtPhone.getText().length());
            }

            //部门
            List<String> departments = cardBean.getContent().getJson().get(0).getDepartment();
            if (departments != null && departments.size() > 0) {
                edtDepartment.setText(departments.get(0));
                edtDepartment.setSelection(edtDepartment.getText().length());
            }

//            //固话
            List<String> tels = cardBean.getContent().getJson().get(0).getTel_work();
            if (tels != null && tels.size() > 0) {
                edtTel.setText(tels.get(0));
                edtTel.setSelection(edtTel.getText().length());
            }
//
//            //邮箱
            List<String> emails = cardBean.getContent().getJson().get(0).getEmail();
            if (emails != null && emails.size() > 0) {
                edtEmail.setText(emails.get(0));
                edtEmail.setSelection(edtEmail.getText().length());
            }

            //地址
            List<String> addrs = cardBean.getContent().getJson().get(0).getAddr();
            if (addrs != null && addrs.size() > 0) {
                edtAddress.setText(addrs.get(0));
                edtAddress.setSelection(edtAddress.getText().length());
            }
            //机构名
            List<String> companys = cardBean.getContent().getJson().get(0).getCompany();
            if (companys != null && companys.size() > 0) {
                scanSearchComplany(companys.get(0));
            }

        }
        mActivity.addTag(TAG1);
        getJobList();
    }

    /**
     * 检查手机号是否被注册
     */
    private void checkPhone(final String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("mobilephone", phone);
        mDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.CHECK_MOBILE, TAG1, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mDialog.dismiss();
                LogUtils.dazhiLog("检查手机号---》" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (GenalralUtils.isEmpty(jsonObject.optString("content"))) {
                        ((RegisterActivity) mActivity).switchToFragment(1);
                    } else {
                        edtPhone.setText("");
                        ToastUtils.shortToast(mActivity, "手机号已经被注册!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                ((RegisterActivity) mActivity).switchToFragment(1);
                LogUtils.dazhiLog("检查手机号错误---》" + errorMsg);
            }
        });
    }

    /**
     * 设置职务列表
     *
     * @param json
     */
    private void setJoblist(String json) {
        JobBean bean = JsonUtils.stringToObject(json, JobBean.class);

        if (bean != null && bean.getCode().equals("0")) {
            jobList = new ArrayList<>();
            jobList.addAll(bean.getContent());
            List<String> list = new ArrayList<>();
            for (JobBean.ContentBean item : jobList) {
                list.add(item.getJobName());
            }
            int jobPosition = 0;//对比位置
            if (jobList.size() > 0) {
                if (cardBean != null) {
                    List<String> jobs = cardBean.getContent().getJson().get(0).getTitle();
                    if (jobs != null && jobs.size() > 0) {
                        for (JobBean.ContentBean item : jobList) {
                            list.add(item.getJobName());
                            if (jobs.get(0).equals(item.getJobName())) {
                                jobName = item.getJobName();
                                jobID = item.getId();
                                jobPosition = list.size() - 1;
                                break;
                            }
                        }
                    }
                }
                if (jobPosition == 0) {
                    jobName = jobList.get(0).getJobName();
                    jobID = jobList.get(0).getId();
                }
            }
            spinnerJob.attachDataSource(list);
            spinnerJob.addOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    jobName = jobList.get(position).getJobName();
                    jobID = jobList.get(position).getId();
                }
            });
            spinnerJob.setSelectedIndex(jobPosition);
        }
    }

    /**
     * 职位列表
     */
    private void getJobList() {
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.JOB_LIST, TAG1, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("职务列表---》" + jsonString);
                setJoblist(jsonString);
                PerferenceUtils.put(AppConstants.DATA_JOB_LIS_KEY, jsonString);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    /**
     * 搜索机构名称
     */
    @OnClick(R.id.tv_complany)
    void searchComplany() {
        Intent intent = new Intent(mActivity, SearchComplanyActivity.class);
        startActivityForResultWithAnim(intent, SEARCH_COMPLANY_CODE);
    }

    @OnClick(R.id.btn_step1_next)
    void nextStep() {
        if (checkDataIsComplete()) {
            String mobilePhone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String tel = edtTel.getText().toString().trim();
            if (!GenalralUtils.checkMobilePhome(mobilePhone)) {
                ToastUtils.shortToast(mActivity, "手机号码格式不正确！");
                return;
            }
            if (!TextUtils.isEmpty(email) && !GenalralUtils.isEmail(email)) {
                ToastUtils.shortToast(mActivity, "邮箱格式不正确！");
                return;
            }
            if (!TextUtils.isEmpty(tel) && !GenalralUtils.isTelephone(tel)) {
                ToastUtils.shortToast(mActivity, "固话格式不正确！");
                return;
            }
            checkPhone(edtPhone.getText().toString());
        } else {
            ToastUtils.shortToast(mActivity, "请您将资料填写完整！");
        }
    }

    /**
     * 检查资料是否填写完成
     *
     * @return
     */
    private boolean checkDataIsComplete() {
        if (TextUtils.isEmpty(mEdtName.getText().toString().trim())
//                || TextUtils.isEmpty(edtEmail.getText().toString().trim())
//                || TextUtils.isEmpty(edtTel.getText().toString().trim())
                || TextUtils.isEmpty(edtDepartment.getText().toString().trim())
                || TextUtils.isEmpty(edtPhone.getText().toString().trim())
                || TextUtils.isEmpty(orgID)
                || TextUtils.isEmpty(orgName)
                || TextUtils.isEmpty(jobID)
                || TextUtils.isEmpty(jobName)) {
            return false;
        } else {
            return true;
        }

    }


    public String getMobilePhone() {
        return edtPhone.getText().toString().trim();
    }


    public Map<String, String> getData() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", mEdtName.getText().toString().trim());
        map.put("email", edtEmail.getText().toString().trim());
        map.put("mobilephone", edtPhone.getText().toString().trim());
        map.put("phone", edtTel.getText().toString().trim());
        map.put("divName", edtDepartment.getText().toString().trim());
        map.put("addr", edtAddress.getText().toString().trim());
        if (genderPosition == 0) {
            map.put("gender", "MAN");
        } else {
            map.put("gender", "WOMAN");
        }
        map.put("orgName", orgName);
        map.put("orgId", orgID);
        map.put("jobId", jobID);
        map.put("jobName", jobName);
        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SEARCH_COMPLANY_CODE) {
            orgName = data.getStringExtra("org_name");
            orgID = data.getStringExtra("org_id");
            tvComplany.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_text));
            tvComplany.setText(orgName);
        }
    }

    void scanSearchComplany(String keyword) {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("orgName", keyword);
        String TAG = "search_complany";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.SEARCH_COMPLANY, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("机构搜索---结果》" + jsonString);
                mDialog.dismiss();
                SearchComplanyBean bean = JsonUtils.stringToObject(jsonString, SearchComplanyBean.class);
                if (bean != null && bean.getCode().equals("0")) {
                    if (bean.getContent() != null && bean.getContent().size() > 0) {
                        orgID = bean.getContent().get(0).getId();
                        orgName = bean.getContent().get(0).getOrgName();
                        tvComplany.setText(orgName);
                        tvComplany.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_text));
                    }
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                LogUtils.dazhiLog("机构搜索---》失败" + errorMsg);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
