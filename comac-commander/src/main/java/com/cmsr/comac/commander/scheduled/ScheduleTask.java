package com.cmsr.comac.commander.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cmsr.comac.commander.common.OcrResponse;
import com.cmsr.comac.commander.config.Pop;
import com.cmsr.comac.commander.config.Rg;
import com.cmsr.comac.commander.pojo.*;
import com.cmsr.comac.commander.service.*;
import com.cmsr.comac.commander.service.rest.CameraRestService;
import com.cmsr.comac.commander.util.ColorDiscernUtils;
import com.cmsr.comac.commander.util.DataUtils;
import com.cmsr.comac.commander.util.TimestampUtil;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.System.in;

/**
 * @author ztw
 * @data 2019/10/8 11:30
 * @description 定时任务
 */

@Component
@EnableScheduling
@Slf4j
public class ScheduleTask {
    /**
     * 请求算法获取数字温度仪图片的url地址
     */
    @Value("${ThermometerCamera}")
    private String thermometerCamera;
    /**
     * 视觉图片
     */
    @Value("${BASE_DIR}")
    private String baseDir;
    /**
     * 备份视觉图片保存的路径
     */
    @Value("${BASE_DIR_COPY}")
    private String baseDirCopy;

    /**
     * 视觉图片保存的路径
     */
    @Value("${Vision_Base_Dir}")
    private String visionBaseDir;
    /**
     * 备份视觉图片保存的路径
     */
    @Value("${Vision_Base_copy_Dir}")
    private String visionBaseCopyDir;

    /**
     * 截屏定时清理周期
     */
    @Value("${screen-archive-policy}")
    private Long screenArchivePolicy;

    /**
     * 油机定时清理周期
     */
    @Value("${engine-archive-policy}")
    private Long engineArchivePolicy;

    /**
     * 数字温度显示仪定时清理周期
     */
    @Value("${thermometer-archive-policy}")
    private Long thermometerArchivePolicy;

    /**
     * 告警数据定时清理周期
     */
    @Value("${alarm_archive_policy}")
    private Long alarmArchivePolicy;

    /**
     * 最大超时时间(可在.yml文件进行配置)
     */
    @Value("${maxTimeOut}")
    private long maxTimeOut;


    /**
     * 截屏业务
     */
    @Autowired
    private ScreenService screenService;

    /**
     * 截屏图片历史业务
     */
    @Autowired
    private ScreenImageHistoryService screenImageHistoryService;

    /**
     * 油机业务
     */
    @Autowired
    private EngineService engineService;

    /**
     * 油机图片历史业务
     */
    @Autowired
    private EngineImageHistoryService engineImageHistoryService;

    /**
     * 数字温度显示仪业务
     */
    @Autowired
    private ThermometerService thermometerService;

    /**
     * 数字温度显示仪图片历史业务
     */
    @Autowired
    private ThermometerImageHistoryService thermometerImageHistoryService;

    /**
     * 数字温度显示仪数据历史业务
     */
    @Autowired
    private ThermometerDataHistoryService thermometerDataHistoryService;

    /**
     * 告警数据业务
     */
    @Autowired
    private AlarmService alarmService;
    /**
     * 相机业务
     */
    @Autowired
    private CameraRestService cameraRestService;
    /**
     * rgb三原色坐标
     */
    @Autowired
    private Rg rg;
    /**
     * testLog框坐标
     */
    @Autowired
    private Pop pop;
    /**
     * 用户管理业务
     */
    @Autowired
    UserService userService;

