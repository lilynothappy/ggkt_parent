package com.lynn.ggkt.live.api;

import com.alibaba.fastjson.JSONObject;
import com.lynn.ggkt.live.service.LiveCourseService;
import com.lynn.ggkt.model.live.LiveCourse;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.utils.AuthContextHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/live/liveCourse")
public class LiveCourseApiController {

    @Autowired
    private LiveCourseService liveCourseService;

    @ApiOperation(value = "获取用户access_token")
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable Long id) {
        Long userId = AuthContextHolder.getUserId();
        JSONObject object = liveCourseService.getAccessToken(id, userId);
        return Result.ok(object);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        Map<String, Object> map = liveCourseService.getInfoById(courseId);
        return Result.ok(map);
    }
}
