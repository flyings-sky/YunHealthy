package wang.fly.com.yunhealth.util.IMUtils;

import android.content.SharedPreferences;

import wang.fly.com.yunhealth.util.MyApplication;

import static android.content.Context.MODE_PRIVATE;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.DEVICE_INSTALL_ID;

/**
 * 本地缓存
 * Created by 兆鹏 on 2017/5/18.
 */

public class CacheUtil {
    //SharedPreference文件的名称
    private static final String SHARE_PFERENCE_NAME = "LoginState";
    private static final String USER_NAME = "userName";
    private static final String TELE_PHONE = "phoneNumber";
    private static SharedPreferences preferences =
            MyApplication.getContext().getSharedPreferences(SHARE_PFERENCE_NAME, MODE_PRIVATE);
    private static SharedPreferences.Editor editor = preferences.edit();

    public static void putInstallId(String id){
        if(!preferences.getString(DEVICE_INSTALL_ID,"").equals("")){
            editor.putString(DEVICE_INSTALL_ID,id);
            editor.commit();
        }
    }

    public static String getInstallId(){
        return preferences.getString(DEVICE_INSTALL_ID,"");
    }

    public static String getUserName(){
        return preferences.getString(USER_NAME,"");
    }

    public static String getTelePhone(){
        return preferences.getString(TELE_PHONE,"");
    }

}
