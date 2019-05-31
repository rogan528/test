package com.sanhai.live.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sanhai.live.module.CommonJson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtil {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static OkHttpClient okHttpClient;
    private static JSONObject jsonObject;


    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (HttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)//10秒连接超时
                            .writeTimeout(10, TimeUnit.SECONDS)//10m秒写入超时
                            .readTimeout(10, TimeUnit.SECONDS)//10秒读取超时
                            //.addInterceptor(new HttpHeaderInterceptor())//头部信息统一处理
                            //.addInterceptor(new CommonParamsInterceptor())//公共参数统一处理
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * @param url      url地址
     * @param callBack 请求回调接口
     */
    public static void get(String url, HttpCallBack callBack) {
        commonGet(getRequestForGet(url, null, null), callBack.GetTtype(), callBack);
    }
//    /**
//     * @param url      url地址
//     * @param cls      泛型返回参数
//     * @param callBack 请求回调接口
//     */
//    public static <T extends CommonJson> void get(String url, Class<T> cls, HttpCallBack<T> callBack) {
//        commonGet(getRequestForGet(url, null, null), callBack.GetTtype(), callBack);
//    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     */
    public static void get(String url, HashMap<String, String> params, HttpCallBack callBack) {
        commonGet(getRequestForGet(url, params, null), callBack.GetTtype(), callBack);
    }

//    /**
//     * @param url      url地址
//     * @param params   HashMap<String, String> 参数
//     * @param cls      泛型返回参数
//     * @param callBack 请求回调接口
//     */
//    public static <T extends CommonJson> void get(String url, HashMap<String, String> params, Class<T> cls, HttpCallBack<T> callBack) {
//        commonGet(getRequestForGet(url, params, null), cls, callBack);
//    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     * @param tag      网络请求tag
     */
    public static void get(String url, HashMap<String, String> params, HttpCallBack callBack, Object tag) {
        commonGet(getRequestForGet(url, params, tag), callBack.GetTtype(), callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     * @param cls      泛型返回参数
     * @param tag      网络请求tag
     */
    public static <T extends CommonJson> void get(String url, HashMap<String, String> params, Class<T> cls, HttpCallBack<T> callBack, Object tag) {
        commonGet(getRequestForGet(url, params, tag), cls, callBack);
    }


    /**
     * @param url      url地址
     * @param callBack 请求回调接口
     */
    public static void post(String url, HttpCallBack callBack) {
        commonPost(getRequestForPost(url, null, null), callBack.GetTtype(), callBack);
    }

//    /**
//     * @param url      url地址
//     * @param cls      泛型返回参数
//     * @param callBack 请求回调接口
//     */
//    public static <T extends CommonJson> void post(String url, Class<T> cls, HttpCallBack<T> callBack) {
//        commonPost(getRequestForPost(url, null, null), cls, callBack);
//    }


    /**
     * @param url      url地址
     * @param params   HashMap<String, Object> 参数
     * @param callBack 请求回调接口
     */
    public static void post(String url, HashMap<String, Object> params, HttpCallBack callBack) {
        commonPost(getRequestForPost(url, params, null), callBack.GetTtype(), callBack);
    }

//    /**
//     * @param url      url地址
//     * @param params   HashMap<String, Object> 参数
//     * @param callBack 请求回调接口
//     * @param tag      网络请求tag
//     */
//    public static void post(String url, HashMap<String, Object> params, HttpCallBack callBack, Object tag) {
//        commonPost(getRequestForPost(url, params, tag), CommonJson.class, callBack);
//    }

//    /**
//     * @param url      url地址
//     * @param params   HashMap<String, Object> 参数
//     * @param cls      泛型返回参数
//     * @param callBack 请求回调接口
//     */
//    public static <T extends CommonJson> void post(String url, HashMap<String, Object> params, Class<T> cls, final HttpCallBack<T> callBack) {
//        commonPost(getRequestForPost(url, params, null), cls, callBack);
//    }

    /**
     * GET请求 公共请求部分
     */
    private static <T extends CommonJson> void commonGet(Request request, final Class<T> cls, final HttpCallBack<T> callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        Log.e("http-request", request.url().toString());


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                try {
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure(e.getMessage());
                            }
                        });
                    }
                } catch (Exception e1) {
                    Log.e("httpRequest", "HttpUtil----commonGet()---onFailure()--->" + e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final T json = JsonUtil.fromJson(response.body().string(), cls);
                    if (callBack != null && mainHandler != null && json != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(json);
                            }
                        });
                    } else {
                        Log.e("httpRequest", "HttpUtil----commonGet()---onResponse()--->" + json.toString());
                    }
                } catch (Exception e) {
                    Log.e("httpRequest", "HttpUtil----commonGet()---onResponse()--->" + e.getMessage());
                }
            }
        });
    }

    /**
     * POST请求 公共请求部分
     */
    private static <T extends CommonJson> void commonPost(Request request, final Class<T> cls, final HttpCallBack<T> callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                try {
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure(e.getMessage());
                            }
                        });
                    }
                } catch (Exception e1) {
                    Log.e("httpRequest", "HttpUtil----commonPost()---onFailure()--->" + e1.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (callBack != null && mainHandler != null) {
                        final T json = JsonUtil.fromJson(response.body().string(), cls);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(json);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("httpRequest", "HttpUtil----commonPost()---onResponse()--->" + e.getMessage());
                }
            }

        });
    }

    private static Request getRequestForPost(String url, Map<String, Object> params, Object tag) {
        if (url == null || "".equals(url)) {
            Log.e("httpRequest", "HttpUtil----getRequestForPost()---->" + "url地址为空 无法执行网络请求!!!");
            return null;
        }
        if (params == null) {
            params = new HashMap<>();
        }
//        //判断用户是否登录 登录带上用户基础参数 如只需要userId token接口无需传参数
//        if (UserManager.getIsLogined()) {
//            params.put("userId", UserManager.getUserId());
//            params.put("token", UserManager.getToken());
//        }

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, JsonUtil.toJson(params));
        Request request;
        if (tag != null) {
            request = new Request.Builder().url(url).post(body).tag(tag).build();
        } else {
            request = new Request.Builder().url(url).post(body).build();
        }
        return request;
    }

    private static Request getRequestForGet(String url, HashMap<String, String> params, Object tag) {
        if (url == null || "".equals(url)) {
            Log.e("httpRequest", "HttpUtil----getRequestForGet()---->" + "url地址为空 无法执行网络请求!!!");
            return null;
        }
        Request request;
        if (tag != null) {
            request = new Request.Builder()
                    .url(paramsToString(url, params))
                    .tag(tag)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(paramsToString(url, params))
                    .build();
        }
        return request;
    }

    private static String getCommonParamsForGet(StringBuilder sb) {
//        if (UserManager.getIsLogined()) {
//            sb.append("&").append("userId").append("=").append(String.valueOf(UserManager.getUserId()));
//            sb.append("&").append("token").append("=").append(UserManager.getToken());
//        }
        return sb.toString();

    }

    /**
     * Post 添加公共参数
     */
    private static JSONObject getCommonParamsForPost() {
        jsonObject = new JSONObject();
//        try {
//            if (UserManager.getIsLogined()) {
//                jsonObject.put("userId", UserManager.getUserId());
//                jsonObject.put("token", UserManager.getToken());
//            }
//        } catch (Exception e) {
//            Log.e("caoliang", "HttpUtil----getCommonParamsForPost()---->" + e.getMessage());
//            return jsonObject;
//        }
        return jsonObject;
    }


    private static String paramsToString(String url, HashMap<String, String> params) {
        StringBuilder url_builder = new StringBuilder();
        url_builder.append(url);
        if (params != null && params.size() > 0) {
            if (!url.contains("?")){
                url_builder.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    url_builder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    url_builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return getCommonParamsForGet(url_builder);
    }

    /**
     * 根据tag标签取消网络请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getInstance().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getInstance().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    /**
     * 取消所有请求请求
     */
    public static void cancelAll() {
        for (Call call : getInstance().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getInstance().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

}


