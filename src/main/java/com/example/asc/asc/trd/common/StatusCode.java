package com.example.asc.asc.trd.common;

/**
 * @author zhanglize
 * @create 2019/11/11
 */
public enum StatusCode {

    Success("000000","成功");

    private String code;
    private String msg;


    StatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
