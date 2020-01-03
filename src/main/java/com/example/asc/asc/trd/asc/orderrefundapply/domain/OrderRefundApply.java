package com.example.asc.asc.trd.asc.orderrefundapply.domain;

/**
 * 退款申请实体类
 *
 * @author zhanglize
 * @create 2020/1/3
 */
public class OrderRefundApply {

    /**
     * 主键
     */
    private Long id;

    /**
     * 交易日期
     */
    private String msghdTrdt;

    /**
     * 合作方交易流水号
     */
    private String srlPtnsrl;

    /**
     * 原交易的合作方交易流水号
     */
    private String dptnsrl;
    /**
     * 发生额(资金单位:分)
     */
    private Long amtAclamt;

    /**
     * 平台交易流水号
     */
    private String srlPlatsrl;

    /**
     * 退款结果
     */
    private String state;

    /**
     * 退款成功/失败时间
     */
    private String restTime;

    /**
     * 失败原因
     */
    private String opion;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 修改时间
     */
    private String updatedAt;

    public OrderRefundApply() {
    }

    public OrderRefundApply(Long id, String msghdTrdt, String srlPtnsrl, String dptnsrl, Long amtAclamt, String srlPlatsrl, String state, String restTime, String opion, String createdAt, String updatedAt) {
        this.id = id;
        this.msghdTrdt = msghdTrdt;
        this.srlPtnsrl = srlPtnsrl;
        this.dptnsrl = dptnsrl;
        this.amtAclamt = amtAclamt;
        this.srlPlatsrl = srlPlatsrl;
        this.state = state;
        this.restTime = restTime;
        this.opion = opion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsghdTrdt() {
        return msghdTrdt;
    }

    public void setMsghdTrdt(String msghdTrdt) {
        this.msghdTrdt = msghdTrdt;
    }

    public String getSrlPtnsrl() {
        return srlPtnsrl;
    }

    public void setSrlPtnsrl(String srlPtnsrl) {
        this.srlPtnsrl = srlPtnsrl;
    }

    public String getDptnsrl() {
        return dptnsrl;
    }

    public void setDptnsrl(String dptnsrl) {
        this.dptnsrl = dptnsrl;
    }

    public Long getAmtAclamt() {
        return amtAclamt;
    }

    public void setAmtAclamt(Long amtAclamt) {
        this.amtAclamt = amtAclamt;
    }

    public String getSrlPlatsrl() {
        return srlPlatsrl;
    }

    public void setSrlPlatsrl(String srlPlatsrl) {
        this.srlPlatsrl = srlPlatsrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public String getOpion() {
        return opion;
    }

    public void setOpion(String opion) {
        this.opion = opion;
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
}
