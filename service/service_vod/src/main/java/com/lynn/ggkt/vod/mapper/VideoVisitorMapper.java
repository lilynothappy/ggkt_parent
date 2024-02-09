package com.lynn.ggkt.vod.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lynn.ggkt.model.vod.VideoVisitor;
import com.lynn.ggkt.vo.vod.VideoVisitorCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author lynn
 * @since 2022-07-07
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    //课程统计
    //List<VideoVisitorCountVo> findCount(Long courseId, String startDate, String endDate);

    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate")String startDate,
                                        @Param("endDate")String endDate);
}