    /**
     * 油机定时任务(暂不实现)
     *
     * @throws Exception
     */
//   @Scheduled(cron = "0/5 * * * * ?")
//    public void engineTask() throws Exception {
//        //查询所有油机列表
//        ResultInfo queryEngineListResult = engineService.queryEngineList(null);
//        List<Engine> engineList = (List<Engine>) queryEngineListResult.getData();
//        //获取每个油机对象
//        for (Engine engine : engineList) {
//            //enable字段为0,则不操作
//            if (engine.getEnable() == 0) {
//                continue;
//            }
//            //油机id
//            Integer id = engine.getId();
//            //RecognitionEnable字段为0,则只读取和保存图片,不对识别后的数据进行处理
//            if (engine.getRecognitionEnable() == 0) {
//                //调用C#获取油机图片流
////                ResultInfo cameraImageResult = cameraRestService.getCameraImage(engine.getCameraId());
////                byte[] bytes = (byte[]) cameraImageResult.getData();
//
//                //获取视觉图片
//                Map<String, Object> imageFile = DataUtils.getImageFile(baseDir + "\\engine\\" + id + "\\engine.jpg", maxTimeOut);
//                //异常，跳过
//                if (null != imageFile.get("error")) {
//                    continue;
//                }
//                //获取图片流
//                byte[] bytes = (byte[]) imageFile.get("imageByteData");
//                //时间戳
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String stringTime = sdf.format(new Date());
//                long timestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
//                //以时间戳命名
//                String fileName = "engine-" + timestamp + ".jpg";
//                //将文件写入指定位置
//                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//                BufferedImage image = ImageIO.read(in);
//
//                //将油机图片数据插入油机图片历史数据库
//                EngineImageHistory engineImageHistory = new EngineImageHistory();
//                engineImageHistory.setEngineId(id);
//                engineImageHistory.setFilename(fileName);
//                engineImageHistory.setTimestamp(timestamp);
//                engineImageHistoryService.addEngineImageHistory(engineImageHistory);
//
//                //最后保存图片
//                try {
//                    File engineImageFile = new File(visionBaseDir + "\\engine\\" + id + "\\" + fileName);
//                    ImageIO.write(image, "jpg", engineImageFile);
//                    log.info(id + "号油机图片文件已保存!");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    in.close();
//                }
//                //RecognitionEnable字段值为1,则读取和保存图片,并将文件名写入数据库
//            } else if (engine.getRecognitionEnable() == 1) {
//                //调用C#获取油机图片流
////                ResultInfo cameraImageResult = cameraRestService.getCameraImage(engine.getCameraId());
////                byte[] bytes = (byte[]) cameraImageResult.getData();
//                //获取视觉图片
//                Map<String, Object> imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
//                //异常，跳过
//                if (null != imageFile.get("error")) {
//                    continue;
//                }
//                //获取图片流
//                byte[] bytes = (byte[]) imageFile.get("imageByteData");
//                //时间戳
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String stringTime = sdf.format(new Date());
//                long timestamp = Long.parseLong(String.valueOf(sdf.parse(stringTime).getTime()));
//                //以时间戳命名
//                String fileName = "engine-" + timestamp + ".jpg";
//                //将文件写入指定位置
//                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//                BufferedImage image = ImageIO.read(in);
//
//                //将油机图片数据插入油机图片历史数据库
//                EngineImageHistory engineImageHistory = new EngineImageHistory();
//                engineImageHistory.setEngineId(id);
//                engineImageHistory.setFilename(fileName);
//                engineImageHistory.setTimestamp(timestamp);
//                engineImageHistoryService.addEngineImageHistory(engineImageHistory);
//
//                //最后保存图片
//                try {
//                    File engineImageFile = new File(visionBaseDir + "\\engine\\" + id + "\\" + fileName);
//                    ImageIO.write(image, "jpg", engineImageFile);
//                    log.info(id + "号油机图片文件已写入!");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    in.close();
//                }
//
//
//                //******获取完图片后,先不保存到本地,把图片传给C#拿到算法识别后的结果后,在保存******
//
//                //TODO 加入油机算法识别  待需求确认后开发
////                try {
////                    File engineImageFile = new File(baseDir + "\\engine\\" + id + "\\" + fileName);
////                    ImageIO.write(image, "jpg", engineImageFile);
////                    log.info(id + "号油机图片文件已写入!");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } finally {
////                    in.close();
////                }
//
//                //将油机图片数据插入油机图片历史数据库
////                EngineImageHistory engineImageHistory = new EngineImageHistory();
////                engineImageHistory.setEngineId(id);
////                engineImageHistory.setFilename(fileName);
////                engineImageHistory.setTimestamp(timestamp);
////                engineImageHistoryService.addEngineImageHistory(engineImageHistory);
//            }
//        }
//    }


