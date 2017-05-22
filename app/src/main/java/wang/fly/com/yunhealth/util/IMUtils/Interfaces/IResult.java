package wang.fly.com.yunhealth.util.IMUtils.Interfaces;

/**
 * Created by 兆鹏 on 2017/5/22.
 */

public interface IResult<T> {
    void onSuccess(T result);
    void onFailure();
}
