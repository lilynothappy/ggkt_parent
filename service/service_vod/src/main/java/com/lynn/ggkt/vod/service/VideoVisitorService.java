package com.lynn.ggkt.vod.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.vod.VideoVisitor;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-07
 */
public interface VideoVisitorService extends IService<VideoVisitor> {

    //课程统计的接口
    Map<String, Object> findCount(Long courseId, String startDate, String endDate);
}
