package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

/**
 * Created by amos on 2018/7/28.
 */

public class YzCodeBean implements Serializable {
    private String state;
    private String msg;
    private String data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
