package com.lynn.ggkt.vod.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.vod.CourseQueryVo;
import com.lynn.ggkt.vod.service.CourseService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    //根据课程分类查询课程列表（分页）
    @GetMapping("{subjectParentId}/{page}/{limit}")
    public Result findPageCourse(@PathVariable Long subjectParentId,
                                 @PathVariable Long page,@PathVariable Long limit){
        //封装条件
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);

        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.findPageCourse(pageParam,courseQueryVo);
        return Result.ok(map);
    }

    //根据课程id查询课程详情
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(@PathVariable Long courseId){
        Map<String,Object> map = courseService.getInfoById(courseId);
        return Result.ok(map);
    }

    //根据课程id查询课程详情(返回对象)
    @GetMapping("inner/getById/{courseId}")
    public Course getById(@PathVariable Long courseId){
        Course course = courseService.getById(courseId);
        return course;
    }


    //根据关键字查询课程
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(
            @ApiParam(value = "关键字", required = true)
            @PathVariable String keyword){
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.like("title",keyword);
        List<Course> list = courseService.list(wrapper);
        return list;
    }
}
