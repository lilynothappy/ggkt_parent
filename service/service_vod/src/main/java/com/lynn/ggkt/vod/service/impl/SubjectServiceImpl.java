package com.lynn.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.model.vod.Subject;
import com.lynn.ggkt.vo.vod.SubjectEeVo;
import com.lynn.ggkt.vod.listener.SubjectListener;
import com.lynn.ggkt.vod.mapper.SubjectMapper;
import com.lynn.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-03
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectListener subjectListener;

    //课程分类列表，可以用于每一层的查询
    @Override
    public List<Subject> selectSubjectList(Long id) {
        //select * from subject where parent_id=0
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Subject> subjectList = baseMapper.selectList(wrapper);

        //遍历subjectList，得到每一个subject对象，判断是否有下一层数据，有就将hasChildren=true
        for(Subject subject:subjectList){
            //获取subject的id值
            Long subjectId = subject.getId();
            boolean isChild = isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return subjectList;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long subjectId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",subjectId);
        Integer count = baseMapper.selectCount(wrapper);
        // 1>0 true 表示有下一层， 0>0 false 表示没有下一层
        return count>0;
    }

    //课程分类导出
    @Override
    public void exportData(HttpServletResponse response) {

        try {
            //设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            //查询课程分类所有数据
            List<Subject> subjectList = baseMapper.selectList(null);

            //将List<Subject>转换为List<SubjectEeVo>
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for(Subject subject:subjectList){
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                //将参数一相同属性值转移到属性二对象中
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            //EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);

        } catch (Exception e) {
            throw new GgktException(20001,"导出失败");
        }
    }

    //课程分类导入
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,subjectListener)
                    .sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }
}
