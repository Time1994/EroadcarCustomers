package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

/**
 * Created by amos on 2018/8/1.
 */

public class CityBean implements Serializable {
    private String pmg_points_country;
    private String shopcount;

    public String getPmg_points_country() {
        return pmg_points_country;
    }

    public void setPmg_points_country(String pmg_points_country) {
        this.pmg_points_country = pmg_points_country;
    }

    public String getShopcount() {
        return shopcount;
    }

    public void setShopcount(String shopcount) {
        this.shopcount = shopcount;
    }
}
