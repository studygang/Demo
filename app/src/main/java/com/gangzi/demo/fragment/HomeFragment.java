package com.gangzi.demo.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gangzi.demo.R;
import com.gangzi.demo.activity.LBSLocationActivity;
import com.gangzi.demo.activity.PayActivity;
import com.gangzi.demo.activity.YoudaoQueryActivity;
import com.gangzi.demo.base.BaseFragment;
import com.gangzi.demo.utils.OkhttpRequest;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/13.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private FloatingActionButton mFloatingActionButton;
    private Button bt_hello;

    @Override
    public View initView() {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.home_fragme,null);
        mFloatingActionButton= (FloatingActionButton) view.findViewById(R.id.bt_float);
        mFloatingActionButton.setOnClickListener(this);
        bt_hello= (Button) view.findViewById(R.id.bt_hello);
        bt_hello.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        final Map<String, Object> map = new Hashtable<String, Object>();
        map.put("serviceId","100");
        map.put("phone","15193321182");
        final String url="http://182.92.130.208/sc/v2/community/video/native/getOpenCmd.json";
        System.out.print("jsonmap----"+new Gson().toJson(map));
      /*  OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(map))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //System.out.println("--------"+response);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        System.out.println("--------"+response);
                    }
                });*/
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client=new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(map));
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        Request request = requestBuilder.build();
        Response response;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("------------response.body().string()--"+response.body().string());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_float:
                startActivity(new Intent(getActivity(), PayActivity.class));
                break;
            case R.id.bt_hello:
                Toast.makeText(getActivity(),"你好",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
