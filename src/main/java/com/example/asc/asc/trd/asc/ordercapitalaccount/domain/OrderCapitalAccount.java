package com.example.asc.asc.trd.asc.ordercapitalaccount.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 订单支付实体类
 *
 * @author zhanglize
 * @create 2019/11/19
 */
public class OrderCapitalAccount {

    /**
     * 主键
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;
    /**
     * 资金账户id
     */
    private int userAccountId;

    /**
     * 日期
     */
    private Date date;

    /**
     * 金额(入金单位为分)
     */
    private Long money;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 第二支付方式
     */
    private String secPayType;

    /**
     * 商品主题描述
     */
    private String subject;

    /**
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 客户请求流水号
     */
    private String ptnSrl;

    /**
     * 中金平台流水号
     */
    private String platSrl;
    /**
     * 平台订单号
     */
    private String orderNo;
    /**
     * 0:支付中 1:完成 2:失败
     */
    private String status;
    /**
     * 0:未回复或者失败   1:成功
     *
     *  发送支付请求时默认是0未回复消息信息
     */
    private String clientStatus;

    /**
     * 通知用户次数
     */
    private int sendToClientTimes;

    /**
     * 发送端标记 0：手机，1：pc端
     */
    private String reqFlg;

    /**
     * 页面通知url
     */
    private String notificationUrl;

    /**
     * 后台通知url
     */
    private String servnoticeUrl;

    /**
     * 资金用途
     */
    private String usage;
    /**
     * 返回url
     */
    private String url;

    /**
     * 返回图片url地址
     */
    private String imageUrl;
    /**
     * 创建时间
     */
    private Timestamp createdAt;
    /**
     * 修改时间
     */
    private Timestamp updatedAt;

    /**
     * 删除时间
     */
    private Timestamp deletedAt;


}
