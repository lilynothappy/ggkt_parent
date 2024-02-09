package com.lynn.ggkt.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.activity.CouponInfo;
import com.lynn.ggkt.model.activity.CouponUse;
import com.lynn.ggkt.vo.activity.CouponUseQueryVo;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //条件查询分页
    IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);

    //更新优惠卷状态
    void updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}
