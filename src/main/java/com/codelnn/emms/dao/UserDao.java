package com.codelnn.emms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codelnn.emms.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 17:40
 **/
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}
