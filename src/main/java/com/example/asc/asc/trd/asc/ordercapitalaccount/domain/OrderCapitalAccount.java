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
     * 时间  HH24MISS(24小时制度)
     */
    private String time;

    /**
     * 日期
     */
    private Date date;

    /**
     * 金额(入金单位为分)
     */
    private Long money;

    /**
     * 客户流水号
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
     * 资金用途
     */
    private String usage;
    /**
     * 付款方手续费
     */
    private String payFee;
    /**
     * 收款方手续费
     */
    private String reciveFee;
    /**
     * 付款方资金账户
     */
    private String paySubbNo;
    /**
     * 付款方资金账户名称
     */
    private String paySubbName;
    /**
     * 收款方资金账户
     */
    private String reciveSubbNo;
    /**
     * 收款方资金账户名称
     */
    private String reciveSubbName;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 修改时间
     */
    private String updatedAt;

    /**
     * 删除时间
     */
    private String deletedAt;

    public OrderCapitalAccount() {
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getPtnSrl() {
        return ptnSrl;
    }

    public void setPtnSrl(String ptnSrl) {
        this.ptnSrl = ptnSrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
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

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getReciveFee() {
        return reciveFee;
    }

    public void setReciveFee(String reciveFee) {
        this.reciveFee = reciveFee;
    }

    public String getPaySubbNo() {
        return paySubbNo;
    }

    public void setPaySubbNo(String paySubbNo) {
        this.paySubbNo = paySubbNo;
    }

    public String getPaySubbName() {
        return paySubbName;
    }

    public void setPaySubbName(String paySubbName) {
        this.paySubbName = paySubbName;
    }

    public String getReciveSubbNo() {
        return reciveSubbNo;
    }

    public void setReciveSubbNo(String reciveSubbNo) {
        this.reciveSubbNo = reciveSubbNo;
    }

    public String getReciveSubbName() {
        return reciveSubbName;
    }

    public void setReciveSubbName(String reciveSubbName) {
        this.reciveSubbName = reciveSubbName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }
}
