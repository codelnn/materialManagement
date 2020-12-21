package com.codelnn.emms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 21:23
 **/
@Data
public class PageVO<T> {
    private long total;

    private List<T> rows = new ArrayList<>();

    public PageVO(long total, List<T> data) {
        this.total = total;
        this.rows = data;
    }
}
