package com.r8c.nfc_demo;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class User extends BmobObject {

    private String  acccout;
    private String  password;
    private String  sate;

    public User() {
    }

    public void setAcccout(String acccout) {
        this.acccout = acccout;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSate(String sate) {
        this.sate = sate;
    }

    public String getAcccout() {
        return acccout;
    }

    public String getPassword() {
        return password;
    }

    public String getSate() {
        return sate;
    }
}
