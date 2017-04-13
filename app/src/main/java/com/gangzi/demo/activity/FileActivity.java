package com.gangzi.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gangzi.demo.R;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileActivity extends AppCompatActivity {

    @BindView(R.id.bt_startSay)
    Button bt_startSay;
    @BindView(R.id.tv_text)
    TextView tv_text;

    private ExecutorService mExectorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);
        //录音JNI函数不具备线程安全性，所以要用单线程
        mExectorService= Executors.newSingleThreadExecutor();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁时，停止后台任务，避免内存泄漏
        mExectorService.shutdownNow();
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
        return true;
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseRecorder() {
    }
    /**
     * 录错误处理音
     */
    private void recorderFail() {
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
        return true;
    }
}
