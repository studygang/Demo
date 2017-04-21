package com.gangzi.demo.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.FrameLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gangzi.demo.R;
import com.gangzi.demo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class ShopFragment extends BaseFragment{

    private SegmentTabLayout mSegmentTabLayout;
    private FrameLayout fl_type;
    private List<BaseFragment>mFragmentList;
    private Fragment tempFragmet;
    private ListLeftFragment mlistFragmet;
    private TypeFragmet mTypeFragmet;


    @Override
    public View initView() {
        View view=View.inflate(mContext,R.layout.type_fragme,null);
        mSegmentTabLayout= (SegmentTabLayout) view.findViewById(R.id.stlayout);
        fl_type= (FrameLayout) view.findViewById(R.id.fl_type);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        initFragment();
        String[] titles={"分类","标签"};
        mSegmentTabLayout.setTabData(titles);
        mSegmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(tempFragmet,mFragmentList.get(position));
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        switchFragment(tempFragmet,mFragmentList.get(0));
    }

    private void initFragment() {
        mFragmentList=new ArrayList<>();
        mlistFragmet=new ListLeftFragment();
        mTypeFragmet=new TypeFragmet();
        mFragmentList.add(mlistFragmet);
        mFragmentList.add(mTypeFragmet);
        switchFragment(tempFragmet,mFragmentList.get(0));
    }

    private void switchFragment(Fragment fromFragmet, BaseFragment nextFragment) {
        if (tempFragmet!=nextFragment){
            tempFragmet=nextFragment;
            if (nextFragment!=null){
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                if (!nextFragment.isAdded()){
                    if (fromFragmet!=null){
                        ft.hide(fromFragmet);
                    }
                    ft.add(R.id.fl_type,nextFragment,"tagFragment").commit();
                }else{
                    if (fromFragmet!=null){
                        ft.hide(fromFragmet);
                    }
                    ft.show(nextFragment).commit();
                }
            }
        }
    }
}
