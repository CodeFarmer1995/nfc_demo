package com.r8c.nfc_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class MeetingAdapter  extends BaseAdapter{
    List<Meeting> dataList;
    private Context context;

    public MeetingAdapter(List<Meeting> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.weijilayout,null);
            holder.tv1= (TextView) view.findViewById(R.id.tv1);
            holder.tv2= (TextView) view.findViewById(R.id.tv2);
            holder.tv3= (TextView) view.findViewById(R.id.tv3);

            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.tv1.setText(dataList.get(i).getTime());
        holder.tv2.setText(dataList.get(i).getLoc());
        holder.tv3.setText(dataList.get(i).getContent());


        return view;
    }
    class ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;

    }
}


