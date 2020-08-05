package com.cmsr.comac.commander.common;

/**
 * @author ztw
 * @data 2019/8/19 10:39
 * @description 响应数据实体类
 */

/**
 * 响应数据结构封装
 *
 * @param <T>
 * @author ztw
 */
public class ResponseData<T> extends BaseResponse {

    /**
     * 响应数据
     */
    private T data;

    /**
     * 空参构造
     */
    private ResponseData() {
    }

    /**
     * 满参构造
     *
     * @param code 状态码
     * @param data 数据
     */
    private ResponseData(CodeEnum code, T data) {
        super(code);
        this.data = data;
    }

    /**
     * 静态工厂
     *
     * @param code 状态
     * @param data 数据
     * @param <T>  参数
     * @return 响应对象
     */
    public static <T> ResponseData<T> out(CodeEnum code, T data) {
        return new ResponseData<T>(code, data);
    }

    /**
     * get
     *
     * @return data
     */
    public T getData() {
        return data;
    }

    /**
     * set
     *
     * @param data 数据
     */
    public void setData(T data) {
        this.data = data;
    }
}
