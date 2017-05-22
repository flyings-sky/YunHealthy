package wang.fly.com.yunhealth.util.IMUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;


import wang.fly.com.yunhealth.util.IMUtils.Interfaces.IResult;

import static wang.fly.com.yunhealth.util.IMUtils.LogUtil.*;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.*;

/**
 *IM相关操作的工具类
 * Created by 兆鹏 on 2017/5/12.
 */

public class Util {
    private static boolean isExist;
    //检查用户名是否存在
    public static boolean isExistUser(String username){
        final AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo(USER_NAME,username);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<AVUser> list = query.find();
                    if(list.size() == 0){
                        isExist = false;
                    }else {
                        isExist = true;
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                    isExist = false;
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogD("threadJoin"+e.getMessage());
            return false;
        }
        return isExist;
    }
    //获取指定用户的信息
    public static void getUserInfo(String username, final IResult<AVUser> result){
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo(USER_NAME , username);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e == null&&list.size() != 0){
                    result.onSuccess(list.get(0));
                }else {
                    result.onFailure();
                }
            }
        });
    }
}
