package com.lynn.ggkt.vod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.vo.vod.CourseFormVo;
import com.lynn.ggkt.vo.vod.CoursePublishVo;
import com.lynn.ggkt.vo.vod.CourseQueryVo;
import com.lynn.ggkt.vo.vod.CourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
public interface CourseService extends IService<Course> {

    //点播课程列表
    Map<String, Object> findPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    //添加课程基本信息
    Long saveCourseInfo(CourseFormVo courseFormVo);

    //根据id查询
    CourseFormVo getCourseFormVoById(Long id);

    //修改
    Long updateCourseById(CourseFormVo courseFormVo);

    //根据课程id查询发布课程信息
    CoursePublishVo getCoursePublishVoById(Long id);

    //课程最终发布
    void publishCourse(Long id);

    //删除
    void removeCourseById(Long id);

    //根据课程分类查询课程列表（分页）
    //Map<String,Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    //根据课程id查询课程详情
    Map<String, Object> getInfoById(Long courseId);

    //查询所有课程
    List<Course> findlist();
}
