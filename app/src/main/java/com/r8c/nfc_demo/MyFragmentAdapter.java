package com.r8c.nfc_demo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager显示Fragment的适配器
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles;
    private int[] images;
    public MyFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragments, String[] titles, int [] images) {
        super(fm);
        this.context=context;
        this.titles=titles;
        this.images=images;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    /*
     自定义TabView
     */
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tabview_layout, null);
        TextView title= (TextView) view.findViewById(R.id.tv_title);
        title.setText(titles[position]);
        ImageView image= (ImageView) view.findViewById(R.id.iv_con);
        image.setImageResource(images[position]);
        return view;
    }
}
