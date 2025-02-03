package com.javahomework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("order_form")
public class OrderForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时间
     */
    private Long id;

    /**
     * user账号
     */
    private String no;

    /**
     * user姓名
     */
    private String name;

    /**
     * 景区id
     */
    private Integer scenicId;

    /**
     * 景区名称
     */
    private String scenicName;

    /**
     * 价格
     */
    private Double tickets;

    private String checks;

    @TableId(value = "pid", type = IdType.AUTO)
    private Integer pid;
    private Integer num;
    private String date;
}
