package com.lynn.ggkt.live.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.live.LiveCourse;
import com.lynn.ggkt.vo.live.LiveCourseConfigVo;
import com.lynn.ggkt.vo.live.LiveCourseFormVo;
import com.lynn.ggkt.vo.live.LiveCourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseService extends IService<LiveCourse> {

    //获取分页列表
    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    //添加直播课程（平台与数据库）
    void saveLive(LiveCourseFormVo liveCourseFormVo);

    //删除直播课程
    void removeLive(Long id);

    //根据id获取课程信息（前台展示）
    LiveCourseFormVo getLiveCourseFormVo(Long id);

    //修改直播课程
    void updateLiveById(LiveCourseFormVo liveCourseFormVo);

    //获取直播配置信息
    LiveCourseConfigVo getCourseConfig(Long id);

    //修改直播配置信息
    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    //获取最近的直播列表
    List<LiveCourseVo> findLatelyList();

    //获取用户access_token
    JSONObject getAccessToken(Long id, Long userId);

    //根据ID查询课程
    Map<String, Object> getInfoById(Long courseId);
}
