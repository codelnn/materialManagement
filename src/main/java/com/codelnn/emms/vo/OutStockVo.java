package com.codelnn.emms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 20:11
 **/
@Data
public class OutStockVo {

    private Long id;

    private String outNum;

    private Integer type;

    private String operator;

    private Date createTime;

    private Integer productNumber;

    private Integer priority;


    //发放的物资列表
    private List<Object> products = new ArrayList<>();

    private String remark;

    //发放单的状态
    private Integer status;


    /*** 如果consumerId不为空**/

    private Long consumerId;

    //去向名
    private String name;

    //联系电话
    private String phone;

    //联系人
    private String contact;

    //排序
    private Integer sort;
}
