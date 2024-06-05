package com.xej.xhjy.common.http.observer;

import android.app.Dialog;
import com.xej.xhjy.common.http.base.BaseObserver;
import com.xej.xhjy.common.http.http.RxHttpManager;
import io.reactivex.disposables.Disposable;

/**
 * @class CommonObserver
 * @author dazhi
 * @Createtime 2018/5/30 15:36
 * @description 用户可以根据自己需求自定义自己的类继承BaseObserver<T>即可
 * @Revisetime
 * @Modifier
 */

public abstract class CommonObserver<T> extends BaseObserver<T> {

    private Dialog mProgressDialog;
    private String mTag;

    public CommonObserver() {
    }

    public CommonObserver(Dialog progressDialog,String tag) {
        mProgressDialog = progressDialog;
        mTag = tag;
    }
    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);
    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);



    @Override
    public void doOnSubscribe(Disposable d) {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
        RxHttpManager.addDisposable(mTag,d);
    }

    @Override
    public void doOnError(String errorMsg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        onError(errorMsg);
        RxHttpManager.removeDisposable(mTag);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
        RxHttpManager.removeDisposable(mTag);
    }

    @Override
    public void doOnCompleted() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
