package com.lynn.ggkt.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.order.service.WXPayService;
import com.lynn.ggkt.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {

    //微信支付
    @Override
    public Map<String, Object> createJsapi(String orderNo) {
        //封装微信支付需要参数，使用map集合
        Map<String, String> paramMap = new HashMap();
        //1、设置参数
        paramMap.put("appid", "wxf913bfa3a2c7eeeb");  //商户id
        paramMap.put("mch_id", "1481962542");  //商户号
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", "test");
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("total_fee", "1");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://glkt.atguigu.cn/api/order/wxPay/notify");
        paramMap.put("trade_type", "JSAPI"); //支付类型，按照生成的固定金额支付
        /*
         * 设置参数值当前微信用户openid
         * 实现逻辑：第一步 根据订单号获取userid  第二步 根据userid获取openid
         *
         * 因为当前使用测试号，测试号不支持支付功能，为了使用正式服务号进行测试，使用下面写法
         * 获取 正式服务号微信openid
         *
         * 通过其他方式获取正式服务号openid，直接设置
         * */

        paramMap.put("openid", "oQTXC56lAy3xMOCkKCImHtHoLL");


        try {
            //通过httpclient调用微信支付接口
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置请求参数
            String paramXml = WXPayUtil.generateSignedXml(paramMap,"MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"); //商户key
            client.setXmlParam(paramXml);
            client.setHttps(true);
            //发送请求
            client.post();

            //微信支付接口返回数据
            String xml = client.getContent();
            System.out.println("xml:"+xml);

            //进行封装，最终返回
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);
            if(null != resultMap.get("result_code")  && !"SUCCESS".equals(resultMap.get("result_code"))) {
                throw new GgktException(20001,"支付失败");
            }

            //4、再次封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", "wxf913bfa3a2c7eeeb");
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9");

            //返回结果
            Map<String,Object> result = new HashMap<>();
            result.put("appId", "wxf913bfa3a2c7eeeb");
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            System.out.println(result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

    }

    //查询订单支付状态（从前端返回数据中）
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        //1、封装参数
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid","wxf913bfa3a2c7eeeb");
        paramMap.put("mch_id","1481962542");
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());


        try {
            //2、设置请求,调用接口httpclient
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,"MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
