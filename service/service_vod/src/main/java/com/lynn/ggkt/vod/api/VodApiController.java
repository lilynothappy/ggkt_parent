package com.lynn.ggkt.vod.api;

import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/vod")
public class VodApiController {

    @Autowired
    private VodService vodService;

    //点播视频播放接口
    @GetMapping("getPlayAuth/{courseId}/{videoId}")
    public Result getPlayAuth(@PathVariable Long courseId, @PathVariable Long videoId) {
        Map<String,Object> map = vodService.getPlayAuth(courseId,videoId);
        return Result.ok(map);
    }
}
