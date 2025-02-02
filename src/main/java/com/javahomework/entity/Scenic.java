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
public class Scenic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 景区名称
     */
    private String name;

    /**
     * 景区类别
     */
    private String type;

    /**
     * 景区详情
     */
    private String details;

    /**
     * 门票价格
     */
    private Double tickets;

    private String img;
}
