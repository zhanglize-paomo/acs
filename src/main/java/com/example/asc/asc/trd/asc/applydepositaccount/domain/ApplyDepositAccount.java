package com.example.asc.asc.trd.asc.applydepositaccount.domain;

/**
 * 出金申请记录信息
 *
 * @author zhanglize
 * @create 2019/11/6
 */
public class ApplyDepositAccount {

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
     * 资金账号
     */
    private String cltaccSubno;

    /**
     * 户名
     */
    private String cltaccCltnm;

    /**
     * 银行账号(卡号)
     */
    private String bkaccAccno;

    /**
     * 开户名称
     */
    private String bkaccAccnm;

    /**
     * 总金额(资金单位:分)
     */
    private String amtTamt;

    /**
     * 发生额(资金单位:分)
     */
    private String amtAclamt;

    /**
     * 转账手续费(资金单位:分)
     */
    private String amtFeeamt;

    /**
     * 手续费率(以万记,千分之三填30)
     */
    private int feeRate;

    /**
     * 结算方式标示,AA=正常结算(默认),T0=T0代付出金,T1=T1代付出金
     */
    private String balflag;

    /**
     * 平台流水号
     */
    private String srlPlatsrl;

    /**
     * 资金用途(附言)
     */
    private String usage;

    /**
     * 交易状态
     */
    private String status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String modifyTime;

    public ApplyDepositAccount() {
    }

    public ApplyDepositAccount(Long id, String msghdTrdt, String srlPtnsrl, String cltaccSubno, String cltaccCltnm, String bkaccAccno, String bkaccAccnm, String amtTamt, String amtAclamt, String amtFeeamt, int feeRate, String balflag, String srlPlatsrl, String usage, String status, String createTime, String modifyTime) {
        this.id = id;
        this.msghdTrdt = msghdTrdt;
        this.srlPtnsrl = srlPtnsrl;
        this.cltaccSubno = cltaccSubno;
        this.cltaccCltnm = cltaccCltnm;
        this.bkaccAccno = bkaccAccno;
        this.bkaccAccnm = bkaccAccnm;
        this.amtTamt = amtTamt;
        this.amtAclamt = amtAclamt;
        this.amtFeeamt = amtFeeamt;
        this.feeRate = feeRate;
        this.balflag = balflag;
        this.srlPlatsrl = srlPlatsrl;
        this.usage = usage;
        this.status = status;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
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

    public String getCltaccSubno() {
        return cltaccSubno;
    }

    public void setCltaccSubno(String cltaccSubno) {
        this.cltaccSubno = cltaccSubno;
    }

    public String getCltaccCltnm() {
        return cltaccCltnm;
    }

    public void setCltaccCltnm(String cltaccCltnm) {
        this.cltaccCltnm = cltaccCltnm;
    }

    public String getBkaccAccno() {
        return bkaccAccno;
    }

    public void setBkaccAccno(String bkaccAccno) {
        this.bkaccAccno = bkaccAccno;
    }

    public String getBkaccAccnm() {
        return bkaccAccnm;
    }

    public void setBkaccAccnm(String bkaccAccnm) {
        this.bkaccAccnm = bkaccAccnm;
    }

    public String getAmtTamt() {
        return amtTamt;
    }

    public void setAmtTamt(String amtTamt) {
        this.amtTamt = amtTamt;
    }

    public String getAmtAclamt() {
        return amtAclamt;
    }

    public void setAmtAclamt(String amtAclamt) {
        this.amtAclamt = amtAclamt;
    }

    public String getAmtFeeamt() {
        return amtFeeamt;
    }

    public void setAmtFeeamt(String amtFeeamt) {
        this.amtFeeamt = amtFeeamt;
    }

    public int getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(int feeRate) {
        this.feeRate = feeRate;
    }

    public String getBalflag() {
        return balflag;
    }

    public void setBalflag(String balflag) {
        this.balflag = balflag;
    }

    public String getSrlPlatsrl() {
        return srlPlatsrl;
    }

    public void setSrlPlatsrl(String srlPlatsrl) {
        this.srlPlatsrl = srlPlatsrl;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
