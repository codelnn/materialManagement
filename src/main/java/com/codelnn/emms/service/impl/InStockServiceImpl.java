package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.*;
import com.codelnn.emms.entity.*;
import com.codelnn.emms.exception.ErrorCodeEnum;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.InStockService;
import com.codelnn.emms.vo.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 17:20
 **/
@Service
public class InStockServiceImpl implements InStockService {

    @Autowired
    private InStockDao inStockDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InStockInfoDao inStockInfoDao;

    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private SupplierDao supplierDao;

    /**
     * 获取入库单列表
     * @param pageNum
     * @param pageSize
     * @param inStockVo
     * @return
     */
    @Override
    public PageVO<InStockVo> findInStockList(Integer pageNum, Integer pageSize, InStockVo inStockVo) {
        Page<InStockEntity> inStockEntityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<InStockEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.orderByAsc("id");
        String inNum = inStockVo.getInNum();
        Integer type = inStockVo.getType();
        Integer status = inStockVo.getStatus();
        Date createTime = inStockVo.getCreateTime();
        Date endTime = inStockVo.getEndTime();
        if (!StringUtils.isEmpty(inNum)){
            entityQueryWrapper.like("IN_NUM",inNum);
        }
        if (type != null){
            entityQueryWrapper.eq("type",type);
        }
        if (status != null){
            entityQueryWrapper.eq("status",status);
        }
        if (createTime != null){
            entityQueryWrapper.ge("CREATE_TIME",createTime);
        }
        if (endTime != null){
            entityQueryWrapper.le("CREATE_TIME",endTime);
        }
        Page<InStockEntity> inStockEntityPage1 = inStockDao.selectPage(inStockEntityPage, entityQueryWrapper);
        List<InStockEntity> records = inStockEntityPage1.getRecords();
        List<InStockVo> collect = records.stream().map(inStockEntity -> {
            InStockVo inStockVo1 = new InStockVo();
            BeanUtils.copyProperties(inStockEntity, inStockVo1);
            SupplierEntity supplierEntity = supplierDao.selectById(inStockEntity.getSupplierId());
            if (supplierEntity != null) {
                inStockVo1.setSupplierName(supplierEntity.getName());
                inStockVo1.setPhone(supplierEntity.getPhone());
                inStockVo1.setContact(supplierEntity.getContact());
                inStockVo1.setAddress(supplierEntity.getAddress());
            }
            return inStockVo1;
        }).collect(Collectors.toList());
        return new PageVO<>(inStockEntityPage1.getTotal(),collect);
    }

    /**
     * 查询入库单详情信息
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public InStockDetailVo detail(Long id, int pageNo, int pageSize) {
        InStockEntity inStockEntity = inStockDao.selectById(id);
        if (inStockEntity == null){
            throw new ServiceException("入库单不存在");
        }
        InStockDetailVo inStockDetailVo = new InStockDetailVo();
        BeanUtils.copyProperties(inStockEntity,inStockDetailVo);
        SupplierEntity supplierEntity = supplierDao.selectById(inStockEntity.getSupplierId());
        if (supplierEntity == null){
            throw new ServiceException("提供物资方不存在,或已被删除");
        }
        SupplierVo supplierVo = new SupplierVo();
        BeanUtils.copyProperties(supplierEntity,supplierVo);
        inStockDetailVo.setSupplierVo(supplierVo);
        // 入库单号
        String inNum = inStockEntity.getInNum();
        Page<InStockInfoEntity> inStockInfoEntityPage = new Page<>(pageNo, pageSize);
        QueryWrapper<InStockInfoEntity> infoEntityQueryWrapper = new QueryWrapper<>();
        infoEntityQueryWrapper.orderByAsc("id");
        infoEntityQueryWrapper.eq("IN_NUM",inNum);
        Page<InStockInfoEntity> inStockInfoEntityPage1 = inStockInfoDao.selectPage(inStockInfoEntityPage, infoEntityQueryWrapper);
        List<InStockInfoEntity> records = inStockInfoEntityPage1.getRecords();
        if (records != null){
            List<InStockItemVo> inStockItemVos = records.stream().map(inStockInfoEntity -> {
                InStockItemVo inStockItemVo = new InStockItemVo();
                // 商品编号
                String pNum = inStockInfoEntity.getPNum();
                QueryWrapper<ProductEntity> p_num = new QueryWrapper<ProductEntity>().eq("P_NUM", pNum);
                ProductEntity productEntity = productDao.selectOne(p_num);
                if (productEntity != null){
                    BeanUtils.copyProperties(productEntity, inStockItemVo);
                    inStockItemVo.setCount(inStockInfoEntity.getProductNumber());
                    return inStockItemVo;
                }else {
                    throw new ServiceException("编号为:["+pNum+"]的物资找不到,或已被删除");
                }
            }).collect(Collectors.toList());
            inStockDetailVo.setTotal(inStockInfoEntityPage1.getTotal());
            inStockDetailVo.setItemVos(inStockItemVos);
        }else {
            throw new ServiceException("入库编号为:["+inNum+"]的明细找不到,或已被删除");
        }
        return inStockDetailVo;
    }

    /**
     * 删除入库单信息
     * @param id
     */
    @Override
    public void delete(Long id) {
        InStockEntity inStockEntity = inStockDao.selectById(id);
        if (inStockEntity == null){
            throw new ServiceException("入库单不存在");
        }else if (inStockEntity.getStatus()!=1&&inStockEntity.getStatus()!=2){
            throw new ServiceException("入库单状态错误,无法删除");
        }else {
            inStockDao.deleteById(id);
            String inNum = inStockEntity.getInNum();
            inStockInfoDao.delete(new QueryWrapper<InStockInfoEntity>().eq("IN_NUM",inNum));
        }
    }

