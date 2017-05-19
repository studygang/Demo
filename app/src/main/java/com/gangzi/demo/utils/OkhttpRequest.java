package com.gangzi.demo.utils;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/18.
 */

public class OkhttpRequest {
    private static final OkHttpClient client=new OkHttpClient();
    private static final MediaType DEFAULT=MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private OkhttpRequest(){
        throw new AssertionError();
    }
    private static String getParamUrl(Map<String,String>params){
        if (params==null||params.isEmpty()){
            return"";
        }
        StringBuilder sb=new StringBuilder();
        for (String key:params.keySet()){
            sb.append(key).append('=').append(params.get(key)).append('&');
        }
        return sb.substring(0,sb.length()-1);
    }
    public static String get(String url){
        return get(url,null);
    }
    public static String get(String url,Map<String,String>params){
        String paramUrl=getParamUrl(params);
        String newUrl= TextUtils.isEmpty(paramUrl)?url:url+"?"+paramUrl;
        Request.Builder builder=new Request.Builder().url(newUrl);
        Request request=builder.build();
        Response response;
        try {
            response=client.newCall(request).execute();
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            return response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String sendPost(String url,Map<String,String>params){
        FormBody.Builder formBody=new FormBody.Builder();
        if (params!=null){
            for (String key:params.keySet()){
                String value=params.get(key);
                if (value==null){
                    value="";
                }
                formBody.add(key,value);
            }
        }
       RequestBody body= formBody.build();
        Request.Builder requestBuilder=new Request.Builder().url(url).post(body);
        Request request=requestBuilder.build();
        Response response;
        try {
            response=client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseData = response.body().string();
            return  responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * json数据请求
     *
     * @param url
     * @param params 请求参数
     * @return
     */
    public static String sendPost(final String url, final String params) {
        RequestBody body = RequestBody.create(DEFAULT, params);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        Request request = requestBuilder.build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("-------response.body().string()---"+response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
