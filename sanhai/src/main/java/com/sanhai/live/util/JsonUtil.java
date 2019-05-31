package com.sanhai.live.util;

import com.google.gson.Gson;


public class JsonUtil {
    private static Gson gson = new Gson();

    /**
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {

        return gson.toJson(src);
    }


}


