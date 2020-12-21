package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.service.SupplierService;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.SupplierVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 物资提供方操作类
 * @author: znx
 * @create: 2020-12-18 19:14
 **/
@RestController
@RequestMapping(value = "/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 获取供应商列表
     * @param pageNum
     * @param pageSize
     * @param supplierVO
     * @return
     */
    @GetMapping("/findSupplierList")
    public CommonResult findSupplierList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         SupplierVo supplierVO) {
        PageVO<SupplierVo> supplierVOPageVO = supplierService.findSupplierList(pageNum, pageSize, supplierVO);
        return CommonResult.success(supplierVOPageVO);
    }

    /**
     * 添加供应商信息
     * @param supplierVO
     * @return
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody SupplierVo supplierVO) {
        supplierService.add(supplierVO);
        return CommonResult.success("添加来源成功");
    }

    /**
     * 编辑供应商信息
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public CommonResult edit(@PathVariable Long id) {
        SupplierVo supplierVO = supplierService.edit(id);
        return CommonResult.success(supplierVO);
    }

    /**
     * 更新供应商信息
     * @param id
     * @param supplierVO
     * @return
     */
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody  SupplierVo supplierVO) {
        supplierService.update(id, supplierVO);
        return CommonResult.success("更新供应商信息成功");
    }

    /**
     * 删除供应商信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        supplierService.delete(id);
        return CommonResult.success("删除供应商信息成功");
    }

    /**
     * 获取所有信息供应商
     * @return
     */
    @GetMapping("/findAll")
    public CommonResult findAll() {
        List<SupplierVo> all = supplierService.findAll();
        return CommonResult.success(all);
    }

}
