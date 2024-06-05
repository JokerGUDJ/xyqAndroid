package com.xej.xhjy.https;


import com.xej.xhjy.common.utils.LogUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class SafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        LogUtils.dazhiLog("----------------------------------"+hostname);
        if (NetConstants.BASE_IP.contains(hostname)) {//校验hostname是否正确，如果正确则建立连接
            return true;
        }
        return false;
    }
}
