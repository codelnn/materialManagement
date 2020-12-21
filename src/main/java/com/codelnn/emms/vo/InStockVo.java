package com.codelnn.emms.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 17:14
 **/
@Data
public class InStockVo {

    private Long id;

    private String inNum;

    private Integer type;

    private String operator;

    private Long supplierId;

    private String supplierName;

    private Date createTime;

    private Date modified;

    /** 该入库单的总数**/
    private Integer productNumber;

    private String remark;

    private List<Object> products = new ArrayList<>();

    private Integer status;

    private Date startTime;

    private Date endTime;


    /** 如果supplierId不存在需要添加供应商信息**/

    private String name;

    private String address;

    private String email;

    private String phone;

    private Integer sort;

    private String contact;

}
