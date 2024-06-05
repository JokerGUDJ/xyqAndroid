package com.xej.xhjy.common.http.observer;

import android.app.Dialog;
import com.xej.xhjy.common.http.base.BaseDataObserver;
import com.xej.xhjy.common.http.bean.BaseData;
import com.xej.xhjy.common.http.http.RxHttpManager;

import io.reactivex.disposables.Disposable;

/**
 * @class DataObserver
 * @author dazhi
 * @Createtime 2018/5/30 09:07
 * @description 针对特定格式的时候设置的通用的Observer
 *         用户可以根据自己需求自定义自己的类继承BaseDataObserver<T>即可
 *         适用于
 *         {
 *         "code":200,
 *         "msg":"成功"
 *         "data":{
 *         "userName":"test"
 *         "token":"abcdefg123456789"
 *         "uid":"1"}
 *         }
 * @Revisetime
 * @Modifier
 */
public abstract class DataObserver<T> extends BaseDataObserver<T> {

    private Dialog mProgressDialog;
    private String mTag;
    public DataObserver() {
    }

    public DataObserver(Dialog progressDialog,String tag) {
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
    protected abstract void onSuccess(T data);

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
    public void doOnNext(BaseData<T> data) {
        onSuccess(data.getData());
        RxHttpManager.removeDisposable(mTag);
        //可以根据需求对code统一处理
//        switch (data.getCode()) {
//            case 200:
//                onSuccess(data.getData());
//                break;
//            case 300:
//            case 500:
//                onError(data.getMsg());
//                break;
//            default:
//        }
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
