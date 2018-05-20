package com.r8c.nfc_demo;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class Sign extends BmobObject {
     private String  name;
    private String   reason;
    private String   time;

    public Sign() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getTime() {
        return time;
    }
}
