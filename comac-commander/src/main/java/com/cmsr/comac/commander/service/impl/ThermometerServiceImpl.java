package com.cmsr.comac.commander.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmsr.comac.commander.common.ActionEnum;
import com.cmsr.comac.commander.common.Compress;
import com.cmsr.comac.commander.common.ExperimentActionEnum;
import com.cmsr.comac.commander.common.OcrResponse;
import com.cmsr.comac.commander.dao.ExperimentDao;
import com.cmsr.comac.commander.dao.ThermometerDao;
import com.cmsr.comac.commander.dao.ThermometerDataHistoryDao;
import com.cmsr.comac.commander.dao.ThermometerImageHistoryDao;
import com.cmsr.comac.commander.pojo.*;
import com.cmsr.comac.commander.service.ThermometerDataHistoryService;
import com.cmsr.comac.commander.service.ThermometerImageHistoryService;
import com.cmsr.comac.commander.service.ThermometerService;
import com.cmsr.comac.commander.util.DataUtils;
import com.cmsr.comac.commander.util.TimestampUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ztw
 * @data 2019/9/29 14:39
 * @description 温度仪实现
 */

@Slf4j
@Service
public class ThermometerServiceImpl implements ThermometerService {
    /**
     * 请求算法获取数字温度仪图片的url地址
     */
    @Value("${ThermometerCamera}")
    private String thermometerCamera;
    /**
     * 修改数字温度仪对应相机位置url
     */
    @Value("${UpdateThermometerCameraOcr}")
    private String updateThermometerCameraOcr;

    /**
     * 视觉图片,TXT存放路径
     */
    @Value("${BASE_DIR}")
    private String baseDir;

    /**
     * 视觉图片保存的路径
     */
    @Value("${Vision_Base_Dir}")
    private String visionBaseDir;

    /**
     * 最大超时时间(可在.yml文件进行配置)
     */
    @Value("${maxTimeOut}")
    private long maxTimeOut;
    /**
     * 温度仪dao
     */
    @Autowired
    private ThermometerDao thermometerDao;
    /**
     * 温度仪图片历史dao
     */
    @Autowired
    private ThermometerImageHistoryDao thermometerImageHistoryDao;
    /**
     * 温度仪历史数据dao
     */
    @Autowired
    private ThermometerDataHistoryDao thermometerDataHistoryDao;
    /**
     * 温度仪历史数据service
     */
    @Autowired
    private ThermometerDataHistoryService thermometerDataHistoryService;
    /**
     * 实验开关dao
     */
    @Autowired
    private ExperimentDao experimentDao;


    /**
     * 数字温度仪表盘列表
     *
     * @param location 实验室
     * @return 温度仪表盘列表
     */
    @Override
    public ResultInfo queryThermometerList(String location) {
        List<Thermometer> thermometerList = thermometerDao.queryThermometerList(location);
        if (CollectionUtils.isEmpty(thermometerList)) {
            return new ResultInfo(false, "数据温度仪表盘数据为空!");
        }
        return new ResultInfo(true, thermometerList);
    }

    /**
     * 获取某个数字温度仪表盘的原始图片
     *
     * @param id 温度仪id
     * @return 某个数字温度仪表盘的原始图片
     */
    @Override
    public ResultInfo queryThermometerById(Integer id) throws Exception {
        Thermometer thermometer = thermometerDao.queryThermometerById(id);
        if (null == thermometer) {
            return new ResultInfo(false, "此数字温度显示仪数据不存在!");
        }
        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }
        long nowTimestamp = System.currentTimeMillis();
        ThermometerImageHistory thermometerImageHistory = thermometerImageHistoryDao.queryThermometerImageHistoryByThermometerId(id, nowTimestamp);

        String filename = thermometerImageHistory.getFilename();
        byte[] bytes = Files.toByteArray(new File(baseDir + "\\thermometer\\" + id + "\\" + filename));
        return new ResultInfo(true, bytes);
    }
    //从数据库先获取最新的文件名,在去保存的图片文件夹获取图片数据
