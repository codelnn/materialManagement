package com.codelnn.emms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 20:18
 **/
@Data
public class OutStockDetailVo {

    private String outNum;

    private Integer status;

    private Integer type;

    private String operator;

    private ConsumerVo consumerVo;
    /**
     * 总数
     */
    private long total;

    private List<OutStockItemVo> itemVos=new ArrayList<>();
}
