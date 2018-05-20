package com.r8c.nfc_demo;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class Meeting extends BmobObject {

    private String content;
    private String loc;
    private String time;

    public Meeting() {
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public String getLoc() {
        return loc;
    }

    public String getTime() {
        return time;
    }
}
