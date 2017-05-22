package wang.fly.com.yunhealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import wang.fly.com.yunhealth.R;
import wang.fly.com.yunhealth.util.IMUtils.QueryUtilField;

import static wang.fly.com.yunhealth.util.IMUtils.LogUtil.LogD;
import static wang.fly.com.yunhealth.util.IMUtils.LogUtil.toastShort;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.DEVICE_INSTALL_ID;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.FRIEND_APPLY_TABLE;


/**
 * 添加好友界面的适配器
 * Created by 兆鹏 on 2017/5/22.
 */

public class AdapterNewFriends extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Map<String,Object>> list;
    private int layout;
    public AdapterNewFriends(Context context, List<Map<String,Object>> list, int layout){
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(layout,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imUserHead = (ImageView) convertView.findViewById(R.id.id_item_new_friends_pic);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.id_item_new_friends_name);
            viewHolder.btAgree = (Button) convertView.findViewById(R.id.id_item_new_friends_agree);
            viewHolder.btDisagree = (Button) convertView.findViewById(R.id.id_item_new_friends_reject);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String name = (String) list.get(position).get("name");
        final String uri = (String) list.get(position).get("img");
        int status = (int) list.get(position).get("status");
        final String installId = (String) list.get(position).get("DeviceId");
        final String userId = (String) list.get(position).get("userId");
        final String objId = (String) list.get(position).get("objId");
        viewHolder.tvUserName.setText(name);
        if(uri != null){
            Glide.with(mContext).load(uri).placeholder(R.mipmap.ic_launcher).into(viewHolder.imUserHead);
        }
        //如果为1，则表示已同意
        if(status == 1){
            viewHolder.btAgree.setVisibility(View.GONE);
            viewHolder.btDisagree.setEnabled(false);
            viewHolder.btDisagree.setText("已同意");
        }else if(status == 2){//如果为2，则表示已拒绝
            viewHolder.btAgree.setVisibility(View.GONE);
            viewHolder.btDisagree.setEnabled(false);
            viewHolder.btDisagree.setText("已回绝");
        }else {
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.btAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVUser.getCurrentUser().followInBackground(userId, new FollowCallback() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                LogD("follow succeed!");
                                AVObject object = AVObject.createWithoutData(QueryUtilField.FRIEND_APPLY_TABLE,objId);
                                object.put("status",1);
                                object.saveInBackground();
                                finalViewHolder.btAgree.setVisibility(View.GONE);
                                finalViewHolder.btDisagree.setEnabled(false);
                                finalViewHolder.btDisagree.setText("已同意");
                            } else if (e.getCode() == AVException.DUPLICATE_VALUE) {
                                LogD("Already followed.");
                            }else {
                                LogD("follow:"+e.getMessage());
                            }
                        }
                    });
                }
            });

            viewHolder.btDisagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVObject object = AVObject.createWithoutData(QueryUtilField.FRIEND_APPLY_TABLE,objId);
                    object.put("status",2);
                    object.saveInBackground();
                    finalViewHolder.btAgree.setVisibility(View.GONE);
                    finalViewHolder.btDisagree.setEnabled(false);
                    finalViewHolder.btDisagree.setText("已回绝");
                }
            });
        }
        return convertView;
    }

    static class ViewHolder{
        TextView tvUserName;
        ImageView imUserHead;
        Button btAgree;
        Button btDisagree;
    }
}
