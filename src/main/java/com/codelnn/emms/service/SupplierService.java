package com.codelnn.emms.service;

import com.codelnn.emms.entity.SupplierEntity;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.SupplierVo;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-18 19:13
 **/
public interface SupplierService {

    /**
     * 添加供应商
     * @param supplierVo
     */
    SupplierEntity add(SupplierVo supplierVo);


    /**
     * 供应商列表
     * @param pageNum
     * @param pageSize
     * @param supplierVo
     * @return
     */
    PageVO<SupplierVo> findSupplierList(Integer pageNum, Integer pageSize, SupplierVo supplierVo);


    /**
     * 编辑供应商
     * @param id
     * @return
     */
    SupplierVo edit(Long id);

    /**
     * 更新供应商
     * @param id
     * @param supplierVo
     */
    void update(Long id, SupplierVo supplierVo);

    /**
     * 删除供应商
     * @param id
     */
    void delete(Long id);

    /**
     * 查询所有供应商
     * @return
     */
    List<SupplierVo> findAll();



}
