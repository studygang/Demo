package com.gangzi.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VoiceActivity extends Activity implements View.OnClickListener{

    private Button bt_file,bt_byte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        bt_byte= (Button) findViewById(R.id.bt_byte);
        bt_byte.setOnClickListener(this);
        bt_file= (Button) findViewById(R.id.bt_file);
        bt_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_file:
                break;
            case R.id.bt_byte:
                break;
        }
    }
}
