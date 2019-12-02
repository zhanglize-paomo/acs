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
    private Timestamp createdAt;
    /**
     * 修改时间
     */
    private Timestamp updatedAt;

    /**
     * 删除时间
     */
    private Timestamp deletedAt;

    public OrderCapitalAccount() {
    }

    public OrderCapitalAccount(int id, String time, Date date, Long money, String ptnSrl, String platSrl, String orderNo, String status, String usage, String payFee, String reciveFee, String paySubbNo, String paySubbName, String reciveSubbNo, String reciveSubbName, Timestamp createdAt, Timestamp updatedAt, Timestamp deletedAt) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.money = money;
        this.ptnSrl = ptnSrl;
        this.platSrl = platSrl;
        this.orderNo = orderNo;
        this.status = status;
        this.usage = usage;
        this.payFee = payFee;
        this.reciveFee = reciveFee;
        this.paySubbNo = paySubbNo;
        this.paySubbName = paySubbName;
        this.reciveSubbNo = reciveSubbNo;
        this.reciveSubbName = reciveSubbName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
}
