package com.xej.xhjy.ui.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.dsbridge.DWebView;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.view.TitleView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dazhi
 * @class WebPagerActivity
 * @Createtime 2018/6/7 17:02
 * @description 其它H5加载页面，没有H5交互
 * @Revisetime
 * @Modifier
 */
public class WebOtherPagerActivity extends BaseActivity {
    /**
     * 网页地址
     */
    public static final String LOAD_URL = "url_address";
    public static final String HEAD_TITLE = "head_title";
    public static final String LOAD_CONTENT = "content_html";
    @BindView(R.id.ll_webContain)
    LinearLayout llWebContain;
    DWebView mWebView;
    String mUrl,content;
    @BindView(R.id.titleview)
    TitleView titleview;
    private ClubLoadingDialog mLoadingDialog;
    private boolean isLoadSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_web_pager);
        //控件绑定
        ButterKnife.bind(this);
        titleview.setTitle(getIntent().getStringExtra(HEAD_TITLE));
        mLoadingDialog = new ClubLoadingDialog(this);
        Intent intent = getIntent();
        if(intent != null){
            mUrl = intent.getStringExtra(LOAD_URL);
            content = intent.getStringExtra(LOAD_CONTENT);
        }
        mWebView = new DWebView(this);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llWebContain.addView(mWebView, ll);
        DWebView.setWebContentsDebuggingEnabled(AppConstants.IS_DEBUG);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    if (!isLoadSuccess) {
                        isLoadSuccess = true;
                        //页面加载完成首先传字典过去
                        mLoadingDialog.dismiss();
                    }
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                isLoadSuccess = false;
                mLoadingDialog.dismiss();
                super.onReceivedError(webView, i, s, s1);
            }
        });

        /**
         * 增加一个方法，显示地图并获取地图选址信息
         */
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http://callback")) {
                    view.loadUrl(url);
                } else {
                    try {
                        String decode = URLDecoder.decode(url, "UTF-8");
                        Uri uri = Uri.parse(decode);
                        String mNewAddress = uri.getQueryParameter("addr");
                        LogUtils.dazhiLog("address=" + mNewAddress);
                        Intent intent = new Intent();
                        intent.putExtra("loaction_addr", mNewAddress);
                        setResult(RESULT_OK, intent);
                        WebOtherPagerActivity.this.finish();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        if(!TextUtils.isEmpty(mUrl)){
            mWebView.loadUrl(mUrl);
        }else if(!TextUtils.isEmpty(content)){
            mWebView.loadDataWithBaseURL(null, content, "text/html" , "utf-8", null);
        }
        mLoadingDialog.show();
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {//webview退出，否则会造成内存泄漏
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
