package com.xej.xhjy.common.http.base;

import com.xej.xhjy.common.http.exception.ApiException;
import com.xej.xhjy.common.http.interfaces.IStringSubscriber;
import com.xej.xhjy.common.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @class BaseStringObserver
 * @author dazhi
 * @Createtime 2018/5/29 17:56
 * @description 结果不做处理直接返回string
 * @Revisetime
 * @Modifier
 */

public abstract class BaseStringObserver implements Observer<String>, IStringSubscriber {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return true;
    }

    @Override
    public void onSubscribe(Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(String string) {
        doOnNext(string);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String error = ApiException.handleException(e).getMessage();
        doOnError(error);
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }

}
