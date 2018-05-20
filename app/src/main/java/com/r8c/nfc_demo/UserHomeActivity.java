package com.r8c.nfc_demo;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserHomeActivity extends AppCompatActivity {
    private Button btSign;
    private Button btMeeting;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        btSign=findViewById(R.id.btsign);
        btMeeting=findViewById(R.id.btmeeting);
        lv=findViewById(R.id.lv);
        btSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText=new EditText(UserHomeActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(UserHomeActivity.this)
                        .setIcon(R.drawable.s1)//设置标题的图片
                        .setTitle("")//设置对话框的标题
                        .setView(editText)
                        .setMessage("请输入姓名")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Date d = new Date();
                                System.out.println(d);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateNowStr = sdf.format(d);
                                Sign sign=new Sign();
                                sign.setTime(dateNowStr);
                                sign.setName(editText.getText().toString());
                                sign.setReason("签到成功");
                                sign.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {

                                        if(e==null){
                                            Toast.makeText(UserHomeActivity.this, "签到成功",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });

        btMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobQuery<Meeting> query = new BmobQuery<Meeting>();
                //返回150条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(150);
                //执行查询方法
                query.findObjects(new FindListener<Meeting>() {
                    @Override
                    public void done(List<Meeting> object, BmobException e) {
                        if(e==null){
                            if(object.size()>0){
                                lv.setVisibility(View.VISIBLE);
                                lv.setAdapter(new MeetingAdapter(object,UserHomeActivity.this));

                            }
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

    }
}
