package com.xej.xhjy.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xej.xhjy.common.base.BaseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dazhi
 * @class JsonUtils 使用GSON解析数据的工具类
 * @Createtime 2018/3/29 09:31
 * @description describe
 * @Revisetime
 * @Modifier
 */

public class JsonUtils {
    private static Gson mGson = new Gson();

    /**
     * 将json字符串转化成实体对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T stringToObject(String json, Class<T> cls){
        T t = null;
        try {
            t = mGson.fromJson(json,cls);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 必须为JSONArray 字符串转化成list bean
     *
     * @param json 必须为JSONArray
     * @param cls  转换的类型bean
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(mGson.fromJson(elem, cls));
        }
        return mList;
    }


    /**
     * jsonObject转Map
     *
     * @param jsonObject JSONObject
     * @return
     */
    public static Map<String, String> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, String> data = new HashMap<String, String>();
        try {
            Iterator ite = jsonObject.keys();
            // 遍历jsonObject数据,添加到Map对象
            while (ite.hasNext()) {
                String key = ite.next().toString();
                String value = jsonObject.get(key).toString();
                data.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static BaseData fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(BaseData.class, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
