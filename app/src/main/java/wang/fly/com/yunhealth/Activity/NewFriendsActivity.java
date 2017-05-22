package wang.fly.com.yunhealth.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wang.fly.com.yunhealth.Adapter.AdapterNewFriends;
import wang.fly.com.yunhealth.R;
import wang.fly.com.yunhealth.util.IMUtils.QueryUtilField;

/**
 *
 * Created by 兆鹏 on 2017/5/22.
 */

public class NewFriendsActivity extends AppCompatActivity {
    private ImageView mBack;
    private ListView listView;
    private Toolbar mToolBar;
    private List<Map<String,Object>> data;
    private AdapterNewFriends adapterNewFriends;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        findViews();
        setEvents();
        mContext = this;
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    private void setEvents() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        listView = (ListView) findViewById(R.id.id_new_friends_list);
        mBack = (ImageView) findViewById(R.id.id_new_friends_back);
        mToolBar = (Toolbar) findViewById(R.id.id_new_friends_toolBar);
        data = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>(QueryUtilField.FRIEND_APPLY_TABLE);
        query.whereEqualTo("receiverName", AVUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",list.get(i).get("requestName"));
                    map.put("img",list.get(i).get("imgUri"));
                    map.put("status",list.get(i).get("status"));
                    map.put("DeviceId",list.get(i).get("installDevicedId"));
                    map.put("userId",list.get(i).get("objId"));
                    map.put("objId",list.get(i).getObjectId());
                    data.add(map);
                }
                adapterNewFriends = new AdapterNewFriends(mContext,data,R.layout.item_new_friends);
                listView.setAdapter(adapterNewFriends);
            }
        });

    }
}