    /**
     * 数字温度仪表盘定时任务
     **/
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void thermometerTask() throws Exception {
       /* //温度仪历史图片信息数据对象
        ThermometerImageHistory thermometerImageHistory = new ThermometerImageHistory();
        //温度仪温度历史数据对象
        ThermometerDataHistory thermometerDataHistory = new ThermometerDataHistory();
        //获取数字温度显示仪列表
        ResultInfo thermometerListResult = thermometerService.queryThermometerList(null);
        List<Thermometer> thermometerList = (List<Thermometer>) thermometerListResult.getData();
        //遍历获取到每个温度仪对象
        for (Thermometer thermometer : thermometerList) {
            //enable字段为0,不操作
            if (thermometer.getEnable() == 0) {
                //跳过
                continue;
            }
            Integer id = thermometer.getId();
            //RecognitionEnable字段为0,只获取和写入图片,不做其他操作
            if (thermometer.getRecognitionEnable() == 0) {
                //时间戳
                long timestamp = TimestampUtil.getTimestamp();
                //以时间戳命名
                String fileName = "thermometer-" + timestamp + ".jpg";

                //将图片历史数据写入thermometer_image_history表
                thermometerImageHistory.setThermometerId(id);
                //文件名
                thermometerImageHistory.setFilename(fileName);
                //时间戳
                thermometerImageHistory.setTimestamp(timestamp);
                //每隔5s往数字温度仪图片历史表中添加一条数据
                thermometerImageHistoryService.addThermometerImageHistory(thermometerImageHistory);*/

               /* //先获取视觉状态文件
                ResultInfo visionStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\thermometer-status.txt", maxTimeOut);
                //视觉状态文件异常
                if (!visionStatusResult.isFlag()) {
                    log.info(id + "号温度仪视觉状态文件异常!");
                    //插入告警
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, Integer.parseInt(visionStatusResult.getMsg()));
                }
                //状态
                List<String> txtData = (List<String>) visionStatusResult.getData();
                //拿到状态数值
                int thermometerStatus = Integer.parseInt(txtData.get(0));
                //视觉状态文件不为1,异常,插入告警
                if (1 != thermometerStatus) {
                    log.info(id + "号视觉状态文件数据为:" + thermometerStatus + ",状态异常!");
                    //视觉状态异常代码1001
                    int type = 1001;
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, type);
                }

                //文件写入状态
                int imageStatus;
                do {
                    ResultInfo imageStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\image_status.txt", maxTimeOut);
                    List<String> status = (List<String>) imageStatusResult.getData();
                    imageStatus = Integer.parseInt(status.get(0));
                    if (0 == imageStatus) {
                        log.info(id + "号温度仪图片正在写入...");
                    }
                } while (0 == imageStatus);

                //获取视觉图片
                ResultInfo imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
                //视觉图片存在异常,跳过
                if (!imageFile.isFlag()) {
                    log.info(id + "号温度仪图片数据异常!");
                    //插入告警数据库
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, Integer.parseInt(imageFile.getMsg()));
                    continue;
                }

                //获取图片流
                byte[] bytes = (byte[]) imageFile.getData();

                //读取到输入流
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                //将原始图片压缩并保存到指定目录
                //压缩图片长宽和质量各一半
                    Thumbnails.of(in).scale(0.5f).outputQuality(0.5f).toFile(  + "\\thermometer\\" + id + "\\" + fileName);
                    log.info(id + "号数字温度显示仪图片文件已保存!");
                    .

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                }
            }**/
    //获取图片流，压缩并保存到本地
      /*          thermometerService.queryThermometerById(id);
        }








            //RecognitionEnable字段为1,读写图片并获取视觉识别结果,插入数据库
            if (thermometer.getRecognitionEnable() == 1) {
                //时间戳
                long timestamp = TimestampUtil.getTimestamp();

                //从温度仪数据文件中读取温度仪数据
                ResultInfo thermometerDataResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\thermometer-data.txt", maxTimeOut);
                //异常，跳过
                if (!thermometerDataResult.isFlag()) {
                    log.info(id + "号温度仪温度文档数据异常!");
                    //插入告警数据库
                    thermometerDataHistoryService.addThermometerDataVisionAlarm(id, timestamp, Integer.parseInt(thermometerDataResult.getMsg()));
                    //温度仪数据文档异常,跳过后续温度仪数据处理
                    continue;
                }

                //获取温度仪温度数据列表
                List<String> thermometerData = (List<String>) thermometerDataResult.getData();
                int upperTop = Integer.parseInt(thermometerData.get(0));
                int middleTop = Integer.parseInt(thermometerData.get(2));
                int downTop = Integer.parseInt(thermometerData.get(4));

                //上段顶部数值
                thermometerDataHistory.setUpperTop(upperTop);
                //上段中部数值
                thermometerDataHistory.setUpperMiddle(Integer.parseInt(thermometerData.get(1)));
                //中段顶部数值
                thermometerDataHistory.setCentreTop(middleTop);
                //中段中部数值
                thermometerDataHistory.setCentreMiddle(Integer.parseInt(thermometerData.get(3)));
                //下段顶部数值
                thermometerDataHistory.setDownTop(downTop);
                //下段中部数值
                thermometerDataHistory.setDownMiddle(Integer.parseInt(thermometerData.get(5)));

                //以时间戳重命名
                String fileName = "thermometer-" + timestamp + "-" + upperTop + "-" + middleTop + "-" + downTop + ".jpg";

                //温度仪数据处理：判断温度是否超过指定阈值，若超过阈值则添加告警
                thermometerDataHistoryService.thermometerDataProcess(id, timestamp, fileName, thermometerDataHistory);

                //将图片历史数据写入thermometer_image_history表
                thermometerImageHistory.setThermometerId(id);
                //文件名
                thermometerImageHistory.setFilename(fileName);
                //时间戳
                thermometerImageHistory.setTimestamp(timestamp);
                thermometerImageHistoryService.addThermometerImageHistory(thermometerImageHistory);

                //先获取视觉状态文件
                ResultInfo visionStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\thermometer-status.txt", maxTimeOut);
                //视觉状态文件异常
                if (!visionStatusResult.isFlag()) {
                    log.info(id + "号温度仪视觉状态文件异常!");
                    //插入告警
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, Integer.parseInt(visionStatusResult.getMsg()));
                }
                //获取状态
                List<String> thermometerTxtData = (List<String>) visionStatusResult.getData();
                //拿到状态数值
                int thermometerStatus = Integer.parseInt(thermometerTxtData.get(0));
                //视觉状态文件不为1,异常,插入告警
                if (1 != thermometerStatus) {
                    log.info(id + "号视觉状态文件数据为:" + thermometerStatus + ",状态异常!");
                    //视觉状态异常代码1001
                    int type = 1001;
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, type);
                }

                //图片写入状态
                int imageStatus;
                do {
                    ResultInfo imageStatusResult = DataUtils.getTxtData(baseDir + "\\thermometer\\" + id + "\\image_status.txt", maxTimeOut);
                    List<String> status = (List<String>) imageStatusResult.getData();
                    imageStatus = Integer.parseInt(status.get(0));
                    if (0 == imageStatus) {
                        log.info(id + "号温度仪图片正在写入...");
                    }
                } while (0 == imageStatus);

                //获取视觉图片
                ResultInfo imageFile = DataUtils.getImageFile(baseDir + "\\thermometer\\" + id + "\\thermometer-raw.jpg", maxTimeOut);
                //视觉图片存在异常,跳过
                if (!imageFile.isFlag()) {
                    log.info(id + "号温度仪图片数据异常!");
                    //插入告警数据库
                    thermometerImageHistoryService.addThermometerVisionAlarm(id, timestamp, Integer.parseInt(imageFile.getMsg()));
                    continue;
                }

                //获取图片流
                byte[] bytes = (byte[]) imageFile.getData();

                //读取到输入流
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);

                //压缩图片长宽和质量各一半保存到视觉目录
                try {
                    Thumbnails.of(in).scale(0.5f).outputQuality(0.5f).toFile(visionBaseDir + "\\thermometer\\" + id + "\\" + fileName);
                    log.info(id + "号数字温度显示仪图片文件已保存!");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                }


            }
        }*/
//    }

    /**
     * 远程获取相机0对应的图片及数据
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void OcrTask1() {
        thermometerService.getThermometerData(0, 1, 2);
    }

    /**
     * 远程获取相机1对应的图片及数据
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void OcrTask2() {
        thermometerService.getThermometerData(1, 3, 4);
    }


    /**
     * 截屏定时任务
     *
     * @throws Exception
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void screenTask() throws Exception {
        ResultInfo queryScreenListResult = screenService.queryScreenList(null);
        List<Screen> screenList = (List<Screen>) queryScreenListResult.getData();
        ScreenImageHistory screenImageHistory = new ScreenImageHistory();
        HashMap<String, String> alarmMap = Maps.newHashMap();
        //遍历屏幕集合
        for (Screen screen : screenList) {
            //屏幕未开启不做任何处理
            if (screen.getEnable() == 0) {
                continue;
            }
            Integer id = screen.getId();
            //RecognitionEnable字段为0,只读取和保存图片
            if (screen.getRecognitionEnable() == 0) {
                //时间戳
                long timestamp = System.currentTimeMillis();
                //以时间戳重命名
                String fileName = "screen-" + timestamp + ".jpg";

                //使用restTemplate远程调用截屏程序获取截屏
                ResultInfo screenImageResult = screenService.getScreenImage(screen);
                //获取图片失败,跳过
                if (!screenImageResult.isFlag()) {
                    //截屏程序异常,获取不到图片数据
                    int errorType = Integer.parseInt(screenImageResult.getMsg());
                    //插入告警列表
                    screenService.getScreenImageAlarm(id, timestamp, errorType);
                    continue;
                }
                //获取截屏图片流
                byte[] bytes = (byte[]) screenImageResult.getData();

                //插入截屏历史数据库
                screenImageHistory.setScreenId(id);
                screenImageHistory.setFilename(fileName);
                screenImageHistory.setTimestamp(timestamp);
                screenImageHistoryService.addScreenImageHistory(screenImageHistory);
                File file = new File(visionBaseDir + "\\screen\\" + id + "\\" + fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                //保存图片
                try {
                    Files.write(bytes, file);
                    log.info(id + "号截屏图片已写入!");
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("截屏图片写入异常!");
                } finally {
                    in.close();
                }
            }
            //RecognitionEnable字段为1,启用视觉识别
            if (screen.getRecognitionEnable() == 1) {
                //时间戳
                long timestamp = TimestampUtil.getTimestamp();
                //以时间戳命名
                String fileName = "screen-" + timestamp + ".jpg";
                //调用截屏程序获取截屏
                ResultInfo screenImageResult = screenService.getScreenImage(screen);
                //获取图片失败,跳过
                if (!screenImageResult.isFlag()) {
                    int errorType = Integer.parseInt(screenImageResult.getMsg());
                    //插入告警列表
                    screenService.getScreenImageAlarm(id, timestamp, errorType);
                    continue;
                }
                //获取截屏图片流
                byte[] bytes = (byte[]) screenImageResult.getData();

                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                //读取到缓冲流
                BufferedImage image = ImageIO.read(in);

                //插入数据库
                screenImageHistory.setScreenId(id);
                screenImageHistory.setFilename(fileName);
                screenImageHistory.setTimestamp(timestamp);
                screenImageHistoryService.addScreenImageHistory(screenImageHistory);

                //截屏颜色异常判断
                //截取红绿状态框区域图片
                BufferedImage rgImage = image.getSubimage(rg.getMaps().get("x"), rg.getMaps().get("y"), rg.getMaps().get("w"), rg.getMaps().get("h"));
                //识别图片中心位置RGB值,判断是否一致
                int[] rgbData = ColorDiscernUtils.getRgbData(rgImage);
                //红绿状态框闪红,实验异常,触发告警
                if (rgbData[1] < 100) {
                    log.info("红绿状态框为红色,实验异常!");
                    alarmMap.put("alarm", "rg");
                    //将屏幕告警信息存入数据库并保存告警图片
                    screenService.addScreenAlarm(id, timestamp, fileName, alarmMap, bytes);
                }

                //截取弹框区域图片
                BufferedImage popImage = image.getSubimage(pop.getMaps().get("x"), pop.getMaps().get("y"), pop.getMaps().get("w"), pop.getMaps().get("h"));
                //获取弹框抠图后中心点像素的RGB值
                int[] popRgbData = ColorDiscernUtils.getRgbData(popImage);
                //存在testLog弹框,实验异常,触发告警
                if (popRgbData[0] < 230) {
                    log.info("testLog框弹出,实验异常!");
                    alarmMap.put("alarm", "pop");
                    //将屏幕告警信息存入数据库并保存告警图片
                    screenService.addScreenAlarm(id, timestamp, fileName, alarmMap, bytes);
                }
                File file = new File(visionBaseDir + "\\screen\\" + id + "\\" + fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Files.write(bytes, file);
                    log.info(id + "号截屏图片文件已写入!");
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("截屏图片写入异常!");
                } finally {
                    in.close();
                }
            }
        }
    }

    /**
     * 清理油机过期数据(暂不实现)
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    public void cleanEngineTask() {
        //获取油机清理周期
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(engineArchivePolicy, ChronoUnit.DAYS);
        //获取N天之前的当前时间的时间戳
        long timestamp = Timestamp.valueOf(now).getTime();
        //筛选数据库中N天以前的所有数据
        List<EngineImageHistory> engineImageHistoryList = engineImageHistoryService.queryEngineImageHistory(timestamp);
        for (EngineImageHistory engineImageHistory : engineImageHistoryList) {
            //油机id
            Integer engineId = engineImageHistory.getEngineId();
            //油机图片名
            String filename = engineImageHistory.getFilename();
            //油机图片保存的路径
            File engineImageFile = new File(baseDir + "\\engine\\" + engineId + "\\" + filename);
            if (engineImageFile.exists()) {
                engineImageFile.delete();
            }
        }
        log.info("油机图片数据清理完成!");
        engineImageHistoryService.deleteEngineImageHistory(timestamp);
        log.info("油机图片历史数据库清理完成!");
    }

    /**
     * 清理数字温度仪过期数据
     */
//    @Scheduled(cron = "0 0/10 * * * ?")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void cleanThermometerTask() {
        //获取数字温度显示仪清理周期
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(thermometerArchivePolicy, ChronoUnit.DAYS);
        //获取N天之前的当前时间的时间戳
        long timestamp = Timestamp.valueOf(now).getTime();
        //筛选数据库中N天以前的所有数据
        List<ThermometerImageHistory> thermometerImageHistoryList = thermometerImageHistoryService.queryThermometerImageHistory(timestamp);
        for (ThermometerImageHistory thermometer : thermometerImageHistoryList) {
            //数字温度显示仪id
            Integer thermometerId = thermometer.getThermometerId();
            //数字温度显示仪图片文件名
            String filename = thermometer.getFilename();
            //数字温度显示仪图片保存的路径
            File thermometerImageFile = new File(baseDir + "\\thermometer\\" + thermometerId + "\\" + filename);
            if (thermometerImageFile.exists()) {
                //过期截屏图片的备份
//                try {
//                    Files.copy(thermometerImageFile, new File(baseDirCopy + "\\thermometer\\" + thermometerId + "\\" + filename));
//                } catch (IOException e) {
//                    log.info("过期数字温度仪图片的备份异常");
//                }
                //删除
                thermometerImageFile.delete();
            }
        }
        //备份过期温度仪图片数据
//        thermometerImageHistoryService.copyThermometerImageHistory(timestamp);
        //过期温度仪温度数据库
//        thermometerDataHistoryService.copyThermometerDataHistory(timestamp);
        log.info("清理过期温度仪图片数据...");
        thermometerImageHistoryService.deleteThermometerImageHistory(timestamp);
        log.info("清理过期温度仪图片历史数据库...");
        thermometerDataHistoryService.deleteThermometerDataHistory(timestamp);
        log.info("清理过期温度仪温度数据库...");
    }

