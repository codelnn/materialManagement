package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.service.ConsumerService;
import com.codelnn.emms.vo.ConsumerVo;
import com.codelnn.emms.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-19 12:14
 **/
@RestController
@RequestMapping(value = "/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    /**
     * 分页   获取物质去处所有列表
     * @param pageNum
     * @param pageSize
     * @param consumerVO
     * @return
     */
    @GetMapping("/findConsumerList")
    public CommonResult findConsumerList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         ConsumerVo consumerVO) {
        PageVO<ConsumerVo> consumerVOPageVO = consumerService.findConsumerList(pageNum, pageSize, consumerVO);
        return CommonResult.success(consumerVOPageVO);
    }

    /**
     * 添加物质去向
     * @param consumerVO
     * @return
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody @Validated ConsumerVo consumerVO) {
        consumerService.add(consumerVO);
        return CommonResult.success();
    }

    /**
     * 编辑物质去向
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public CommonResult edit(@PathVariable Long id) {
        ConsumerVo consumerVO = consumerService.edit(id);
        return CommonResult.success(consumerVO);
    }

    /**
     * 更新物质去向
     * @param id
     * @param consumerVO
     * @return
     */
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody  ConsumerVo consumerVO) {
        consumerService.update(id, consumerVO);
        return CommonResult.success();
    }

    /**
     * 删除物质去向
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        consumerService.delete(id);
        return CommonResult.success();
    }

    /**
     * 查询所有物质去向
     * @return
     */
    @GetMapping("/findAll")
    public CommonResult findAll() {
        List<ConsumerVo> consumerVOS = consumerService.findAll();
        return CommonResult.success(consumerVOS);
    }
    
    
}
