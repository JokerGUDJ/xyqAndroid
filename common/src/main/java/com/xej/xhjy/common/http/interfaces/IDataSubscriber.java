package com.xej.xhjy.common.http.interfaces;

import com.xej.xhjy.common.http.bean.BaseData;

import io.reactivex.disposables.Disposable;

/**
 * @class IDataSubscriber
 * @author dazhi
 * @Createtime 2018/5/29 17:57
 * @description 定义请求结果处理接口
 * @Revisetime
 * @Modifier
 */

public interface IDataSubscriber<T> {

    /**
     * doOnSubscribe 回调
     *
     * @param d Disposable
     */
    void doOnSubscribe(Disposable d);

    /**
     * 错误回调
     *
     * @param errorMsg 错误信息
     */
    void doOnError(String errorMsg);

    /**
     * 成功回调
     *
     * @param baseData 基础泛型
     */
    void doOnNext(BaseData<T> baseData);

    /**
     * 请求完成回调
     */
    void doOnCompleted();
}
