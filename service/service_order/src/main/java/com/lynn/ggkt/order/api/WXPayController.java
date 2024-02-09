package com.lynn.ggkt.order.api;

import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.order.service.OrderInfoService;
import com.lynn.ggkt.order.service.WXPayService;
import com.lynn.ggkt.result.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/order/wxPay")
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private OrderInfoService orderInfoService;

    //微信支付
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@ApiParam(name = "orderNo", value = "订单No", required = true)
                              @PathVariable("orderNo") String orderNo) {
        Map<String,Object> map = wxPayService.createJsapi(orderNo);
        return Result.ok(map);
    }

    //查询支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) { //参数非订单id，在数据库中为out_trade_no
        //根据订单号调用微信接口查询支付状态
        Map<String,String> resultMap = wxPayService.queryPayStatus(orderNo);
        //判断支付是否成功：根据微信支付状态接口判断
        if(resultMap == null){
            throw new GgktException(20001,"支付失败");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) { //支付成功
            //根据订单号更新订单状态
            String out_trade_no = resultMap.get("out_trade_no");
            orderInfoService.updateOrderStatus(out_trade_no);

            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");

    }


}
