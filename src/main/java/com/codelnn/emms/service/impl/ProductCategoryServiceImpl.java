package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.ProductCategoryDao;
import com.codelnn.emms.dao.ProductDao;
import com.codelnn.emms.entity.ProductCategoryEntity;
import com.codelnn.emms.entity.ProductEntity;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.ProductCategoryService;
import com.codelnn.emms.utils.CategoryTreeBuilderUtil;
import com.codelnn.emms.utils.ListPageUtil;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductCategoryTreeNodeVo;
import com.codelnn.emms.vo.ProductCategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 15:15
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 添加商品类别
     * @param productCategoryVo
     */
    @Override
    public void add(ProductCategoryVo productCategoryVo) {
        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        BeanUtils.copyProperties(productCategoryVo,productCategoryEntity);
        productCategoryEntity.setCreateTime(new Date());
        productCategoryEntity.setModifiedTime(new Date());
        productCategoryDao.insert(productCategoryEntity);
    }

    /**
     * 商品类别列表
     * @param pageNum
     * @param pageSize
     * @param productCategoryVo
     * @return
     */
    @Override
    public PageVO<ProductCategoryVo> findProductCategoryList(Integer pageNum, Integer pageSize, ProductCategoryVo productCategoryVo) {
        Page<ProductCategoryEntity> productCategoryEntityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductCategoryEntity>  queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        String name = productCategoryVo.getName();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        Page<ProductCategoryEntity> categoryEntityPage = productCategoryDao.selectPage(productCategoryEntityPage, queryWrapper);
        List<ProductCategoryEntity> records = categoryEntityPage.getRecords();
        List<ProductCategoryVo> collect = records.stream().map(productCategoryEntity -> {
            ProductCategoryVo productCategoryVo1 = new ProductCategoryVo();
            BeanUtils.copyProperties(productCategoryEntity, productCategoryVo);
            return productCategoryVo;
        }).collect(Collectors.toList());
        return new PageVO<>(categoryEntityPage.getTotal(),collect);
    }

    @Override
    public ProductCategoryVo edit(Long id) {
        ProductCategoryEntity productCategoryEntity = productCategoryDao.selectById(id);
        if (productCategoryEntity == null){
            throw new ServiceException("该id的分类不存在");
        }
        ProductCategoryVo productCategoryVo = new ProductCategoryVo();
        BeanUtils.copyProperties(productCategoryEntity,productCategoryVo);
        return productCategoryVo;
    }

    @Override
    public void update(Long id, ProductCategoryVo productCategoryVo) {
        ProductCategoryEntity productCategoryEntity = productCategoryDao.selectById(id);
        if (productCategoryEntity == null){
            throw new ServiceException("该id的分类不存在");
        }
        ProductCategoryEntity productCategoryEntity2 = new ProductCategoryEntity();
        BeanUtils.copyProperties(productCategoryVo,productCategoryEntity2);
        productCategoryEntity2.setModifiedTime(new Date());
        productCategoryEntity2.setId(id);
        productCategoryDao.updateById(productCategoryEntity2);
    }

    /**
     * 删除商品类别
     * @param id
     */
    @Override
    public void delete(Long id) {
        ProductCategoryEntity productCategoryEntity = productCategoryDao.selectById(id);
        if (productCategoryEntity == null){
            throw new ServiceException("该id的分类不存在");
        }
        //TODO 1、查询本id是否为父节点
        QueryWrapper<ProductCategoryEntity> pid = new QueryWrapper<ProductCategoryEntity>().eq("pid", id);
        Integer childCount = productCategoryDao.selectCount(pid);
        if (childCount != null && childCount != 0){
            throw  new ServiceException("存在子节点,无法直接删除");
        }
        //TODO 2、查询分类id是否有商品
        QueryWrapper<ProductEntity> productEntityQueryWrapper = new QueryWrapper<ProductEntity>().eq("ONE_CATEGORY_ID", id).or().eq("TWO_CATEGORY_ID", id).or()
                .eq("THREE_CATEGORY_ID", id);
        Integer count = productDao.selectCount(productEntityQueryWrapper);
        if (count != null && count != 0){
            throw new ServiceException("该分类存在物资引用,无法直接删除");
        }

        //TODO 3、删除该分类
        productCategoryDao.deleteById(id);
    }

    @Override
    public List<ProductCategoryVo> findAll() {
        List<ProductCategoryEntity> productCategoryEntities = productCategoryDao.selectList(null);
        List<ProductCategoryVo> collect = productCategoryEntities.stream().map(productCategoryEntity -> {
            ProductCategoryVo productCategoryVo = new ProductCategoryVo();
            BeanUtils.copyProperties(productCategoryEntity, productCategoryVo);
            return productCategoryVo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 分类树形结构
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVo> categoryTree(Integer pageNum, Integer pageSize) {
        List<ProductCategoryVo> all = findAll();
        if (all != null){
            List<ProductCategoryTreeNodeVo> collect = all.stream().map(productCategoryVo -> {
                ProductCategoryTreeNodeVo productCategoryTreeNodeVo = new ProductCategoryTreeNodeVo();
                BeanUtils.copyProperties(productCategoryVo, productCategoryTreeNodeVo);
                return productCategoryTreeNodeVo;
            }).collect(Collectors.toList());
            List<ProductCategoryTreeNodeVo> tree = CategoryTreeBuilderUtil.build(collect);
            if (pageSize!=null && pageNum != null){
                List<ProductCategoryTreeNodeVo> page  = ListPageUtil.page(tree, pageSize, pageNum);
                return new PageVO<>(page.size(),page);
            }else {
                return new PageVO<>(tree.size(),tree);
            }
        }else {
            return new PageVO<>(0,null);
        }
    }

    @Override
    public List<ProductCategoryTreeNodeVo> getParentCategoryTree() {
        List<ProductCategoryVo> all = findAll();
        List<ProductCategoryTreeNodeVo> collect = all.stream().map(productCategoryVo -> {
            ProductCategoryTreeNodeVo productCategoryTreeNodeVo = new ProductCategoryTreeNodeVo();
            BeanUtils.copyProperties(productCategoryVo, productCategoryTreeNodeVo);
            return productCategoryTreeNodeVo;
        }).collect(Collectors.toList());
        return CategoryTreeBuilderUtil.buildParent(collect);
    }
}
