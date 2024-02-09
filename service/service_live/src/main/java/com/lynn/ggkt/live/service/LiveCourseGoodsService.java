package com.lynn.ggkt.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.live.LiveCourseGoods;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseGoodsService extends IService<LiveCourseGoods> {

    //根据课程id课程商品列表
    List<LiveCourseGoods> getListByCourseId(Long id);
}
