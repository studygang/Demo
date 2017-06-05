package com.gangzi.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gangzi.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JcaoVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.jc_video)
    JCVideoPlayerStandard mJCVideoPlayerStandard;
    @BindView(R.id.tiny_window)
    Button tiny_window;
    @BindView(R.id.auto_tiny_window)
    Button auto_tiny_window;
    @BindView(R.id.play_directly_without_layout)
    Button play_directly_without_layout;
    @BindView(R.id.about_listview)
    Button about_listview;
    @BindView(R.id.about_api)
    Button about_api;
    @BindView(R.id.about_webview)
    Button about_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jcao_video_player);
        tiny_window.setOnClickListener(this);
        auto_tiny_window.setOnClickListener(this);
        play_directly_without_layout.setOnClickListener(this);
        about_listview.setOnClickListener(this);
        about_api.setOnClickListener(this);
        about_webview.setOnClickListener(this);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tiny_window:
                break;
            case R.id.auto_tiny_window:
                break;
            case R.id.play_directly_without_layout:
                break;
            case R.id.about_listview:
                break;
            case R.id.about_webview:
                break;
            case R.id.about_api:
                break;
        }
    }
}
