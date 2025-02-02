package com.javahomework.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.javahomework.common.AlipayConfig;
import com.javahomework.entity.PayOrder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay")
public class AlipayController {

    @Autowired
    private AlipayConfig alipayConfig;

    @PostMapping("/create")
    public String createOrder(@RequestBody PayOrder order) throws AlipayApiException {
        // 初始化AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getGateway(), // 网关地址
                alipayConfig.getAppId(),      // APP_ID
                alipayConfig.getPrivateKey(), // 应用私钥
                "json",                       // 返回格式
                "UTF-8",                      // 字符集
                alipayConfig.getAlipayPublicKey(), // 支付宝公钥
                "RSA2"                        // 签名算法
        );

        // 创建支付请求
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayConfig.getReturnUrl()); // 同步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl()); // 异步通知地址

        // 设置支付参数
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(order.getOutTradeNo()); // 订单号
        model.setTotalAmount(order.getTotalAmount()); // 金额
        model.setSubject(order.getSubject()); // 订单标题
        model.setProductCode("FAST_INSTANT_TRADE_PAY"); // 销售产品码（固定值）
        request.setBizModel(model);

        // 发起支付请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            return response.getBody(); // 返回支付表单HTML
        } else {
            throw new RuntimeException("支付宝支付请求失败：" + response.getMsg());
        }
    }

    @PostMapping("/notify")
    public String handleNotify(HttpServletRequest request) {
        Map<String, String> params = convertRequestToMap(request);
        try {
            boolean signVerified = com.alipay.api.internal.util.AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    "UTF-8",
                    "RSA2"
            );

            if (signVerified) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    // 更新订单状态为已支付
                    String outTradeNo = params.get("out_trade_no");
                    System.out.println("订单支付成功，订单号：" + outTradeNo);
                    return "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("订单支付失败：" + e.getMessage());
        }
        return "failure";
    }

    private Map<String, String> convertRequestToMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}