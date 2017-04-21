package com.gangzi.demo.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.gangzi.demo.R;
import com.gangzi.demo.adapter.TypeLeftAdapter;
import com.gangzi.demo.base.BaseFragment;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ListLeftFragment extends BaseFragment{

    private RecyclerView mRecyclerView;
    private ListView mListView;
    private TypeLeftAdapter mAdapter;

    @Override
    public View initView() {
        View view=View.inflate(mContext,R.layout.left_list_fragme,null);
        mListView= (ListView) view.findViewById(R.id.lv_left);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_right);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
