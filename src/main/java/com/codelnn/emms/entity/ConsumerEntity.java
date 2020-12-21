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
@TableName("TB_CONSUMER")
@KeySequence(value = "seq_tb_consumer", clazz = Long.class)
public class ConsumerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "ID",type = IdType.INPUT)
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
