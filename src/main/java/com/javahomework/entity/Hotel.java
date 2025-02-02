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
public class Hotel implements Serializable {

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
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String txt;

    private String img;

    private Integer pid;

    private Double tickets;

    private Double distance;
}
