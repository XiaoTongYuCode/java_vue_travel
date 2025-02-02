package com.javahomework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author com
 * @since 2024-09-06
 */
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    @TableId(value = "no")  // 指定主键字段
    private String no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色 0管理, 1用户
     */
    private Integer roleId;

    private Integer money;
}
