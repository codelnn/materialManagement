package com.codelnn.emms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * @author codelnn
 * @email 2543703057@qq.com
 * @date 2020-12-16 17:31:08
 */
@Data
@TableName("TB_USER")
@KeySequence(value = "seq_tb_user", clazz = Long.class)
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 联系电话
	 */
	private String phoneNumber;
	/**
	 *  状态 0锁定 1有效
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date modifiedTime;
	/**
	 * 性别 0男 1女 2保密
	 */
	private Integer sex;
	/**
	 * 盐 用于密码加密
	 */
	private String salt;
	/**
	 * 密码
	 */
	private String password;

}
