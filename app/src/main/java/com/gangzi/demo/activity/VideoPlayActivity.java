package com.gangzi.demo.activity;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gangzi.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.video)
    VideoView mVideoView;
    private MediaController mediaController;//使用mediaController控制视频播放
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/monkey.mp4";//视频路径
        mediaController=new MediaController(this);
        mVideoView.setMediaController(mediaController);//videoView与MediaController建立连接
        mediaController.setMediaPlayer(mVideoView); //设置mdiaConroller与VideoView建立连接
        playFromLocal();
        playFromNet();
    }

    /**
     * 网络播放视频
     */
    private void playFromNet() {
        mVideoView.setVideoURI(Uri.parse(path));
    }

    /**
     * 本地播放视频
     */
    private void playFromLocal() {
        mVideoView.setVideoPath(path);
    }
}
