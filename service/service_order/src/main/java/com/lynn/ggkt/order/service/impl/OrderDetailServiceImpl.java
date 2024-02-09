package com.lynn.ggkt.order.service.impl;

import com.lynn.ggkt.model.order.OrderDetail;
import com.lynn.ggkt.order.mapper.OrderDetailMapper;
import com.lynn.ggkt.order.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-09
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
