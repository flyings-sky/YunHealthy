package wang.fly.com.yunhealth.util.IMUtils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import wang.fly.com.yunhealth.R;

/**
 * 自定义圆形进度条
 * Created by 兆鹏 on 2017/5/22.
 */

public class LoadingImgDialog {
    private Context mContext;
    private PopupWindow popupDialog;
    private LayoutInflater layoutInflater;
    private RelativeLayout layout;
    private RelativeLayout layoutBg;
    private int residBg;//背景图片的ID
    private View loadingDialog;
    //背景旋转动画
    private RotateAnimation rotateAnimation;
    //透明度动画效果
    private AlphaAnimation alphaAnimationIn;
    private AlphaAnimation alphaAnimationOut;

    public LoadingImgDialog(Context context,int residBg){
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.residBg = residBg;
    }

    private void initAnim(){
        rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        //使动画无限循环
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        //设置匀速变化的插值器
        rotateAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimationIn = new AlphaAnimation(0f,1f);
        //设置动画结束后，停留在最后的位置
        alphaAnimationIn.setFillAfter(true);
        alphaAnimationIn.setDuration(200);
        alphaAnimationIn.setInterpolator(new LinearInterpolator());
        alphaAnimationOut = new AlphaAnimation(1f,0f);
        alphaAnimationIn.setFillAfter(true);
        alphaAnimationIn.setDuration(100);
        alphaAnimationIn.setInterpolator(new LinearInterpolator());
        // 监听动作，动画结束时，隐藏LoadingColorDialog
        alphaAnimationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                dismiss();
            }
        });
    }

    public void dismiss(){
        if(popupDialog != null&&popupDialog.isShowing()){
            layoutBg.clearAnimation();
            loadingDialog.clearAnimation();
            popupDialog.dismiss();
        }
    }
    public boolean isShowing(){
        if(popupDialog != null&&popupDialog.isShowing()){
            return true;
        }
        return false;
    }

    public void show(){
        dismiss();
        initAnim();
        layout = (RelativeLayout) layoutInflater.inflate(R.layout.view_loadingdialog,null);
        loadingDialog = layout.findViewById(R.id.loading_dialog);
        loadingDialog.setBackgroundResource(residBg);
        layoutBg = (RelativeLayout) layout.findViewById(R.id.bgLayout);
        popupDialog = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        View parentView = ((Activity)mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        popupDialog.showAtLocation(parentView, Gravity.CENTER,0,0);
        layoutBg.startAnimation(alphaAnimationIn);
        loadingDialog.startAnimation(rotateAnimation);
    }
}
