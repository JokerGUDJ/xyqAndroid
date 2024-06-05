package com.xej.xhjy.common.dsbridge;

/**
 * @class OnReturnValue
 * @author dazhi
 * @Createtime 2018/7/4 18:48
 * @description  回调
 * @Revisetime
 * @Modifier
 */

public interface OnReturnValue<T> {
    void onValue(T retValue);
}
