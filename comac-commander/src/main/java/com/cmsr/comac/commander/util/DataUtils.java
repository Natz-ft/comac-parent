package com.cmsr.comac.commander.util;


import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.pojo.Screen;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;


/**
 * @author ztw
 * @data 2019/9/8 15:22
 * @description 数据获取工具类
 */


@Slf4j
public class DataUtils {

    /**
     * 获取指定路径图片数据
     *
     * @param path 路径
     * @return result
     * @throws Exception 异常
     */
    public static ResultInfo getImageFile(String path, Long maxTimeOut) throws Exception {

        File file = new File(path);

        //文件不存在
        if (!file.exists()) {
            log.info("此路径:" + path + "下,未找到指定文件");
            new ResultInfo(false, "3001");
        }

        //获取文件最后修改的时间毫秒值
        long lastModified = file.lastModified();

        long currentTimeMillis = System.currentTimeMillis();

        //获取到超时时间差
        long timeOut = currentTimeMillis - lastModified;

        //超时
        if (timeOut > maxTimeOut) {
            log.info("此路径:" + path + "下,图片文件在规定时间内未更新!");
            return new ResultInfo(false, "3004");
        }

        //获取文件的扩展名
        String fileExtension = Files.getFileExtension(file.getName());
        if (!"jpg".equals(fileExtension)) {
            log.info("此路径:" + path + "下,文件扩展名不是jpg格式!");
            return new ResultInfo(false, "3003");
        }

        //获取图片
        ByteSource byteSource = Files.asByteSource(file);
        byte[] bytes = byteSource.read();
        return new ResultInfo(true, bytes);
    }

    /**
     * 获取txt文档信息数据
     *
     * @param path 路径
     * @return result
     */
    public static ResultInfo getTxtData(String path, Long maxTimeOut) throws Exception {

        File file = new File(path);

        if (!file.exists()) {
            log.info("此路径:" + path + "下,未找到指定文件");
            return new ResultInfo(false, "2001");
        }

        if (!file.isFile()) {
            log.info("此路径:" + path + "下,的文件不是文件格式!");
            return new ResultInfo(false, "2002");
        }

        //获取文件最后修改的时间毫秒值
        long lastModified = file.lastModified();

        long currentTimeMillis = System.currentTimeMillis();

        //获取到超时时间差
        long timeOut = currentTimeMillis - lastModified;

        //超时
        if (timeOut > maxTimeOut) {
            log.info("此路径:" + path + "下,的文件在指定时间内未更新!");
            return new ResultInfo(false, "2003");
        }

        ArrayList<String> list = Lists.newArrayList();
        //按行读取文件内容
        List<String> strings = Files.readLines(file, Charsets.UTF_8);
        for (String string : strings) {
            //按空格切割,并去除首尾的空格和换行
            List<String> stringList = Splitter.on(" ").trimResults().splitToList(string);
            //添加到集合
            list.addAll(stringList);
        }

        //没有内容
        if (list.size() == 0) {
            log.info("此路径:" + path + "下,的文件内容为空!");
            return new ResultInfo(false, "2004");
        }

        //文本内容不符合规则
        if (list.size() != 6 && list.size() != 1) {
            log.info("此路径:" + path + "下,的文件内容不符合规则!");
            return new ResultInfo(false, "2005");
        }

        return new ResultInfo(true, list);
    }

}