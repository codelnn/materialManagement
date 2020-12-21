package com.codelnn.emms.service;

import com.codelnn.emms.entity.ConsumerEntity;
import com.codelnn.emms.vo.ConsumerVo;
import com.codelnn.emms.vo.PageVO;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 11:39
 **/
public interface ConsumerService {

    /**
     * 添加物质去向
     * @param consumerVo
     * @return
     */
    ConsumerEntity add(ConsumerVo consumerVo);


    /**
     * 获取物质去向列表
     * @param pageNum
     * @param pageSize
     * @param consumerVo
     * @return
     */
    PageVO<ConsumerVo> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVo consumerVo);

    /**
     * 编辑物质去向
     * @param id
     * @return
     */
    ConsumerVo edit(Long id);

    /**
     * 更新物质去向
     * @param id
     * @param consumerVo
     */
    void update(Long id,ConsumerVo consumerVo);

    /**
     * 删除物质去向
     * @param id
     */
    void delete(Long id);

    /**
     * 查询所有物质去向
     * @return
     */
    List<ConsumerVo> findAll();


}
