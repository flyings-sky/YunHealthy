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
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import wang.fly.com.yunhealth.R;
import wang.fly.com.yunhealth.util.IMUtils.LogUtil;

import static wang.fly.com.yunhealth.util.IMUtils.LogUtil.LogD;
import static wang.fly.com.yunhealth.util.IMUtils.LogUtil.toastShort;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.DEVICE_INSTALL_ID;
import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.FRIEND_APPLY_TABLE;



/**
 * 添加好友界面的适配器
 * Created by 兆鹏 on 2017/5/22.
 */

public class AdapterFindNewFriends extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Map<String,Object>> list;
    private int layout;
    public AdapterFindNewFriends(Context context, List<Map<String,Object>> list, int layout){
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
            viewHolder.btAddFriend = (Button) convertView.findViewById(R.id.id_item_new_friends_add);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String name = (String) list.get(position).get("userName");
        final String uri = (String) list.get(position).get("userPic");
        final String installId = (String) list.get(position).get("installId");
        final String myInstallId = (String) AVUser.getCurrentUser().get(DEVICE_INSTALL_ID);
        viewHolder.tvUserName.setText(name);
        viewHolder.btAddFriend.setText((CharSequence) list.get(position).get("btAdd"));

        if(uri != null){
            Glide.with(mContext).load(uri).placeholder(R.mipmap.ic_launcher).into(viewHolder.imUserHead);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isEnabled()){
                    AVObject friendApply = new AVObject(FRIEND_APPLY_TABLE);
                    friendApply.put("requestName", AVUser.getCurrentUser().getUsername());
                    friendApply.put("receiverName",name);
                    friendApply.put("imgUri",uri);
                    friendApply.put("status",0);
                    friendApply.put(DEVICE_INSTALL_ID, myInstallId);
                    friendApply.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                AVQuery query = AVInstallation.getQuery();
                                query.whereEqualTo("installationId",installId);
                                AVPush push = new AVPush();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("name",name);
                                    object.put("imageUri",uri);
                                    object.put("installId",myInstallId);
                                    push.setData(object);
                                    push.setPushToAndroid(true);
                                    push.setQuery(query);
                                    push.sendInBackground(new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if(e == null){
                                                finalViewHolder.btAddFriend.setEnabled(false);
                                                finalViewHolder.btAddFriend.setText("已申请");
                                                toastShort(mContext,"申请发送成功");
                                            }else {
                                                LogD("Push:推送失败"+e.getMessage());
                                            }
                                        }
                                    });
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }else {
                                toastShort(mContext,"发送失败，请重试");
                                LogD("saveFriendApply:"+e.getMessage());
                            }
                        }
                    });

                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView tvUserName;
        ImageView imUserHead;
        Button btAddFriend;
    }
}
