package com.lynn.ggkt.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.client.course.CourseFeignClient;
import com.lynn.ggkt.client.user.UserInfoFeignClient;
import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.live.mapper.LiveCourseMapper;
import com.lynn.ggkt.live.mtcloud.CommonResult;
import com.lynn.ggkt.live.mtcloud.MTCloud;
import com.lynn.ggkt.live.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynn.ggkt.model.live.*;
import com.lynn.ggkt.model.user.UserInfo;
import com.lynn.ggkt.model.vod.Teacher;
import com.lynn.ggkt.utils.DateUtil;
import com.lynn.ggkt.vo.live.LiveCourseConfigVo;
import com.lynn.ggkt.vo.live.LiveCourseFormVo;
import com.lynn.ggkt.vo.live.LiveCourseGoodsView;
import com.lynn.ggkt.vo.live.LiveCourseVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {

    @Autowired
    private LiveCourseDescriptionService liveCourseDescriptionService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @Autowired
    private LiveCourseConfigService liveCourseConfigService;

    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private MTCloud mtCloud;

    //获取分页列表
    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        Page<LiveCourse> page = baseMapper.selectPage(pageParam,null);
        List<LiveCourse> list = page.getRecords();
        for(LiveCourse liveCourse : list){
            Teacher teacher = courseFeignClient.getByTeacherId(liveCourse.getTeacherId());
            liveCourse.getParam().put("teacherName",teacher.getName());
            liveCourse.getParam().put("teacherLevel",teacher.getLevel());
        }
        return page;
    }

    //添加直播课程（平台与数据库）
    @Override
    public void saveLive(LiveCourseFormVo liveCourseFormVo) {

        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);

        Teacher teacher = courseFeignClient.getByTeacherId(liveCourseFormVo.getTeacherId());

        //调用方法添加直播课程

