package com.codelnn.emms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:  入库单详情
 * @author: znx
 * @create: 2020-12-19 17:16
 **/
@Data
public class InStockDetailVo {

    private String inNum;

    private Integer status;

    private Integer type;

    private String operator;

    private SupplierVo supplierVo;
    /**
     * 总数
     */
    private long total;

    private List<InStockItemVo> itemVos=new ArrayList<>();

}
