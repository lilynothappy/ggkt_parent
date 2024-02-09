package com.lynn.ggkt.vod.service.impl;

import com.lynn.ggkt.model.vod.VideoVisitor;
import com.lynn.ggkt.vo.vod.VideoVisitorCountVo;
import com.lynn.ggkt.vo.vod.VideoVisitorVo;
import com.lynn.ggkt.vod.mapper.VideoVisitorMapper;
import com.lynn.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-07
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {


    //课程统计的接口
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        List<VideoVisitorCountVo> videoVisitorCountVoList = baseMapper.findCount(courseId,startDate,endDate);
        List<String> dateList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getJoinTime).collect(Collectors.toList());
        List<Integer> countList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());

        Map<String,Object> map = new HashMap<>();
        map.put("xDate",dateList);
        map.put("yDate",countList);

        return map;
    }
}
