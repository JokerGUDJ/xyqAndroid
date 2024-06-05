package com.xej.xhjy.common.base;

/**
 * @class BaseBean
 * @author dazhi
 * @Createtime 2018/7/3 09:27
 * @description describe 网络请求集成bean
 * @Revisetime
 * @Modifier
 */
public class BaseData<T> {

    private static int SUCCESS_CODE = 0;//成功的code
    private int code;
    private String msg;
    private T content;

    public boolean isSuccess() {
        return getCode() == SUCCESS_CODE;
    }

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

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
