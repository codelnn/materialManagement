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
@TableName("TB_IN_STOCK")
@KeySequence(value = "seq_tb_in_stock", clazz = Long.class)
public class InStockEntity implements Serializable {
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
	 * 类型：1：捐赠，2：下拨，3：采购,4:退货入库
	 */
	private Integer type;
	/**
	 * 操作人员
	 */
	private String operator;
	/**
	 * 入库单创建时间
	 */
	private Date createTime;
	/**
	 * 入库单修改时间
	 */
	private Date modifiedTime;
	/**
	 * 物资总数
	 */
	private Integer productNumber;
	/**
	 * 来源
	 */
	private Long supplierId;
	/**
	 * 描述信息
	 */
	private String remark;
	/**
	 * 0:正常入库单,1:已进入回收,2:等待审核
	 */
	private Integer status;

}
