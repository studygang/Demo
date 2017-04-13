package com.gangzi.demo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gangzi.demo.adapter.RecycleAdapter;

public class FirstActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton bt_floatButton;
    private RecycleAdapter mAdapter;
    private int[]image=new int[]{R.drawable.watermelon,R.drawable.banana,R.drawable.cherry,R.drawable.grape
            ,R.drawable.mango,R.drawable.pear,R.drawable.pineapple,R.drawable.orange,R.drawable.strawberry};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycle);
        mAdapter=new RecycleAdapter(FirstActivity.this,image);
        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        bt_floatButton= (FloatingActionButton) findViewById(R.id.bt_float);
        bt_floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstActivity.this,SecondActivity.class));
            }
        });
    }
}
