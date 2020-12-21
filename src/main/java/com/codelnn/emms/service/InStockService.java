package com.codelnn.emms.service;

import com.codelnn.emms.vo.InStockDetailVo;
import com.codelnn.emms.vo.InStockVo;
import com.codelnn.emms.vo.PageVO;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:22
 **/
public interface InStockService {

    /**
     * 入库单列表
     * @param pageNum
     * @param pageSize
     * @param inStockVo
     * @return
     */
    PageVO<InStockVo> findInStockList(Integer pageNum, Integer pageSize, InStockVo inStockVo);


    /**
     * 入库单明细
     * @param id
     * @return
     */
    InStockDetailVo detail(Long id, int pageNo, int pageSize);

    /**
     * 删除入库单
     * @param id
     */
    void delete(Long id);

    /**
     * 物资入库
     * @param inStockVO
     */
    void addIntoStock(InStockVo inStockVO);

    /**
     * 移入回收站
     * @param id
     */
    void remove(Long id);

    /**
     * 还原从回收站中
     * @param id
     */
    void back(Long id);

    /**
     * 入库审核
     * @param id
     */
    void publish(Long id);



}
