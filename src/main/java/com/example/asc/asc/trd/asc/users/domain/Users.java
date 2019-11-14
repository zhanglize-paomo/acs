package com.example.asc.asc.trd.asc.users.domain;

import java.sql.Timestamp;

/**
 * 用户信息
 *
 *
 * @author zhanglize
 * @create 2019/11/14
 */
public class Users {

    /**
     * 主键
     */
    private int id;
    /**
     * 电话号
     */
    private String tel;
    /**
     * 密码
     */
    private String password;
    /**
     * appId
     */
    private String appId;

    /**
     * secret
     */
    private String secret;
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

    public Users() {
    }

    public Users(int id, String tel, String password, String appId, String secret, Timestamp createdAt, Timestamp updatedAt, Timestamp deletedAt) {
        this.id = id;
        this.tel = tel;
        this.password = password;
        this.appId = appId;
        this.secret = secret;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
