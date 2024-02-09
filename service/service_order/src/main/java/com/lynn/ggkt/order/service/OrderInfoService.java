package com.lynn.ggkt.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.order.OrderInfo;
import com.lynn.ggkt.vo.order.OrderFormVo;
import com.lynn.ggkt.vo.order.OrderInfoQueryVo;
import com.lynn.ggkt.vo.order.OrderInfoVo;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-09
 */
public interface OrderInfoService extends IService<OrderInfo> {

    ////分页查询列表
    Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo);

    //生成订单方法
    Long submitOrder(OrderFormVo orderFormVo);

    //获取订单信息（前台展示）
    OrderInfoVo getOrderInfoVoById(Long id);

    //根据订单号更新订单状态
    void updateOrderStatus(String out_trade_no);
}
