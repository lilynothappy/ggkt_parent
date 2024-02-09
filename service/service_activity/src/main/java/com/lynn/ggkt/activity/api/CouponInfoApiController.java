package com.lynn.ggkt.activity.api;

import com.lynn.ggkt.activity.service.CouponInfoService;
import com.lynn.ggkt.model.activity.CouponInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity/couponInfo")
public class CouponInfoApiController {

    @Autowired
    private CouponInfoService couponInfoService;

    //根据优惠卷id查询优惠卷信息
    @GetMapping("inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable  Long couponId){
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return couponInfo;
    }

    //更新优惠卷状态
    @PutMapping(value = "inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId) {
        couponInfoService.updateCouponInfoUseStatus(couponUseId, orderId);
        return true;
    }
}
