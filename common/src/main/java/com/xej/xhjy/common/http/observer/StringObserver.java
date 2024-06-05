package com.xej.xhjy.common.http.observer;

import android.app.Dialog;

import com.xej.xhjy.common.http.base.BaseStringObserver;
import com.xej.xhjy.common.http.http.RxHttpManager;

import io.reactivex.disposables.Disposable;

/**
 * @class StringObserver
 * @author dazhi
 * @Createtime 2018/5/30 09:08
 * @description describe
 * @Revisetime
 * @Modifier
 */
public abstract class StringObserver extends BaseStringObserver {

    private Dialog mProgressDialog;
    private String mTag;
    public StringObserver() {
    }
    public StringObserver(String tag) {
        mTag = tag;
    }
    public StringObserver(Dialog progressDialog,String tag) {
        mProgressDialog = progressDialog;
        mTag = tag;
    }

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param data 结果
     */
    protected abstract void onSuccess(String data);


    @Override
    public void doOnSubscribe(Disposable d) {
        RxHttpManager.addDisposable(mTag,d);
    }

    @Override
    public void doOnError(String errorMsg) {
        dismissLoading();
        onError(errorMsg);
        RxHttpManager.removeDisposable(mTag);
    }

    @Override
    public void doOnNext(String string) {
        onSuccess(string);
        RxHttpManager.removeDisposable(mTag);
    }


    @Override
    public void doOnCompleted() {
        dismissLoading();
    }
    /**
     * 隐藏loading对话框
     */
    private void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
