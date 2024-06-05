package com.xej.xhjy.common.http.base;

import com.xej.xhjy.common.http.exception.ApiException;
import com.xej.xhjy.common.http.interfaces.ISubscriber;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * @class BaseObserver
 * @author dazhi
 * @Createtime 2018/5/29 17:56
 * @description 基类BaseObserver
 * @Revisetime
 * @Modifier
 */

public abstract class BaseObserver<T> implements Observer<T>, ISubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return false;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
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
