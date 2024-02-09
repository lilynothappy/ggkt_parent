package com.lynn.ggkt.client.activity;

import com.lynn.ggkt.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "service-activity")
public interface CouponInfoFeignClient {

    //根据优惠卷id查询优惠卷信息
    @GetMapping("/api/activity/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable  Long couponId);

    //更新优惠卷状态
    @PutMapping(value = "/api/activity/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId);
}
