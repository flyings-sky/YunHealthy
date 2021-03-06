package wang.fly.com.yunhealth.MVP.Presenters;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wang.fly.com.yunhealth.Activity.LoginActivityCopy;
import wang.fly.com.yunhealth.DataBasePackage.SignUserData;
import wang.fly.com.yunhealth.MVP.Bases.BasePresenter;
import wang.fly.com.yunhealth.MVP.Views.LoginActivityInterface;
import wang.fly.com.yunhealth.util.IMUtils.CacheUtil;
import wang.fly.com.yunhealth.util.IMUtils.LogUtil;
import wang.fly.com.yunhealth.util.MyApplication;
import wang.fly.com.yunhealth.util.SharedPreferenceHelper;
import wang.fly.com.yunhealth.util.UtilClass;

/**
 *
 * Created by noclay on 2017/4/15.
 */

public class LoginActivityPresenter extends BasePresenter<LoginActivityInterface> {
    private SignUserData user;
    private Context context;

    public LoginActivityPresenter(Context context) {
        this.context = context;
    }

    public void loadImage(final String userName){
        if (!UtilClass.checkNetwork(MyApplication.getContext())){
            getView().badNet();
        }
        BmobQuery<SignUserData> userQuery = new BmobQuery<>();
        userQuery.addWhereEqualTo("phoneNumber", userName);
        userQuery.setLimit(10);
        userQuery.findObjects(new FindListener<SignUserData>() {
            @Override
            public void done(List<SignUserData> list, BmobException e) {
                if (e == null){
                    if (list == null){
                        getView().loadImage(null);
                    }else{
                        user = list.get(0);
                        getView().loadImage(user.getUserImage());
                    }
                }else{
                    e.printStackTrace();
                    getView().loadImage(null);
                }
            }
        });
    }

    public void login(String name, String password){
        if (name.length() >= 16 || name.length() < 6){
            getView().loginFailed("手机号格式非法");
        }else if (password.length() >= 16 || password.length() <6){
            getView().loginFailed("密码过短或过长");
        }
        if (user == null){
            getView().loginFailed("该手机号未注册");
        }else if (user.getPhoneNumber().compareTo(name) != 0){
            getView().loginFailed("手机号错误");
        }else if (user.getPhoneNumber().compareTo(name) == 0){
            if (user.getPassWord().compareTo(password) != 0){
                getView().loginFailed("密码错误");
            }else{
                //如果Cache里面不存在InstallId，就保存InstallId到缓存
                if(CacheUtil.getInstallId().equals("")){
                    AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                                CacheUtil.putInstallId(installationId);
                            }else {
                                LogUtil.LogD("login:install"+ e.getMessage());
                            }
                        }
                    });
                }
                //LeanCloud用户登录
                AVUser.logInInBackground(user.getUserName(), user.getPhoneNumber(),
                        new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(e == null){
                            getView().loginFailed("登录成功");
                            LogUtil.LogD("登陆LeanCloud成功");
                            SharedPreferenceHelper.editLoginState(user, true);
                            getView().loginSuccess();
                        }else {
                            getView().loginFailed("登录失败");
                            LogUtil.LogD("登陆LeanCloud失败"+e.getMessage());
                        }
                    }
                });

            }
        }
    }

    public void loginIfRemember(){
        if (SharedPreferenceHelper.isLogin()){
            //如果记住登录状态，直接进行LeanCloud登录
            AVUser.logInInBackground(CacheUtil.getUserName(),CacheUtil.getTelePhone(),
                    new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if(e == null){
                                getView().loginSuccess();
                                LogUtil.LogD("登陆LeanCloud成功");
                            }else {
                                getView().loginFailed("登录失败");
                                LogUtil.LogD("登陆LeanCloud失败"+e.getMessage());
                            }
                        }
                    });
        }
    }
//
//    /**
//     *用于修改本地的是用户登陆信息
//     * @param user
//     * @param isRemember
//     */
//    public static void editLoginState(SignUserData user, Boolean isRemember) {
//        SharedPreferences.Editor editor =
//                MyApplication.getContext().getSharedPreferences("LoginState", MODE_PRIVATE).edit();
//        Log.d("test", "editLoginState: edit = " + editor);
//        if (user.getObjectId() != null){
//            editor.putString("userId", user.getObjectId());
//        }
//        if (user.getUserName() != null){
//            editor.putString("userName", user.getUserName());
//        }
//        if (user.getPhoneNumber() != null){
//            editor.putString("phoneNumber", user.getPhoneNumber());
//        }
//        if (user.getMan() != null){
//            editor.putBoolean("isMan", user.getMan());
//        }
//        if (user.getHeight() != null){
//            editor.putInt("height", user.getHeight());
//        }
//        if (user.getWeight() != null){
//            editor.putFloat("weight", user.getWeight());
//        }
//        if (user.getIdNumber() != null){
//            editor.putString("idNumber", user.getIdNumber());
//        }
//        if (user.getBirthday() != null){
//            editor.putString("birthday", user.getBirthday().getDate());
//        }
//        if (user.getUserImage() != null){
//            editor.putString("userImage", user.getUserImage().getUrl());
//        }
//        if (isRemember != null){
//            editor.putBoolean("loginRememberState", isRemember);
//        }
//        editor.commit();
//    }
}