    /**
     * 备份并清理截屏过期数据
     * 每周周末晚上12点触发备份和清除操作
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void copyAndCleanScreenTask() {
        //获取截屏清理周期
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(screenArchivePolicy, ChronoUnit.DAYS);
        //获取N天之前的当前时间的时间戳
        long timestamp = Timestamp.valueOf(now).getTime();
        //筛选数据库中N天以前的所有数据
        List<ScreenImageHistory> screenImageHistoryList = screenImageHistoryService.queryScreenImageHistory(timestamp);
        for (ScreenImageHistory screen : screenImageHistoryList) {
            //截屏id
            Integer screenId = screen.getScreenId();
            //截屏图片文件名
            String filename = screen.getFilename();
            //截屏图片保存的路径
            File screenImageFile = new File(visionBaseDir + "\\screen\\" + screenId + "\\" + filename);
            if (screenImageFile.exists()) {
                //过期截屏图片的备份
//                try {
//                    Files.copy(screenImageFile, new File(visionBaseCopyDir + "\\screen\\" + screenId + "\\" + filename));
//                } catch (IOException e) {
//                    log.info("过期截屏图片的备份异常");
//                }
                //过期截屏图片的删除
                screenImageFile.delete();
                log.info("清理过期截屏图片...");
            }
        }
        //数据库过期历史截屏数据的备份
//        screenImageHistoryService.copyScreenImageHistory(timestamp);
//        log.info("备份过期截屏图片历史数据库...");
        //数据库过期历史截屏数据的删除
        screenImageHistoryService.deleteScreenImageHistory(timestamp);
        log.info("清理过期截屏图片历史数据库...");
    }

    /**
     * 过期告警数据备份和定时清理
     */
//    @Scheduled(cron = "0 0/30 * * * ?")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void cleanAlarmTask() {
        //获取截屏清理周期
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(alarmArchivePolicy, ChronoUnit.DAYS);
        //获取N天之前的当前时间的时间戳
        long timestamp = Timestamp.valueOf(now).getTime();
        //备份过期告警数据
//        alarmService.copyAlarmByTimestamp(timestamp);
        //清除过期告警数据
        alarmService.deleteAlarmByTimestamp(timestamp);
        log.info("清理过期告警数据...");
    }

    /**
     * 定时更新用户信息
     * 每天24点更新用户信息
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    public void updateUserInfo() throws JSONException {
        //从商发拉取用户信息
        //获取用户列表
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResponseVO> responseEntity = new RestTemplate().exchange("http://10.43.0.90:8000/aecc/ldap/get_user_list/", HttpMethod.GET, entity, ResponseVO.class);
        ResponseVO responseVO = responseEntity.getBody();
        //拉取用户列表成功
        if (responseVO.is_success()) {
            List<UsersBean> users = responseVO.getUsers();
            List<UsersBean> usersPressence = new ArrayList<>();
            //用户存在更则新，不存在则新增
            users.forEach(usersBean -> {
                ResultInfo resultInfo = userService.queryUserByUserName(usersBean.getName());
                //如果存在则存入usersPressence中
                if (resultInfo.isFlag()) {
                    usersPressence.add(usersBean);
                }
                //如果用户不存在则从users中去除
                else {
                    users.remove(usersBean);
                }
            });
            if (!CollectionUtils.isEmpty(users)) {
                //不存在用户批量存入数据库
                ResultInfo addResultinfo = userService.addUserNotPressence(users);
                log.info("用户添加{}", addResultinfo.isFlag() ? "成功" : "失败");
            }
        }

    }
}
