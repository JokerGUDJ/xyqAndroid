package com.xej.xhjy.https;

import android.app.Application;
import android.app.Dialog;
import android.util.Log;

import com.google.gson.Gson;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.http.RxHttpUtils;
import com.xej.xhjy.common.http.interceptor.Transformer;
import com.xej.xhjy.common.http.interfaces.ApiService;
import com.xej.xhjy.common.http.observer.StringObserver;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.xej.xhjy.common.http.RxHttpUtils.getSInstance;

/**
 * @author dazhi
 * @class RxHttpClient
 * @Createtime 2018/5/30 11:26
 * @description 再次封装的HttpClient请求
 * @Revisetime
 * @Modifier
 */
public class RxHttpClient {
    private static ApiService apiService;

    /**
     * 初始化网络请求配置，需传入Application
     *
     * @param app Application
     */
    public static void initHttpClient(Application app) {
        RxHttpUtils.init(app);
        RxHttpUtils.getInstance()
                .config()
                //全局的BaseUrl
                .setBaseUrl(NetConstants.BASE_URL)
                .addInterceptor(new ReceivedCookiesInterceptor())
                //添加加密拦截器
                .addInterceptor(new EncryptInterceptor())
                //添加Header拦截器
                .addInterceptor(new MyHeaderInterceptor())
                .setCache()
                //全局超时配置
                .setReadTimeout(NetConstants.READ_TIME_OUT)
                //全局超时配置
                .setWriteTimeout(NetConstants.WRITE_TIME_OUT)
                //全局超时配置
                .setConnectTimeout(NetConstants.CONNECT_TIME_OUT)
                //全局是否打开请求log日志
                .setLog(AppConstants.IS_DEBUG)
                .setSslSocketFactory();
        apiService = RxHttpUtils.getInstance().createApi(ApiService.class);
    }


    /**
     * 下载文件
     *
     * @param fileUrl 地址
     * @return ResponseBody
     */
    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadCommonRetrofit.downloadFile(fileUrl);
    }


    /**
     * 自定义网络请求时，需要获取apiService调用方法
     *
     * @return
     */
    public static ApiService getApiService() {
        return apiService;
    }


    /**
     * 传入Url返回String
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param maps     需要拼的地址
     * @param dialog   需要的Dialog
     */
    public static void doPostStringWithUrl(final BaseActivity activity, String url, String tag, Map<String, String> maps, Dialog dialog, final HttpCallBack callBack) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(maps);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        apiService.executePost(url, body)
                .compose(Transformer.<String>switchSchedulers(dialog))
                .subscribe(new StringObserver(dialog, tag) {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        callBack.onSucess(data);
                        HttpDialogUtils.doBackString(activity, data);
                    }
                });
    }

    /**
     * 传入Url返回String,不需要地址参数map拼接
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param dialog   需要的Dialog
     */
    public static void doPostStringWithUrl(final BaseActivity activity, String url, String tag, Dialog dialog, final HttpCallBack callBack) {
        Map<String, String> maps = new HashMap<>();
        Gson gson = new Gson();
        String strEntity = gson.toJson(maps);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        apiService.executePost(url, body)
                .compose(Transformer.<String>switchSchedulers(dialog))
                .subscribe(new StringObserver(dialog, tag) {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        callBack.onSucess(data);
                        HttpDialogUtils.doBackString(activity, data);
                    }
                });
    }

    /**
     * 传入Url返回String不需要Dialog
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param maps     需要拼的地址
     */
    public static void doPostStringWithUrl(final BaseActivity activity, String url, final String tag, Map<String, String> maps, final HttpCallBack callBack) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(maps);
        final ClubLoadingDialog dialog = new ClubLoadingDialog(activity);
        Log.d("lirong", "tag ="+tag);
        if(dialog != null){
            dialog.show();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        apiService.executePost(url, body)
                .compose(Transformer.<String>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(dialog != null){
                            dialog.dismiss();
                            Log.d("lirong", "tag_end ="+tag);
                        }
                    }
                })
                .subscribe(new StringObserver(tag) {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        HttpDialogUtils.doBackString(activity, data);
                        callBack.onSucess(data);
                    }
                });
    }

    /**
     * 传入Url返回String,不需要地址参数map拼接，不需要Dialog
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     */
    public static void doPostStringWithUrl(final BaseActivity activity, String url, final String tag, final HttpCallBack callBack) {
        Map<String, String> maps = new HashMap<>();
        Gson gson = new Gson();
        String strEntity = gson.toJson(maps);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        apiService.executePost(url, body)
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new StringObserver(tag) {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        callBack.onSucess(data);
                        HttpDialogUtils.doBackString(activity, data);
                    }
                });
    }

    /**
     * 传入Url返回String
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param maps     需要拼的地址
     * @param dialog   需要的Dialog
     */
    public static void doGetStringWithUrl(String url, String tag, Map<String, String> maps, Dialog dialog, final HttpCallBack callBack) {
        apiService.executeGet(url, maps)
                .compose(Transformer.<String>switchSchedulers(dialog))
                .subscribe(new StringObserver(dialog, tag) {
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

    /**
     * 传入Url返回String,不需要地址参数map拼接
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param dialog   需要的Dialog
     */
    public static void doGetStringWithUrl(String url, String tag, Dialog dialog, final HttpCallBack callBack) {
        apiService.executeGet(url)
                .compose(Transformer.<String>switchSchedulers(dialog))
                .subscribe(new StringObserver(dialog, tag) {
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

    /**
     * 传入Url返回String不需要Dialog
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     * @param maps     需要拼的地址
     */
    public static void doGetStringWithUrl(String url, String tag, Map<String, String> maps, final HttpCallBack callBack) {
        apiService.executeGet(url, maps)
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

    /**
     * 传入Url返回String,不需要地址参数map拼接，不需要Dialog
     *
     * @param url      传入后缀而非全拼Url
     * @param callBack 回调函数
     * @param tag      每个页面的tag，用于取消请求，防止内存泄漏
     */
    public static void doGetStringWithUrl(String url, String tag, final HttpCallBack callBack) {
        apiService.executeGet(url)
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new StringObserver() {
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

    /**
     * 单个非全局网关请求示例，要在ApiService类似getBookString里定义接口请求
     *
     * @param baseurl  新的网关
     * @param callBack 回调
     */
    public void singleHttp(String baseurl, final HttpCallBack callBack) {
        getSInstance()
                .baseUrl(baseurl)
                //注意这两个配置的顺序
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .createSApi(ApiService.class)
                .getOtherString()
                .compose(Transformer.<String>switchSchedulers())
                .subscribe(new StringObserver() {
                    @Override
                    protected void onError(String errorMsg) {
                        callBack.onError(errorMsg);
                        LogUtils.dazhiLog("单个请求errorMsg-----" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        callBack.onSucess(data);
                        LogUtils.dazhiLog("单个请求-----" + data);
                    }
                });
    }
}