    /**
     * 添加入库单信息
     * @param inStockVO
     */
    @Override
    public void addIntoStock(InStockVo inStockVO) {
          // 随机生成UUID 入库单号
        String IN_STOCK_NUM = UUID.randomUUID().toString().substring(0, 32)+System.currentTimeMillis();
        //记录该单的总数
        int itemNumber=0;
        List<Object> products = inStockVO.getProducts();
        if (!CollectionUtils.isEmpty(products)){
            for (Object product : products) {
                LinkedHashMap item = (LinkedHashMap) product;
                //入库数量
                int productNumber = (int) item.get("productNumber");
                //商品id
                Integer productId = (Integer) item.get("productId");
                // 查询商品信息
                ProductEntity productEntity = productDao.selectById(productId);
                if (productEntity == null){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
                }else if (productEntity.getStatus() == 1){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_IS_REMOVE, productEntity.getName() + "物资已被回收,无法入库");
                }else if (productEntity.getStatus() == 2){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_WAIT_PASS, productEntity.getName() + "物资待审核,无法入库");
                }else if (productNumber <= 0){
                    throw new ServiceException(ErrorCodeEnum.PRODUCT_IN_STOCK_NUMBER_ERROR,productEntity.getName()+"入库数量不合法,无法入库");
                }else {
                    itemNumber += productNumber;
                    // 插入入库单明细
                    InStockInfoEntity inStockInfoEntity = new InStockInfoEntity();
                    inStockInfoEntity.setCreateTime(new Date());
                    inStockInfoEntity.setModifiedTime(new Date());
                    inStockInfoEntity.setProductNumber(productNumber);
                    inStockInfoEntity.setInNum(IN_STOCK_NUM);
                    inStockInfoEntity.setPNum(productEntity.getPNum());
                    inStockInfoDao.insert(inStockInfoEntity);
                }
            }
            InStockEntity inStockEntity = new InStockEntity();
            BeanUtils.copyProperties(inStockVO,inStockEntity);
            inStockEntity.setCreateTime(new Date());
            inStockEntity.setModifiedTime(new Date());
            inStockEntity.setProductNumber(itemNumber);
            inStockEntity.setInNum(IN_STOCK_NUM);
            inStockEntity.setStatus(2);
            UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
            inStockEntity.setOperator(userEntity.getUsername());
            inStockDao.insert(inStockEntity);
        }else {
            throw new ServiceException(ErrorCodeEnum.PRODUCT_IN_STOCK_EMPTY);
        }
    }

    /**
     * 移入回收站
     * @param id
     */
    @Override
    public void remove(Long id) {
        InStockEntity inStockEntity = inStockDao.selectById(id);
        if (inStockEntity.getStatus()!=0){
            throw new ServiceException("入库单状态不正确");
        }
        inStockEntity.setStatus(1);
        inStockDao.updateById(inStockEntity);
    }

    /**
     * 还原从回收站中
     * @param id
     */
    @Override
    public void back(Long id) {
        InStockEntity inStockEntity = inStockDao.selectById(id);
        if (inStockEntity.getStatus()!=1){
            throw new ServiceException("入库单状态不正确");
        }
        inStockEntity.setStatus(0);
        inStockDao.updateById(inStockEntity);
    }

    /**
     * 入库审核
     * @param id
     */
    @Override
    public void publish(Long id) {
        InStockEntity inStockEntity = inStockDao.selectById(id);
        if (inStockEntity == null){
            throw new ServiceException("入库单不存在");
        }
        if (inStockEntity.getStatus() != 2){
            throw new ServiceException("入库单状态错误");
        }
        SupplierEntity supplierEntity = supplierDao.selectById(inStockEntity.getSupplierId());
        if (supplierEntity == null){
            throw new ServiceException("入库来源信息错误");
        }
        // 入库单号
        String inNum = inStockEntity.getInNum();
        // 商品库存信息增加
        List<InStockInfoEntity> in_num = inStockInfoDao.selectList(new QueryWrapper<InStockInfoEntity>().eq("IN_NUM", inNum));
        if (!CollectionUtils.isEmpty(in_num)){
            for (InStockInfoEntity inStockInfoEntity : in_num) {
                // 商品编号
                String pNum = inStockInfoEntity.getPNum();
                // 入库数目
                Integer productNumber =inStockInfoEntity.getProductNumber();
                ProductEntity p_num = productDao.selectOne(new QueryWrapper<ProductEntity>().eq("P_NUM", pNum));
                if (p_num != null){
                    // 如果该商品库存已经有了 就增加数目，否则新增商品库存信息
                    ProductStockEntity productStockEntity = productStockDao.selectOne(new QueryWrapper<ProductStockEntity>().eq("P_NUM", pNum));
                    if (productStockEntity != null){
                        // 商品库存信息不等于空，则增加商品数目
                        Long newStock = productStockEntity.getStock()+ productNumber;
                        productStockEntity.setStock(newStock);
                        productStockDao.updateById(productStockEntity);
                    }else {
                        ProductStockEntity productStockEntity1 = new ProductStockEntity();
                        long l = productNumber.longValue();
                        productStockEntity1.setStock(l);
                        productStockEntity1.setPNum(pNum);
                        productStockDao.insert(productStockEntity1);
                    }
                    // 修改入库单状态
                    inStockEntity.setModifiedTime(new Date());
                    inStockEntity.setStatus(0);
                    inStockDao.updateById(inStockEntity);
                }else {
                    throw new ServiceException("物资编号为:["+pNum+"]的物资不存在");
                }
            }
        }else {
            throw new ServiceException("入库的明细不能为空");
        }
    }
}
