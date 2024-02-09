package com.lynn.ggkt.client.course;

import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.model.vod.Teacher;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-vod")
public interface CourseFeignClient {

    //根据关键字查询课程
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findByKeyword(@PathVariable String keyword);

    //根据课程id查询课程详情(返回对象)
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    Course getById(@PathVariable Long courseId);

    //根据id查询（返回对象）
    @GetMapping("/admin/vod/teacher/inner/getById/{id}")
    Teacher getByTeacherId(@PathVariable Long id);
}
