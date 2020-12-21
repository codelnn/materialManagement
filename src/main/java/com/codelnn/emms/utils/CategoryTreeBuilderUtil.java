package com.codelnn.emms.utils;

import com.codelnn.emms.vo.ProductCategoryTreeNodeVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 15:48
 **/
public class CategoryTreeBuilderUtil {

    public static List<ProductCategoryTreeNodeVo> build(List<ProductCategoryTreeNodeVo> nodes){
        //根节点
        List<ProductCategoryTreeNodeVo> root = new ArrayList<>();
        for (ProductCategoryTreeNodeVo nav : nodes) {
            if(nav.getPid()==0){
                nav.setLev(1);
                root.add(nav);
            }
        }

        Collections.sort(root,ProductCategoryTreeNodeVo.order());
        for (ProductCategoryTreeNodeVo productCategoryTreeNodeVo : root) {
            List<ProductCategoryTreeNodeVo> child = getChild(productCategoryTreeNodeVo, nodes);
            productCategoryTreeNodeVo.setChildren(child);
        }
        return root;
    }

    private static List<ProductCategoryTreeNodeVo> getChild(ProductCategoryTreeNodeVo pNode,List<ProductCategoryTreeNodeVo> nodes){
        //子菜单
        List<ProductCategoryTreeNodeVo> childList = new ArrayList<>();
        for (ProductCategoryTreeNodeVo nav : nodes) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getPid().equals(pNode.getId())){
                nav.setLev(pNode.getLev()+1);
                childList.add(nav);
            }
        }
        //递归
        for (ProductCategoryTreeNodeVo nav : childList) {
            nav.setChildren(getChild(nav, nodes));
        }
        //排序
        Collections.sort(childList,ProductCategoryTreeNodeVo.order());
        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return null;
        }
        return childList;
    }


    //    获取二级父级分类

    public static List<ProductCategoryTreeNodeVo> buildParent(List<ProductCategoryTreeNodeVo> nodes) {
        //根节点
        List<ProductCategoryTreeNodeVo> rootMenu = new ArrayList<>();
        for (ProductCategoryTreeNodeVo nav : nodes) {
            if(nav.getPid()==0){
                nav.setLev(1);
                rootMenu.add(nav);
            }
        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootMenu,ProductCategoryTreeNodeVo.order());
        /*为根菜单设置子菜单，getChild是递归调用的*/
        for (ProductCategoryTreeNodeVo nav : rootMenu) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<ProductCategoryTreeNodeVo> childList = getParentChild(nav, nodes);
            //给根节点设置子节点
            nav.setChildren(childList);
        }
        return rootMenu;
    }

    private static List<ProductCategoryTreeNodeVo> getParentChild(ProductCategoryTreeNodeVo pNode, List<ProductCategoryTreeNodeVo> nodes) {
        //子菜单
        List<ProductCategoryTreeNodeVo> childList = new ArrayList<>();
        for (ProductCategoryTreeNodeVo nav : nodes) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getPid().equals(pNode.getId())){
                nav.setLev(2);
                childList.add(nav);
            }
        }
        return childList;
    }







}
