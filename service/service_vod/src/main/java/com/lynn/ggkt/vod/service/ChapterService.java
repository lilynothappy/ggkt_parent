package com.lynn.ggkt.vod.service;

import com.lynn.ggkt.model.vod.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
public interface ChapterService extends IService<Chapter> {

    //获取章节与小结列表
    List<ChapterVo> getTreeList(Long courseId);

    //根据课程id删除章节
    void removeByCourseId(Long id);
}
