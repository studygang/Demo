package com.gangzi.demo.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;

import com.gangzi.demo.R;
import com.gangzi.demo.activity.YoudaoQueryActivity;
import com.gangzi.demo.base.BaseFragment;

/**
 * Created by Administrator on 2017/4/13.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private FloatingActionButton mFloatingActionButton;

    @Override
    public View initView() {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.home_fragme,null);
        mFloatingActionButton= (FloatingActionButton) view.findViewById(R.id.bt_float);
        mFloatingActionButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_float:
                startActivity(new Intent(getActivity(), YoudaoQueryActivity.class));
                break;
        }
    }
}
