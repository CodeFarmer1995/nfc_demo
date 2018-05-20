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
import cn.bmob.v3.listener.SaveListener;

public class UserLoginActivity extends Activity implements View.OnClickListener{
    EditText etUsername;
    EditText etPassword;

    Button btRegist;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
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
            case R.id.bt_regist:
                //用户注册
                if((etUsername.getText().toString()!=null)&&(etPassword.getText().toString()!=null)) {
                    User user=new User();
                    user.setAcccout(etUsername.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.setSate("待审核");
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(UserLoginActivity.this, "注册成功，等待管理员的审核", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(UserLoginActivity.this, "注册失败"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(UserLoginActivity.this, "请完善注册信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_log:

                //开始登录
                //将登录信息发送至服务端校验
                if((etUsername.getText().toString()!=null)&&(etPassword.getText().toString()!=null)) {
                    BmobQuery<User> query = new BmobQuery<User>();
//查询playerName叫“比目”的数据
                    query.addWhereEqualTo("acccout",etUsername.getText().toString());
//返回50条数据，如果不加上这条语句，默认返回10条数据
                    query.setLimit(50);
//执行查询方法
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if(e==null){
                                if(object.size()>0){
                                    for (User u:object){
                                        if((u.getPassword().equals(etPassword.getText().toString())&&(u.getSate().equals("待审核")))){
                                            Toast.makeText(UserLoginActivity.this, "当前账号，等待管理员的审核", Toast.LENGTH_SHORT).show();
                                        }
                                        if((u.getPassword().equals(etPassword.getText().toString())&&(u.getSate().equals("审核通过")))){
                                            Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(UserLoginActivity.this,UserHomeActivity.class));
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(UserLoginActivity.this, "登录失败"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(UserLoginActivity.this, "请完善登录信息", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
