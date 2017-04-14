package com.gangzi.demo.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gangzi.demo.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileActivity extends AppCompatActivity {

    @BindView(R.id.bt_startSay)
    Button bt_startSay;
    @BindView(R.id.tv_text)
    TextView tv_text;

    private ExecutorService mExectorService;
    private MediaRecorder mRecorder;
    private File mAudioFile;
    private long startRecorderTime,stopRecorderTime;
    private Handler mHandler;
    //主线程和后台线程数据同步
    private volatile boolean mIsPalying;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);
        //录音JNI函数不具备线程安全性，所以要用单线程
        mExectorService= Executors.newSingleThreadExecutor();
       /* mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };*/
       mHandler=new Handler(Looper.getMainLooper());
        //按下说话，释放发送
        bt_startSay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecord();
                        break;
                }
                //处理了点击事件，返回true
                return true;
            }
        });
    }
    @OnClick(R.id.bt_play)
    public void play(){
        //检测当前状态，防止重复播放
        if (mAudioFile!=null&&!mIsPalying){
            mIsPalying=true;
            //提交后台任务，开始播放
            mExectorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(mAudioFile);
                }
            });
        }
    }

    /**
     * 实际播放逻辑
     * @param audioFile
     */
    private void doPlay(File audioFile) {
        //配置播放器mediaPlay
        mediaPlayer=new MediaPlayer();
        try {
            //设置声音文件
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            //设置监听回调
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    //播放结束
                    stopPlay();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer player, int i, int i1) {
                    //提示用户
                    playFail();
                    //释放播放器
                    stopPlay();
                    //错误已经处理，返回true
                    return true;
                }
            });
            //配置音量，是否循环
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setLooping(false);
            //准备，开始
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (RuntimeException|IOException e){
            e.printStackTrace();
            //异常处理，防止闪退
            playFail();
            stopPlay();
        }
    }

    /**
     * 提醒用户播放失败
     */
    private void playFail() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileActivity.this,"播放失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        //重置播放状态
        mIsPalying=false;
        if (mediaPlayer!=null){
            //重置监听器，防止内存泄漏
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁时，停止后台任务，避免内存泄漏
        mExectorService.shutdownNow();
        releaseRecorder();
        stopPlay();
    }

    private void startRecord() {
        bt_startSay.setText("正在说话...");
        //提交后台任务，执行录音逻辑
        mExectorService.submit(new Runnable() {
            @Override
            public void run() {
                //释放之前录音的recoder
                releaseRecorder();
                //执行录音逻辑，如果失败提示用户
                if (!doStart()){
                    recorderFail();
                }
            }
        });
    }

    /**
     * 启动录音逻辑
     * @return
     */
    private boolean doStart() {
       //创建mediaRecorder
        mRecorder=new MediaRecorder();
        //创建录音文件
        try {
            mAudioFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Demo/"+System.currentTimeMillis()+".m4a");
            mAudioFile.getParentFile().mkdirs();
            mAudioFile.createNewFile();
            //配置MediaRecorder
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//从麦克风采集
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//保存文件格式为MP4格式
            mRecorder.setAudioSamplingRate(44100);//所有Android系统都支持的采样频率
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//通用的AAC编码格式
            mRecorder.setAudioEncodingBitRate(96000);//音质比较好的频率
            mRecorder.setOutputFile(mAudioFile.getAbsolutePath());//设置录音文件位置
            //开始录音
            mRecorder.prepare();
            mRecorder.start();
            //记录开始录音的时间，用于统计时长
            startRecorderTime=System.currentTimeMillis();
        } catch (IOException |RuntimeException e) {
            e.printStackTrace();
            //捕获异常，避免闪退，返回false提醒用户
            return false;
        }
        //录音成功
        return true;
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseRecorder() {
        if (mRecorder!=null){
            mRecorder.release();
            mRecorder=null;
        }
    }
    /**
     * 录错误处理音
     */
    private void recorderFail() {
        mAudioFile=null;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileActivity.this,"录音失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void stopRecord() {
        bt_startSay.setText("按住说话");
        //提交后台任务，执行停止录音逻辑
        mExectorService.submit(new Runnable() {
            @Override
            public void run() {
                //执行停止录音逻辑，失败就要提示用户
                if (!doSop()){
                    recorderFail();
                }
                //释放录音MediaRecorder
                releaseRecorder();
            }
        });
    }

    /**
     * 停止录音逻辑
     * @return
     */
    private boolean doSop() {
        //停止录音
        try {
            mRecorder.stop();
            //记录停止时间，统计时长
            stopRecorderTime=System.currentTimeMillis();
            //只接受超过3S的录音，在UI上显示
            final int second=(int)((stopRecorderTime-startRecorderTime)/1000);
            if (second>3){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_text.setText(tv_text.getText()+"\n录音成功"+second+"秒");
                    }
                });
            }
        }catch (RuntimeException e){
            e.printStackTrace();;
            //捕获异常，避免闪退，返回false提醒用户失败
            return false;
        }
        return true;
    }
}
