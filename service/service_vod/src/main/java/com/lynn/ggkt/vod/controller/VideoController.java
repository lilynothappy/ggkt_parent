package com.lynn.ggkt.vod.controller;


import com.lynn.ggkt.model.vod.Video;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vod.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/admin/vod/video")
//@CrossOrigin
public class VideoController {

    @Autowired
    private VideoService videoService;

    //添加
    @PostMapping("save")
    public Result save(@RequestBody Video video){
        videoService.save(video);
        return Result.ok(null);
    }

    //根据id查询
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        Video video = videoService.getById(id);
        return Result.ok(video);
    }

    //修改
    @PutMapping("update")
    public Result update(@RequestBody Video video){
        videoService.updateById(video);
        return Result.ok(null);
    }

    //删除
    /*@DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        videoService.removeVideoById(id);
        return Result.ok(null);
    }
*/
}

