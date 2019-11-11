package com.example.asc.asc.trd.asc.useraccountsettlement.domain;

import java.util.Date;

/**
 * 用户申请绑定银行数据表
 *
 *
 * @author zhanglize
 * @create 2019/11/11
 */
public class UserAccountSettlement {

    /**
     * 主键
     */
    private int id;

    /**
     * 用户申请信息表
     */
   private int userAccountId;

    /**
     * 用户ID
     */
   private int userId;

    /**
     * 客户流水号
     */
    private String ptnSrl;

    /**
     * 客户流水号
     */
    private Date date;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 银行开户名
     */
    private String accNm;
    /**
     * 银行卡号
     */
    private String accNo;

    /**
     * 银行编号
     */
    private int bkId;

    /**
     * 账户类型
     */
    private String accTp;

    /**
     * 银行卡账户类型
     */
    private String crdTp;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 跨行标识 1:本行 2:跨行
     */
    private String crsMk;

    /**
     * 跨行时此字段必填
     */
    private String openBkCd;

    /**
     * 业务功能标识
     */
    private String fcFlg;

    /**
     * 办理银行卡证件类型
     */
    private String cdType;

    /**
     * 办理银行卡证件号码
     */
    private String cdNo;

    /**
     * 绑定验证状态 0:未验证 1:验证成功  2:验证失败
     */
    private int status;

    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public UserAccountSettlement() {
    }

    public UserAccountSettlement(int id, int userAccountId, int userId, String ptnSrl, Date date, String orderNo, String accNm, String accNo, int bkId, String accTp, String crdTp, String phone, String crsMk, String openBkCd, String fcFlg, String cdType, String cdNo, int status, String createdAt, String updatedAt, String deletedAt) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.userId = userId;
        this.ptnSrl = ptnSrl;
        this.date = date;
        this.orderNo = orderNo;
        this.accNm = accNm;
        this.accNo = accNo;
        this.bkId = bkId;
        this.accTp = accTp;
        this.crdTp = crdTp;
        this.phone = phone;
        this.crsMk = crsMk;
        this.openBkCd = openBkCd;
        this.fcFlg = fcFlg;
        this.cdType = cdType;
        this.cdNo = cdNo;
        this.status = status;
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

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPtnSrl() {
        return ptnSrl;
    }

    public void setPtnSrl(String ptnSrl) {
        this.ptnSrl = ptnSrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAccNm() {
        return accNm;
    }

    public void setAccNm(String accNm) {
        this.accNm = accNm;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public int getBkId() {
        return bkId;
    }

    public void setBkId(int bkId) {
        this.bkId = bkId;
    }

    public String getAccTp() {
        return accTp;
    }

    public void setAccTp(String accTp) {
        this.accTp = accTp;
    }

    public String getCrdTp() {
        return crdTp;
    }

    public void setCrdTp(String crdTp) {
        this.crdTp = crdTp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCrsMk() {
        return crsMk;
    }

    public void setCrsMk(String crsMk) {
        this.crsMk = crsMk;
    }

    public String getOpenBkCd() {
        return openBkCd;
    }

    public void setOpenBkCd(String openBkCd) {
        this.openBkCd = openBkCd;
    }

    public String getFcFlg() {
        return fcFlg;
    }

    public void setFcFlg(String fcFlg) {
        this.fcFlg = fcFlg;
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
    }

    public String getCdNo() {
        return cdNo;
    }

    public void setCdNo(String cdNo) {
        this.cdNo = cdNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
