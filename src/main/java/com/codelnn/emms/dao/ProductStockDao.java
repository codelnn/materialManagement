package com.codelnn.emms.dao;

import com.codelnn.emms.entity.ProductStockEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codelnn.emms.vo.ProductStockVo;
import com.codelnn.emms.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author codelnn
 * @email 2543703057@qq.com
 * @date 2020-12-16 17:38:04
 */
@Mapper
public interface ProductStockDao extends BaseMapper<ProductStockEntity> {

    List<ProductStockVo> findProductStocks(ProductVo productVo);

    List<ProductStockVo> findAllStocks(ProductVo productVo);

}
