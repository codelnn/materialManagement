package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.service.ProductCategoryService;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductCategoryTreeNodeVo;
import com.codelnn.emms.vo.ProductCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:  物资类别接口
 * @author: znx
 * @create: 2020-12-18 16:06
 **/
@RestController
@RequestMapping(value = "/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 添加分类信息
     * @param productCategoryVo
     * @return
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody ProductCategoryVo productCategoryVo){
        productCategoryService.add(productCategoryVo);
        return CommonResult.success();
    }

    /**
     * 编辑分类信息
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public CommonResult edit(@PathVariable("id") Long id){
        ProductCategoryVo edit = productCategoryService.edit(id);
        return CommonResult.success(edit);
    }

    /**
     * 更新分类信息
     * @param id
     * @param productCategoryVo
     * @return
     */
    @PutMapping("/update/{id}")
   public CommonResult update(@PathVariable("id") Long id,@RequestBody ProductCategoryVo productCategoryVo){
        productCategoryService.update(id,productCategoryVo);
        return CommonResult.success();
   }

    /**
     * 删除分类信息
     * @param id
     * @return
     */
   @DeleteMapping("/delete/{id}")
   public CommonResult delete(@PathVariable("id") Long id){
        productCategoryService.delete(id);
        return CommonResult.success();
   }

    /**
     * 查询分类所有信息
     *  分类树形结构(分页)
     * @return
     */
   @GetMapping("/categoryTree")
   public CommonResult categoryTree(@RequestParam(value = "pageNum", required = false)Integer pageNum,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize){
       PageVO<ProductCategoryTreeNodeVo> nodeVoPageVO = productCategoryService.categoryTree(pageNum, pageSize);
       return CommonResult.success(nodeVoPageVO);
   }

    /**
     * 获取父级分类树：2级树
     * @return
     */
    @GetMapping("/getParentCategoryTree")
    public CommonResult getParentCategoryTree() {
        List<ProductCategoryTreeNodeVo> parentTree = productCategoryService.getParentCategoryTree();
        return CommonResult.success(parentTree);
    }

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping("/findAll")
    public CommonResult findAll() {
        List<ProductCategoryVo> productCategoryVOS = productCategoryService.findAll();
        return CommonResult.success(productCategoryVOS);
    }

    /**
     * 物资分类列表,根据物资分类名模糊查询
     * @param pageNum
     * @param pageSize
     * @param productCategoryVO
     * @return
     */
    @GetMapping("/findProductCategoryList")
    public CommonResult findProductCategoryList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize,
            ProductCategoryVo productCategoryVO) {
        PageVO<ProductCategoryVo> departmentsList = productCategoryService.findProductCategoryList(pageNum, pageSize, productCategoryVO);
        return CommonResult.success(departmentsList);
    }




}
