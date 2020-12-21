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
@TableName("TB_PRODUCT_STOCK")
@KeySequence(value = "seq_tb_product_stock", clazz = Long.class)
public class ProductStockEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	private Long id;
	/**
	 * 商品编号
	 */
	private String pNum;
	/**
	 * 商品库存结余
	 */
	private Long stock;

}
