package com.example.asc.asc.trd.asc.useraccount.domain;

/**
 * 用户申请信息
 *
 * @author zhanglize
 * @create 2019/11/6
 */
public class UserAccount {
    /**
     * 主键
     */
    private int id;
    /**
     * 用户Id
     */
    private int userId;
    /**
     * 客户性质(0个人,1公司）
     */
   private String type;
    /**
     * 开户名称(姓名,公司名称)
     */
    private String name;
    /**
     * 证件类型
     */
    private String idcardType;
    /**
     * 证件号码
     */
    private String idcardNo;
    /**
     * 自然人/法人姓名
     */
    private String idcardName;
    /**
     * 手机号码
     */
    private String tel;
    /**
     * 账户类型（1:客户资金账户，3:合作方收益账户）
     */
    private String accountType;

    /**
     * 统一社会信用代码(公司必填)
     */
    private String uscId;
    /**
     * 开户流水号
     */
    private String ptnSrl;
    /**
     * 平台交易流水号
     */
    private String orderNo;
    /**
     * 开户状态（0:开户中 1:已开户 2:已销户  3:冻结  8:开户失败）
     */
    private String status;
    /**
     * 平台客户号
     */
    private String cltPid;
    /**
     * 银行电子账号
     */
    private String bnkEid;
    /**
     * 资金账号
     */
    private String subNo;
    /**
     * 电子账户归属支行号
     */
    private String openBkCd;
    /**
     * 电子账户归属支行名称
     */
    private String openBkNm;
    /**
     * 手续费率(以万记,千分之三填30)
     */
    private String feeRate;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public UserAccount() {
    }

    public UserAccount(int id, int userId, String type, String name, String idcardType, String idcardNo, String idcardName, String tel, String accountType, String uscId, String ptnSrl, String orderNo, String status, String cltPid, String bnkEid, String subNo, String openBkCd, String openBkNm, String feeRate, String createdAt, String updatedAt, String deletedAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.idcardType = idcardType;
        this.idcardNo = idcardNo;
        this.idcardName = idcardName;
        this.tel = tel;
        this.accountType = accountType;
        this.uscId = uscId;
        this.ptnSrl = ptnSrl;
        this.orderNo = orderNo;
        this.status = status;
        this.cltPid = cltPid;
        this.bnkEid = bnkEid;
        this.subNo = subNo;
        this.openBkCd = openBkCd;
        this.openBkNm = openBkNm;
        this.feeRate = feeRate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(String idcardType) {
        this.idcardType = idcardType;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getIdcardName() {
        return idcardName;
    }

    public void setIdcardName(String idcardName) {
        this.idcardName = idcardName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUscId() {
        return uscId;
    }

    public void setUscId(String uscId) {
        this.uscId = uscId;
    }

    public String getPtnSrl() {
        return ptnSrl;
    }

    public void setPtnSrl(String ptnSrl) {
        this.ptnSrl = ptnSrl;
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

    public String getCltPid() {
        return cltPid;
    }

    public void setCltPid(String cltPid) {
        this.cltPid = cltPid;
    }

    public String getBnkEid() {
        return bnkEid;
    }

    public void setBnkEid(String bnkEid) {
        this.bnkEid = bnkEid;
    }

    public String getSubNo() {
        return subNo;
    }

    public void setSubNo(String subNo) {
        this.subNo = subNo;
    }

    public String getOpenBkCd() {
        return openBkCd;
    }

    public void setOpenBkCd(String openBkCd) {
        this.openBkCd = openBkCd;
    }

    public String getOpenBkNm() {
        return openBkNm;
    }

    public void setOpenBkNm(String openBkNm) {
        this.openBkNm = openBkNm;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
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
