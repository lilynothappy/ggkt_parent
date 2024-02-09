package com.lynn.ggkt.activity.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynn.ggkt.activity.service.CouponInfoService;
import com.lynn.ggkt.model.activity.CouponInfo;
import com.lynn.ggkt.model.activity.CouponUse;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.activity.CouponUseQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    //查所有分页
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit){
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        IPage<CouponInfo> iPage = couponInfoService.page(pageParam);
        return Result.ok(iPage);
    }

    //获取已使用优惠券列表（条件分页查询）
    @GetMapping("couponUse/{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        CouponUseQueryVo couponUseQueryVo){
        Page<CouponUse> pageParam = new Page<>(page,limit);
        IPage<CouponUse> iPage = couponInfoService.selectCouponUsePage(pageParam,couponUseQueryVo);
        return Result.ok(iPage);
    }



    @ApiOperation(value = "获取优惠券")
    @GetMapping("get/{id}")
    public Result get(@PathVariable String id) {
        CouponInfo couponInfo = couponInfoService.getById(id);
        return Result.ok(couponInfo);
    }

    @ApiOperation(value = "新增优惠券")
    @PostMapping("save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改优惠券")
    @PutMapping("update")
    public Result updateById(@RequestBody CouponInfo couponInfo) {
        couponInfoService.updateById(couponInfo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除优惠券")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        couponInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value="根据id列表删除优惠券")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        couponInfoService.removeByIds(idList);
        return Result.ok(null);
    }

}

