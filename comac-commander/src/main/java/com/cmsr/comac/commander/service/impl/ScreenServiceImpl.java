package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.AlarmTargetEnum;
import com.cmsr.comac.commander.common.Compress;
import com.cmsr.comac.commander.common.ExperimentActionEnum;
import com.cmsr.comac.commander.dao.AlarmDao;
import com.cmsr.comac.commander.dao.ExperimentDao;
import com.cmsr.comac.commander.dao.ScreenDao;
import com.cmsr.comac.commander.dao.ScreenImageHistoryDao;
import com.cmsr.comac.commander.pojo.*;
import com.cmsr.comac.commander.service.ScreenService;
import com.cmsr.comac.commander.util.TimestampUtil;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 10:17
 * @description 截屏实现
 */

@Slf4j
@Service
public class ScreenServiceImpl implements ScreenService {
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
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;
    /**
     * 截屏dao
     */
    @Autowired
    private ScreenDao screenDao;
    /**
     * restTemplate
     */
    @Autowired
    RestTemplate restTemplate;
    /**
     * 实验开关dao
     */
    @Autowired
    private ExperimentDao experimentDao;
    /**
     * 告警gao
     */
    @Autowired
    private AlarmDao alarmDao;


    /**
     * 查询截屏列表
     *
     * @param location 实验室
     * @return 截屏列表
     */
    @Override
    public ResultInfo queryScreenList(String location) {
        List<Screen> screenList = screenDao.queryScreenList(location);
        if (CollectionUtils.isEmpty(screenList)) {
            return new ResultInfo(false, "屏幕列表不存在!");
        }
        return new ResultInfo(true, screenList);
    }

    /**
     * 根据id查询截屏对象
     *
     * @return 截屏对象
     */
    @Override
    public ResultInfo queryScreenById(Integer id) throws Exception {
        Screen screen = screenDao.queryScreenById(id);
        if (null == screen) {
            return new ResultInfo(false, "此截屏数据不存在!");
        }

        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }

        //获取截屏图片数据
        String screenUrl = screen.getUrl();

        //把token设置到头信息
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        byte[] bytes = new byte[0];

