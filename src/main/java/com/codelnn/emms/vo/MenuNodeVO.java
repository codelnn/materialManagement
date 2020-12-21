package com.codelnn.emms.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-17 14:22
 **/
@ToString
@Data
public class MenuNodeVO {

    private Long id;

    private Long parentId;

    private String menuName;

    private String url=null;

    private String icon;

    private Long orderNum;

    private Integer open;

    private boolean disabled;

    private Integer type;

    private List<MenuNodeVO> children=new ArrayList<>();

    /*
     * 排序,根据order排序
     */
    public static Comparator<MenuNodeVO> order(){
        Comparator<MenuNodeVO> comparator = (o1, o2) -> {
            if(o1.getOrderNum() != o2.getOrderNum()){
                return (int) (o1.getOrderNum() - o2.getOrderNum());
            }
            return 0;
        };
        return comparator;
    }
}
