package com.cmsr.comac.commander.controller;

/**
 * @author ZQ
 * @create 2020-08-05
 * @description
 */

import com.cmsr.comac.commander.util.DownloadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class FileDownloadController {
    //文件名
    String FILENAME = "美女.jpg";
    //下载展示的文件名
    String NEWNAME = "美女.jpg";

    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object downloadModel() {
        ResponseEntity<InputStreamResource> response = null;
        try {
            response = DownloadFileUtil.download(FILENAME, NEWNAME);
        } catch (Exception e) {
            log.error("文件下载失败");
        }
        return response;
    }
}
