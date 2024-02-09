package com.lynn.ggkt.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.vod.CourseFormVo;
import com.lynn.ggkt.vo.vod.CoursePublishVo;
import com.lynn.ggkt.vo.vod.CourseQueryVo;
import com.lynn.ggkt.vod.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/admin/vod/course")
//@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("添加课程基本信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo){
        //为了后续章节添加，需要传回id值
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        CourseFormVo courseFormVo = courseService.getCourseFormVoById(id);
        return Result.ok(courseFormVo);
    }

    @ApiOperation("修改")
    @PutMapping("update")
    public Result updateById(@RequestBody CourseFormVo courseFormVo){
        //为了后续章节添加，需要传回id值
        Long courseId = courseService.updateCourseById(courseFormVo);
        return Result.ok(courseId);
    }

    @ApiOperation("点播课程列表")
    @GetMapping("{page}/{limit}")
    public Result courseList(@PathVariable Long page,
                             @PathVariable Long limit,
                             CourseQueryVo courseQueryVo){
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.findPageCourse(pageParam,courseQueryVo);

        return Result.ok(map);
    }

    //根据课程id查询发布课程信息
    @GetMapping("getCoursePublishVoById/{id}")
    public Result getCoursePublishVoById(@PathVariable Long id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        return Result.ok(coursePublishVo);
    }

    //课程最终发布
    @PutMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable Long id){
        courseService.publishCourse(id);
        return Result.ok(null);
    }

    //删除课程
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseById(id);
        return Result.ok(null);
    }

    //查询所有课程
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = courseService.findlist();
        return Result.ok(list);
    }
}

