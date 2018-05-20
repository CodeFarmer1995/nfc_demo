package com.r8c.nfc_demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *  "宿管主界面" 主界面
 */
public class MianUIActivity extends AppCompatActivity {
    public TabLayout tl;
    private ViewPager vp;
    private String[] titles={"发布","签到","审核"};
    private int[] imagesIds={R.drawable.fabu,R.drawable.chakan,R.drawable.geren};
    private MyFragmentAdapter adapter;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian_ui);
        tl= (TabLayout) findViewById(R.id.main_tb);
        vp= (ViewPager) findViewById(R.id.main_vp);
        fragments=new ArrayList<>();
        fragments.add(new DorMessageFragment());
        fragments.add(new DorQueFragment());
        fragments.add(new DorPersonFragment());
        adapter=new MyFragmentAdapter(getSupportFragmentManager(),this,fragments,titles,imagesIds);
        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);
        for (int i = 0; i <tl.getTabCount() ; i++) {
            TabLayout.Tab tab = tl.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
            if(tab.isSelected()){
                View vi = tab.getCustomView();
                ImageView iv= (ImageView) vi.findViewById(R.id.iv_con);
                iv.setImageResource(R.drawable.fabu);
            }
        }
        tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View vi= tab.getCustomView();
                ImageView iv= (ImageView) vi.findViewById(R.id.iv_con);
                int posi = tab.getPosition();
                switch (posi){
                    case 0:
                        iv.setImageResource(R.drawable.fabu);
                        TabLayout.Tab tab1 = tl.getTabAt(1);
                        ImageView iv1= (ImageView) tab1.getCustomView().findViewById(R.id.iv_con);
                        iv1.setImageResource(R.drawable.chakan);
                        TabLayout.Tab tab2 = tl.getTabAt(2);
                        ImageView iv2= (ImageView) tab2.getCustomView().findViewById(R.id.iv_con);
                        iv2.setImageResource(R.drawable.geren);

                        break;
                    case 1:
                        iv.setImageResource(R.drawable.chakan);

                        TabLayout.Tab tab0 = tl.getTabAt(0);
                        ImageView iv0= (ImageView) tab0.getCustomView().findViewById(R.id.iv_con);
                        iv0.setImageResource(R.drawable.fabu);
                        TabLayout.Tab tab21 = tl.getTabAt(2);
                        ImageView iv21= (ImageView) tab21.getCustomView().findViewById(R.id.iv_con);
                        iv21.setImageResource(R.drawable.geren);

                        break;
                    case 2:
                        iv.setImageResource(R.drawable.geren);
                        TabLayout.Tab tab01 = tl.getTabAt(0);
                        ImageView iv01= (ImageView) tab01.getCustomView().findViewById(R.id.iv_con);
                        iv01.setImageResource(R.drawable.fabu);
                        TabLayout.Tab tab11 = tl.getTabAt(1);
                        ImageView iv11= (ImageView) tab11.getCustomView().findViewById(R.id.iv_con);
                        iv11.setImageResource(R.drawable.chakan);

                        break;
                }
                Log.i("show","选中"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    }

