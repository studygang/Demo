package com.gangzi.demo.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.gangzi.demo.R;
import com.gangzi.demo.utils.Tools;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/21.
 */

public class VoiceService extends Service {

    private MediaPlayer mPlayer;
    private VoiceReceiver receicer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer=new MediaPlayer();
        receicer = new VoiceReceiver();
        IntentFilter filter=new IntentFilter("com.gangzi.voice");
        registerReceiver(receicer,filter);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        ready();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mPlayer!=null&&mPlayer.isPlaying()){
            mPlayer.stop();
        }
        if (mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
        }
        unregisterReceiver(receicer);
        super.onDestroy();
    }

    private void start(){
        mPlayer.start();
    }
    private void pause(){
        mPlayer.pause();
    }
    private void stop(){
        mPlayer.stop();
        ready();
    }

    private void ready() {
        mPlayer.reset();
        mPlayer.create(this, R.raw.alarm);//已经到prepare状态，不用再mplayer.preapare()方法；
       /* try {
            File file=new File(Environment.getExternalStorageDirectory(),"a.mp3");
            Uri uri=Uri.fromFile(file);
            mPlayer.setDataSource(this,uri);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
      /*  try {
            mPlayer.setDataSource("/sdcard/a.mp3");
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    class VoiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd=intent.getIntExtra("cmd",-1);
            switch (cmd){
                case Tools.PLAY:
                    start();
                    break;
                case Tools.PAUSE:
                    pause();
                    break;
                case Tools.STOP:
                    stop();
                    break;
            }
        }
    }
}
