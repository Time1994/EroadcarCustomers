package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

/**
 * Created by amos on 2018/8/1.
 */

public class GuzBean implements Serializable {
    private String gc_id;
    private String gc_shopsid;
    private String gc_type;

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_shopsid() {
        return gc_shopsid;
    }

    public void setGc_shopsid(String gc_shopsid) {
        this.gc_shopsid = gc_shopsid;
    }

    public String getGc_type() {
        return gc_type;
    }

    public void setGc_type(String gc_type) {
        this.gc_type = gc_type;
    }

    public String getGc_guzhang_desc() {
        return gc_guzhang_desc;
    }

    public void setGc_guzhang_desc(String gc_guzhang_desc) {
        this.gc_guzhang_desc = gc_guzhang_desc;
    }

    private String gc_guzhang_desc;


}