//        //时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String stringTime = sdf.format(new Date());
//        //当前时间戳(精确到毫秒)
//        long nowTimestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
//        //获取最近一次保存的截屏图片历史数据
//        ThermometerImageHistory htermometerImageHistory = thermometerImageHistoryDao.queryThermometerImageHistoryByThermometerId(id, nowTimestamp);
//        //如果数据库还没有最新图片历史数据,就先从视觉文件获取
//        if (null == thermometerImageHistory) {
//            //文件写入状态
//            int imageStatus = 0;
//            do {
//                ResultInfo imageStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\image_status.txt", maxTimeOut);
//                List<String> status = (List<String>) imageStatusResult.getData();
//                imageStatus = Integer.parseInt(status.get(0));
//                if (0 == imageStatus) {
//                    log.info(id + "号温度仪图片正在写入...");
//                }
//            } while (0 == imageStatus);
//
//            ResultInfo imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
//            if (!imageFile.isFlag()) {
//                return new ResultInfo(false, "未获取到数据!");
//            }
//            byte[] bytes = (byte[]) imageFile.getData();
//            byte[] compress = Compress.compress(bytes, 0.5f);
//            return new ResultInfo(true, compress);
//        }
//        //文件名
//        String filename = thermometerImageHistory.getFilename();
//
//        //文件尚未写入
//        File file;
//        do {
//            file = new File(visionBaseDir + "\\thermometer
//        ByteSource byteSource = Files.asByteSource(file);
//        byte[] bytes = byteSource.read();
//        return new ResultInfo(true, bytes);

