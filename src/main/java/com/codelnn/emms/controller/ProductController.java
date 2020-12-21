package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.service.ProductService;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.ProductStockVo;
import com.codelnn.emms.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:19
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取商品列表
     */
    @GetMapping("/findProductList")
    public CommonResult findProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "categorys", required = false) String categorys,
                                        ProductVo productVo) {
        buildCategorySearch(categorys,productVo);
        PageVO<ProductVo> productList = productService.findProductList(pageNum, pageSize, productVo);
        return CommonResult.success(productList);
    }

    /**
     * 可入库商品（入库页面使用）
     * @param pageNum
     * @param pageSize
     * @param categorys
     * @param productVo
     * @return
     */
    @GetMapping("/findProducts")
    public CommonResult findProducts(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value = "categorys", required = false) String categorys,
                                     ProductVo productVo){
        productVo.setStatus(0);
        buildCategorySearch(categorys, productVo);
        PageVO<ProductVo> productList = productService.findProductList(pageNum, pageSize, productVo);
        return CommonResult.success(productList);
    }

    /**
     * 查询库存列表
     * @param pageNum
     * @param pageSize
     * @param categorys
     * @param productVo
     * @return
     */
    @GetMapping("/findProductStocks")
    public CommonResult findProductStocks(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize") Integer pageSize,
                                          @RequestParam(value = "categorys", required = false) String categorys,
                                          ProductVo productVo) {
        buildCategorySearch(categorys, productVo);
        PageVO<ProductStockVo> productStocks = productService.findProductStocks(pageNum, pageSize, productVo);
        return CommonResult.success(productStocks);
    }

    /**
     * 查询所有库存
     * @param pageNum
     * @param pageSize
     * @param categorys
     * @param productVo
     * @return
     */
    @GetMapping("/findAllStocks")
    public CommonResult findAllStocks(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize") Integer pageSize,
                                      @RequestParam(value = "categorys", required = false) String categorys,
                                      ProductVo productVo) {
        buildCategorySearch(categorys, productVo);
        List<ProductStockVo> allStocks = productService.findAllStocks(productVo);
        return CommonResult.success(allStocks);
    }

    /**
     * 新增商品
     * @param productVo
     * @return
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody ProductVo productVo) {
        if (productVo.getCategoryKeys().length != 3) {
            return CommonResult.error("物资需要3级分类");
        }
        productService.add(productVo);
        return CommonResult.success("新增商品成功");
    }

    /**
     * 编辑商品
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public CommonResult edit(@PathVariable Long id) {
        ProductVo edit = productService.edit(id);
        return CommonResult.success(edit);
    }

    /**
     * 更新商品
     * @param id
     * @param productVo
     * @return
     */
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody ProductVo productVo) {
        if (productVo.getCategoryKeys().length != 3) {
            return CommonResult.error("物资需要3级分类");
        }
        productService.update(id, productVo);
        return CommonResult.success("更新商品信息成功");
    }

    /**
     * 删除商品信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        productService.delete(id);
        return CommonResult.success("删除商品成功");
    }


    @PutMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Long id) {
        productService.remove(id);
        return CommonResult.success();
    }

    @PutMapping("/publish/{id}")
    public CommonResult publish(@PathVariable Long id) {
        productService.publish(id);
        return CommonResult.success();
    }


    @PutMapping("/back/{id}")
    public CommonResult back(@PathVariable Long id) {
        productService.back(id);
        return CommonResult.success();
    }

    /**
     * 封装物资查询条件
     * @param categorys
     * @param productVo
     */
    private void buildCategorySearch(@RequestParam(value = "categorys", required = false) String categorys, ProductVo productVo) {
        if (categorys != null && !"".equals(categorys)) {
            String[] split = categorys.split(",");
            switch (split.length) {
                case 1:
                    productVo.setOneCategoryId(Long.parseLong(split[0]));
                    break;
                case 2:
                    productVo.setOneCategoryId(Long.parseLong(split[0]));
                    productVo.setTwoCategoryId(Long.parseLong(split[1]));
                    break;
                case 3:
                    productVo.setOneCategoryId(Long.parseLong(split[0]));
                    productVo.setTwoCategoryId(Long.parseLong(split[1]));
                    productVo.setThreeCategoryId(Long.parseLong(split[2]));
                    break;
            }
        }
    }

}
