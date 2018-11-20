package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    private String mm_id;
    private String mm_name;
    private String mm_cellphone;
    private String mm_car_vin;

    public String getMm_car_vin() {
        if (mm_car_vin == null) {
            mm_car_vin = "";
        }
        return mm_car_vin;
    }

    public void setMm_car_vin(String mm_car_vin) {
        this.mm_car_vin = mm_car_vin;
    }

    public String getMm_car_lisence() {
        if (mm_car_lisence == null) {
            mm_car_lisence = "";
        }
        return mm_car_lisence;
    }

    public void setMm_car_lisence(String mm_car_lisence) {
        this.mm_car_lisence = mm_car_lisence;
    }

    private String mm_car_lisence;

    public String getMm_id() {
        if (mm_id == null) {
            mm_id = "";
        }
        return mm_id;
    }

    public void setMm_id(String mm_id) {
        this.mm_id = mm_id;
    }

    public String getMm_name() {
        if (mm_name == null) {
            mm_name = "";
        }
        return mm_name;
    }

    public void setMm_name(String mm_name) {
        this.mm_name = mm_name;
    }

    public String getMm_cellphone() {
        if (mm_cellphone == null) {
            mm_cellphone = "";
        }
        return mm_cellphone;
    }

    public void setMm_cellphone(String mm_cellphone) {
        this.mm_cellphone = mm_cellphone;
    }
}
