package com.lynn.ggkt.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.live.LiveCourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseDescriptionService extends IService<LiveCourseDescription> {

    //根据课程id查询描述信息
    LiveCourseDescription getLiveCourseById(Long id);
}
