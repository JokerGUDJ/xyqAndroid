package com.xej.xhjy.common.http.bean;

/**
 * @class BaseData
 * @author dazhi
 * @Createtime 2018/5/29 17:57
 * @description 返回数据基类
 * @Revisetime
 * @Modifier
 */

public class BaseData<T> {
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
