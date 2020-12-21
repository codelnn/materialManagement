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
@TableName("TB_OUT_STOCK")
@KeySequence(value = "seq_tb_out_stock", clazz = Long.class)
public class OutStockEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	private Long id;
	/**
	 * 出库单
	 */
	private String outNum;
	/**
	 * 出库类型:0:直接出库,1:审核出库
	 */
	private Integer type;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 出库时间
	 */
	private Date createTime;
	/**
	 * 出库总数
	 */
	private Integer productNumber;
	/**
	 * 消费者id
	 */
	private Long consumerId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态:0:正常入库,1:已进入回收,2:等待审核
	 */
	private Integer status;
	/**
	 * 紧急程度:1:不急,2:常规,3:紧急4:特急
	 */
	private Integer priority;

}
