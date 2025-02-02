package com.javahomework.service;

import com.javahomework.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javahomework
 * @since 2023-12-11
 */
public interface IUserService extends IService<User> {

    public void delFile(String id);

    public List<User> listAll();

}
