package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.entity.ConsumerEntity;
import com.codelnn.emms.service.ConsumerService;
import com.codelnn.emms.service.OutStockService;
import com.codelnn.emms.vo.ConsumerVo;
import com.codelnn.emms.vo.OutStockDetailVo;
import com.codelnn.emms.vo.OutStockVo;
import com.codelnn.emms.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-20 15:36
 **/
@RestController
@RequestMapping("/outStock")
public class OutStockController {

    @Autowired
    private OutStockService outStockService;
    
    @Autowired
    private ConsumerService consumerService;

    /**
     * 提交物质发放单
     * @param outStockVO
     * @return
     */
    @PostMapping("/addOutStock")
    public CommonResult addOutStock(@RequestBody @Validated OutStockVo outStockVO){
        if(outStockVO.getConsumerId()==null){
            //说明现在添加物资来源
            ConsumerVo consumerVO = new ConsumerVo();
            BeanUtils.copyProperties(outStockVO,consumerVO);
            if("".equals(consumerVO.getName())||consumerVO.getName()==null){
                return CommonResult.error("物资去向名不能为空");
            }
            if("".equals(consumerVO.getContact())||consumerVO.getContact()==null){
                return CommonResult.error("联系人不能为空");
            }

            if("".equals(consumerVO.getPhone())||consumerVO.getPhone()==null){
                return CommonResult.error("联系方式不能为空");
            }
            if(consumerVO.getSort()==null){
                return CommonResult.error("排序不能为空");
            }
            ConsumerEntity consumer = consumerService.add(consumerVO);
            outStockVO.setConsumerId(consumer.getId());
        }
        //提交发放单
        outStockService.addOutStock(outStockVO);
        return CommonResult.success();
    }

    /**
     * 发放单列表
     * @param pageNum
     * @param pageSize
     * @param outStockVO
     * @return
     */
    @GetMapping("/findOutStockList")
    public CommonResult findInStockList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize,
            OutStockVo outStockVO) {
        PageVO<OutStockVo> outStockList = outStockService.findOutStockList(pageNum, pageSize, outStockVO);
        return CommonResult.success(outStockList);
    }

    /**
     * 移入回收站
     * @param id
     * @return
     */
    @PutMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Long id) {
        outStockService.remove(id);
        return CommonResult.success();
    }

    /**
     *  物资发放单详细
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/detail/{id}")
    public CommonResult detail(@PathVariable Long id,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "3") Integer pageSize) {
        OutStockDetailVo detail = outStockService.detail(id,pageNum,pageSize);
        return CommonResult.success(detail);
    }

    /**
     * 删除物资发放单
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        outStockService.delete(id);
        return CommonResult.success();
    }

    /**
     * 发放审核
     * @param id
     * @return
     */
    @PutMapping("/publish/{id}")
    public CommonResult publish(@PathVariable Long id) {
        outStockService.publish(id);
        return CommonResult.success();
    }

    /**
     * 恢复数据从回收站
     * @param id
     * @return
     */
    @PutMapping("/back/{id}")
    public CommonResult back(@PathVariable Long id) {
        outStockService.back(id);
        return CommonResult.success();
    }
    
    
}
