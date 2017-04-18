package com.gangzi.demo;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.gangzi.demo.adapter.RecycleAdapter;
import com.gangzi.demo.base.BaseFragment;
import com.gangzi.demo.fragment.HomeFragment;
import com.gangzi.demo.fragment.LifeFragment;
import com.gangzi.demo.fragment.PersonFragment;
import com.gangzi.demo.fragment.ShopFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private RadioGroup rp_main;
    private List<BaseFragment>mFragments;
    private int position;
    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        initClickListener();
    }

    private void initView() {
        rp_main= (RadioGroup) findViewById(R.id.rp_main);
    }

    private void initFragment() {
        mFragments=new ArrayList<BaseFragment>();
        HomeFragment homeFragment=new HomeFragment();
        LifeFragment lifeFragment=new LifeFragment();
        ShopFragment shopFragment=new ShopFragment();
        PersonFragment personFragment=new PersonFragment();
        mFragments.add(homeFragment);
        mFragments.add(lifeFragment);
        mFragments.add(shopFragment);
        mFragments.add(personFragment);
    }
    private void initClickListener() {
        rp_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                switch (i){
                    case R.id.home:
                        position=0;
                        break;
                    case R.id.life:
                        position=1;
                        break;
                    case R.id.shop:
                        position=2;
                        break;
                    case R.id.me:
                        position=3;
                        break;
                    default:
                        position=0;
                        break;
                }
                BaseFragment toFragment= getFragment(position);
                switchFragment(fragment,toFragment);
            }
        });
        rp_main.check(R.id.home);
    }

    private void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        if (fragment!=nextFragment){
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction fr=fm.beginTransaction();
            fragment=nextFragment;
            if (nextFragment!=null){
                if (!nextFragment.isAdded()){
                    if (fromFragment!=null){
                        fr.hide(fromFragment);
                    }
                    fr.add(R.id.content_layout,nextFragment).commit();
                }else{
                    if (fromFragment!=null){
                        fr.hide(fromFragment);
                    }
                    fr.show(nextFragment).commit();
                }
            }
        }
    }

    private BaseFragment getFragment(int position) {
        if (mFragments!=null&&mFragments.size()>0){
            return  mFragments.get(position);
        }
       return  null;
    }


}
