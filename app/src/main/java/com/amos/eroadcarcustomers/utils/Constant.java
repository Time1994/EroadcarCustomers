package com.amos.eroadcarcustomers.utils;

import java.util.regex.Pattern;

public class Constant {
    public final static String HTTP = "http://116.236.115.124:9090/";
    public final static String MEMBERLOGIN = "";
    // APP类型
    public final static String APPTYPE = "android";
    // 接口版本
    public final static String VERSION = "1.0";
    //获取短信验证码
    public final static String getVerificateCode = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/getVerificateCode";
    //    个人版注册接口
    public final static String signUp = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/signUp";
    //    登陆接口
    public final static String signIn = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/signIn";
    //    个人中心
    public final static String getPersonalCenter = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/getPersonalCenter";
    //    门店列表
    public final static String getPoints = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/getPoints";
    //    修改密码接口
    public final static String modifyPassword = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/modifyPassword";
    //完善资料
    public final static String fillInfo = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Public/fillInfo";
    //获取门店服务
    public final static String getShopServices = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/getShopServices";

    //    获取市区
    public final static String getCountyByCity = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/getCountyByCity";
    //    根据区获取门店
    public final static String getShopsByCounty = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/getShopsByCounty";
    //    获取评论
    public final static String getCommentsByShop = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/getCommentsByShop";
    //    提交评论
    public final static String submitComments = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Point/submitComments";

    //首页模块
    public final static String GETHOMEMODULES = HTTP + "/workdir/ernet/ernet/index.php/Client/Public/getHomeModules";
    //更多服务
    public final static String GETRECOMMENDS = HTTP + "/workdir/ernet/ernet/index.php/Client/Public/getRecommends";
    //故障原因
    public final static String GETLUJIUCAUSES = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/getLujiuCauses";
    //提交路救
    public final static String REQLUJIU = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/reqLujiu";
    //取消路救
    public final static String CANCELLUJIU = HTTP + "workdir/ernet/ernet/index.php/Client/Lujiu/cancelLujiu";
    //师傅到达
    public final static String WORKERARRIVETIME = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/workerArriveTime";

    //获取评价标签
    public final static String GETCOMMENTTABS = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/getCommentTabs";
    //获取路救订单
    public final static String GETLUJIUORDERS = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/getLujiuOrders";
    //获取路救订单状态
    public final static String GETLUJIUSTATEBYCODE = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/getLujiuStateByCode";
    //路救评价提交
    public final static String SUBMITCOMMENTS = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/submitComments";
    //根据订单号获取订单内容
    public final static String GETCOMMENTSBYORDER = HTTP + "/workdir/ernet/ernet/index.php/Client/Lujiu/getCommentsByOrder";
    //倒計時时间
    public final static String getDaoJiShiTime = HTTP
            + "/workdir/ernet/ernet/index.php/Client/Lujiu/getDaoJiShiTime";
}
