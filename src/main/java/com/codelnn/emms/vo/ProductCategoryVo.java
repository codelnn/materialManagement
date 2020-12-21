package com.codelnn.emms.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 14:56
 **/
@Data
public class ProductCategoryVo {

    private Long id;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifiedTime;
    /**
     * 父级分类id
     */
    private Long pid;
}
