package com.gangzi.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gangzi.demo.R;
import com.gangzi.demo.adapter.CilipViewAdapter;
import com.gangzi.demo.utils.ScalePageTransformer;
import com.gangzi.demo.view.ClipViewPager;

public class SecondActivity extends AppCompatActivity {

    private ClipViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private CilipViewAdapter mAdapter;
    private int[]image=new int[]{R.drawable.watermelon,R.drawable.banana,R.drawable.cherry,R.drawable.grape
            ,R.drawable.mango,R.drawable.pear,R.drawable.pineapple,R.drawable.orange,R.drawable.strawberry};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mViewPager= (ClipViewPager) findViewById(R.id.viewpager);
        mRelativeLayout= (RelativeLayout) findViewById(R.id.pager_container);
        mRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mAdapter=new CilipViewAdapter(this,image);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(image.length);
    }
}
