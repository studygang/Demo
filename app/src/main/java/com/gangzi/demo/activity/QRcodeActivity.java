package com.gangzi.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangzi.demo.R;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRcodeActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.bt_scan)
    Button bt_scan;
    @BindView(R.id.tv_show_result)
    TextView tv_show_result;
    @BindView(R.id.et_text)
    EditText et_text;
    @BindView(R.id.bt_make)
    Button bt_make;
    @BindView(R.id.iv_show_code)
    ImageView iv_show_code;
    @BindView(R.id.check_logo)
    CheckBox check_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcode);
        ButterKnife.bind(this);
        bt_scan.setOnClickListener(this);
        bt_make.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_scan:
                startActivityForResult(new Intent(QRcodeActivity.this, CaptureActivity.class),0);
                break;
            case R.id.bt_make:
                makeQRcode();
                break;
        }
    }

    private void makeQRcode() {
        String content=et_text.getText().toString().trim();
        if (content!=null){
           Bitmap bitmap= EncodingUtils.createQRCode(content,500,500,check_logo.isChecked()?
            BitmapFactory.decodeResource(getResources(),R.drawable.banana):null);
            iv_show_code.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&&resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            String result=bundle.getString("result");
            tv_show_result.setText(result);
        }
    }
}
