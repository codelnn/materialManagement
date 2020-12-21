package com.codelnn.emms.service;

import com.codelnn.emms.entity.UserEntity;
import com.codelnn.emms.vo.MenuNodeVO;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.UserInfoVo;
import com.codelnn.emms.vo.UserVo;

import java.util.List;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 18:20
 **/
public interface UserService {

   /**
    * 根据用户名查询用户
    * @param name
    * @return
    */
   UserEntity findUserByName(String name);

   /**
    * 用户登入
    *
    * @param username
    * @param password
    * @return
    */
   String login(String username, String password);


   /**
    * 根据id删除用户
    * @param id
    */
   void deleteById(Long id);

   /**
    * 添加一个用户
    * @param userEntity
    */
   void add(UserEntity userEntity);

   /**
    * 更新状态
    *
    * @param id
    * @param status
    */
   void updateStatus(Long id, Boolean status);

   /**
    * 用户详情
    * @return
    */
   UserInfoVo info();

   /**
    * 获取菜单
    * @return
    */
   List<MenuNodeVO> findMenu();

   /**
    * 用户列表
    * @param userVO
    * @return
    */
   PageVO<UserVo> findUserList(Integer pageNum, Integer pageSize, UserVo userVO);


   /**
    * 查询所有用户
    */
   List<UserVo> findAll();

   /**
    * 编辑用户
    * @param id
    * @return
    */
    UserVo edit(Long id);


   /**
    * 更新用户
    * @param id
    * @param userVo
    */
   void update(Long id,UserVo userVo);


}
