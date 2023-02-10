package edu.gdut.web.controller;

import edu.gdut.web.constant.ResponseConstant;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应格式
 *
 * @author Trust
 */
@Data
public class ResponseDate {
    /**
     * 响应码
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 是否成功
     */
    private boolean isSuccess;
    /**
     * 响应数据
     */
    private Map<String, Object> data;

    private ResponseDate() {}

    public static ResponseDate success() {
        ResponseDate resp = new ResponseDate();
        resp.code = ResponseConstant.SuccessCode;
        resp.isSuccess = true;
        resp.message = ResponseConstant.DefaultSuccessMessage;
        return resp;
    }

    public static ResponseDate failure() {
        ResponseDate resp = new ResponseDate();
        resp.code = ResponseConstant.FailCode;
        resp.isSuccess = false;
        resp.message = ResponseConstant.DefaultFailMessage;
        return resp;
    }

    /* 链式调用, 例如 ResponseData.success().code(xx).message(xxx).data(xxx) */

    public ResponseDate code(int code) {
        this.code = code;
        return this;
    }

    public ResponseDate message(String message) {
        this.message = message;
        return this;
    }

    public ResponseDate data(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public ResponseDate data(String key, Object value) {
        if (this.data == null)
            this.data = new HashMap<>();
        this.data.put(key, value);
        return this;
    }
}
