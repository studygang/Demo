package com.gangzi.demo.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;

import com.gangzi.demo.R;
import com.gangzi.demo.activity.LBSLocationActivity;
import com.gangzi.demo.activity.PayActivity;
import com.gangzi.demo.activity.YoudaoQueryActivity;
import com.gangzi.demo.base.BaseFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

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
    public void initData() {
        super.initData();
        Map<String, Object> map = new Hashtable<String, Object>();
        map.put("serviceId","100");
        map.put("phone","15193321182");
        String url="http://182.92.130.208/sc/v2/community/video/native/getOpenCmd.json";
        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(map))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        System.out.println("--------"+response);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_float:
                startActivity(new Intent(getActivity(), PayActivity.class));
                break;
        }
    }
}
