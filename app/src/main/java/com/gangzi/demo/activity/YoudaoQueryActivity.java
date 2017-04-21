package com.gangzi.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangzi.demo.R;
import com.gangzi.demo.model.YoudaoEntity;
import com.gangzi.demo.utils.Contants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class YoudaoQueryActivity extends AppCompatActivity {

    @BindView(R.id.bt_query)
    Button bt_query;
    @BindView(R.id.et_input)
    EditText mEditText;
    @BindView(R.id.iv_voice)
    ImageView iv_voice;
    @BindView(R.id.tv_show)
    TextView tv_show;
    private String word;
    private String url;
    private static final String TAG="YoudaoQueryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youdao_query);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.bt_query)
    public void query(){
        word=mEditText.getText().toString();
        StringBuffer sb=new StringBuffer();
        if (word!=null||"".equals(word)){
            url=sb.append(Contants.urlYouDao).append(word).toString();
        }
       // String url= Contants.urlYouDao+word;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                process(response);
            }
        });
    }
    @OnClick(R.id.iv_voice)
    public void VoiceQuery(){

    }
    private void process(String response) {
        Gson gson=new Gson();
        //转成单一实体对象
        YoudaoEntity youDao=gson.fromJson(response, YoudaoEntity.class);
        List<String>translation=youDao.getTranslation();
        for (int i=0;i<translation.size();i++){
            String translate=translation.get(i);
            tv_show.setText("translation:"+translate+",");
        }
        //转换成列表类型
      //  List<YoudaoEntity>youdaos=gson.fromJson(response, new TypeToken<List<YoudaoEntity>>(){}.getType());
    }

}
