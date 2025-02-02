package com.javahomework.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOrder {
    private String outTradeNo; // 订单号
    private String totalAmount; // 订单金额
    private String subject; // 订单标题
}