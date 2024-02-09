package com.lynn.ggkt.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.vod.Video;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
public interface VideoService extends IService<Video> {

    //根据课程id删除小节
    void removeByCourseId(Long id);

    //void removeVideoById(Long id);
}
