package com.codelnn.emms.controller;

import com.codelnn.emms.common.CommonResult;
import com.codelnn.emms.entity.UserEntity;
import com.codelnn.emms.service.UserService;
import com.codelnn.emms.vo.MenuNodeVO;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.UserInfoVo;
import com.codelnn.emms.vo.UserVo;
import com.wuwenze.poi.ExcelKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 18:22
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public CommonResult login(@NotBlank(message = "账号必填") String username,
                              @NotBlank(message = "密码必填") String password) {
        String token = userService.login(username, password);
        return CommonResult.success((Object) token);
    }

    @GetMapping("/info")
    public CommonResult info(){
        UserInfoVo info = userService.info();
        return CommonResult.success(info);
    }

    @GetMapping("/findMenu")
    public CommonResult findMenu() {
        List<MenuNodeVO> menuTreeVOS = userService.findMenu();
        return CommonResult.success(menuTreeVOS);
    }

    /**
     * 用户列表
     *
     * @return
     */
    @GetMapping("/findUserList")
    public CommonResult findUserList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                     UserVo userVO) {
        PageVO<UserVo> userList = userService.findUserList(pageNum, pageSize, userVO);
        return CommonResult.success(userList);
    }

    /**
     * 添加用户
     * @param userEntity
     * @return
     */
    @PostMapping("/add")
    public CommonResult add(@RequestBody UserEntity userEntity){
        System.out.println(userEntity);
        userService.add(userEntity);
        return CommonResult.success();
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        userService.deleteById(id);
        return CommonResult.success();
    }

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/updateStatus/{id}/{status}")
    public CommonResult updateStatus(@PathVariable Long id, @PathVariable Boolean status) {
        userService.updateStatus(id, status);
        return CommonResult.success();
    }

    /**
     * 导出excel
     * @param response
     */
    @PostMapping("/excel")
    public void export(HttpServletResponse response) {
        List<UserVo> users = this.userService.findAll();
        try {
            ExcelKit.$Export(UserVo.class, response).downXlsx(users, false);
        } catch (Exception e) {
            throw new RuntimeException("导入Excel失败");
        }
    }

    /**
     * 编辑用户
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public CommonResult edit(@PathVariable Long id) {
        UserVo userVo = userService.edit(id);
        return CommonResult.success(userVo);
    }

    /**
     * 更新用户信息
     * @param id
     * @param userVo
     * @return
     */
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UserVo userVo) {
        userService.update(id, userVo);
        return CommonResult.success();
    }


}
