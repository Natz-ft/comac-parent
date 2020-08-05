package com.cmsr.comac.commander.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZQ
 * @create 2020-07-24
 * @description 温度仪算法识别结果封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrResponse {
    private List<Integer> result1;
    private String image1;
    private List<Integer> result2;
    private String image2;
}
