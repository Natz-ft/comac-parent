package com.cmsr.comac.commander.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author ZQ
 * @create 2020-08-05
 * @description
 */
@Slf4j
public class DownloadFileUtil {

    public static final String separator = File.separator;

    /**
     * 下载样表
     *
     * @param fileName 文件名
     * @param newName  下载的展示文件名
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String fileName, String newName) {
        String route = "document" + separator;
        String path = null;
        ResponseEntity<InputStreamResource> response = null;

        try {
            path = route + separator + fileName;
            ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream = classPathResource.getInputStream();
            //File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            headers.add("Content-Disposition",
//                    "attachment; filename="
//                            + new String(newName.getBytes("utf-8"), "iso8859-1"));
            headers.add("Content-Disposition",
                    "attachment; filename="
                            + URLEncoder.encode(fileName,"utf-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e1) {
            log.error("找不到指定的文件", e1);
        } catch (IOException e) {
            log.error("获取不到文件流", e);
        }
        return response;
    }
}

