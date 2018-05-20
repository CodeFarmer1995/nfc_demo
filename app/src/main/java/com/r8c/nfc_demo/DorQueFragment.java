package com.r8c.nfc_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 管理 签到 界面
 */

public  class DorQueFragment extends android.support.v4.app.Fragment {
     private ListView lv;
     private Button bt;
     private ImageView iv;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.find_fragment_layout,container,false);
        lv=view.findViewById(R.id.lv);
        bt=view.findViewById(R.id.btNFC);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(getActivity(),NfcDemoActivity.class));
            }
        });
        iv=view.findViewById(R.id.iv);
        //开始查询
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobQuery<Sign> query = new BmobQuery<Sign>();
                query.setLimit(50);
                //执行查询方法
                query.findObjects(new FindListener<Sign>() {
                    @Override
                    public void done(List<Sign> object, BmobException e) {
                        if(e==null){
                            if(object.size()>0){
                                lv.setAdapter(new MyAdapter(object));
                            }
                        }else{

                        }
                    }
                });


            }
        });
        return view;
    }



    class MyAdapter extends BaseAdapter {
        List<Sign> dataList;

        public MyAdapter(List<Sign> dataList) {
            this.dataList = dataList;
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
                view= LayoutInflater.from(getActivity()).inflate(R.layout.lvsysinfo_layout,null);
                holder.tvTime= (TextView) view.findViewById(R.id.tvTime);
                holder.tvContent= (TextView) view.findViewById(R.id.tvreas);
                holder.tvName= (TextView) view.findViewById(R.id.tvname);
                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            holder.tvTime.setText(dataList.get(i).getTime());
            holder.tvContent.setText("状态："+dataList.get(i).getReason());
            holder.tvName.setText("人员："+dataList.get(i).getName());
            return view;
        }
        class ViewHolder{
            TextView tvTime;
            TextView tvName;
            TextView tvContent;
        }
    }

}
