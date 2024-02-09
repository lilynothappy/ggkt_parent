package com.lynn.ggkt.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.live.LiveCourseConfig;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {

    //根据courseId获得基本配置信息
    LiveCourseConfig getByCourseId(Long id);
}
