package com.lynn.ggkt.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.model.vod.Course;
import com.lynn.ggkt.model.vod.CourseDescription;
import com.lynn.ggkt.model.vod.Subject;
import com.lynn.ggkt.model.vod.Teacher;
import com.lynn.ggkt.vo.vod.*;
import com.lynn.ggkt.vod.mapper.CourseMapper;
import com.lynn.ggkt.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-05
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;

    //点播课程列表
    //根据课程分类查询课程列表（分页）
    @Override
    public Map<String, Object> findPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();
        Long subjectId = courseQueryVo.getSubjectId();//一级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId(); //二级分类
        Long teacherId = courseQueryVo.getTeacherId();

        //判断条件是否为空
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }

        //调用方法实现条件查询分页
        Page<Course> pages = baseMapper.selectPage(pageParam,wrapper);
        long totalCount = pages.getTotal();
        long totalPage = pages.getPages();
        List<Course> list = pages.getRecords();

        //查询关联id的对应名称，进行封装，显示
        //遍历封装讲师和分类名称
       /* list.stream().forEach(item ->{
           getNameById(item);
        });*/

        for(Course course: list){
            getNameById(course);
        }

        //封装数据
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",list);

        return map;
    }

    //添加课程基本信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {

        //添加课程基本信息，操作course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.insert(course);


        //添加课程描述，操作course_description表
        if(courseFormVo.getDescription() != null){
            CourseDescription courseDescription = new CourseDescription();
            courseDescription.setDescription(courseFormVo.getDescription());
            //course_description数据表中id设置了与course_id完全相同
            courseDescription.setCourseId(course.getId());
            courseDescriptionService.save(courseDescription);
        }
        return course.getId();
    }

    //根据id查询
    @Override
    public CourseFormVo getCourseFormVoById(Long id) {
        CourseFormVo courseFormVo = new CourseFormVo();

        //从course表中取数据
        Course course = baseMapper.selectById(id);
        BeanUtils.copyProperties(course,courseFormVo);

        //从course_description表中取数据
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        if(courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }

        return courseFormVo;
    }

    //修改
    @Override
    public Long updateCourseById(CourseFormVo courseFormVo) {
        //添加课程基本信息，操作course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);

        //添加课程描述，操作course_description表
        if(courseFormVo.getDescription() != null){
            CourseDescription courseDescription = new CourseDescription();
            courseDescription.setDescription(courseFormVo.getDescription());
            //course_description数据表中id设置了与course_id完全相同
            courseDescription.setCourseId(course.getId());
            courseDescriptionService.updateById(courseDescription);
        }
        return course.getId();
    }

    //根据课程id查询发布课程信息
    @Override
    public CoursePublishVo getCoursePublishVoById(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    //课程最终发布
    @Override
    public void publishCourse(Long id) {
        //根据id查对象并修改状态
        Course course = baseMapper.selectById(id);
        course.setStatus(1);//表示课程已发布状态
        course.setPublishTime(new Date());

        //最终修改
        baseMapper.updateById(course);

    }

    //删除
    @Override
    public void removeCourseById(Long id) {
        //根据课程id删除小节
        videoService.removeByCourseId(id);
        //根据课程id删除章节
        chapterService.removeByCourseId(id);
        //根据课程id删除课程描述
        courseDescriptionService.removeById(id);
        //根据课程id删除课程
        baseMapper.deleteById(id);
    }

    //根据课程分类查询课程列表（分页）
    /*@Override
    public Map<String,Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师

        //判断条件值是否为空
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }
        //调用方法进行分页查询
        Page<Course> pages =  baseMapper.selectPage(pageParam,wrapper);

        //获取数据
        long totalCount = pages.getTotal(); //总记录数
        long totalPage = pages.getPages(); //总页数
        long currentPage = pages.getCurrent(); //当前页
        long size = pages.getSize(); //每页记录数
        //每页数据集合
        List<Course> records = pages.getRecords();
        records.stream().forEach(item ->{
            getNameById(item);
        });

        //封装其他数据
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);

        return map;
    }*/

    //根据课程id查询课程详情
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //根据课程查id
        Course course = baseMapper.selectById(courseId);
        //view_count浏览数量+1
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);

        //课程详情数据
        CourseVo courseVo = baseMapper.selectCourseVoById(courseId);
        //课程章节小节数据
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
        //课程描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        //课程所属讲师信息
        Teacher teacher = teacherService.getById(course.getTeacherId());
        //封装到map
        Map<String,Object> map = new HashMap<>();
        map.put("courseVo",courseVo);
        map.put("chapterVoList",chapterVoList);
        map.put("description",null != courseDescription ? courseDescription.getDescription() : "");
        map.put("teacher",teacher);
        map.put("isBuy",false); //是否购买

        return map;
    }

    //查询所有课程
    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.stream().forEach(item -> {
            getNameById(item);
        });
        return list;
    }


    //查询关联id的对应名称，进行封装，显示
    private Course getNameById(Course course) {

        //根据讲师id获取讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if(teacher != null){
            String name = teacher.getName();
            //Course实体类继承了BaseEntity，其中有一个命名为param的map参数可以放入其他参数
            course.getParam().put("teacherName",name);
        }

        //根据课程分类id获取课程分类名称
        //最下层分类（二级分类）
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null){
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }

        //上层分类（一级）
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null){
            course.getParam().put("subjectParentTitle",subjectTwo.getTitle());
        }

        return course;
    }
}
