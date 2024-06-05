package com.xej.xhjy.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author dazhi
 * @class BaseWebview
 * @Createtime 2018/6/7 10:52
 * @description 初始化一些webview的基本设置，便于后期封装或者替换
 * @Revisetime
 * @Modifier
 */
public class BaseWebview extends WebView {
    private BaseActivity activity;
    //加载失败的地址，一般放在assest里的静态页面
    private String errorUrl;
    private WebLoadingCallback mCallBack;
    public BaseWebview(Context context) {
        super(context);
        setUpWebview();
        activity = (BaseActivity)context;
    }

    public BaseWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUpWebview();
        activity = (BaseActivity)context;
    }

    private void setUpWebview() {
        this.setInitialScale(0);
        this.setVerticalScrollBarEnabled(false);
        this.requestFocusFromTouch();
        WebSettings settings = this.getSettings();
        //支持JS通信
        settings.setJavaScriptEnabled(true);
        //支持插件
        settings.setPluginsEnabled(true);
        //设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 不需要缓存数据
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setDomStorageEnabled(false);
        // 支持定位
        settings.setGeolocationEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        settings.setAllowFileAccess(true); //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片

        this.setWebViewClient(new MyWebViewClient());
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mCallBack!=null){
                    mCallBack.onProgressChanged(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }
    public void setCallBack(WebLoadingCallback callBack){
        this.mCallBack = callBack;
    }
    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            if (mCallBack!=null){
                mCallBack.start();
            }

        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (mCallBack!=null){
                mCallBack.finish();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            LogUtils.dazhiLog("调用的url---"+url);
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String errorFlagString = "";
            switch (errorCode) {
                // User authentication failed on server
                case ERROR_AUTHENTICATION:
                    errorFlagString = "用户认证失败!";
                    break;
                // Failed to connect to the server
                case ERROR_CONNECT:
                    errorFlagString = "连接服务器失败!";
                    break;
                // Connection timed out
                case ERROR_TIMEOUT:
                    errorFlagString = "网络连接超时!";
                    break;
                case ERROR_PROXY_AUTHENTICATION:
                    errorFlagString = "用户代理验证失败!";
                    break;
                case ERROR_HOST_LOOKUP:
                    errorFlagString = "服务器绑定或代理失败!";
                    break;
                case ERROR_BAD_URL:
                    errorFlagString = "URL 格式错误!";
                    break;
                default:
                    errorFlagString = "未知错误!";
                    break;
            }
            ToastUtils.shortToast(activity,errorFlagString);
            if (getErrorUrl()!=null&&!"".equals(getErrorUrl())) {
                view.loadUrl(getErrorUrl());
            }
        }
    }
}
