package wang.fly.com.yunhealth.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wang.fly.com.yunhealth.Adapter.AdapterFindNewFriends;
import wang.fly.com.yunhealth.R;
import wang.fly.com.yunhealth.util.IMUtils.Interfaces.IResult;
import wang.fly.com.yunhealth.util.IMUtils.LoadingImgDialog;
import wang.fly.com.yunhealth.util.IMUtils.LogUtil;
import wang.fly.com.yunhealth.util.IMUtils.Util;

import static wang.fly.com.yunhealth.util.IMUtils.QueryUtilField.DEVICE_INSTALL_ID;

/**
 *
 * Created by 兆鹏 on 2017/5/22.
 */

public class FindFriendsActivity extends AppCompatActivity implements IResult<AVUser>{
    private Toolbar mToolBar;
    private EditText mFindContent;
    private Button mSearch;
    private Context mContext;
    private ListView listView;
    private ImageView mBack;
    private LoadingImgDialog loadingImgDialog;
    private AdapterFindNewFriends adapterFindNewFriends;
    private List<Map<String,Object>> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_friends);
        mContext = this;
        findViews();
        setEvents();
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    private void setEvents() {
        mFindContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                if(mFindContent.getText().toString().trim().equals("")){
                    mSearch.setEnabled(false);
                }else {
                    mSearch.setEnabled(true);
                }
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.getUserInfo(mFindContent.getText().toString(),FindFriendsActivity.this);
                loadingImgDialog = new LoadingImgDialog(mContext,R.drawable.loadings);
                loadingImgDialog.show();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        mToolBar = (Toolbar) findViewById(R.id.id_find_friends_toolBar);
        mFindContent = (EditText) findViewById(R.id.id_find_friends_find_content);
        mSearch = (Button) findViewById(R.id.id_find_friends_search);
        listView = (ListView) findViewById(R.id.id_find_friends_list);
        mBack = (ImageView) findViewById(R.id.id_find_friends_back);
    }

    @Override
    public void onSuccess(AVUser result) {
        Map<String,Object> map = new HashMap<>();
        map.put("userPic",result.get("imageUri"));
        map.put("userName",result.getUsername());
        map.put("btAdd","添加好友");
        map.put("installId",result.get(DEVICE_INSTALL_ID));
        if(!data.contains(map)){
            data.add(map);
        }
        adapterFindNewFriends = new AdapterFindNewFriends(mContext,data,R.layout.item_find_new_friends);
        listView.setAdapter(adapterFindNewFriends);
        loadingImgDialog.dismiss();
        LogUtil.toastShort(mContext,"查找成功");
    }

    @Override
    public void onFailure() {
        loadingImgDialog.dismiss();
        LogUtil.toastShort(mContext,"查找失败");
    }
}
