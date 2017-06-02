package com.gangzi.demo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gangzi on 2017/4/13.
 */

public abstract class BaseFragment extends Fragment{

    public Context mContext;
    //当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    private boolean isFragmentVisible=true;
    //是否是第一次开启网络加载
    public boolean isFirst=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isFragmentVisible&&isFirst){
            onFragmentVisibleChange(true);
        }
        return initView();
    }

    public abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isFragmentVisible=true;
        }
        if(initView()==null){
            return;
        }
        /**
         * 可见，并且没有加载过
         */
        if (isVisibleToUser&&isFirst){
            isFragmentVisible=true;
            isFirst=true;
            onFragmentVisibleChange(true);
            return;
        }
        /**
         *  由可见——>不可见 已经加载过
         */
        if (!isVisibleToUser&&!isFirst){
            isFragmentVisible=false;
            isFirst=false;
            onFragmentVisibleChange(false);
            return;
        }
    }
    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作.
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }
}
