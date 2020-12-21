package com.codelnn.emms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author codelnn
 * @email 2543703057@qq.com
 * @date 2020-12-16 17:38:04
 */
@Data
@TableName("TB_IN_STOCK_INFO")
@KeySequence(value = "seq_tb_in_stock_info", clazz = Long.class)
public class InStockInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	private Long id;
	/**
	 * 入库单编号
	 */
	private String inNum;
	/**
	 * 商品编号
	 */
	private String pNum;
	/**
	 * 数量
	 */
	private Integer productNumber;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date modifiedTime;

}
