package com.gangzi.demo.activity;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gangzi.demo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamActivity extends AppCompatActivity {

    @BindView(R.id.bt_start)
    Button bt_stream;
    @BindView(R.id.tv_show)
    TextView tv_show;
    //录音状态 volatile保证多线程内存同步，避免出现问题
    private volatile boolean mIsRecordering;
    private ExecutorService mService;
    private Handler mHandler;
    private AudioRecord mAudioRecord;
    private long mstartTime,endTime;
    private File mFile;
    private byte[]buffer;
    private FileOutputStream fos;
    private static final int BUFFER_SIZE=2048;//BUFFER_SIZE不能太大，避免OOM
    private MediaPlayer mediaPlay;
    private volatile boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        ButterKnife.bind(this);
        mService= Executors.newSingleThreadExecutor();
        mHandler=new Handler(Looper.getMainLooper());
        buffer=new byte[BUFFER_SIZE];

    }
    @OnClick(R.id.bt_streamPlay)
    public void play(){
        if (mFile!=null&&!isPlaying){
            isPlaying=true;
            mService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(mFile);
                }
            });
        }
    }

    private void doPlay(File file) {
        //配置播放器
        int streamTye= AudioManager.STREAM_MUSIC;//音乐类型，扬声器播放
        int sampleRate=44100;//录音时采用的采样频率，所以播放的时候使用同样的采样频率
        int audioFormat=AudioFormat.ENCODING_PCM_16BIT;//录音时使用16bit，播放也要相同
        int mode= AudioTrack.MODE_STREAM;//流模式
        int channelConfig=AudioFormat.CHANNEL_OUT_MONO;//单声道
        int minBufferSize=AudioTrack.getMinBufferSize(sampleRate,channelConfig,audioFormat);
        //构造AudioTrack
        AudioTrack audioTrack=new AudioTrack(streamTye,sampleRate,channelConfig,audioFormat,Math.max(minBufferSize,BUFFER_SIZE),mode);
        //播放
        //从文件流中读数据
        FileInputStream fis=null;
        try {
            //循环读取数据，写到播放器去播放
            fis=new FileInputStream(mFile);
            int read;
            //只要没读完，循环写播放
            while ((read=fis.read(buffer))>0){
                int result=audioTrack.write(buffer,0,read);
                //检查write的返回值，错误处理
                switch (result){
                    case AudioTrack.ERROR_INVALID_OPERATION:
                    case AudioTrack.ERROR_BAD_VALUE:
                    case AudioManager.ERROR_DEAD_OBJECT:
                        playFail();
                        return;
                    default:
                        break;
                }
            }
            audioTrack.play();
        }catch (RuntimeException|IOException e){
            e.printStackTrace();
        }finally {
            isPlaying=false;
            if (fis!=null){
                closeQuietly(fis);
            }
            resetQuietly(audioTrack);
        }

    }

    /**
     * 提醒用户播放失败
     */
    private void playFail() {
        mFile=null;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamActivity.this,"播放失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetQuietly(AudioTrack track) {
        try {
            track.stop();
            track.release();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    private void closeQuietly(FileInputStream fis){
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.shutdownNow();
    }

    @OnClick(R.id.bt_start)
    public void start(){
        //根据当前状态，改变UI，执行开始、停止录音的逻辑
        if (mIsRecordering){
            //改变UI状态
            bt_stream.setText("开始");
            mIsRecordering=false;
         
        }else{
            bt_stream.setText("停止");
            mIsRecordering=true;
            //提交后台任务，执行录音逻辑
            mService.submit(new Runnable() {
                @Override
                public void run() {
                    releaseRecorder();
                    if (!doStart()){
                        recorderFail();
                    }
                }
            });
        }
    }

    private void releaseRecorder() {

    }

    private boolean doStart() {

        try {
            //创建录音文件
            mFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Demo/"+System.currentTimeMillis()+".pcm");
            mFile.getParentFile().mkdirs();
            mFile.createNewFile();
            //创建文件输出流
            fos=new FileOutputStream(mFile);
            //配置AudidoRecorder;
            int audioResource= MediaRecorder.AudioSource.MIC;//从麦克风采集
            int sampleRate=44100;//所有Android系统都支持的频率
            int channelConfig= AudioFormat.CHANNEL_IN_MONO;//单声道输入
            int audioFormat=AudioFormat.ENCODING_PCM_16BIT;//PCM16是所有Android系统都支持
            int minBufferSize=AudioRecord.getMinBufferSize(sampleRate,channelConfig,audioFormat);//计算audioRecorder内部buffer的最小大小
            //buffer不能小于最低要求，也不能小于我们每次读取的大小
            mAudioRecord=new AudioRecord(audioResource,sampleRate,channelConfig,audioFormat,Math.max(minBufferSize,BUFFER_SIZE));
            //开始录音
            mAudioRecord.startRecording();
            //记录开始录音的时间，用于统计时长
            mstartTime=System.currentTimeMillis();
            //循环读取数据，写到输出流中
            while (mIsRecordering){
                int read=mAudioRecord.read(buffer,0,BUFFER_SIZE);
                if (read>0){
                    fos.write(buffer,0,read);
                }else{
                    return false;
                }
            }
            //退出循环，停止录音，释放资源
            return stopRecord();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }finally {
            if (mAudioRecord!=null){
                mAudioRecord.release();
            }
        }
    }

    private boolean stopRecord() {
        //停止录音，关闭文件输出流
        try {
            mAudioRecord.stop();
            mAudioRecord.release();;
            mAudioRecord=null;
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //记录结束时间，统计录音时长
        endTime=System.currentTimeMillis();
        final int time= (int) ((endTime-mstartTime)/1000);
        if (time>3){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    tv_show.setText(tv_show.getText()+"\n录音成功"+time+"秒");
                }
            });
        }
        return true;
    }

    private void recorderFail() {
        mFile=null;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamActivity.this,"录音失败!",Toast.LENGTH_SHORT).show();
                mIsRecordering=false;
                bt_stream.setText("开始");
            }
        });
    }
}
