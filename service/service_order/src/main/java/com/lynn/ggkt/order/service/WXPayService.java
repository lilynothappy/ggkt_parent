package com.lynn.ggkt.order.service;

import java.util.Map;

public interface WXPayService {

    //微信支付
    Map<String, Object> createJsapi(String orderNo);

    //查询订单支付状态（从前端返回数据中）
    Map<String, String> queryPayStatus(String orderNo);
}
