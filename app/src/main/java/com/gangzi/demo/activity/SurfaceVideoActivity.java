package com.gangzi.demo.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.gangzi.demo.R;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

public class SurfaceVideoActivity extends Activity implements View.OnClickListener{

    private Button bt_start,bt_pause,bt_stop;
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_video);
        initView();
        mMediaPlayer=new MediaPlayer();
    }

    private void initView() {
        bt_pause= (Button) findViewById(R.id.bt_surface_pause);
        bt_pause.setOnClickListener(this);
        bt_start= (Button) findViewById(R.id.bt_surface_play);
        bt_start.setOnClickListener(this);
        bt_stop= (Button) findViewById(R.id.bt_surface_stop);
        bt_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_surface_play:
                play();
                break;
            case R.id.bt_surface_pause:
                pause();
                break;
            case R.id.bt_surface_stop:
                stop();
                break;
        }
    }
    private void play() {
        mMediaPlayer.reset();//Idle状态
        Uri uri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"b.mp4"));
        try {
            mMediaPlayer.setDataSource(this,uri);
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stop() {
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }
    private void pause() {
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer!=null&& mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
        super.onDestroy();
    }
}
