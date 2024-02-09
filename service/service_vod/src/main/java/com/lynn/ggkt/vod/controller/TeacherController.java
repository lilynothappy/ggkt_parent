package com.lynn.ggkt.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.model.vod.Teacher;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.vod.TeacherQueryVo;
import com.lynn.ggkt.vod.service.TeacherService;
import org.springframework.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-01
 */
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping("/admin/vod/teacher")
//@CrossOrigin //解决跨域问题
public class TeacherController {

    @Resource
    private TeacherService teacherService;


    /*@GetMapping("findAll")
    public List<Teacher> findAll(){
        return teacherService.list();
    }*/

    //使用自定义结果类返回
    @GetMapping("findAll")
    public Result findAll(){
        /*try{
            int i = 10/0;
        }catch (Exception e){
            throw new GgktException(600,"抛出自定义异常**");
        }*/
        List<Teacher> list = teacherService.list();
        return Result.ok(list);
        //没有追加.message()，则默认信息为“成功”
    }

    /*@ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public boolean removeById(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id")Long id){
        return teacherService.removeById(id);
    }*/

    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable("id")Long id){
        boolean isSuccess = teacherService.removeById(id);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("逻辑批量删除")
    @DeleteMapping("removeBatch")
    public Result removeBatch(@RequestBody List<Long> idList){
        boolean isSuccess = teacherService.removeByIds(idList);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail();
        }
    }

    /**
     *
     * @param current 当前页
     * @param limit    每页显示条数
     * @param teacherQueryVo 封装了查询条件的类
     * @return  自定义结果类
     */
    @ApiOperation("条件查询分页")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           @RequestBody(required = false)TeacherQueryVo teacherQueryVo) {
        //创建page对象
        Page<Teacher> pageParam = new Page<>(current,limit);
        //判断teacherQueryVo对象是否为空
        if(teacherQueryVo == null) {//查询全部
            IPage<Teacher> pageModel =
                    teacherService.page(pageParam,null);
            return Result.ok(pageModel);
        } else {
            //获取条件值，
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            //进行非空判断，条件封装
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(name)) {
                wrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(level)) {
                wrapper.eq("level",level);
            }
            if(!StringUtils.isEmpty(joinDateBegin)) {
                wrapper.ge("join_date",joinDateBegin);
            }
            if(!StringUtils.isEmpty(joinDateEnd)) {
                wrapper.le("join_date",joinDateEnd);
            }
            //调用方法分页查询
            IPage<Teacher> pageModel = teacherService.page(pageParam, wrapper);
            //返回
            return Result.ok(pageModel);
        }
    }


    @ApiOperation("添加讲师")
    @PostMapping("save")
    public Result save(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.save(teacher);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("getById/{id}")
    public Result getById(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }

    //根据id查询（返回对象）
    @GetMapping("inner/getById/{id}")
    public Teacher getByTeacherId(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        return teacher;
    }

    @ApiOperation("修改")
    @PostMapping("update")
    public Result update(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail();
        }
    }



}

