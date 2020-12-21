package com.codelnn.emms.vo;

import lombok.Data;

/**
 * @description:  物质库存vo
 * @author: znx
 * @create: 2020-12-19 12:28
 **/
@Data
public class ProductStockVo {
    private Long id;

    private String name;

    private String pNum;

    private String model;

    private String unit;

    private String remark;

    private Long stock;

    private String imageUrl;
}
