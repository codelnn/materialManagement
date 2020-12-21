package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.ConsumerDao;
import com.codelnn.emms.entity.ConsumerEntity;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.ConsumerService;
import com.codelnn.emms.vo.ConsumerVo;
import com.codelnn.emms.vo.PageVO;
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
 * @create: 2020-12-19 11:39
 **/
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerDao consumerDao;

    @Override
    public ConsumerEntity add(ConsumerVo consumerVo) {
        ConsumerEntity consumerEntity = new ConsumerEntity();
        BeanUtils.copyProperties(consumerVo,consumerEntity);
        consumerEntity.setCreateTime(new Date());
        consumerEntity.setModifiedTime(new Date());
        consumerDao.insert(consumerEntity);
        return consumerEntity;
    }

    @Override
    public PageVO<ConsumerVo> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVo consumerVo) {
        Page<ConsumerEntity> consumerEntityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ConsumerEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.orderByAsc("id");
        String name = consumerVo.getName();
        String contact = consumerVo.getContact();
        if (!StringUtils.isEmpty(name)){
            entityQueryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(contact)){
            entityQueryWrapper.like("contact",contact);
        }
        Page<ConsumerEntity> consumerEntityPage1 = consumerDao.selectPage(consumerEntityPage, entityQueryWrapper);
        List<ConsumerEntity> records = consumerEntityPage1.getRecords();
        List<ConsumerVo> collect = records.stream().map(consumerEntity -> {
            ConsumerVo consumerVo1 = new ConsumerVo();
            BeanUtils.copyProperties(consumerEntity, consumerVo1);
            return consumerVo1;
        }).collect(Collectors.toList());
        return new PageVO<>(consumerEntityPage1.getTotal(),collect);
    }

    @Override
    public ConsumerVo edit(Long id) {
        ConsumerEntity consumerEntity = consumerDao.selectById(id);
        if(consumerEntity == null){
            throw new ServiceException("该id的物质去向信息不存在");
        }
        ConsumerVo consumerVo = new ConsumerVo();
        BeanUtils.copyProperties(consumerEntity,consumerVo);
        return consumerVo;
    }

    @Override
    public void update(Long id, ConsumerVo consumerVo) {
        ConsumerEntity consumerEntity = consumerDao.selectById(id);
        if(consumerEntity == null){
            throw new ServiceException("该id的物质去向信息不存在");
        }
        ConsumerEntity consumerEntity1 = new ConsumerEntity();
        BeanUtils.copyProperties(consumerVo,consumerEntity1);
        consumerEntity1.setModifiedTime(new Date());
        consumerEntity1.setId(id);
        consumerDao.updateById(consumerEntity1);
    }

    @Override
    public void delete(Long id) {
        ConsumerEntity consumerEntity = consumerDao.selectById(id);
        if(consumerEntity == null){
            throw new ServiceException("该id的物质去向信息不存在");
        }
        consumerDao.deleteById(id);
    }

    @Override
    public List<ConsumerVo> findAll() {
        List<ConsumerEntity> consumerEntities = consumerDao.selectList(null);
        List<ConsumerVo> collect = consumerEntities.stream().map(consumerEntity -> {
            ConsumerVo consumerVo = new ConsumerVo();
            BeanUtils.copyProperties(consumerEntity, consumerVo);
            return consumerVo;
        }).collect(Collectors.toList());
        return collect;
    }
}
