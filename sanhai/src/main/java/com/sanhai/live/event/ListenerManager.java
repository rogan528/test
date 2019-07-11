package com.sanhai.live.event;

import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private static ListenerManager instance;
    private Map<String, Object> ListenterMap = new HashMap();

    private ListenerManager() {
    }

    public static ListenerManager getInstance() {
        if (instance == null) {
            Class var0 = ListenerManager.class;
            synchronized(ListenerManager.class) {
                if (instance == null) {
                    instance = new ListenerManager();
                }
            }
        }

        return instance;
    }

    public Object getListener(String var1) {
        return this.ListenterMap.get(var1);
    }

    public void setListener(String var1, Object var2) {
        this.ListenterMap.put(var1, var2);
    }

    public boolean isContainsListener(String var1) {
        return this.ListenterMap.containsKey(var1);
    }

    public void clearAllListener() {
        if (this.ListenterMap != null) {
            this.ListenterMap.clear();
        }

    }
}
