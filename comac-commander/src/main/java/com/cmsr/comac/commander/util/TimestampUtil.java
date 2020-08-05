package com.cmsr.comac.commander.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ztw
 * @data 2019/11/8 14:42
 * @description 时间戳工具类
 */

@Slf4j
public class TimestampUtil {
    public static long getTimestamp() throws Exception {
        //当前时间的时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringTime = sdf.format(new Date());
        return Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
    }
}
