package com.example.asc.asc.trd.asc.entryexitaccount.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;

/**
 * 入金支付-集成交易_异步[T3061]
 *
 * @author zhanglize
 * @create 2019/11/8
 */
public class EntryExitAccount extends BaseRowModel implements Serializable {

    /**
     * 主键
     */
    @ExcelProperty(value = {"主键"}, index = 0)
    private int id;

    /**
     * 用户id
     */
    @ExcelProperty(value = {"用户id"}, index = 1)
    private int userId;
    /**
     * 资金账户id
     */
    @ExcelProperty(value = {"资金账户id"}, index = 2)
    private int userAccountId;

    /**
     * 日期
     */
    @ExcelProperty(value = {"日期"}, index = 3)
    private String date;

    /**
     * 金额(入金单位为分)
     */
    @ExcelProperty(value = {"金额(入金单位为分)"}, index = 4)
    private Long money;

    /**
     * 支付方式
     */
    @ExcelProperty(value = {"支付方式"}, index = 5)
    private String payType;

    /**
     * 第二支付方式
     */
    @ExcelProperty(value = {"第二支付方式"}, index = 6)
    private String secPayType;

    /**
     * 商品主题描述
     */
    @ExcelProperty(value = {"商品主题描述"}, index = 7)
    private String subject;

    /**
     * 商品描述
     */
    @ExcelProperty(value = {"商品描述"}, index = 8)
    private String goodsDesc;

    /**
     * 客户请求流水号
     */
    @ExcelProperty(value = {"客户请求流水号"}, index = 9)
    private String ptnSrl;

    /**
     * 中金平台流水号
     */
    @ExcelProperty(value = {"中金平台流水号"}, index = 10)
    private String platSrl;
    /**
     * 平台订单号
     */
    @ExcelProperty(value = {"平台订单号"}, index = 11)
    private String orderNo;
    /**
     * 0:支付中 1:完成 2:失败
     */
    @ExcelProperty(value = {"交易状态"}, index = 12)
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
    @ExcelProperty(value = {"页面通知url"}, index = 13)
    private String notificationUrl;

    /**
     * 后台通知url
     */
    @ExcelProperty(value = {"异步通知url"}, index = 14)
    private String servnoticeUrl;

    /**
     * 资金用途
     */
    @ExcelProperty(value = {"资金用途"}, index = 15)
    private String usage;
    /**
     * 返回url
     */
    @ExcelProperty(value = {"返回url"}, index = 16)
    private String url;

    /**
     * 返回图片url地址
     */
    private String imageUrl;
    /**
     * 创建时间
     */
    @ExcelProperty(value = {"交易时间"}, index = 17)
    private String createdAt;

    /**
     * 修改时间
     */
    private String updatedAt;

    /**
     * 删除时间
     */
    private String deletedAt;

    public EntryExitAccount() {
    }

    public EntryExitAccount(int id, int userId, int userAccountId, String date, Long money, String payType, String secPayType,
                            String subject, String goodsDesc, String ptnSrl, String platSrl, String orderNo, String status,
                            String clientStatus, int sendToClientTimes, String reqFlg, String notificationUrl,
                            String servnoticeUrl, String usage, String url, String imageUrl, String createdAt, String updatedAt, String deletedAt) {
        this.id = id;
        this.userId = userId;
        this.userAccountId = userAccountId;
        this.date = date;
        this.money = money;
        this.payType = payType;
        this.secPayType = secPayType;
        this.subject = subject;
        this.goodsDesc = goodsDesc;
        this.ptnSrl = ptnSrl;
        this.platSrl = platSrl;
        this.orderNo = orderNo;
        this.status = status;
        this.clientStatus = clientStatus;
        this.sendToClientTimes = sendToClientTimes;
        this.reqFlg = reqFlg;
        this.notificationUrl = notificationUrl;
        this.servnoticeUrl = servnoticeUrl;
        this.usage = usage;
        this.url = url;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSecPayType() {
        return secPayType;
    }

    public void setSecPayType(String secPayType) {
        this.secPayType = secPayType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getPtnSrl() {
        return ptnSrl;
    }

    public void setPtnSrl(String ptnSrl) {
        this.ptnSrl = ptnSrl;
    }

    public String getPlatSrl() {
        return platSrl;
    }

    public void setPlatSrl(String platSrl) {
        this.platSrl = platSrl;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public int getSendToClientTimes() {
        return sendToClientTimes;
    }

    public void setSendToClientTimes(int sendToClientTimes) {
        this.sendToClientTimes = sendToClientTimes;
    }

    public String getReqFlg() {
        return reqFlg;
    }

    public void setReqFlg(String reqFlg) {
        this.reqFlg = reqFlg;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public String getServnoticeUrl() {
        return servnoticeUrl;
    }

    public void setServnoticeUrl(String servnoticeUrl) {
        this.servnoticeUrl = servnoticeUrl;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
