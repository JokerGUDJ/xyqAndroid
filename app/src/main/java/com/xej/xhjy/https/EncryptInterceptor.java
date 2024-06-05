package com.xej.xhjy.https;

import com.xej.xhjy.common.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author dazhi
 * @class EncryptInterceptor
 * @Createtime 2018/6/6 10:48
 * @description 请求加解密拦截器
 * @Revisetime
 * @Modifier
 */
public class EncryptInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //这个是请求的url，也就是咱们前面配置的baseUrl
        String url = request.url().toString();
        LogUtils.dazhiLog("请求的url---"+url);
        //这个是请求方法
//        String method = request.method();
//        LogUtils.dazhiLog("请求的方法---"+method);
        //获取请求body，只有@Body 参数的requestBody 才不会为 null
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            okio.Buffer buffer = new okio.Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String string = buffer.readString(charset);
            LogUtils.dazhiLog("加密前的参数---"+string);
            String encryptStr = HttpEncryptUtils.encryptRequest(string);
//            LogUtils.dazhiLog("加密之后参数---"+encryptStr);
            RequestBody body = MultipartBody.create(contentType, encryptStr);
            request = request.newBuilder()
                    .post(body)
                    .build();
        }
        Response response = chain.proceed(request);
        if (response.isSuccessful()) {
            //the response data
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String string = buffer.clone().readString(charset);
            String bodyString = HttpEncryptUtils.decryptResponse(string);
            LogUtils.dazhiLog("解密后数据---"+bodyString);
            ResponseBody responseBody = ResponseBody.create(contentType, bodyString);
            response = response.newBuilder().body(responseBody).build();
        }
        return response;
    }
}
