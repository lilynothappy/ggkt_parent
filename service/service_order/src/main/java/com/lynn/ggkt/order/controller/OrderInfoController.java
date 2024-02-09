package com.lynn.ggkt.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.model.order.OrderInfo;
import com.lynn.ggkt.order.service.OrderInfoService;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.order.OrderInfoQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-09
 */

@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order/orderInfo")
//@CrossOrigin
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    //分页查询列表
    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable Long page, @PathVariable Long limit, OrderInfoQueryVo orderInfoQueryVo){
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        Map<String ,Object> map = orderInfoService.selectOrderInfoPage(pageParam,orderInfoQueryVo);
        return Result.ok(map);

    }
}

