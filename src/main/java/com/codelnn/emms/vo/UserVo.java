package com.codelnn.emms.vo;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;


/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 21:24
 **/
@ToString
@Data
@Excel("userVo")
public class UserVo {

    @ExcelField(value = "编号", width = 50)
    private Long id;
    /**
     * 用户名
     */
    @ExcelField(value = "用户名", width = 100)
    private String username;
    /**
     * 昵称
     */
    @ExcelField(value = "昵称", width = 100)
    private String nickname;
    /**
     * 邮箱
     */
    @ExcelField(value = "邮箱", width = 150)
    private String email;
    /**
     * 头像
     */
    @ExcelField(value = "头像url", width = 500)
    private String avatar;
    /**
     * 联系电话
     */
    @ExcelField(value = "电话号码", width = 100)
    private String phoneNumber;
    /**
     *  状态 0锁定 1有效
     */
    private Boolean status;

    /**
     * 性别 0男 1女 2保密
     */
    @ExcelField(//
            value = "性别",
            readConverterExp = "男=1,女=0",
            writeConverterExp = "1=男,0=女"
            ,width = 50
    )
    private Integer sex;

}
