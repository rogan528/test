package com.zhangbin.paint;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.example.lijian.sf_im_sdk.IM_SDK;
import com.example.lijian.sf_im_sdk.OnGetInterface;
import com.sanhai.live.consts.Constatans;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private IM_SDK mIm_sdk;
    private static MyApplication myApplication = null;
    private static Context applicationContext;
    private List<Activity> activities = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        //在这里初始化
        BugtagsOptions options = new BugtagsOptions.Builder().trackingCrashLog(true).build();
        Bugtags.start("2a63961cc73893b2b2709103505438d0", this, Bugtags.BTGInvocationEventNone,options);
        mIm_sdk = new IM_SDK();
        myApplication = this;
        applicationContext = getApplicationContext();
    }
    public static MyApplication getApplication(){
        return myApplication;
    }
    public void setCallback(OnGetInterface callback){
        if (mIm_sdk != null)
        mIm_sdk.setCalReCallBackListenner(callback);
    }
    /**
     * 获取IM对象
     * @return
     */
    public IM_SDK getmIm_sdk(){
        if (mIm_sdk == null){
            mIm_sdk = new IM_SDK();
        }
        return mIm_sdk;
    }
    /**
     * 存放 Activity 到 List 中
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 遍历存放在 List 中的 Activity 并退出
     */
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            activity.finish();
            activities.remove(activity);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
