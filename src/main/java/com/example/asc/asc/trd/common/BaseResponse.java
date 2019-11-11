package com.example.asc.asc.trd.common;

/**
 * 定义了返回的json格式，即json含有哪些字段
 *
 * @author zhanglize
 * @create 2019/11/11
 */
public class BaseResponse<T> {
    private String code;
    private String msg;
    private T data;

    public BaseResponse() {
    }
    public BaseResponse(StatusCode statusCode) {
        this.code=statusCode.getCode();
        this.msg=statusCode.getMsg();
    }
    public BaseResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(T data, StatusCode statusCode) {
        this.data = data;
        this.code=statusCode.getCode();
        this.msg=statusCode.getMsg();
    }

    public BaseResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
