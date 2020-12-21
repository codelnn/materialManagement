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
@TableName("TB_PRODUCT_CATEGORY")
@KeySequence(value = "seq_tb_product_category", clazz = Long.class)
public class ProductCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类别id
	 */
	@TableId(value = "ID",type = IdType.INPUT)
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
