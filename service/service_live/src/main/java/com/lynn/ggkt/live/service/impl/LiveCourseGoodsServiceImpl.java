package com.lynn.ggkt.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lynn.ggkt.live.mapper.LiveCourseGoodsMapper;
import com.lynn.ggkt.live.service.LiveCourseGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynn.ggkt.model.live.LiveCourseGoods;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
@Service
public class LiveCourseGoodsServiceImpl extends ServiceImpl<LiveCourseGoodsMapper, LiveCourseGoods> implements LiveCourseGoodsService {

    //根据课程id课程商品列表
    @Override
    public List<LiveCourseGoods> getListByCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,id);
        List<LiveCourseGoods> list = baseMapper.selectList(wrapper);
        return list;
    }
}
