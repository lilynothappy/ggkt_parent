package com.lynn.ggkt.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.activity.mapper.CouponInfoMapper;
import com.lynn.ggkt.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynn.ggkt.activity.service.CouponUseService;
import com.lynn.ggkt.client.user.UserInfoFeignClient;
import com.lynn.ggkt.model.activity.CouponInfo;
import com.lynn.ggkt.model.activity.CouponUse;
import com.lynn.ggkt.model.user.UserInfo;
import com.lynn.ggkt.vo.activity.CouponUseQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponUseService couponUseService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    //条件分页查询
    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {
        //1.取出查询条件值
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();

        QueryWrapper<CouponUse> wrapper = new QueryWrapper<>();
        //2.非空判断，封装入QueryWrapper中
        if (!StringUtils.isEmpty(couponId)) {
            wrapper.eq("coupon_id", couponId);
        }
        if (!StringUtils.isEmpty(couponStatus)) {
            wrapper.eq("coupon_status", couponStatus);
        }
        if (!StringUtils.isEmpty(getTimeBegin)) {
            wrapper.ge("get_time", getTimeBegin);
        }
        if (!StringUtils.isEmpty(getTimeEnd)) {
            wrapper.le("get_time", getTimeEnd);
        }
        //3.根据条件调用page方法做分页查询
        IPage<CouponUse> iPage = couponUseService.page(pageParam, wrapper);
        List<CouponUse> records = iPage.getRecords();

        //4.循环列表，查询user详情
        for (CouponUse couponUse : records) {
            this.getUserInfoById(couponUse);
        }
//        records.stream().forEach(couponUse ->{
//            this.getUserInfoById(couponUse);
//        });

        return iPage;
    }

    //更新优惠卷状态
    @Override
    public void updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponUseId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("1");
        couponUse.setUsingTime(new Date());
        couponUseService.updateById(couponUse);
    }

    //5.根据用户id，通过远程调用得到用户信息
    private CouponUse getUserInfoById(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if(!StringUtils.isEmpty(userId)){
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if(userInfo != null){
                //封装用户昵称和手机号
                couponUse.getParam().put("nickName",userInfo.getNickName());
                couponUse.getParam().put("phone",userInfo.getPhone());
            }
        }
        return couponUse;
    }


}


