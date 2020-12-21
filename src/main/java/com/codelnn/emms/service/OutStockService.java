package com.codelnn.emms.service;

import com.codelnn.emms.vo.OutStockDetailVo;
import com.codelnn.emms.vo.OutStockVo;
import com.codelnn.emms.vo.PageVO;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 20:30
 **/
public interface OutStockService {
    /**
     * 出库单列表
     * @param pageNum
     * @param pageSize
     * @param outStockVo
     * @return
     */
    PageVO<OutStockVo> findOutStockList(Integer pageNum, Integer pageSize, OutStockVo outStockVo);


    /**
     * 出库单明细
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    OutStockDetailVo detail(Long id,int pageNo,int pageSize);

    /**
     * 添加出库单
     * @param outStockVo
     */
    void addOutStock(OutStockVo outStockVo);

    /**
     * 删除出库单
     * @param id
     */
    void delete(Long id);

    /**
     * 移入回收站
     * @param id
     */
    void remove(Long id);

    /**
     *从回收站还原
     * @param id
     */
    void back(Long id);

    /**
     * 出库审核
     * @param id
     */
    void publish(Long id);

}
