package com.xej.xhjy.common.dsbridge;

/**
 * @class CompletionHandler
 * @author dazhi
 * @Createtime 2018/7/4 18:47
 * @description 回调
 * @Revisetime
 * @Modifier
 */

public interface  CompletionHandler<T> {
    void complete(T retValue);
    void complete();
    void setProgressData(T value);
}
