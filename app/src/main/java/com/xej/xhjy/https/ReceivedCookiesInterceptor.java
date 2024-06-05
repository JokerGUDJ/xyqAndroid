package com.xej.xhjy.https;


import android.text.TextUtils;

import com.xej.xhjy.common.storage.PerferenceUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

import static java.util.Calendar.getInstance;

/**
 * @class ReceivedCookiesInterceptor
 * @author dazhi
 * @Createtime 2018/5/30 09:05
 * @description 接受服务器发的cookie   并保存到本地并在下次请求的请求头带上所有cookie字段
 * @Revisetime
 * @Modifier
 */

public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            //将所有的cookie存储起来 放在请求头里用
            for (String header : originalResponse.headers("Set-Cookie")) {
//                LogUtils.dazhiLog("cookie---"+header);
                String [] cookie = header.split(";")[0].split("=");
                if (cookie.length == 1){
                    continue;
                }
                //拿到存储的cookie的所有key
                String cookieKey = PerferenceUtils.get(NetConstants.COOKIE_KEY,"");
                String [] keys = cookieKey.split(",");
                boolean isHas = false;
                //对比此次key是否有重复
                for (String key :keys){
                    if (key.equals(cookie[0])){
                        isHas = true;
                        break;
                    }
                }
                if (!isHas){
                    if (TextUtils.isEmpty(cookieKey)){
                        cookieKey = cookie[0];
                    } else {
                        cookieKey = cookieKey+","+cookie[0];
                    }
                }
//                LogUtils.dazhiLog("cookieKey---"+cookieKey);
                //存储cookie Key
                PerferenceUtils.put(NetConstants.COOKIE_KEY,cookieKey);
                //存储cookie Key和值
                PerferenceUtils.put(cookie[0],cookie[1]);
                cookies.add(header);
            }
        }
        //获取服务器相应时间--用于计算倒计时的时间差
//        if (!originalResponse.header("Date").isEmpty()) {
//            long date = dateToStamp(originalResponse.header("Date"));
//            PerferenceUtils.put(NetConstants.DATE, date);
//        }
        return originalResponse;
    }


    /**
     * 将时间转换为时间戳
     *
     * @param s date
     * @return long
     * @throws android.net.ParseException
     */
    public static long dateToStamp(String s) throws android.net.ParseException {
        //转换为标准时间对象
        Date date = new Date(s);
        Calendar calendar = getInstance();
        calendar.setTime(date);
        long mTimeInMillis = calendar.getTimeInMillis();
        return mTimeInMillis;
    }
}
