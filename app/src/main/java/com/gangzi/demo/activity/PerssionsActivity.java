package com.gangzi.demo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gangzi.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 权限测试，读写SDcard权限和打电话权限
 */
public class PerssionsActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.bt_call)
    Button bt_call;
    @BindView(R.id.bt_readsd)
    Button bt_fileDownLoadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perssions);
        ButterKnife.bind(this);
        bt_call.setOnClickListener(this);
        bt_fileDownLoadView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_call:
                call();
                break;
            case R.id.bt_readsd:
                sdCardPermission();
                break;
        }
    }

    /**
     * 读写SD卡权限操作
     */
    private void sdCardPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            //做权限的申请处理
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }else{
            doReadSDCardPermission();
        }
    }

    private void doReadSDCardPermission() {
    }

    /**
     * 拨打电话
     */
    private void call() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            //做权限的申请处理
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            doCallPhone();
        }
    }

    private void doCallPhone() {
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri data=Uri.parse("tel:"+"18289797580");
        intent.setData(data);
        this.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                //打电话权限回调处理
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    doCallPhone();
                }else{
                    //提示用户权限未被授予
                    Toast.makeText(this,"权限未被授予",Toast.LENGTH_SHORT).show();
                }
                break;
            case 0:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    doReadSDCardPermission();
                }else{
                    //提示用户权限未被授予
                    Toast.makeText(this,"权限未被授予",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
