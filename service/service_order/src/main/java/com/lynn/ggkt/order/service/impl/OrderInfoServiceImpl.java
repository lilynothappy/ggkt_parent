package com.lynn.ggkt.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.client.activity.CouponInfoFeignClient;
import com.lynn.ggkt.client.course.CourseFeignClient;
import com.lynn.ggkt.client.user.UserInfoFeignClient;
import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.model.activity.CouponInfo;
import com.lynn.ggkt.model.order.OrderDetail;
import com.lynn.ggkt.model.order.OrderInfo;
import com.lynn.ggkt.model.user.UserInfo;
import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.order.mapper.OrderInfoMapper;
import com.lynn.ggkt.order.service.OrderDetailService;
import com.lynn.ggkt.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynn.ggkt.utils.AuthContextHolder;
import com.lynn.ggkt.utils.OrderNoUtils;
import com.lynn.ggkt.vo.order.OrderFormVo;
import com.lynn.ggkt.vo.order.OrderInfoQueryVo;
import com.lynn.ggkt.vo.order.OrderInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-09
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    //分页查询列表
    @Override
    public Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        //1.OrderInfoQueryVo获取查询值
        Long userId = orderInfoQueryVo.getUserId();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String phone = orderInfoQueryVo.getPhone();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        //2.非空判断，封装入QueryWrapper中
        if(!StringUtils.isEmpty(userId)){
            wrapper.eq("user_id",userId);
        }
        if(!StringUtils.isEmpty(outTradeNo)){
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if(!StringUtils.isEmpty(phone)){
            wrapper.eq("phone",phone);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }
        if(!StringUtils.isEmpty(orderStatus)){
            wrapper.eq("order_status",orderStatus);
        }

        //3.根据条件做分页查询
        Page<OrderInfo> pages = baseMapper.selectPage(pageParam,wrapper);
        long totalCount = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords();

        //4.根据订单id查询订单详情
        records.stream().forEach(orderInfo ->{
            this.getOrderDetail(orderInfo);
        });

        /*for(OrderInfo orderInfo : records){
            getOrderDetail(orderInfo);
        }*/

        //6.封装到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pageCount",pageCount);
        map.put("records",records);

        return map;
    }

    //生成订单方法
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        //1 获取生成订单条件值
        Long couponId = orderFormVo.getCouponId();
        Long courseId = orderFormVo.getCourseId();
        Long userId = AuthContextHolder.getUserId();

        //2 判断当前用户,针对当前课程是否已经生成订单
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getCourseId,courseId);
        wrapper.eq(OrderDetail::getUserId,userId);
        OrderDetail queryOrderDetail = orderDetailService.getOne(wrapper);
        if(queryOrderDetail != null){
            return queryOrderDetail.getId(); //订单已存在，返回订单id
        }

        //3 根据课程id查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if(course == null){
            throw new GgktException(20001,"课程不存在");
        }

        //4 根据用户id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if(userInfo == null){
            throw new GgktException(20001,"用户不存在");
        }

        //5 根据优惠卷id查询优惠卷信息
        BigDecimal couponReduce = new BigDecimal(0);
        if(couponId != null){
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();
        }

        //6 封装订单到对象，添加到数据库
        //6.1 封装数据到OrderInfo对象
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());  //用工具类生成随机订单号
        orderInfo.setTradeBody(course.getTitle()); //课程名称
        orderInfo.setOrderStatus("0");

        baseMapper.insert(orderInfo);

        //6.2 封装数据到OrderDetail对象
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(couponReduce); //老师代码：new BigDecimal(0)
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));

        orderDetailService.save(orderDetail);

        //7 更新优惠卷状态
        if(null != orderFormVo.getCouponUseId()){
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(),orderInfo.getId());
        }

        //8 返回订单id
        return orderInfo.getId();
    }

    //根据订单id获取订单信息（前台展示）
    @Override
    public OrderInfoVo getOrderInfoVoById(Long id) {
        OrderInfo orderInfo = baseMapper.selectById(id);
        OrderDetail orderDetail = orderDetailService.getById(id);

        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfo,orderInfoVo);
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());

        return orderInfoVo;
    }

    @Override
    public void updateOrderStatus(String out_trade_no) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);

        orderInfo.setOrderStatus("1");
        baseMapper.updateById(orderInfo);
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());
        if(orderDetail != null){
            String courseName = orderDetail.getCourseName();
            //5.将详情封装进对象
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
