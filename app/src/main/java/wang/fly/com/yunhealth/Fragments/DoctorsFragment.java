package wang.fly.com.yunhealth.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import wang.fly.com.yunhealth.Activity.FindFriendsActivity;
import wang.fly.com.yunhealth.R;
import wang.fly.com.yunhealth.util.TabLayoutViewPagerAdapter;

/**
 *  IM功能的主要界面
 * Created by 兆鹏 on 2016/11/2.
 */
public class DoctorsFragment extends Fragment implements
        View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private ImageView mImagePlus;//添加功能
    private View view;
    private RelativeLayout mSearchBar;
    private RelativeLayout mNewFriends;//新的好友界面
    private TabLayout mTabLayout;
    private ViewPager mPageLayout;
    private TabLayout mChanelTab;
    private List<String> mTitles;
    private List<Fragment> mPages;
    public static final String [] CHANELS = {
            "最近聊天",
            "好友列表",
            "智能医生"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctors, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        mSearchBar = (RelativeLayout) v.findViewById(R.id.search_bar);
        mSearchBar.setOnClickListener(this);
        mNewFriends = (RelativeLayout) v.findViewById(R.id.newFriends);
        mNewFriends.setOnClickListener(this);
        mImagePlus = (ImageView) v.findViewById(R.id.plus);
        mImagePlus.setOnClickListener(this);
        mTabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        mPageLayout = (ViewPager) v.findViewById(R.id.pageLayout);
        mChanelTab = (TabLayout) v.findViewById(R.id.chanelTab);
        mTitles = new ArrayList<>();
        mPages = new ArrayList<>();
        for (int i = 0; i < CHANELS.length; i++) {
            mChanelTab.addTab(mChanelTab.newTab().setText(CHANELS[i]));
            mTitles.add(CHANELS[i]);
        }
        mPages.add(new DoctorsTalkFragment());
        mPages.add(new DoctorsFriendsFragment());
        mPages.add(new DoctorsIntelligentFragment());
        TabLayoutViewPagerAdapter adapter = new TabLayoutViewPagerAdapter(
                getChildFragmentManager(),
                mTitles, mPages);
        mPageLayout.setAdapter(adapter);
        mPageLayout.addOnPageChangeListener(this);
        mChanelTab.setupWithViewPager(mPageLayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_bar:
                break;
            case R.id.newFriends:
                break;
            case R.id.plus:
                Intent intent = new Intent(getActivity(), FindFriendsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
