package com.codelnn.emms.service;

import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductStockVo;
import com.codelnn.emms.vo.ProductVo;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:23
 **/
public interface ProductService {


    void add(ProductVo productVo);


    PageVO<ProductVo> findProductList(Integer pageNum,Integer pageSize, ProductVo productVo);


    ProductVo edit(Long id);


    void update(Long id,ProductVo productVo);

    void delete(Long id);

    PageVO<ProductStockVo> findProductStocks(Integer pageNum, Integer pageSize, ProductVo productVo);

    List<ProductStockVo> findAllStocks(ProductVo productVo);

    void remove(Long id);


    void back(Long id);


    void publish(Long id);
}