//        params.put("course_name", course_name);
//        params.put("account", account); //直播账号->讲师id
//        params.put("start_time", start_time);
//        params.put("end_time", end_time);
//        params.put("nickname", nickname); //主播昵称->讲师名称
//        params.put("accountIntro", accountIntro); //见识介绍
//        params.put("options", options); //其他参数，map集合

        HashMap<Object,Object> options = new HashMap<>();
        options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
        options.put("password", liveCourseFormVo.getPassword());

        try {
            String res = mtCloud.courseAdd(liveCourse.getCourseName(), teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(), teacher.getIntro(), options);
            System.out.println("res:"+res);

            //将返回结果res转换为json格式，判断是否成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) { //成功
                //添加直播基本信息（数据库）
                //从返回结果中取得数据
                JSONObject object = commonResult.getData();
                Long course_id = object.getLong("course_id");//直播平台生成的直播课程id
                liveCourse.setCourseId(course_id);
                baseMapper.insert(liveCourse);

                //添加直播描述信息（数据库）
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId()); //数据库中课程主键id
                liveCourseDescriptionService.save(liveCourseDescription);

                //添加直播账号信息（数据库）
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(object.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setAdminKey(object.getString("admin_key"));
                liveCourseAccount.setUserKey(object.getString("user_key"));
                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
                liveCourseAccountService.save(liveCourseAccount);

            }else {
                System.out.println(commonResult.getmsg());
                throw new GgktException(20001,"添加直播课程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //删除直播课程
    @Override
    public void removeLive(Long id) {
        //根据id查询直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        if(liveCourse != null){
            //获取直播courseid
            Long courseId = liveCourse.getCourseId();
            //调用方法删除平台直播课程
            try {
                mtCloud.courseDelete(courseId.toString());
                //删除表数据
                baseMapper.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GgktException(20001,"删除直播课程失败");
            }
        }

    }

    //根据id获取课程信息（前台展示）
    @Override
    public LiveCourseFormVo getLiveCourseFormVo(Long id) {
        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();

        LiveCourse liveCourse = baseMapper.selectById(id);
        BeanUtils.copyProperties(liveCourse,liveCourseFormVo);

        //根据课程id查询描述信息
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(id);
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());

        return liveCourseFormVo;
    }

    //修改直播课程
    @Override
    public void updateLiveById(LiveCourseFormVo liveCourseFormVo) {
        LiveCourse liveCourse = baseMapper.selectById(liveCourseFormVo.getId()); //也可以直接new
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);

        Teacher teacher = courseFeignClient.getByTeacherId(liveCourseFormVo.getTeacherId());

        /*
        params.put("course_id", course_id);  //添加方法中没有得参数
        params.put("course_name", course_name);
        params.put("account", account); //直播账号->讲师id
        params.put("start_time", start_time);
        params.put("end_time", end_time);
        params.put("nickname", nickname); //主播昵称->讲师名称
        params.put("accountIntro", accountIntro); //见识介绍
        params.put("options", options); //其他参数，map集合
         */
        HashMap<Object,Object> options = new HashMap<Object,Object>();
        try {
            String res = mtCloud.courseUpdate(liveCourse.getCourseId().toString(),liveCourse.getCourseName(), teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),teacher.getIntro(), options);

            //返回结果转换
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                //更新直播基本信息
                liveCourse.setCourseId(object.getLong("course_id")); //可以省略？
                baseMapper.updateById(liveCourse);

                //更新直播描述信息
                LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescriptionService.updateById(liveCourseDescription);

                //不用更新直播账号信息？

            }else {
                throw new GgktException(20001,"修改直播课程失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取直播配置信息
    @Override
    public LiveCourseConfigVo getCourseConfig(Long id) {
        LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();
        //根据courseId获得基本配置信息
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getByCourseId(id);

        if(liveCourseConfig != null){
            BeanUtils.copyProperties(liveCourseConfig,liveCourseConfigVo);
            //根据课程id课程商品列表
            List<LiveCourseGoods> list = liveCourseGoodsService.getListByCourseId(id);
            liveCourseConfigVo.setLiveCourseGoodsList(list);
        }

        return liveCourseConfigVo;
    }

    //修改直播配置信息
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //1 修改直播配置表
        LiveCourseConfig liveCourseConfig = new LiveCourseConfig();
        BeanUtils.copyProperties(liveCourseConfigVo,liveCourseConfig);
        if(liveCourseConfigVo.getId() == null){
            liveCourseConfigService.save(liveCourseConfig);
        }else {
            liveCourseConfigService.updateById(liveCourseConfig);
        }

        //2 修改直播商品表
        //2.1 根据课程id删除直播商品列表
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,liveCourseConfigVo.getLiveCourseId());
        liveCourseGoodsService.remove(wrapper);

        //2.2 添加直播商品列表
        if(!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())){
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());
        }

        //3 修改直播平台信息
        this.updateLifeConfig(liveCourseConfigVo);
    }

    //获取最近的直播列表
    @Override
    public List<LiveCourseVo> findLatelyList() {
        List<LiveCourseVo> list = baseMapper.findLatelyList();

        for(LiveCourseVo liveCourseVo: list){
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));

            Long teacherId = liveCourseVo.getTeacherId();
            Teacher teacher = courseFeignClient.getByTeacherId(teacherId);
            liveCourseVo.setTeacher(teacher);

            liveCourseVo.setLiveStatus(getLiveStatus(liveCourseVo));

        }
        return list;
    }

    //获取用户access_token
    @Override
    public JSONObject getAccessToken(Long id, Long userId) {
        // 根据课程id获取直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);

        // 根据用户id获取用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);

        /**
         *  进入一个课程
         *  @param  String  course_id      课程ID
         *  @param  String  uid            用户唯一ID
         *  @param  String  nickname       用户昵称
         *  @param  String  role           用户角色，枚举见:ROLE 定义
         *  @param  Int     expire         有效期,默认:3600(单位:秒)
         *  @param  Array   options        可选项，包括:gender:枚举见上面GENDER定义,avatar:头像地址
         */
        try {
            HashMap<Object, Object> options = new HashMap<Object, Object>();
            String res = mtCloud.courseAccess(liveCourse.getCourseId().toString(), userId.toString(),
                    userInfo.getNickName(), MTCloud.ROLE_USER, 3600, options);

            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                System.out.println("access::" + object.getString("access_token"));
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        LiveCourse liveCourse = this.getById(courseId);
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        Teacher teacher = courseFeignClient.getByTeacherId(liveCourse.getTeacherId());
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(courseId);

        Map<String, Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != liveCourseDescription) {
            map.put("description", liveCourseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }

    /**
     * 直播状态 0：未开始 1：直播中 2：直播结束
     * @param liveCourse
     * @return
     */
    private int getLiveStatus(LiveCourse liveCourse) {
        // 直播状态 0：未开始 1：直播中 2：直播结束
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    //修改直播平台信息
    private void updateLifeConfig(LiveCourseConfigVo liveCourseConfigVo) {
        LiveCourse liveCourse = baseMapper.selectById(liveCourseConfigVo.getLiveCourseId());

        //参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        //界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        //观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        //观看人数开关
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());
        number.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        //商城列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }

        try {
            String res = mtCloud.updateLifeConfig(liveCourse.getCourseId().toString(), options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new GgktException(20001,"修改配置信息失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
