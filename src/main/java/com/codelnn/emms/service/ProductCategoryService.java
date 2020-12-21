package com.codelnn.emms.service;

import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductCategoryTreeNodeVo;
import com.codelnn.emms.vo.ProductCategoryVo;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 14:55
 **/
public interface ProductCategoryService {

    /**
     * 添加物资类别
     * @param productCategoryVo
     */
    void add(ProductCategoryVo productCategoryVo);


    /**
     * 商品分类类别
     * @param pageNum
     * @param pageSize
     * @param productCategoryVo
     * @return
     */
    PageVO<ProductCategoryVo> findProductCategoryList(Integer pageNum, Integer pageSize, ProductCategoryVo productCategoryVo);


    /**
     * 编辑物资类别
     * @param id
     * @return
     */
    ProductCategoryVo edit(Long id);

    /**
     * 更新物资类别
     * @param id
     * @param productCategoryVo
     */
    void update(Long id, ProductCategoryVo productCategoryVo);

    /**
     * 删除物资类别
     * @param id
     */
    void delete(Long id);

    /**
     * 查询所物资类别
     * @return
     */
    List<ProductCategoryVo> findAll();

    /**
     * 分类树形
     * @return
     */
    PageVO<ProductCategoryTreeNodeVo> categoryTree(Integer pageNum, Integer pageSize);

    /**
     * 获取父级分类（2级树）
     * @return
     */
    List<ProductCategoryTreeNodeVo> getParentCategoryTree();


}
