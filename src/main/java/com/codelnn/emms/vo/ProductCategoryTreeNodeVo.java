package com.codelnn.emms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 14:59
 **/
@Data
public class ProductCategoryTreeNodeVo {
    private Long id;

    private String name;

    private String remark;

    private Integer sort;

    private Date createTime;

    private Date modifiedTime;

    private Long pid;

    private int lev;

    private List<ProductCategoryTreeNodeVo> children;

    /*
     * 排序,根据order排序
     */
    public static Comparator<ProductCategoryTreeNodeVo> order(){
        Comparator<ProductCategoryTreeNodeVo> comparator = (o1, o2) -> {
            if(o1.getSort() != o2.getSort()){
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
