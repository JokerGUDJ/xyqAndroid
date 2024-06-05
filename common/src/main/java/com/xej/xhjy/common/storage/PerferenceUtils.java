package com.xej.xhjy.common.storage;

import android.app.Application;

/**
 * @author dazhi
 * @class PerferenceUtils
 * @Createtime 2018/5/31 11:25
 * @description 轻量化加密存储
 * @Revisetime
 * @Modifier
 */
public class PerferenceUtils {
    private static CommonPerference prefStore;

    public static void init(Application app) {
        try {
            prefStore = new CommonPerference(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * String存取
     */
    public static void put(String key, String value) {
        prefStore.putString(key, value);
    }

    public static String get(String key, String defValue) {
        return prefStore.getString(key, defValue);
    }

    /**
     * int存取
     */
    public static void put(String key, int value) {
        prefStore.putInt(key, value);
    }

    public static int get(String key, int defValue) {
        return prefStore.getInt(key, defValue);
    }

    /**
     * boolean 存储
     *
     * @param key
     * @param defValue
     * @return
     */

    public static boolean get(String key, boolean defValue) {
        return prefStore.getBoolean(key, defValue);

    }

    public static void put(String key, boolean defValue) {
        prefStore.putBoolean(key, defValue);

    }

//    /**
//     * long存取
//     */
//    public static void put(String key, long value) {
//        prefStore.edit().putLong(key, value).apply();
//    }
//
//    public static long get(String key, long defValue) {
//        return prefStore.getLong(key, defValue);
//    }
//    /**
//     * HashSet存取
//     */
//    public static void put(String key, HashSet value) {
//        prefStore.edit().putStringSet(key, value).apply();
//    }
//
//    public static Set<String> get(String key, Set<String> defaultValue) {
//        return prefStore.getStringSet(key, defaultValue);
//    }

    /**
     * 删除一个key
     */
    public static void removeKey(String key) {
        prefStore.getEdit().remove(key).apply();
    }


}
