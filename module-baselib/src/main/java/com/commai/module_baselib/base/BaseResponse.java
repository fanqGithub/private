package com.commai.module_baselib.base;

/**
 * Created by fanqi on 2018/4/27.
 * Description:这个作为主响应类，需根据服务器的响应而定
 */

public class BaseResponse<T> {

    //响应码 如：200
    private int code;

    //具体的返回数据
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
