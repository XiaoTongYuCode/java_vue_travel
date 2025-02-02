package com.javahomework.service.impl;

import com.javahomework.entity.User;
import com.javahomework.mapper.UserMapper;
import com.javahomework.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javahomework
 * @since 2023-12-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void delFile(String id) {
        //删除云盘
        String folderPath =System.getProperty("user.dir") + "/static/file/" + id + "/";
        // 创建一个File对象
        File folder = new File(folderPath);
        // 检查文件夹是否存在
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if(files !=null){
                for (File file : files){
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }
}
