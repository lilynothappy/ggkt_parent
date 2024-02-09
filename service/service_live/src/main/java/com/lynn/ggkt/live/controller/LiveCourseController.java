package com.lynn.ggkt.live.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.live.service.LiveCourseAccountService;
import com.lynn.ggkt.live.service.LiveCourseConfigService;
import com.lynn.ggkt.live.service.LiveCourseService;
import com.lynn.ggkt.model.live.LiveCourse;
import com.lynn.ggkt.model.live.LiveCourseAccount;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.live.LiveCourseConfigVo;
import com.lynn.ggkt.vo.live.LiveCourseFormVo;
import com.lynn.ggkt.vo.live.LiveCourseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
@RestController
@RequestMapping("/admin/live/liveCourse")
public class LiveCourseController {

    @Autowired
    private LiveCourseService liveCourseService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    //获取最近的直播列表
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> list = liveCourseService.findLatelyList();
        return Result.ok(list);
    }

    //修改直播配置信息
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    //获取直播配置信息
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
        LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCourseConfig(id);
        return Result.ok(liveCourseConfigVo);
    }

    //获取直播账号信息
    @GetMapping("getLiveCourseAccount/{id}")
    public Result getLiveCourseAccount(@PathVariable Long id) {
        LiveCourseAccount liveCourseAccount = liveCourseAccountService.getByLiveCourseId(id);
        return Result.ok(liveCourseAccount);
    }

    //获取分页列表
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,@PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page,limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //添加直播课程（平台与数据库）
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.saveLive(liveCourseFormVo);
        return Result.ok(null);
    }

    //删除直播课程
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        liveCourseService.removeLive(id);
        return Result.ok(null);
    }

    //根据id获取课程信息
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }

    //根据id获取课程信息（前台展示）
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseFormVo(id);
        return Result.ok(liveCourseFormVo);
    }

    //修改直播课程
    @PutMapping("update")
    public Result updateById(@RequestBody LiveCourseFormVo liveCourseFormVo) {
        System.out.println("从前端获取对象："+liveCourseFormVo);
        liveCourseService.updateLiveById(liveCourseFormVo);
        return Result.ok(null);
    }

}

