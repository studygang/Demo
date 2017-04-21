package com.gangzi.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gangzi.demo.R;

import java.io.File;
import java.net.URI;

public class CameraActivity extends Activity implements View.OnClickListener{

    private Button bt_system_camera;
    private ImageView iv_image;
    private String fileName="";
    private Button bt_my_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
    }

    private void initView() {
        bt_system_camera= (Button) findViewById(R.id.bt_system_camera);
        bt_system_camera.setOnClickListener(this);
        iv_image= (ImageView) findViewById(R.id.iv_image);
        bt_my_camera= (Button) findViewById(R.id.bt_my_camera);
        bt_my_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_system_camera:
                systemCamera();
                break;
            case R.id.bt_my_camera:
                startActivity(new Intent(this,SurfaceCameraActivity.class));
                break;
        }
    }

    private void systemCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //指定存储的照片的位置和名字
        File dir= Environment.getExternalStorageDirectory();//存在SD卡中
        fileName= "gangzi"+ System.currentTimeMillis()+".jpg";
        File file=new File(dir,fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&& resultCode==RESULT_OK){
            //照片文件
            File dir= Environment.getExternalStorageDirectory();//存在SD卡中
            File file=new File(dir,fileName);
            iv_image.setImageURI(Uri.fromFile(file));
        }
    }
}
