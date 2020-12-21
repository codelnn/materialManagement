package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.entity.SupplierEntity;
import com.codelnn.emms.service.InStockService;
import com.codelnn.emms.service.SupplierService;
import com.codelnn.emms.vo.InStockDetailVo;
import com.codelnn.emms.vo.InStockVo;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.SupplierVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:20
 **/
@RestController
@RequestMapping("/inStock")
public class InStockController {

    @Autowired
    private InStockService inStockService;

    @Autowired
    private SupplierService supplierService;

    /**
     * 查询入库单列表
     * @param pageNum
     * @param pageSize
     * @param inStockVO
     * @return
     */
    @GetMapping("/findInStockList")
    public CommonResult findInStockList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize,
            InStockVo inStockVO) {
        PageVO<InStockVo> inStockList = inStockService.findInStockList(pageNum, pageSize, inStockVO);
        return CommonResult.success(inStockList);
    }

    /**
     * 新增入库单
     * @param inStockVO
     * @return
     */
    @PostMapping("/addIntoStock")
    public CommonResult addIntoStock(@RequestBody InStockVo inStockVO) {
        if(inStockVO.getSupplierId()==null){
            //说明现在添加物资来源
            SupplierVo supplierVO = new SupplierVo();
            BeanUtils.copyProperties(inStockVO,supplierVO);
            if("".equals(supplierVO.getName())||supplierVO.getName()==null){
                return CommonResult.error("物资提供方名不能为空");
            }
            if("".equals(supplierVO.getEmail())||supplierVO.getEmail()==null){
                return CommonResult.error("邮箱不能为空");
            }
            if("".equals(supplierVO.getContact())||supplierVO.getContact()==null){
                return CommonResult.error("联系人不能为空");
            }
            if("".equals(supplierVO.getAddress())||supplierVO.getAddress()==null){
                return CommonResult.error("地址不能为空");
            }
            if("".equals(supplierVO.getPhone())||supplierVO.getPhone()==null){
                return CommonResult.error("联系方式不能为空");
            }
            if(supplierVO.getSort()==null){
                return CommonResult.error("排序不能为空");
            }
            SupplierEntity supplier = supplierService.add(supplierVO);
            inStockVO.setSupplierId(supplier.getId());
        }
        inStockService.addIntoStock(inStockVO);
        return CommonResult.success();
    }

    /**
     * 审核该入单
     * @param id
     * @return
     */
    @PutMapping("/publish/{id}")
    public CommonResult publish(@PathVariable Long id) {
        inStockService.publish(id);
        return CommonResult.success();
    }

    @GetMapping("/detail/{id}")
    public CommonResult detail(@PathVariable Long id,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "3") Integer pageSize) {
        InStockDetailVo detail = inStockService.detail(id,pageNum,pageSize);
        return CommonResult.success(detail);
    }

    @GetMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        inStockService.delete(id);
        return CommonResult.success();
    }

    @PutMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Long id) {
        inStockService.remove(id);
        return CommonResult.success();
    }

    @PutMapping("/back/{id}")
    public CommonResult back(@PathVariable Long id) {
        inStockService.back(id);
        return CommonResult.success();
    }


}
