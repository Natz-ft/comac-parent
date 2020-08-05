package com.cmsr.comac.commander.common;


import lombok.Getter;
import lombok.Setter;

/**
 * @author ztw
 * @data 2019/8/19 10:33
 * @description 基本响应封装
 */
@Getter
@Setter
public class BaseResponse {
    /**
     * 状态码
     */
    private int code;
    /**
     * 响应信息
     */
    private String msg;

    /**
     * 空参构造
     */
    public BaseResponse() {
    }

    /**
     * 获取响应状态码和信息
     *
     * @param code 状态码
     */
    public BaseResponse(CodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }


    /**
     * @param code 状态码
     * @return 响应对象
     */
    public static BaseResponse out(CodeEnum code) {
        return new BaseResponse(code);
    }
}
