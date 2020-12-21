package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.ProductDao;
import com.codelnn.emms.dao.ProductStockDao;
import com.codelnn.emms.entity.ProductEntity;
import com.codelnn.emms.exception.ErrorCodeEnum;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.ProductService;
import com.codelnn.emms.utils.ListPageUtil;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductStockVo;
import com.codelnn.emms.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:30
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductStockDao productStockDao;

    /**
     * 添加商品
     *
     * @param productVo
     */
    @Override
    public void add(ProductVo productVo) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productVo, productEntity);
        int length = productVo.getCategoryKeys().length;
        Long[] categoryKeys = productVo.getCategoryKeys();
        if (length == 3) {
            productEntity.setOneCategoryId(categoryKeys[0]);
            productEntity.setTwoCategoryId(categoryKeys[1]);
            productEntity.setThreeCategoryId(categoryKeys[2]);
            productEntity.setStatus(2);
            productEntity.setPNum(UUID.randomUUID().toString().substring(0, 32)+System.currentTimeMillis());
            productEntity.setCreateTime(new Date());
            productEntity.setModifiedTime(new Date());
            productDao.insert(productEntity);
        } else {
            throw new ServiceException("商品必须属于三级分类");
        }
    }

    @Override
    public PageVO<ProductVo> findProductList(Integer pageNum, Integer pageSize, ProductVo productVo) {
        Page<ProductEntity> productEntityPage = new Page<>(pageNum, pageSize);
        Integer status = productVo.getStatus();
        String name = productVo.getName();
        Long oneCategoryId = productVo.getOneCategoryId();
        Long twoCategoryId = productVo.getTwoCategoryId();
        Long threeCategoryId = productVo.getThreeCategoryId();
        QueryWrapper<ProductEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.orderByAsc("id");
        if (status != null) {
            entityQueryWrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            entityQueryWrapper.like("name", name);
        }
        if (oneCategoryId != null) {
            entityQueryWrapper.eq("ONE_CATEGORY_ID", oneCategoryId);
        }
        if (twoCategoryId != null) {
            entityQueryWrapper.eq("TWO_CATEGORY_ID", twoCategoryId);
        }
        if (threeCategoryId != null) {
            entityQueryWrapper.eq("THREE_CATEGORY_ID", threeCategoryId);
        }
        Page<ProductEntity> productEntityPage1 = productDao.selectPage(productEntityPage, entityQueryWrapper);
        List<ProductEntity> records = productEntityPage1.getRecords();
        if (records != null) {
            List<ProductVo> collect = records.stream().map(productEntity -> {
                ProductVo productVo1 = new ProductVo();
                BeanUtils.copyProperties(productEntity, productVo1);
                return productVo1;
            }).collect(Collectors.toList());
            return new PageVO<>(productEntityPage1.getTotal(), collect);
        }
        return new PageVO<>(0, null);
    }

    @Override
    public ProductVo edit(Long id) {
        ProductEntity productEntity = productDao.selectById(id);
        if (productEntity == null) {
            throw new ServiceException("该id对应的商品不存在");
        }
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(productEntity, productVo);
        return productVo;
    }

    @Override
    public void update(Long id, ProductVo productVo) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productVo, productEntity);
        productEntity.setModifiedTime(new Date());
        Long[] categoryKeys = productVo.getCategoryKeys();
        if (categoryKeys != null){
            int length = categoryKeys.length;
            if (length == 3) {
                productEntity.setOneCategoryId(categoryKeys[0]);
                productEntity.setTwoCategoryId(categoryKeys[1]);
                productEntity.setThreeCategoryId(categoryKeys[2]);
            }
        }
        productDao.updateById(productEntity);
    }

    /**
     * 删除商品
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 只有商品处于回收站或者待审核的情况下可删除
        ProductEntity productEntity = productDao.selectById(id);
        if (productEntity.getStatus() != 1 && productEntity.getStatus() != 2) {
            throw new ServiceException(ErrorCodeEnum.PRODUCT_STATUS_ERROR);
        }
        productDao.deleteById(id);
    }

    /**
     * 获取商品库存列表信息
     *
     * @param pageNum
     * @param pageSize
     * @param productVo
     * @return
     */
    @Override
    public PageVO<ProductStockVo> findProductStocks(Integer pageNum, Integer pageSize, ProductVo productVo) {
        List<ProductStockVo> productStockVoList = productStockDao.findProductStocks(productVo);
        List<ProductStockVo> page = ListPageUtil.page(productStockVoList, pageSize, pageNum);
        return new PageVO<>(productStockVoList.size(), page);
    }

    /**
     * 获取商品所有库存信息，用于画饼状图
     *
     * @param productVo
     * @return
     */
    @Override
    public List<ProductStockVo> findAllStocks(ProductVo productVo) {
        List<ProductStockVo> productStockVoList = productStockDao.findAllStocks(productVo);
        return productStockVoList;
    }

    /**
     * 移入回收站
     * @param id
     */
    @Override
    public void remove(Long id) {
        ProductEntity productEntity = productDao.selectById(id);
        if (productEntity.getStatus() != 0) {
            throw new ServiceException(ErrorCodeEnum.PRODUCT_STATUS_ERROR);
        }
        productEntity.setStatus(1);
        productEntity.setModifiedTime(new Date());
        productDao.updateById(productEntity);
    }

    /**
     * 从回收站返回
     * @param id
     */
    @Override
    public void back(Long id) {
        ProductEntity productEntity = productDao.selectById(id);
        if (productEntity.getStatus() != 1){
            throw new ServiceException(ErrorCodeEnum.PRODUCT_STATUS_ERROR);
        }
        productEntity.setStatus(0);
        productEntity.setModifiedTime(new Date());
        productDao.updateById(productEntity);
    }

    /**
     * 商品审核
     * @param id
     */
    @Override
    public void publish(Long id) {
        ProductEntity productEntity = productDao.selectById(id);
        if (productEntity.getStatus() != 2){
            throw new ServiceException(ErrorCodeEnum.PRODUCT_STATUS_ERROR);
        }
        productEntity.setStatus(0);
        productEntity.setModifiedTime(new Date());
        productDao.updateById(productEntity);
    }
}
