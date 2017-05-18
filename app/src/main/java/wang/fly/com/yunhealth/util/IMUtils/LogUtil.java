package wang.fly.com.yunhealth.util.IMUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 用于简化Log的工具
 * Created by 兆鹏 on 2017/5/12.
 */

public class LogUtil {
    public static final String TAG = "IM";
    public static void LogV(String s){
        Log.v(TAG,s);
    }

    public static void LogD(String s){
        Log.d(TAG,s);
    }

    public static void LogI(String s){
        Log.i(TAG,s);
    }

    public static void LogW(String s){
        Log.w(TAG,s);
    }

    public static void LogE(String s){
        Log.e(TAG,s);
    }

    public static void toastShort(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }
}
