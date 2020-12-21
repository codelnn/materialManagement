package com.codelnn.emms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author codelnn
 * @email 2543703057@qq.com
 * @date 2020-12-16 17:38:04
 */
@Data
@TableName("TB_PRODUCT")
@KeySequence(value = "seq_tb_product", clazz = Long.class)
public class ProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "ID",type = IdType.INPUT)
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
	 * 是否删除:0物资正常,1:物资回收,2:物资审核中
	 */
	private Integer status;

}
