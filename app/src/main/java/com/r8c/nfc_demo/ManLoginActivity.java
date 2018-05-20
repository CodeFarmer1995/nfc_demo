package com.r8c.nfc_demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ManLoginActivity extends Activity implements View.OnClickListener {
    EditText etUsername;
    EditText etPassword;

    Button btRegist;
    Button btLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_login);
        etUsername= (EditText) findViewById(R.id.et_username);
        etPassword= (EditText) findViewById(R.id.et_password);
        btRegist= (Button) findViewById(R.id.bt_regist);
        btLogin= (Button) findViewById(R.id.bt_log);
        btRegist.setOnClickListener(this);
        btLogin.setOnClickListener(this);


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_log:

                //将登录信息发送至服务端校验
                if((etUsername.getText().toString()!=null)&&(etPassword.getText().toString()!=null)) {
                    BmobQuery<Manager> query = new BmobQuery<Manager>();
//查询playerName叫“比目”的数据
                    query.addWhereEqualTo("account",etUsername.getText().toString());
//返回50条数据，如果不加上这条语句，默认返回10条数据
                    query.setLimit(50);
//执行查询方法
                    query.findObjects(new FindListener<Manager>() {
                        @Override
                        public void done(List<Manager> object, BmobException e) {
                            if(e==null){
                                if(object.size()>0){
                                    for (Manager u:object){
                                        if(u.getCode().equals(etPassword.getText().toString())){
                                            Toast.makeText(ManLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                              startActivity(new Intent(ManLoginActivity.this,MianUIActivity.class));
                                              finish();

                                        }else {
                                            Toast.makeText(ManLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(ManLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(ManLoginActivity.this, "请完善登录信息", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }
}
