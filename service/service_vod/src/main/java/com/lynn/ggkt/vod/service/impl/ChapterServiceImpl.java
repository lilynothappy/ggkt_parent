package com.lynn.ggkt.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lynn.ggkt.model.vod.Chapter;
import com.lynn.ggkt.model.vod.Video;
import com.lynn.ggkt.vo.vod.ChapterVo;
import com.lynn.ggkt.vo.vod.VideoVo;
import com.lynn.ggkt.vod.mapper.ChapterMapper;
import com.lynn.ggkt.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynn.ggkt.vod.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;


    //获取章节与小结列表
    @Override
    public List<ChapterVo> getTreeList(Long courseId) {
        List<ChapterVo> finalList = new ArrayList<>();

        //根据courseId获取课程中的所有章节
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);

        //根据courseId获取课程中的所有小节
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getCourseId,courseId);
        List<Video> videoList = videoService.list(lambdaQueryWrapper);


        //封装章节
        for(int i=0; i<chapterList.size(); i++){
            //得到每个章节
            Chapter chapter = chapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            finalList.add(chapterVo);

            //封装小节
            List<VideoVo> videoVoList = new ArrayList<>();

            for(Video video: videoList){
                //判断小节是属于哪个章节
                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);

        }
        return finalList;
    }

    //根据课程id删除章节
    @Override
    public void removeByCourseId(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }


}
