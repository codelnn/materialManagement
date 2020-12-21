package com.codelnn.emms.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 20:40
 **/
@Data
public class SupplierVo {

    private Long id;
    /**
     * 供应商名称
     */
    private String name;
    /**
     * 供应商地址
     */
    private String address;
    /**
     * 供应商邮箱
     */
    private String email;
    /**
     * 供应商电话
     */
    private String phone;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 联系人
     */
    private String contact;


}
