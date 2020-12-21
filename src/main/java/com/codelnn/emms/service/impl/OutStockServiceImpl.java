package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.*;
import com.codelnn.emms.entity.*;
import com.codelnn.emms.exception.ErrorCodeEnum;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.OutStockService;
import com.codelnn.emms.vo.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-20 14:02
 **/
@Service
public class OutStockServiceImpl implements OutStockService {

    @Autowired
    private OutStockDao outStockDao;

    @Autowired
    private OutStockInfoDao outStockInfoDao;

    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductStockDao productStockDao;

    @Override
    public PageVO<OutStockVo> findOutStockList(Integer pageNum, Integer pageSize, OutStockVo outStockVo) {
        Page<OutStockEntity> outStockEntityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<OutStockEntity> entityQueryWrapper = new QueryWrapper<OutStockEntity>();
        entityQueryWrapper.orderByAsc("id");
        String outNum = outStockVo.getOutNum();
        Integer status = outStockVo.getStatus();
        if (!StringUtils.isEmpty(outNum)){
            entityQueryWrapper.like("OUT_NUM",outNum);
        }
        if (status != null){
            entityQueryWrapper.eq("status",status);
        }
        Page<OutStockEntity> stockEntityPage = outStockDao.selectPage(outStockEntityPage, entityQueryWrapper);
        List<OutStockEntity> records = stockEntityPage.getRecords();
        List<OutStockVo> collect = records.stream().map(outStockEntity -> {
            OutStockVo outStockVo1 = new OutStockVo();
            BeanUtils.copyProperties(outStockEntity, outStockVo1);
            ConsumerEntity consumerEntity = consumerDao.selectById(outStockEntity.getConsumerId());
            if (consumerEntity != null) {
                outStockVo1.setName(consumerEntity.getName());
                outStockVo1.setContact(consumerEntity.getContact());
                outStockVo1.setPhone(consumerEntity.getPhone());
            }
            return outStockVo1;
        }).collect(Collectors.toList());
        return new PageVO<>(stockEntityPage.getTotal(),collect);
    }

