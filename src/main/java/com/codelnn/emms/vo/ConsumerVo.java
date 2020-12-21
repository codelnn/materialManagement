package com.codelnn.emms.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 11:41
 **/
@Data
public class ConsumerVo {

    private Long id;
    /**
     * 物资消费方  医院区域名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 联系电话
     */
    private String phone;
    /**
     *
     */
    private Integer sort;
    /**
     * 联系人姓名
     */
    private String contact;

}
