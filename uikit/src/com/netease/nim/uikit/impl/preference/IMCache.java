package com.netease.nim.uikit.impl.preference;

import android.content.Context;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by jezhee on 2/20/15.
 */
public class IMCache {

    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        IMCache.account = account;
        NimUIKit.setAccount(account);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        IMCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        IMCache.context = context.getApplicationContext();
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        IMCache.mainTaskLaunching = mainTaskLaunching;
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }
}
