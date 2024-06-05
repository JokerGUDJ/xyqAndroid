package com.xej.xhjy.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.SearchComplanyBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.KeybordUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecyclerView;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class SearchComplanyActivity 机构搜索页面
 * @Createtime 2018/6/22 10:49
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class SearchComplanyActivity extends BaseActivity {
    final String TAG = "search_complany";
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.search_complany_list)
    CommonRecyclerView searchComplanyList;
    ComplanySearchAdapter searchAdapter;
    private List<SearchComplanyBean.ContentBean> mList;
    private ClubLoadingDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_complany);
        ButterKnife.bind(this);
        mDialog = new ClubLoadingDialog(this);
        mList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchComplanyList.setLayoutManager(layoutManager);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        searchComplanyList.setLayoutAnimation(animation);
        searchAdapter = new ComplanySearchAdapter(this, mList, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent();
                intent.putExtra("org_name", mList.get(position).getOrgName());
                intent.putExtra("org_id", mList.get(position).getId());
                setResult(RESULT_OK, intent);
                finishWithAnim();
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        searchComplanyList.setAdapter(searchAdapter);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    search();
                    return true;
                }
                return false;
            }
        });
    }


    private void search() {

        String keyword = edtSearch.getText().toString().trim();
        if (GenalralUtils.isEmpty(keyword)){
            ToastUtils.shortToast(this,"请输入机构关键字！");
            return;
        }
        if (KeybordUtils.isSoftInputShow(this)) {
            KeybordUtils.closeKeybord(edtSearch, this);
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("orgName",keyword);
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.SEARCH_COMPLANY, TAG,map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("机构搜索---结果》" + jsonString);
                mDialog.dismiss();
                SearchComplanyBean bean = JsonUtils.stringToObject(jsonString, SearchComplanyBean.class);
                if (bean != null && bean.getCode().equals("0")) {
                    mList.clear();
                    mList.addAll(bean.getContent());
                    if (mList != null && mList.size() > 0) {
                        LogUtils.dazhiLog("机构搜索---mList》" + mList.size());
                        searchAdapter.notifyDataSetChanged();
                        searchComplanyList.scheduleLayoutAnimation();
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
}
