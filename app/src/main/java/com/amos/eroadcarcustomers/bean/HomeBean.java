package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

/**
 * Created by amos on 2018/8/1.
 */

public class HomeBean implements Serializable {
    private String hm_id;
    private String hm_type;
    private String hm_module;
    private String hm_tab;
    private String hm_img;

    public String getHm_id() {
        return hm_id;
    }

    public void setHm_id(String hm_id) {
        this.hm_id = hm_id;
    }

    public String getHm_type() {
        return hm_type;
    }

    public void setHm_type(String hm_type) {
        this.hm_type = hm_type;
    }

    public String getHm_module() {
        return hm_module;
    }

    public void setHm_module(String hm_module) {
        this.hm_module = hm_module;
    }

    public String getHm_tab() {
        return hm_tab;
    }

    public void setHm_tab(String hm_tab) {
        this.hm_tab = hm_tab;
    }

    public String getHm_img() {
        return hm_img;
    }

    public void setHm_img(String hm_img) {
        this.hm_img = hm_img;
    }

    public String getHm_remark() {
        return hm_remark;
    }

    public void setHm_remark(String hm_remark) {
        this.hm_remark = hm_remark;
    }

    private String hm_remark;
}
