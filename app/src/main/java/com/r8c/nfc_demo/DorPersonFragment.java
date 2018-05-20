package com.r8c.nfc_demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 审核 的界面
 */
public  class DorPersonFragment extends android.support.v4.app.Fragment {
    private ListView lv,lv2;
    private Button btAllNotice,btRepari;
    private List<User> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dorperson_fragment_layout,container,false);
        btAllNotice=view.findViewById(R.id.btAllnotice);
        btRepari=view.findViewById(R.id.btrepair);
        lv=view.findViewById(R.id.lv);
        lv2=view.findViewById(R.id.lv2);
        //通过审核
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.s1)//设置标题的图片
                        .setTitle("通过审核")//设置对话框的标题
                        .setMessage("是否通过当前用户审核？")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                User  gameScore = list.get(position);
                                gameScore.setSate("审核通过");
                                gameScore.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            BmobQuery<User> query = new BmobQuery<User>();
                                            query.addWhereEqualTo("sate", "待审核");
                                            //返回150条数据，如果不加上这条语句，默认返回10条数据
                                            query.setLimit(150);
                                            //执行查询方法
                                            query.findObjects(new FindListener<User>() {
                                                @Override
                                                public void done(List<User> object, BmobException e) {
                                                    if(e==null){
                                                        if(object.size()>0){
                                                            lv.setVisibility(View.VISIBLE);
                                                            lv.setAdapter(new ThiAdapter(object));
                                                            lv2.setVisibility(View.GONE);
                                                        }
                                                    }else{
                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                }
                                            });
                                        }else{
                                            Log.i("show","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                                gameScore.update( new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });


                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });


        //待审核
        btAllNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("sate", "待审核");
                //返回150条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(150);
                //执行查询方法
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if(e==null){
                            list=object;
                            if(object.size()>0){
                                lv.setVisibility(View.VISIBLE);
                                lv.setAdapter(new ThiAdapter(object));
                                lv2.setVisibility(View.GONE);
                            }
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
        btRepari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("sate", "审核通过");
                //返回150条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(150);
                //执行查询方法
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if(e==null){
                            if(object.size()>0){
                                lv2.setVisibility(View.VISIBLE);
                                lv2.setAdapter(new ThiAdapter(object));
                                lv.setVisibility(View.GONE);
                            }
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

        return view;
    }
   class ThiAdapter extends BaseAdapter {
        List<User> dataList;

        public ThiAdapter(List<User> dataList) {
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
                view= LayoutInflater.from(getActivity()).inflate(R.layout.repair,null);
                holder.tv1= (TextView) view.findViewById(R.id.tv1);
                holder.tv2= (TextView) view.findViewById(R.id.tv2);
                holder.tv3= (TextView) view.findViewById(R.id.tv3);

                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            holder.tv1.setText(dataList.get(i).getAcccout());
            holder.tv2.setText(dataList.get(i).getPassword());
            holder.tv3.setText(dataList.get(i).getSate());


            return view;
        }
        class ViewHolder{
            TextView tv1;
            TextView tv2;
            TextView tv3;

        }
    }

}
