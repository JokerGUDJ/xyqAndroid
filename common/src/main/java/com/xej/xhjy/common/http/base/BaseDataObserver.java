package com.xej.xhjy.common.http.base;
import com.xej.xhjy.common.http.bean.BaseData;
import com.xej.xhjy.common.http.exception.ApiException;
import com.xej.xhjy.common.http.interfaces.IDataSubscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
/**
 * @class BaseDataObserver
 * @author dazhi
 * @Createtime 2018/5/29 17:56
 * @description 基类BaseObserver使用BaseData
 * @Revisetime
 * @Modifier
 */

public abstract class BaseDataObserver<T> implements Observer<BaseData<T>>, IDataSubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(BaseData<T> baseData) {
        doOnNext(baseData);
    }

    @Override
    public void onError(Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        setError(error);
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }

}
