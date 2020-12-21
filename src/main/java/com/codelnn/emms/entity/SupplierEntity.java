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
 * @date 2020-12-16 17:31:08
 */
@Data
@TableName("TB_SUPPLIER")
@KeySequence(value = "seq_tb_supplier", clazz = Long.class)
public class SupplierEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	private Long id;
	/**
	 * 供应商名称
	 */
	private String name;
	/**
	 * 供应商地址
	 */
	private String address;
	/**
	 * 供应商邮箱
	 */
	private String email;
	/**
	 * 供应商电话
	 */
	private String phone;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date modifiedTime;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 联系人
	 */
	private String contact;

}