/* @Override
    public ResultInfo queryThermometerById(Integer id) throws Exception {
        Thermometer thermometer = thermometerDao.queryThermometerById(id);
        if (null == thermometer) {
            return new ResultInfo(false, "此数字温度显示仪数据不存在!");
        }
        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }

        //判断文件是否处于正在写入状态
        int imageStatus;
        do {
            ResultInfo imageStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\image_status.txt", maxTimeOut);
            List<String> status = (List<String>) imageStatusResult.getData();
            imageStatus = Integer.parseInt(status.get(0));
            if (0 == imageStatus) {
                log.info(id + "号温度仪图片处于写入状态中...");
            }
        } while (0 == imageStatus);

        //从视觉文件夹获取
        ResultInfo imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
        if (!imageFile.isFlag()) {
            return new ResultInfo(false, "未获取到数据!");
        }
        byte[] bytes = (byte[]) imageFile.getData();
        byte[] compress = Compress.compress(bytes, 0.5f);
        return new ResultInfo(true, compress);

        //从数据库先获取最新的文件名,在去保存的图片文件夹获取图片数据
//        //时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String stringTime = sdf.format(new Date());
//        //当前时间戳(精确到毫秒)
//        long nowTimestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
//        //获取最近一次保存的截屏图片历史数据
//        ThermometerImageHistory thermometerImageHistory = thermometerImageHistoryDao.queryThermometerImageHistoryByThermometerId(id, nowTimestamp);
//        //如果数据库还没有最新图片历史数据,就先从视觉文件获取
//        if (null == thermometerImageHistory) {
//            //文件写入状态
//            int imageStatus = 0;
//            do {
//                ResultInfo imageStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\image_status.txt", maxTimeOut);
//                List<String> status = (List<String>) imageStatusResult.getData();
//                imageStatus = Integer.parseInt(status.get(0));
//                if (0 == imageStatus) {
//                    log.info(id + "号温度仪图片正在写入...");
//                }
//            } while (0 == imageStatus);
//
//            ResultInfo imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
//            if (!imageFile.isFlag()) {
//                return new ResultInfo(false, "未获取到数据!");
//            }
//            byte[] bytes = (byte[]) imageFile.getData();
//            byte[] compress = Compress.compress(bytes, 0.5f);
//            return new ResultInfo(true, compress);
//        }
//        //文件名
//        String filename = thermometerImageHistory.getFilename();
//
//        //文件尚未写入
//        File file;
//        do {
//            file = new File(visionBaseDir + "\\thermometer\\" + id + "\\" + filename);
//        } while (!file.exists());
//
//        ByteSource byteSource = Files.asByteSource(file);
//        byte[] bytes = byteSource.read();
//        return new ResultInfo(true, bytes);
    }*/

    /**
     * 获取数字温度显示仪文档数据
     *
     * @return 数字温度显示仪文档数据
     */
    @Override
    public ResultInfo getThermometerTxtData(Integer id) throws Exception {
        Thermometer thermometer = thermometerDao.queryThermometerById(id);
        if (null == thermometer) {
            return new ResultInfo(false, "此数字温度显示仪数据不存在!");
        }

        if (1 != thermometer.getRecognitionEnable()) {
            return new ResultInfo(false, id + "号温度仪视觉识别已关闭!");
        }
        long timeStamp = System.currentTimeMillis();
        ThermometerDataHistory thermometerDataHistory = thermometerDataHistoryDao.queryThermometerDataHistoryLatestById(id, timeStamp);


//        ResultInfo thermometerDataResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\thermometer-data.txt", maxTimeOut);
//        if (!thermometerDataResult.isFlag()) {
//            return new ResultInfo(false, "未获取到数据!");
//        }
//        List<String> thermometerDataList = (List<String>) thermometerDataResult.getData();
        //封装结果集
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();
        //上段温度数据
        map.put("top", thermometerDataHistory.getUpperTop().toString());
        map.put("top", thermometerDataHistory.getUpperMiddle().toString());
        //中段温度数据
        map.put("middle", thermometerDataHistory.getCentreTop().toString());
        map.put("middle", thermometerDataHistory.getCentreMiddle().toString());
        //下段温度数据
        map.put("bottom", thermometerDataHistory.getDownTop().toString());
        map.put("bottom", thermometerDataHistory.getDownMiddle().toString());
        return new ResultInfo(true, map.asMap());

//        //当前时间时间戳
//        long nowTimestamp = TimestampUtil.getTimestamp();
//        //获取10s内,最近一次保存的温度仪温度历史数据
//        ThermometerDataHistory thermometerDataHistory = thermometerDataHistoryDao.queryThermometerDataHistoryByThermometerId(id, nowTimestamp);
//        //如果数据库还没有最新的温度仪数据,就先从视觉目录文件获取
//        if (null == thermometerDataHistory) {
//            ResultInfo thermometerDataResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\thermometer-data.txt", maxTimeOut);
//            if (!thermometerDataResult.isFlag()) {
//                return new ResultInfo(false, "未获取到数据!");
//            }
//            List<String> thermometerDataList = (List<String>) thermometerDataResult.getData();
//            //封装结果集
//            ArrayListMultimap<String, String> map = ArrayListMultimap.create();
//            //上段温度数据
//            map.put("top", thermometerDataList.get(0));
//            map.put("top", thermometerDataList.get(1));
//            //中段温度数据
//            map.put("middle", thermometerDataList.get(2));
//            map.put("middle", thermometerDataList.get(3));
//            //下段温度数据
//            map.put("bottom", thermometerDataList.get(4));
//            map.put("bottom", thermometerDataList.get(5));
//            return new ResultInfo(true, map.asMap());
//        }

//        //从数据库中查出的温度仪温度数据
//        //封装结果集
//        ArrayListMultimap<String, Integer> map = ArrayListMultimap.create();
//        //上段温度数据
//        map.put("top", thermometerDataHistory.getUpperTop());
//        map.put("top", thermometerDataHistory.getUpperMiddle());
//        //中段温度数据
//        map.put("middle", thermometerDataHistory.getCentreTop());
//        map.put("middle", thermometerDataHistory.getCentreMiddle());
//        //下段温度数据
//        map.put("bottom", thermometerDataHistory.getDownTop());
//        map.put("bottom", thermometerDataHistory.getDownMiddle());
//        return new ResultInfo(true, map.asMap());
    }

    /**
     * 关闭/开启某个数字温度仪表盘的采集
     *
     * @param id        温度仪id
     * @param actionMap map
     * @return result
     */
    @Override
    public ResultInfo updateThermometerEnableOrEngineRecognitionEnableToOff(Integer id, Map<String, String> actionMap) {
        Thermometer thermometer = thermometerDao.queryThermometerById(id);
        if (null == thermometer) {
            return new ResultInfo(false, "此数字温度显示仪不存在!");
        }

        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }

        String action = actionMap.get("action");
        if (ExperimentActionEnum.DISABLE.getAction().equals(action)) {
            //关闭温度仪采集
            try {
                thermometerDao.updateThermometerEnableToOff(id);
                return new ResultInfo(true, "数字温度显示仪功能已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭数字温度显示仪采集失败!");
            }
        } else if (ActionEnum.RECOGNITION_DISABLE.getAction().equals(action)) {
            //关闭温度仪视觉识别
            try {
                thermometerDao.updateEngineRecognitionEnableToOff(id);
                return new ResultInfo(true, "数字温度显示仪视觉识别已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭数字温度显示仪视觉识别失败!");
            }
        } else if (ExperimentActionEnum.ENABLE.getAction().equals(action)) {
            //开启数字温度仪采集
            try {
                thermometerDao.updateThermometerEnableToOn(id);
                return new ResultInfo(true, "数字温度显示仪采集已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启数字温度显示仪采集失败!");
            }
        } else if (ExperimentActionEnum.RECOGNITION_ENABLE.getAction().equals(action)) {
            //开启数字温度仪视觉识别
            try {
                thermometerDao.updateEngineRecognitionEnableToOn(id);
                return new ResultInfo(true, "数字温度显示仪视觉识别已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启数字温度显示仪视觉识别失败!");
            }
        } else {
            return new ResultInfo(false, "请求错误!");
        }
    }

    @Override
    public ResultInfo UpdateThermometerCameraOcr(JSONObject jsonObject) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, JSONObject> map = new HashMap<>(16);
        map.put("UpdateThermometerCameraOcr", jsonObject);
        HttpEntity<Object> httpEntity = new HttpEntity<>(map, null);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(thermometerCamera, HttpMethod.POST, httpEntity, JSONObject.class);
        if (exchange != null) {
            JSONObject body = exchange.getBody();
            if (!body.isNull("status")) {
                Integer status = (Integer) body.get("status");
                if (status == 0) {
                    return new ResultInfo(true, "更改数字温度仪坐标成功");
                }
            }
        }
        return new ResultInfo(false, "更改数字温度仪坐标失败");
    }

    @Override
    public ResultInfo getThermometerData(Integer cameraId, Integer... id) {
        Thermometer thermometer = thermometerDao.queryThermometerById(id[0]);
        if (null == thermometer) {
            return new ResultInfo(false, "此数字温度显示仪数据不存在!");
        }
        //温度仪历史图片信息数据对象
        ThermometerImageHistory thermometerImageHistory = new ThermometerImageHistory();
        //温度仪温度历史数据对象
        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id[0]);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }
        //开启识别
        if (thermometer.getRecognitionEnable() == 1) {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Integer> map = new HashMap<>(16);
            map.put("camera_id", cameraId);
            HttpEntity<Object> httpEntity = new HttpEntity<>(map, null);
            ResponseEntity<String> exchange = restTemplate.exchange(thermometerCamera, HttpMethod.POST, httpEntity, String.class);
            if (exchange != null) {
                String body = exchange.getBody();
                OcrResponse ocrResponse = JSON.parseObject(body, OcrResponse.class);
                String image1 = ocrResponse.getImage1();
                String image2 = ocrResponse.getImage2();
                List<Integer> result1 = ocrResponse.getResult1();
                List<Integer> result2 = ocrResponse.getResult2();
                if (!CollectionUtils.isEmpty(result1)) {
                    int upperTop = result1.get(0);
                    int upperMiddle = result1.get(1);
                    int centreTop = result1.get(2);
                    int centreMiddle = result1.get(3);
                    int downTop = result1.get(4);
                    int downMiddle = result1.get(5);
                    thermometerDataHistory.setUpperTop(upperTop);
                    thermometerDataHistory.setUpperMiddle(upperMiddle);
                    thermometerDataHistory.setCentreTop(centreTop);
                    thermometerDataHistory.setCentreMiddle(centreMiddle);
                    thermometerDataHistory.setDownMiddle(downMiddle);
                    thermometerDataHistory.setDownTop(downTop);
                    //时间戳
                    long timestamp = System.currentTimeMillis();
                    //以时间戳命名
                    String fileName = "thermometer-" + timestamp + "-" + upperTop + "-" + centreTop + "-" + downTop + ".jpg";
                    //将图片历史数据写入thermometer_image_history表
                    thermometerImageHistory.setThermometerId(id[0]);
                    //文件名
                    thermometerImageHistory.setFilename(fileName);
                    //时间戳
                    thermometerImageHistory.setTimestamp(timestamp);
                    //每隔5s往数字温度仪图片历史表中添加一条数据
                    thermometerImageHistoryDao.addThermometerImageHistory(thermometerImageHistory);
                    //解密压缩保存图片1
                    ResultInfo resultInfo = decodeAndCompress(id[0], image1, fileName);
                    byte[] bytes = (byte[]) resultInfo.getData();

                    //保存温度仪历史数据和图片
                    try {
                        thermometerDataHistoryService.thermometerDataProcess(id[0], timestamp, fileName, thermometerDataHistory,bytes);
                    } catch (Exception e) {
                    }
                }
                if (!CollectionUtils.isEmpty(result2)) {
                    int upperTop = result2.get(0);
                    int upperMiddle = result2.get(1);
                    int centreTop = result2.get(2);
                    int centreMiddle = result2.get(3);
                    int downTop = result2.get(4);
                    int downMiddle = result2.get(5);
                    thermometerDataHistory.setUpperTop(upperTop);
                    thermometerDataHistory.setUpperMiddle(upperMiddle);
                    thermometerDataHistory.setCentreTop(centreTop);
                    thermometerDataHistory.setCentreMiddle(centreMiddle);
                    thermometerDataHistory.setDownTop(downTop);
                    thermometerDataHistory.setDownMiddle(downMiddle);
                    //时间戳
                    long timestamp = System.currentTimeMillis();
                    //以时间戳命名
                    String fileName = "thermometer-" + timestamp + "-" + upperTop + "-" + centreTop + "-" + downTop + ".jpg";
                    //将图片历史数据写入thermometer_image_history表
                    thermometerImageHistory.setThermometerId(id[1]);
                    //文件名
                    thermometerImageHistory.setFilename(fileName);
                    //时间戳
                    thermometerImageHistory.setTimestamp(timestamp);
                    //每隔5s往数字温度仪图片历史表中添加一条数据
                    thermometerImageHistoryDao.addThermometerImageHistory(thermometerImageHistory);
                    //解密压缩保存图片1
                    ResultInfo resultInfo = decodeAndCompress(id[1], image2, fileName);
                    byte[] bytes = (byte[]) resultInfo.getData();
                    //保存温度仪历史数据
                    try {
                        thermometerDataHistoryService.thermometerDataProcess(id[1], timestamp, fileName, thermometerDataHistory,bytes);
                    } catch (Exception e) {
                    }
                }
            }
        } else if (thermometer.getRecognitionEnable() == 0) {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Integer> map = new HashMap<>(16);
            map.put("camera_id", cameraId);
            HttpEntity<Object> httpEntity = new HttpEntity<>(map, null);
            ResponseEntity<String> exchange = restTemplate.exchange(thermometerCamera, HttpMethod.POST, httpEntity, String.class);
            if (exchange != null) {
                String body = exchange.getBody();
                OcrResponse ocrResponse = JSON.parseObject(body, OcrResponse.class);
                if (StringUtils.isNotEmpty(body)) {
                    String image1 = ocrResponse.getImage1();
                    //时间戳
                    long timestamp = System.currentTimeMillis();
                    //以时间戳命名
                    String fileName = "thermometer-" + timestamp + ".jpg";
                    //将图片历史数据写入thermometer_image_history表
                    thermometerImageHistory.setThermometerId(id[0]);
                    //文件名
                    thermometerImageHistory.setFilename(fileName);
                    //时间戳
                    thermometerImageHistory.setTimestamp(timestamp);
                    //每隔5s往数字温度仪图片历史表中添加一条数据
                    thermometerImageHistoryDao.addThermometerImageHistory(thermometerImageHistory);
                    decodeAndCompress(id[0], image1, fileName);
                    String image2 = ocrResponse.getImage2();
                    timestamp = System.currentTimeMillis();
                    fileName = "thermometer-" + timestamp + ".jpg";
                    thermometerImageHistory.setThermometerId(id[1]);
                    //文件名
                    thermometerImageHistory.setFilename(fileName);
                    //时间戳
                    thermometerImageHistory.setTimestamp(timestamp);
                    //每隔5s往数字温度仪图片历史表中添加一条数据
                    thermometerImageHistoryDao.addThermometerImageHistory(thermometerImageHistory);
                    decodeAndCompress(id[1], image2, fileName);
                }
            }


        }


        return new ResultInfo(false, "获取数字温度仪图片失败");
    }

    /**
     * @param id
     * @param imgBase64
     * @return com.cmsr.comac.commander.pojo.ResultInfo
     * @Description imgbase64 解密图片并压缩存储图片
     */
    private ResultInfo decodeAndCompress(Integer id, String imgBase64, String fileName) {
        if (!StringUtils.isNotEmpty(imgBase64)) {
            return new ResultInfo(false, "图片加密字符串为空");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        String imgName = null;
        OutputStream out = null;
        byte[] b = null;
        try {
            //解密
            b = decoder.decodeBuffer(imgBase64);
            //处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //图片名称
            long timeStamp = System.currentTimeMillis();
            imgName = baseDir + "\\thermometer\\" + id + "-rawImage" + "\\thermometer-raw.jpg";
            File image = new File(imgName);
            if (!image.exists()) {
                image.getParentFile().mkdirs();
            }
            out = new FileOutputStream(imgName);
            out.write(b);
            out.flush();
        } catch (IOException e) {
            log.info("图片存储异常");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ArrayUtils.isNotEmpty(b)) {
            //读取到输入流
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            //将原始图片压缩并保存到指定目录
            //压缩图片长宽和质量各一半
            try {
                File compressFile = new File(baseDir + "\\thermometer\\" + id + "\\" + fileName);
                if (!compressFile.exists()) {
                    compressFile.getParentFile().mkdirs();
                }
                Thumbnails.of(in).scale(0.5f).outputQuality(0.5f).toFile(baseDir + "\\thermometer\\" + id + "\\" + fileName);
            } catch (IOException e) {
                log.info("{}号字温度显示仪原始图片压缩存储失败", id);
            }
            log.info(id + "号数字温度显示仪压缩后图片文件已保存!");
            return new ResultInfo(true, b, "数据获取和图片压缩存储成功");
        }
        return new ResultInfo(false, "数据获取和图片压缩存储失败");
    }

}
