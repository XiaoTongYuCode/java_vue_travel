package com.javahomework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 景区id
     */
    private Integer scenicId;

    /**
     * user账号
     */
    private String no;

    /**
     * user姓名
     */
    private String name;

    /**
     * 内容
     */
    private String txt;

    /**
     * 时间
     */
    private Long time;
}
