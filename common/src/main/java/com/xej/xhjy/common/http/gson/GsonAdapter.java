package com.xej.xhjy.common.http.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @class GsonAdapter
 * @author dazhi
 * @Createtime 2018/5/29 18:00
 * @description describe
 * @Revisetime
 * @Modifier
 */

public class GsonAdapter {

    public static Gson buildGson() {
        Gson gson = null;
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .create();
        }
        return gson;
    }
}
