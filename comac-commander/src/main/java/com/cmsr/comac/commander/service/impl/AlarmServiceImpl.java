package com.cmsr.comac.commander.service.impl;


import com.cmsr.comac.commander.dao.AlarmDao;
import com.cmsr.comac.commander.pojo.Alarm;
import com.cmsr.comac.commander.pojo.PageBean;
import com.cmsr.comac.commander.pojo.ResultInfo;
import com.cmsr.comac.commander.service.AlarmService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author ztw
 * @data 2019/9/24 20:50
 * @description 告警实现
 */

@Service
public class AlarmServiceImpl implements AlarmService {
    /**
     * 视觉图片保存的路径
     */
    @Value("${Vision_Base_Dir}")
    private String visionBaseDir;

    /**
     * 视觉图片,TXT存放路径
     */
    @Value("${BASE_DIR}")
    private String baseDir;
    /**
     * 告警dao
     */
    @Autowired
    private AlarmDao alarmDao;

    /**
     * 按条件查询所有告警信息
     *
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @param status    状态
     * @param target    类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 所有告警信息
     */
    @Override
    public ResultInfo queryAlarmList(Integer pageNum, Integer pageSize, Integer status, String target, Long startTime, Long endTime) {
        PageBean<Alarm> alarmPageData = new PageBean<>();
        //设置当前页
        alarmPageData.setPageNum(pageNum);
        //每页大小
        alarmPageData.setPageSize(pageSize);
        //调用分页助手
        PageHelper.startPage(pageNum, pageSize);

        //查询数据库
        List<Alarm> alarmList = alarmDao.queryAlarmList(status, target, startTime, endTime);

        if (CollectionUtils.isEmpty(alarmList)) {
            return new ResultInfo(false, "不存在此类告警信息!");
        }

        //分页结果数据
        return new ResultInfo(true, new PageInfo<>(alarmList));
    }

    /**
     * 根据id查询告警详情
     *
     * @param alarmId 告警id
     * @return 告警详情
     */
    @Override
    public ResultInfo queryAlarmByAlarmId(Long alarmId) {
        Alarm alarm = alarmDao.queryAlarmByAlarmId(alarmId);
        if (null == alarm) {
            return new ResultInfo(false, "无此告警信息!");
        }
        return new ResultInfo(true, alarm);
    }


    /**
     * 关闭某个告警
     *
     * @param id        告警id
     * @param statusMap 状态map
     * @return result
     */
    @Override
    public ResultInfo updateAlarmStatusToOff(Long id, Map<String, Integer> statusMap, String username) {
        Integer status = statusMap.get("status");
        //参数值必须是2
        if (2 != status) {
            return new ResultInfo(false, "告警状态值设置有误！");
        }
        //获取告警对象
        Alarm alarm = alarmDao.queryAlarmByAlarmId(id);

        if (null == alarm) {
            return new ResultInfo(false, "此告警信息不存在!");
        }

        //告警已关闭
        if (2 == alarm.getAlarmStatus()) {
            return new ResultInfo(false, "此告警状态已是关闭状态！");
        }

        //关闭告警
        try {
            alarmDao.updateAlarmStatusById(id, status, username);
            return new ResultInfo(true, "告警状态已关闭!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false, "服务器异常!");
        }
    }

    /**
     * 新增告警
     *
     * @param alarm 告警
     */
    @Override
    public void addAlarm(Alarm alarm) {
        alarmDao.addAlarm(alarm);
    }

    /**
     * 获取某个告警关联的原始图片
     *
     * @param alarm 告警
     * @return 某个告警关联的原始图片
     */
    @Override
    public ResultInfo getAlarmConnectImage(Alarm alarm) throws Exception {
        //告警目标
        String alarmTarget = alarm.getAlarmTarget();
        //告警目标的id
        Integer alarmTargetId = alarm.getAlarmTargetId();
        //告警图片名
        String alarmPicName = alarm.getAlarmPicName();
        //获取到告警图片
        File alarmImageFile = new File(visionBaseDir + "\\" + alarmTarget + "-alarm"+"\\" + alarmTargetId + "\\" + alarmPicName);
        if (!alarmImageFile.exists()) {
            return new ResultInfo(false, "不存在此告警图片！");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //缓冲流
        BufferedImage image = ImageIO.read(alarmImageFile);
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return new ResultInfo(true, bytes);
    }

    /**
     * 删除告警过期数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void deleteAlarmByTimestamp(long timestamp) {
        alarmDao.deleteAlarmByTimestamp(timestamp);
    }
    /**
     * 备份告警过期数据
     *
     * @param timestamp 时间戳
     */
    @Override
    public void copyAlarmByTimestamp(long timestamp) {
        alarmDao.copyAlarmByTimestamp(timestamp);

    }

}
