package com.cmsr.comac.commander.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersBean {

    private String distinguishedName;
    private String displayName;
    private String description;
    private String title;
    private String name;
    private String manager;
    private String mail;
    private String cn;

}

