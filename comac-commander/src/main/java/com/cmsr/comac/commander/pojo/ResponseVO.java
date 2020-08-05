package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO {

    /**
     * is_success : true
     * users : [{"distinguishedName":"CN=wp,OU=Dev2,OU=Shanghai,OU=Users,DC=corp,DC=test,DC=net,DC=cn","displayName":"Peng.Wang","description":"test FTE","title":"Developer","name":"Peng.Wang","manager":"CN=Elton Zhang,OU=Partner,OU=Users,OU=test,DC=corp,DC=wedo,DC=net,DC=cn","mail":"peng.wang@test.Com","cn":"Peng.Wang"},"..."]
     * error_msg :
     * traceback :
     */

    private boolean is_success;
    private String error_msg;
    private String traceback;
    private List<UsersBean> users;

}
