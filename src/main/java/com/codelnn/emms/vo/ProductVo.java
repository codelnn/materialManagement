package com.codelnn.emms.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:24
 **/
@ToString
@Data
public class ProductVo {

    private Long id;
    /**
     * 商品编号
     */
    private String pNum;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 图片
     */
    private String imageUrl;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 计算单位
     */
    private String unit;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;

    /**
     * 分类
     */
    private Long[] categoryKeys;

    /**
     * 1级分类
     */
    private Long oneCategoryId;
    /**
     * 2级分类
     */
    private Long twoCategoryId;
    /**
     * 3级分类
     */
    private Long threeCategoryId;

    /**
     * 是否删除:1物资正常,0:物资回收,2:物资审核中
     */
    private Integer status;
}
