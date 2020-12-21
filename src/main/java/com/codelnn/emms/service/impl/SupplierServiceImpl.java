package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.SupplierDao;
import com.codelnn.emms.entity.SupplierEntity;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.SupplierService;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.SupplierVo;
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
 * @create: 2020-12-18 19:13
 **/
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierDao supplierDao;

    /**
     * 添加供应商
     * @param supplierVo
     */
    @Override
    public SupplierEntity add(SupplierVo supplierVo) {
        SupplierEntity supplierEntity = new SupplierEntity();
        BeanUtils.copyProperties(supplierVo,supplierEntity);
        supplierEntity.setCreateTime(new Date());
        supplierEntity.setModifiedTime(new Date());
        supplierDao.insert(supplierEntity);
        System.out.println(supplierEntity);
        return supplierEntity;
    }

    /**
     * 分页获取获取供应商列表
     * @param pageNum
     * @param pageSize
     * @param supplierVo
     * @return
     */
    @Override
    public PageVO<SupplierVo> findSupplierList(Integer pageNum, Integer pageSize, SupplierVo supplierVo) {
        Page<SupplierEntity> entityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<SupplierEntity> entityQueryWrapper = new QueryWrapper<>();
        String name = supplierVo.getName();
        String address = supplierVo.getAddress();
        String contact = supplierVo.getContact();
        if (!StringUtils.isEmpty(name)){
            entityQueryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(address)){
            entityQueryWrapper.like("address",address);
        }
        if (!StringUtils.isEmpty(contact)){
            entityQueryWrapper.like("contact",contact);
        }
        entityQueryWrapper.orderByAsc("id");
        Page<SupplierEntity> supplierEntityPage = supplierDao.selectPage(entityPage, entityQueryWrapper);
        List<SupplierEntity> records = supplierEntityPage.getRecords();
        if (records != null){
            List<SupplierVo> collect = records.stream().map(supplierEntity -> {
                SupplierVo supplierVo1 = new SupplierVo();
                BeanUtils.copyProperties(supplierEntity, supplierVo1);
                return supplierVo1;
            }).collect(Collectors.toList());
            return new PageVO<>(entityPage.getTotal(),collect);
        }
        return new PageVO<>(0,null);
    }

    /**
     * 编辑供应商信息
     * @param id
     * @return
     */
    @Override
    public SupplierVo edit(Long id) {
        SupplierEntity supplierEntity = supplierDao.selectById(id);
        if (supplierEntity == null){
            throw new ServiceException("该id对应的供应商不存在");
        }
        SupplierVo supplierVo = new SupplierVo();
        BeanUtils.copyProperties(supplierEntity,supplierVo);
        return supplierVo;
    }

    /**
     * 更新供应商信息
     * @param id
     * @param supplierVo
     */
    @Override
    public void update(Long id, SupplierVo supplierVo) {
        SupplierEntity supplierEntity = supplierDao.selectById(id);
        if (supplierEntity == null){
            throw new ServiceException("该id对应的供应商不存在");
        }
        SupplierEntity supplierEntity1 = new SupplierEntity();
        BeanUtils.copyProperties(supplierVo,supplierEntity1);
        supplierEntity1.setId(id);
        supplierEntity1.setModifiedTime(new Date());
        supplierDao.updateById(supplierEntity1);
    }

    /**
     * 删除供应商信息
     * @param id
     */
    @Override
    public void delete(Long id) {
        SupplierEntity supplierEntity = supplierDao.selectById(id);
        if (supplierEntity == null){
            throw new ServiceException("该id对应的供应商不存在");
        }
        supplierDao.deleteById(id);
    }

    /**
     * 查询所有供应商信息
     * @return
     */
    @Override
    public List<SupplierVo> findAll() {
        List<SupplierEntity> supplierEntities = supplierDao.selectList(null);
        List<SupplierVo> collect = supplierEntities.stream().map(supplierEntity -> {
            SupplierVo supplierVo = new SupplierVo();
            BeanUtils.copyProperties(supplierEntity, supplierVo);
            return supplierVo;
        }).collect(Collectors.toList());
        return collect;
    }
}
