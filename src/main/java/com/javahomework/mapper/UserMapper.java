package com.javahomework.mapper;

import com.javahomework.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javahomework
 * @since 2023-12-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> listAll();
}