        try {
            //调用截屏程序
            ResponseEntity<byte[]> entity = restTemplate.exchange(screenUrl, HttpMethod.GET, httpEntity, byte[].class);
            //图片流
            bytes = entity.getBody();
            return new ResultInfo(true, bytes);
        } catch (RestClientException e) {
            e.printStackTrace();
            return new ResultInfo(false, "调用" + id + "号截屏程序异常!");
        }

//        //时间戳
//        long nowTimestamp = TimestampUtil.getTimestamp();
//        //获取最近一次保存的截屏图片历史数据
//        ScreenImageHistory screenImageHistory = screenImageHistoryDao.queryScreenImageHistoryByScreenId(id, nowTimestamp);
//        //超时
//        if (null == screenImageHistory) {
//            return new ResultInfo(false, id + "号截屏图片历史数据尚未写入!");
//        }
//        //文件名
//        String filename = screenImageHistory.getFilename();
//        //读取图片
//        File screenImageFile;
//        do {
//            //获取文件
//            screenImageFile = new File(visionBaseDir + "\\screen\\" + id + "\\" + filename);
//        } while (!screenImageFile.exists());
//
//        ByteSource byteSource = Files.asByteSource(screenImageFile);
//        byte[] bytes = byteSource.read();
//        return new ResultInfo(true, bytes);
    }

    /**
     * 压缩图片
     *
     * @param bytes 图片流
     * @return 截屏压缩图片
     */
    @Override
    public ResultInfo compressImage(byte[] bytes) {
        //将截屏按照比例压缩生成缩略图
        byte[] thumbnail = new byte[0];
        try {
            thumbnail = Compress.compressUnderSize(bytes, 0.2);
            return new ResultInfo(true, thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false, "图片压缩异常!");
        }
    }

    /**
     * 关闭屏幕功能
     *
     * @param id        屏幕id
     * @param actionMap map
     * @return result
     */
    @Override
    public ResultInfo updateScreenEnableOrRecognitionEnableToOff(Integer id, Map<String, String> actionMap) {
        String action = actionMap.get("action");
        if (ExperimentActionEnum.DISABLE.getAction().equals(action)) {
            //关闭截屏采集
            try {
                screenDao.updateScreenEnableToOff(id);
                return new ResultInfo(true, "屏幕截屏已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭屏幕截屏失败!");
            }
        } else if (ExperimentActionEnum.RECOGNITION_DISABLE.getAction().equals(action)) {
            //关闭截屏视觉识别
            try {
                screenDao.updateScreenRecognitionEnableToOff(id);
                return new ResultInfo(true, "屏幕截屏视觉识别已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭屏幕视觉识别失败!");
            }
        } else if (ExperimentActionEnum.ENABLE.getAction().equals(action)) {
            //开启截屏采集
            try {
                screenDao.updateScreenEnableToOn(id);
                return new ResultInfo(true, "屏幕截屏已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启屏幕截屏失败!");
            }
        } else if (ExperimentActionEnum.RECOGNITION_ENABLE.getAction().equals(action)) {
            //开启截屏视觉识别
            try {
                screenDao.updateScreenRecognitionEnableToOn(id);
                return new ResultInfo(true, "屏幕截屏视觉识别已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启屏幕视觉识别失败!");
            }
        } else {
            return new ResultInfo(false, "参数错误!");
        }
    }

    /**
     * 获取截屏图片
     *
     * @param screen 截屏
     * @return 截屏图片
     */
    @Override
    public ResultInfo getScreenImage(Screen screen) {
        //获取截屏图片数据
        String screenUrl = screen.getUrl();

        //屏幕id
        Integer id = screen.getId();

        //把token设置到头信息
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", screenToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        byte[] bytes = new byte[0];

        try {
            //远程调用截屏程序
            ResponseEntity<byte[]> entity = restTemplate.exchange(screenUrl, HttpMethod.GET, httpEntity, byte[].class);
            //图片流
            bytes = entity.getBody();
            return new ResultInfo(true, bytes);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("调用" + id + "号截屏程序异常!");
            return new ResultInfo(false, "5003");
        }
    }

    /**
     * 新增截屏告警
     *
     * @param id        屏幕id
     * @param timestamp 时间戳
     * @param fileName  文件名
     * @param alarmMap  告警
     */
    @Override
    public void addScreenAlarm(Integer id, long timestamp, String fileName, Map<String, String> alarmMap, byte[] bytes) {
        //先查询告警列表是否已存在告警中,并且告警类型为error的数据
        Alarm rgAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(5001);
        Alarm popAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(5002);
        if ("rg".equals(alarmMap.get("alarm")) && null == rgAlarm) {
            log.info(id + "号截屏红绿状态框为红色!");
            //写入告警记录表
            Alarm alarm = new Alarm();
            //告警目标
            String alarmTarget = AlarmTargetEnum.SCREEN.getAlarmTarget();
            alarm.setAlarmTarget(alarmTarget);
            //告警目标的id
            alarm.setAlarmTargetId(id);
            //告警中
            alarm.setAlarmStatus(1);
            //告警触发时间,默认为获取到图时的时间
            alarm.setAlarmTime(timestamp);
            //告警类型5001:截屏图片红绿状态框闪红
            alarm.setAlarmType(5001);
            //触发告警的图片文件名
            alarm.setAlarmPicName(fileName);
            alarmDao.addAlarm(alarm);
//            File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget + "\\" + id + "\\" + fileName);
            File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget + "-alarm" + "\\" + id + "\\" + fileName);
            if (!alarmImageFile.exists()) {
                alarmImageFile.getParentFile().mkdirs();
            }
            try {
                Files.write(bytes, alarmImageFile);
            } catch (IOException e) {
                log.info("id={}的屏幕告警图片写入异常", id);
            }


        } else if ("pop".equals(alarmMap.get("alarm")) && null == popAlarm) {
            log.info(id + "号截屏弹出testLog异常状态框!");
            //写入告警记录表
            Alarm alarm = new Alarm();
            //告警目标
            String alarmTarget = AlarmTargetEnum.SCREEN.getAlarmTarget();
            alarm.setAlarmTarget(alarmTarget);
            //告警目标的id
            alarm.setAlarmTargetId(id);
            //告警中
            alarm.setAlarmStatus(1);
            //告警触发时间,默认为获取到图时的时间
            alarm.setAlarmTime(timestamp);
            //告警类型5002:截屏弹出testLog异常状态框!
            alarm.setAlarmType(5002);
            //触发告警的图片文件名
            alarm.setAlarmPicName(fileName);
            alarmDao.addAlarm(alarm);
//            File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget  + "\\" + id + "\\" + fileName);
            File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget + "-alarm" + "\\" + id + "\\" + fileName);
            if (!alarmImageFile.exists()) {
                alarmImageFile.getParentFile().mkdirs();
            }
            try {
                Files.write(bytes, alarmImageFile);
            } catch (IOException e) {
                log.info("id={}的屏幕告警图片写入异常", id);
            }
        }
    }

    /**
     * 截屏获取不到图片时告警
     *
     * @param id        屏幕id
     * @param timestamp 时间戳
     * @param errorType 告警
     */
    @Override
    public void getScreenImageAlarm(Integer id, long timestamp, Integer errorType) {
        //先查询告警列表是否已存在告警中,并且告警类型为error的数据
        Alarm queryAlarm = alarmDao.queryAlarmByAlarmTypeAndAlarmStatus(errorType);
        //如果没有,则插入告警
        if (null == queryAlarm) {
            //写入告警记录表
            Alarm alarm = new Alarm();
            //告警目标
            alarm.setAlarmTarget(AlarmTargetEnum.SCREEN.getAlarmTarget());
            //告警目标的id
            alarm.setAlarmTargetId(id);
            //告警中
            alarm.setAlarmStatus(1);
            //告警触发时间,默认为获取到图时的时间
            alarm.setAlarmTime(timestamp);
            //告警类型5003:截屏程序调用异常获取不到图片
            alarm.setAlarmType(errorType);
            alarmDao.addAlarm(alarm);
        }
    }
}
