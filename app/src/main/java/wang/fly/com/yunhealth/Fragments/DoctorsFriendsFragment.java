package wang.fly.com.yunhealth.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import wang.fly.com.yunhealth.Adapter.AdapterSection;
import wang.fly.com.yunhealth.R;

/**
 * Created by noclay on 2017/5/10.
 */

public class DoctorsFriendsFragment extends Fragment {
    private View view;
    private ExpandableListView mSectionRecyclerView;
    private Context mContext;
    private AdapterSection mAdapterSection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctos_friends, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mContext = getContext();
        mSectionRecyclerView = (ExpandableListView) view.findViewById(R.id.sectionListView);
        mAdapterSection = new AdapterSection(mContext);
        mSectionRecyclerView.setAdapter(mAdapterSection);
        mSectionRecyclerView.setGroupIndicator(null);
    }
}
