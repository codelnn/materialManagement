package com.codelnn.emms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 15:06
 **/
@Data
@TableName(value = "tb_menu")
public class MenuEntity {

    private Long id;
    private Long parentId;
    private String menuName;
    private String url;
    private String perms;
    private String icon;
    private Integer type;
    private Long orderNum;
    private Integer open;
    private Integer available;

}
