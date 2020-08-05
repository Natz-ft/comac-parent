package com.cmsr.comac.commander.service.impl;

import com.cmsr.comac.commander.common.ExperimentActionEnum;
import com.cmsr.comac.commander.dao.EngineDao;
import com.cmsr.comac.commander.dao.EngineImageHistoryDao;
import com.cmsr.comac.commander.dao.ExperimentDao;
import com.cmsr.comac.commander.pojo.*;
import com.cmsr.comac.commander.service.EngineService;
import com.cmsr.comac.commander.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/29 10:17
 * @description 油机实现
 */
@Slf4j
@Service
public class EngineServiceImpl implements EngineService {
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
     * 静态token
     */
    @Value("${screenToken}")
    private String screenToken;

    /**
     * 最大超时时间(可在.yml文件进行配置)
     */
    @Value("${maxTimeOut}")
    private long maxTimeOut;
    /**
     * 油机dao
     */
    @Autowired
    private EngineDao engineDao;
    /**
     * 实验开关dao
     */
    @Autowired
    private ExperimentDao experimentDao;
    /**
     * 油机图片历史数据dao
     */
    @Autowired
    private EngineImageHistoryDao engineImageHistoryDao;


    /**
     * 查询油机列表
     *
     * @param location 实验室
     * @return 油机列表
     */
    @Override
    public ResultInfo queryEngineList(String location) {
        List<Engine> engineList = engineDao.queryEngineList(location);
        if (CollectionUtils.isEmpty(engineList)) {
            return new ResultInfo(false, "无此油机数据!");
        }
        return new ResultInfo(true, engineList);
    }

    /**
     * 获取油机图片
     *
     * @return 油机图片
     */
    @Override
    public ResultInfo queryEngineById(Integer id) throws Exception {
        Engine engine = engineDao.queryEngineById(id);
        if (null == engine) {
            return new ResultInfo(false, "此油机数据不存在!");
        }

        //实验总开关
        Experiment experiment = experimentDao.getExperimentActionById(id);
        if (0 == experiment.getEnable()) {
            return new ResultInfo(false, "请先开启本条实验开关!");
        }

        //时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringTime = sdf.format(new Date());
        //当前时间戳(精确到毫秒)
        long nowTimestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
        //获取最近一次保存的油机图片历史数据
        EngineImageHistory engineImageHistory = engineImageHistoryDao.queryEngineImageHistoryByEngineId(id, nowTimestamp);
        //超时
        if (null == engineImageHistory) {
            return new ResultInfo(false, id + "号油机图片历史数据不存在!");
        }
        //文件名
        String filename = engineImageHistory.getFilename();
        //获取文件
        File engineImageFile = new File(visionBaseDir + "\\engine\\" + id + "\\" + filename);
        //不存在
        if (!engineImageFile.exists()) {
            log.info(id + "号油机图片文件：" + filename + ",不存在！");
            return new ResultInfo(false, "不存在此油机图片数据！");
        }
        long oldLen = 0;
        do {
            //获取一次文件长度
            oldLen = engineImageFile.length();
            //长度不一致,睡眠100毫秒,继续获取文件长度
            Thread.sleep(10);
        } while (oldLen != engineImageFile.length());
        //输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(engineImageFile);
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return new ResultInfo(true, bytes);
    }

    /**
     * 关闭某个油机的采集
     *
     * @param id        油机id
     * @param actionMap 功能map
     * @return result
     */
    @Override
    public ResultInfo updateEngineEnableOrRecognitionEnableToOff(Integer id, Map<String, String> actionMap) {
        String action = actionMap.get("action");
        if (ExperimentActionEnum.DISABLE.getAction().equals(action)) {
            //关闭油机采集
            try {
                engineDao.updateEngineEnableToOff(id);
                return new ResultInfo(true, "油机功能已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭油机功能失败!");
            }
        } else if (ExperimentActionEnum.RECOGNITION_DISABLE.getAction().equals(action)) {
            //关闭油机视觉识别
            try {
                engineDao.updateEngineRecognitionEnableToOff(id);
                return new ResultInfo(true, "油机视觉识别已关闭!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "关闭视觉识别失败!");
            }
        } else if (ExperimentActionEnum.ENABLE.getAction().equals(action)) {
            //开启油机采集
            try {
                engineDao.updateEngineEnableToOn(id);
                return new ResultInfo(true, "油机功能已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启油机采集失败!");
            }
        } else if (ExperimentActionEnum.RECOGNITION_ENABLE.getAction().equals(action)) {
            //开启油机视觉识别
            try {
                engineDao.updateEngineRecognitionEnableToOn(id);
                return new ResultInfo(true, "油机视觉识别已开启!");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultInfo(false, "开启油机视觉识别失败!");
            }
        } else {
            return new ResultInfo(false, "参数错误!");
        }
    }
}
