package com.lynn.ggkt.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lynn.ggkt.model.live.LiveCourse;
import com.lynn.ggkt.vo.live.LiveCourseVo;

import java.util.List;

/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    //获取最近的直播列表
    List<LiveCourseVo> findLatelyList();
}
