package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZQ
 * @create 2020-07-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsaToken {
    String ip;
    Long timestamp;
}
