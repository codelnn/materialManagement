package com.codelnn.emms.vo;

import lombok.Data;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 17:16
 **/
@Data
public class InStockItemVo {

    private Long id;

    private String pNum;

    private String name;

    private String model;

    private String unit;

    private String imageUrl;

    private int count;
}
