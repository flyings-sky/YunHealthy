package wang.fly.com.yunhealth.util;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.smssdk.SMSSDK;

/**
 *
 * Created by noclay on 2017/4/15.
 */

public class MyApplication extends Application {

    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化一些资源
        sContext = getApplicationContext();
        initDir();
        initDependencies();
        initIM();
    }

    private void initIM() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"l5CVYhd0SuDev52kXp2JK9pf-gzGzoHsz","Kenvb31EVgcqrNX1ELxL87J7");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }

    private void initDir() {
        File file = new File(Environment.getExternalStorageDirectory() + "/CloudHealthy/userImage");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 初始化依赖第三方：
     * Bmob
     * Mob
     */
    public void initDependencies() {
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("d2e2a48caabc1e5c399b20b2adea85eb")
                .setConnectTimeout(15)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        //初始化Mob
        SMSSDK.initSDK(this, "195be1e7755e2", "5bdd8a14d2e2f5734797443c982b0db4");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
