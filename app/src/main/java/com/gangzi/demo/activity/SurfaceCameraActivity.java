package com.gangzi.demo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gangzi.demo.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SurfaceCameraActivity extends Activity implements View.OnClickListener{

    private SurfaceView surfaceView_camera;
    private Button bt_camera;
    private ImageView iv_show;
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_camera);
        initView();
    }

    private void initView() {
        surfaceView_camera= (SurfaceView) findViewById(R.id.surfaceView_camera);
        bt_camera= (Button) findViewById(R.id.bt_camera);
        bt_camera.setOnClickListener(this);
        iv_show= (ImageView) findViewById(R.id.iv_show);
        surfaceHolder=surfaceView_camera.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera=getCamera();
                if (camera!=null){
                    try {
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera=getCamera();
        if (camera!=null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    private Camera getCamera(){
        if (camera==null){
            try{
                camera=Camera.open();
                return camera;
            }catch (Exception e){
                return null;
            }
        }
        return camera;
    }
    private void releaseCamera(){
        if (camera!=null){
            camera.release();
            camera=null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_camera:
                takePhoto();
                break;
        }
    }

    private void takePhoto() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //预览
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                iv_show.setImageBitmap(bitmap);
                //保存
                BufferedOutputStream bos=null;
                try {
                    File dir= Environment.getExternalStorageDirectory();
                    String fileName="gangzi"+System.currentTimeMillis()+".jpg";
                    bos=new BufferedOutputStream(new FileOutputStream(new File(dir,fileName)));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    if (bos!=null){
                        try {
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
