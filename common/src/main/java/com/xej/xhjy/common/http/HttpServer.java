package com.xej.xhjy.common.http;

import com.google.gson.Gson;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.interceptor.Transformer;
import com.xej.xhjy.common.http.interfaces.ApiService;
import com.xej.xhjy.common.http.interfaces.HttpCallBack;
import com.xej.xhjy.common.http.observer.StringObserver;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.common.http
 * @ClassName: HttpServer
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/7/15 下午10:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/15 下午10:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HttpServer {


    /**
     * 传入Url返回String不需要Dialog
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param maps     需要拼的地址
     */
    public static void doPostStringWithUrl(final BaseActivity activity, String url, final String tag, Map<String, String> maps, final HttpCallBack callBack) {
        ApiService api = RxHttpUtils.getInstance().createApi(ApiService.class);
        Gson gson = new Gson();
        String strEntity = gson.toJson(maps);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        api.executePost(url, body)
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new StringObserver(tag) {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        callBack.onSucess(data);
                    }
                });
    }


}
