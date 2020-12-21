package com.codelnn.emms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codelnn.emms.dao.MenuDao;
import com.codelnn.emms.dao.UserDao;
import com.codelnn.emms.entity.MenuEntity;
import com.codelnn.emms.entity.UserEntity;
import com.codelnn.emms.enums.UserStatusEnum;
import com.codelnn.emms.exception.ErrorCodeEnum;
import com.codelnn.emms.exception.ServiceException;
import com.codelnn.emms.service.UserService;
import com.codelnn.emms.shiro.JwtToken;
import com.codelnn.emms.utils.JwtUtil;
import com.codelnn.emms.utils.Md5Util;
import com.codelnn.emms.utils.MenuTreeBuilderUtil;
import com.codelnn.emms.vo.MenuNodeVO;
import com.codelnn.emms.vo.PageVO;
import com.codelnn.emms.vo.UserInfoVo;
import com.codelnn.emms.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 18:20
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public UserEntity findUserByName(String name) {
        return userDao.selectOne(new QueryWrapper<UserEntity>().eq("username",name));
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        UserEntity userByName = findUserByName(username);
        if (userByName == null){
            throw new ServiceException(ErrorCodeEnum.USER_ACCOUNT_NOT_FOUND);
        }else {
            String salt = userByName.getSalt();
            String target = Md5Util.md5Encryption(password, salt);
            // 生成token
            if (userByName.getPassword().equals(target)){
                token = JwtUtil.sign(username,target);
                JwtToken jwtToken = new JwtToken(token);
                try {
                    SecurityUtils.getSubject().login(jwtToken);
                } catch (AuthenticationException e) {
                    throw new ServiceException(e.getMessage());
                }
            }else {
                throw new ServiceException(ErrorCodeEnum.USER_PASSWORD_ERROR);
            }
        }
        return token;
    }

    @Override
    public void deleteById(Long id) {
        UserEntity userEntity = userDao.selectById(id);
        if (userEntity == null){
            throw new ServiceException("要删除的用户不存在");
        }
        UserEntity userEntity2 = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        if (userEntity2.getId().equals(id)){
            throw new ServiceException("不能删除当前登入用户");
        }
        userDao.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(UserEntity userEntity) {
        String username = userEntity.getUsername();
        UserEntity userByName = findUserByName(username);
        if (userByName != null){
            throw new ServiceException("该用户名已被占用");
        }
        String salt = UUID.randomUUID().toString().substring(0, 32);
        userEntity.setSalt(salt);
        userEntity.setPassword(Md5Util.md5Encryption(userEntity.getPassword(),salt));
        userEntity.setCreateTime(new Date());
        userEntity.setModifiedTime(new Date());
        /**
         * 设置 新添加的用户默认启用
         */
        userEntity.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());
        userDao.insert(userEntity);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        UserEntity userEntity = userDao.selectById(id);
        if (userEntity == null){
            throw new ServiceException("要更新状态的用户不存在");
        }
        userEntity.setStatus(status?UserStatusEnum.DISABLE.getStatusCode():UserStatusEnum.AVAILABLE.getStatusCode());
        userDao.updateById(userEntity);
    }

    @Override
    public UserInfoVo info() {
        UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        if (userEntity == null){
            throw new ServiceException("该用户不存在");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userEntity,userInfoVo);

        return userInfoVo;
    }

    @Override
    public List<MenuNodeVO> findMenu() {
        List<MenuEntity> menuEntityList1 = menuDao.selectList(new QueryWrapper<MenuEntity>(null));
        List<MenuNodeVO> menuNodeVOS = menuEntityList1.stream().map(menuEntity -> {
            MenuNodeVO menuNodeVO = new MenuNodeVO();
            BeanUtils.copyProperties(menuEntity, menuNodeVO);
            if (menuEntity.getAvailable() == 1) {
                menuNodeVO.setDisabled(false);
            } else {
                menuNodeVO.setDisabled(true);
            }
            return menuNodeVO;
        }).collect(Collectors.toList());
       return MenuTreeBuilderUtil.build(menuNodeVOS);
    }


    @Override
    public PageVO<UserVo> findUserList(Integer pageNum, Integer pageSize, UserVo userVO) {
        Page<UserEntity> userEntityPage = new Page<>(pageNum, pageSize);
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<UserEntity>();
        String username = userVO.getUsername();
        String nickname = userVO.getNickname();
        String email = userVO.getEmail();
        Integer sex = userVO.getSex();
        if (!StringUtils.isEmpty(username)){
            userEntityQueryWrapper.like("username",username);
        }
        if (!StringUtils.isEmpty(nickname)){
            userEntityQueryWrapper.like("nickname",nickname);
        }
        if (!StringUtils.isEmpty(email)){
            userEntityQueryWrapper.like("email",email);
        }
        if (sex != null){
            userEntityQueryWrapper.eq("sex",sex);
        }
        Page<UserEntity> userEntityPage1 = userDao.selectPage(userEntityPage, userEntityQueryWrapper);
        List<UserEntity> records = userEntityPage1.getRecords();
        List<UserVo> collect = records.stream().map(userEntity -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userEntity, userVo);
            userVo.setStatus(userEntity.getStatus()==1?false:true);
            return userVo;
        }).collect(Collectors.toList());
        collect.sort(new Comparator<UserVo>() {
            @Override
            public int compare(UserVo o1, UserVo o2) {
                return (int) (o1.getId()-o2.getId());
            }
        });
        long total = userEntityPage1.getTotal();

        return new PageVO<>(total,collect);
    }


    @Override
    public List<UserVo> findAll() {
        List<UserEntity> userEntities = userDao.selectList(null);
        List<UserVo> collect = userEntities.stream().map(userEntity -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userEntity, userVo);
            return userVo;
        }).collect(Collectors.toList());
        collect.sort(new Comparator<UserVo>() {
            @Override
            public int compare(UserVo o1, UserVo o2) {
                return  (int) (o1.getId()-o2.getId());
            }
        });
        return collect;
    }


    @Override
    public UserVo edit(Long id) {
        UserEntity userEntity = userDao.selectById(id);
        if (userEntity == null){
            throw new ServiceException("要编辑的用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userEntity,userVo);
        userVo.setStatus(userEntity.getStatus()==1?false:true);
        return userVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Long id, UserVo userVo) {
        UserEntity userEntity = userDao.selectById(id);
        if (userEntity == null){
            throw new ServiceException("要更新的用户不存在");
        }
        UserEntity userEntity1 = new UserEntity();
        BeanUtils.copyProperties(userVo,userEntity1);
        userEntity1.setModifiedTime(new Date());
        userDao.updateById(userEntity1);
    }
}
