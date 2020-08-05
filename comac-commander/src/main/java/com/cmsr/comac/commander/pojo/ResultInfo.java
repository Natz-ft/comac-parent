package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * 结果类封装
 *
 * @author ztw
 */
@Data
public class ResultInfo implements Serializable {
    /**
     * 后端返回结果正常为true，发生异常返回false
     */
    private boolean flag;

    /**
     * 后端返回结果数据对象
     */
    private Object data;

    /**
     * 发生异常的错误消息
     */
    private String msg;

    public ResultInfo() {
    }

    public ResultInfo(boolean flag) {
        this.flag = flag;
    }

    /**
     * @param flag 判断
     * @param msg  信息
     */
    public ResultInfo(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    /**
     * @param flag 判断
     * @param data 数据
     * @param msg  信息
     */
    public ResultInfo(boolean flag, Object data, String msg) {
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }

    public ResultInfo(boolean flag, Object data) {
        this.flag = flag;
        this.data = data;
    }

}
