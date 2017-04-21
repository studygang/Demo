package com.gangzi.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gangzi.demo.R;
import com.gangzi.demo.service.VoiceService;
import com.gangzi.demo.utils.Tools;

public class VoiceActivity extends Activity implements View.OnClickListener{

    private Button bt_file,bt_byte,bt_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        bt_byte= (Button) findViewById(R.id.bt_byte);
        bt_byte.setOnClickListener(this);
        bt_file= (Button) findViewById(R.id.bt_file);
        bt_file.setOnClickListener(this);
        bt_stop= (Button) findViewById(R.id.bt_stop);
        startService(new Intent(this, VoiceService.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_file:
                Intent intent=new Intent("com.gangzi.voice");
                intent.putExtra("cmd", Tools.PLAY);
                sendBroadcast(intent);
                break;
            case R.id.bt_byte:
                Intent intent2=new Intent("com.gangzi.voice");
                intent2.putExtra("cmd", Tools.PAUSE);
                sendBroadcast(intent2);
                break;
            case R.id.bt_stop:
                Intent intent3=new Intent("com.gangzi.voice");
                intent3.putExtra("cmd", Tools.STOP);
                sendBroadcast(intent3);
                break;
        }
    }
}
