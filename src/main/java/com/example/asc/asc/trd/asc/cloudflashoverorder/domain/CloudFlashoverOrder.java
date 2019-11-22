package com.example.asc.asc.trd.asc.cloudflashoverorder.domain;

/**
 * 云闪付支付信息
 *
 * @author zhanglize
 * @create 2019/11/22
 */
public class CloudFlashoverOrder {

    private Long id;
    private String orderId;
    private String bizType;
    private String backUrl;
    private String txnSubType;
    private String signature;
    private String txnType;
    private String channelType;
    private String frontUrl;
    private String certId;
    private String encoding;
    private String version;
    private String accessType;
    private String txnTime;
    private String merId;
    private String payTimeout;
    private String currencyCode;
    private String signMethod;
    private Long txnAmt;
    private String riskRateInfo;
    private String authCode;
    private String createAt;

    public CloudFlashoverOrder() {
    }

    public CloudFlashoverOrder(Long id, String orderId, String bizType, String backUrl, String txnSubType, String signature, String txnType, String channelType, String frontUrl, String certId, String encoding, String version, String accessType, String txnTime, String merId, String payTimeout, String currencyCode, String signMethod, Long txnAmt, String riskRateInfo, String authCode, String createAt) {
        this.id = id;
        this.orderId = orderId;
        this.bizType = bizType;
        this.backUrl = backUrl;
        this.txnSubType = txnSubType;
        this.signature = signature;
        this.txnType = txnType;
        this.channelType = channelType;
        this.frontUrl = frontUrl;
        this.certId = certId;
        this.encoding = encoding;
        this.version = version;
        this.accessType = accessType;
        this.txnTime = txnTime;
        this.merId = merId;
        this.payTimeout = payTimeout;
        this.currencyCode = currencyCode;
        this.signMethod = signMethod;
        this.txnAmt = txnAmt;
        this.riskRateInfo = riskRateInfo;
        this.authCode = authCode;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getPayTimeout() {
        return payTimeout;
    }

    public void setPayTimeout(String payTimeout) {
        this.payTimeout = payTimeout;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public Long getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(Long txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getRiskRateInfo() {
        return riskRateInfo;
    }

    public void setRiskRateInfo(String riskRateInfo) {
        this.riskRateInfo = riskRateInfo;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
