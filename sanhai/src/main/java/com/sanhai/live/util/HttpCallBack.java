package com.sanhai.live.util;

import java.lang.reflect.ParameterizedType;

/**
 * @ClassName HttpCallBack
 * @Description TODO
 * @Author yangjie
 * @Date 2019/5/16 下午5:36
 */

public abstract class HttpCallBack<T> {
    public abstract void onFailure(String message);

    public abstract <T> void onSuccess(T json);

    public Class<T> GetTtype() {
        Class<T> rawType;
        rawType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return (Class<T>) rawType;
    }


}
