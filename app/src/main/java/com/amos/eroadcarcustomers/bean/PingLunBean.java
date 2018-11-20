package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

/**
 * Created by amos on 2018/8/2.
 */

public class PingLunBean implements Serializable {
    private String cmt_id;
    private String cmt_shop;
    private String cmt_context;
    private String cmt_level;
    private String cmt_time;
    private String cmt_author;
    private String cmt_img1;
    private String cmt_img2;
    private String cmt_img3;

    public String getCmt_id() {
        return cmt_id;
    }

    public void setCmt_id(String cmt_id) {
        this.cmt_id = cmt_id;
    }

    public String getCmt_shop() {
        return cmt_shop;
    }

    public void setCmt_shop(String cmt_shop) {
        this.cmt_shop = cmt_shop;
    }

    public String getCmt_context() {
        return cmt_context;
    }

    public void setCmt_context(String cmt_context) {
        this.cmt_context = cmt_context;
    }

    public String getCmt_level() {
        return cmt_level;
    }

    public void setCmt_level(String cmt_level) {
        this.cmt_level = cmt_level;
    }

    public String getCmt_time() {
        return cmt_time;
    }

    public void setCmt_time(String cmt_time) {
        this.cmt_time = cmt_time;
    }

    public String getCmt_author() {
        return cmt_author;
    }

    public void setCmt_author(String cmt_author) {
        this.cmt_author = cmt_author;
    }

    public String getCmt_img1() {
        return cmt_img1;
    }

    public void setCmt_img1(String cmt_img1) {
        this.cmt_img1 = cmt_img1;
    }

    public String getCmt_img2() {
        return cmt_img2;
    }

    public void setCmt_img2(String cmt_img2) {
        this.cmt_img2 = cmt_img2;
    }

    public String getCmt_img3() {
        return cmt_img3;
    }

    public void setCmt_img3(String cmt_img3) {
        this.cmt_img3 = cmt_img3;
    }
}
