package com.r8c.nfc_demo;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/16 0016.
 */


public class Manager extends BmobObject {

    private String account;
    private String code;

    public Manager() {
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public String getCode() {
        return code;
    }
}