    /**
     * 查询出库单详情信息
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public OutStockDetailVo detail(Long id, int pageNo, int pageSize) {
        OutStockEntity outStockEntity = outStockDao.selectById(id);
        if (outStockEntity == null){
            throw new ServiceException("出库单不存在");
        }
        OutStockDetailVo outStockDetailVo = new OutStockDetailVo();
        BeanUtils.copyProperties(outStockEntity,outStockDetailVo);
        ConsumerEntity consumerEntity = consumerDao.selectById(outStockEntity.getConsumerId());
        if (consumerEntity == null){
            throw new ServiceException("物资使用方不存在，或已被删除");
        }
        ConsumerVo consumerVo = new ConsumerVo();
        BeanUtils.copyProperties(consumerEntity,consumerVo);
        outStockDetailVo.setConsumerVo(consumerVo);
        // 出库单号
        String outNum = outStockEntity.getOutNum();
        Page<OutStockInfoEntity> stockInfoEntityPage = new Page<>(pageNo, pageSize);
        QueryWrapper<OutStockInfoEntity> entityQueryWrapper = new QueryWrapper<OutStockInfoEntity>().orderByAsc("id");
        entityQueryWrapper.eq("OUT_NUM",outNum);
        Page<OutStockInfoEntity> outStockInfoEntityPage = outStockInfoDao.selectPage(stockInfoEntityPage, entityQueryWrapper);
        List<OutStockInfoEntity> records = outStockInfoEntityPage.getRecords();
        if (records != null){
            List<OutStockItemVo> collect = records.stream().map(outStockInfoEntity -> {
                OutStockItemVo outStockItemVo = new OutStockItemVo();
                // 商品编号
                String pNum = outStockInfoEntity.getPNum();
                QueryWrapper<ProductEntity> p_num = new QueryWrapper<ProductEntity>().eq("P_NUM", pNum);
                ProductEntity productEntity = productDao.selectOne(p_num);
                if (productEntity != null) {
                    BeanUtils.copyProperties(productEntity, outStockItemVo);
                    outStockItemVo.setCount(outStockInfoEntity.getProductNumber());
                    return outStockItemVo;
                } else {
                    throw new ServiceException("编号为:[" + pNum + "]的物资找不到,或已被删除");
                }
            }).collect(Collectors.toList());
            outStockDetailVo.setTotal(outStockInfoEntityPage.getTotal());
            outStockDetailVo.setItemVos(collect);
        }else {
            throw new ServiceException("出库编号：["+outNum+"]的明细找不到，或已被删除");
        }
        return outStockDetailVo;
    }

    /**
     * 添加出库信息
     * @param outStockVo
     */
    @Override
    public void addOutStock(OutStockVo outStockVo) {
        String OUT_STOCK_NUM = UUID.randomUUID().toString().substring(0, 32)+System.currentTimeMillis();
        // 记录该单的总数
        int itemNumber=0;
        List<Object> products = outStockVo.getProducts();
        if (!CollectionUtils.isEmpty(products)){
            for (Object product : products) {
                LinkedHashMap item = (LinkedHashMap) product;
                //出库数量
                int productNumber = (int) item.get("productNumber");
                //商品id
                Integer productId = (Integer) item.get("productId");
                // 查询商品信息
                ProductEntity productEntity = productDao.selectById(productId);
                if (productEntity == null){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
                }else if (productNumber <= 0){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_OUT_STOCK_NUMBER_ERROR,productEntity.getName()+"出库数量不合法,无法出库");
                }else{
                    // 检查库存
                    ProductStockEntity productStockEntity = productStockDao.selectOne(new QueryWrapper<ProductStockEntity>().eq("P_NUM", productEntity.getPNum()));
                    if (productStockEntity == null){
                        throw new ServiceException("该物质库存处于空的状态");
                    }
                    if (productNumber > productStockEntity.getStock()){
                        throw new ServiceException(ErrorCodeEnum.PRODUCT_STOCK_ERROR,productEntity.getName()+"库存不足,库存剩余:"+productStockEntity.getStock());
                    }
                    itemNumber += productNumber;
                    // 插入出库单明细表内容
                    OutStockInfoEntity outStockInfoEntity = new OutStockInfoEntity();
                    outStockInfoEntity.setCreateTime(new Date());
                    outStockInfoEntity.setModifiedTime(new Date());
                    outStockInfoEntity.setOutNum(OUT_STOCK_NUM);
                    outStockInfoEntity.setPNum(productEntity.getPNum());
                    outStockInfoEntity.setProductNumber(productNumber);
                    outStockInfoDao.insert(outStockInfoEntity);
                }
            }
            OutStockEntity outStockEntity = new OutStockEntity();
            BeanUtils.copyProperties(outStockVo,outStockEntity);
            outStockEntity.setCreateTime(new Date());
            outStockEntity.setOutNum(OUT_STOCK_NUM);
            outStockEntity.setProductNumber(itemNumber);
            outStockEntity.setStatus(2);
            // 设置操作人员用户名
            UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
            outStockEntity.setOperator(userEntity.getUsername());
            outStockDao.insert(outStockEntity);
        }else {
            throw new ServiceException(ErrorCodeEnum.PRODUCT_OUT_STOCK_EMPTY);
        }
    }

    @Override
    public void delete(Long id) {
        OutStockEntity outStockEntity = outStockDao.selectById(id);
        if (outStockEntity == null){
            throw new ServiceException("出库单不存在");
        }else if (outStockEntity.getStatus()!=1&&outStockEntity.getStatus()!=2){
            throw new ServiceException("出库单状态错误，无法删除");
        }else {
            outStockDao.deleteById(id);
            String outNum = outStockEntity.getOutNum();
            outStockInfoDao.delete(new QueryWrapper<OutStockInfoEntity>().eq("OUT_NUM",outNum));
        }
    }

    @Override
    public void remove(Long id) {
        OutStockEntity outStockEntity = outStockDao.selectById(id);
        if (outStockEntity.getStatus()!=0){
            throw new ServiceException("出库单状态不正确");
        }
        outStockEntity.setStatus(0);
        outStockDao.updateById(outStockEntity);
    }

    @Override
    public void back(Long id) {
        OutStockEntity outStockEntity = outStockDao.selectById(id);
        if (outStockEntity.getStatus()!=1){
            throw new ServiceException("出库单状态不正确");
        }
        outStockEntity.setStatus(1);
        outStockDao.updateById(outStockEntity);
    }

    /**
     * 出库审核
     * @param id
     */
    @Override
    public void publish(Long id) {
        OutStockEntity outStockEntity = outStockDao.selectById(id);
        if (outStockEntity == null){
            throw new ServiceException("出库单不存在");
        }
        if (outStockEntity.getStatus() != 2) {
            throw new ServiceException("出库单状态错误");
        }
        ConsumerEntity consumerEntity = consumerDao.selectById(outStockEntity.getConsumerId());
        if (consumerEntity == null){
            throw new ServiceException("出库去处信息错误");
        }
        // 出库单号
        String outNum = outStockEntity.getOutNum();
        List<OutStockInfoEntity> out_num = outStockInfoDao.selectList(new QueryWrapper<OutStockInfoEntity>().eq("OUT_NUM", outNum));
        if (out_num != null){
            // 对应商品库存减少
            for (OutStockInfoEntity outStockInfoEntity : out_num) {
                String pNum = outStockInfoEntity.getPNum();
                ProductEntity p_num = productDao.selectOne(new QueryWrapper<ProductEntity>().eq("P_NUM", pNum));
                if (p_num != null) {
                    ProductStockEntity productStockEntity = productStockDao.selectOne(new QueryWrapper<ProductStockEntity>().eq("P_NUM", pNum));
                    if (productStockEntity != null) {

                        long newStock = productStockEntity.getStock() - outStockInfoEntity.getProductNumber();
                        if (newStock >= 0) {
                            productStockEntity.setStock(newStock);
                            productStockDao.updateById(productStockEntity);
                        } else {
                            throw new ServiceException("物资编号为：[" + pNum + "]的物质库存不足");
                        }
                        // 修改出库单状态
                        outStockEntity.setStatus(0);
                        outStockDao.updateById(outStockEntity);
                    } else {
                        throw new ServiceException("该物资在库存中找不到");
                    }
                }else {
                    throw new ServiceException("物资编号为:["+pNum+"]的物资不存在");
                }
            }

        }else {
            throw new ServiceException("出库的明细不能为空");
        }
    }
}
