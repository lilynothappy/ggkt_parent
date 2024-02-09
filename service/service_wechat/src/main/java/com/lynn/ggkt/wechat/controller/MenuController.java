package com.lynn.ggkt.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.lynn.ggkt.exception.GgktException;
import com.lynn.ggkt.model.wechat.Menu;
import com.lynn.ggkt.result.Result;
import com.lynn.ggkt.vo.wechat.MenuVo;
import com.lynn.ggkt.wechat.service.MenuService;
import com.lynn.ggkt.wechat.utils.ConstantPropertiesUtil;
import com.lynn.ggkt.wechat.utils.HttpClientUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    //公众号菜单删除
    @DeleteMapping("removeMenu")
    public Result removeMenu() {
        menuService.removeMenu();
        return Result.ok(null);
    }

    //同步菜单方法
    @GetMapping("syncMenu")
    public Result createMenu(){
        menuService.syncMenu();
        return Result.ok(null);
    }


    //获取access_token，非必要方法，WxMpService中已实现
    @GetMapping("getAccessToken")
    public Result getAccessToken(){
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");

        //设置路径参数
        String url = String.format(buffer.toString(),
                                ConstantPropertiesUtil.ACCESS_KEY_ID,
                                ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        //get请求
        String tokenString = null;
        try {
            tokenString = HttpClientUtils.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            throw new GgktException(20001,"获取access_token失败");
        }
        return Result.ok(null);
    }


    //获取所有菜单，按照一级和二级菜单封装
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        List<MenuVo> list =  menuService.findAll();
        return Result.ok(list);
    }

    //获取所有一级菜单
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo(){
        List<Menu> list =  menuService.findFirst();
        return Result.ok(list);
    }



    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.ok(menu);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        menuService.removeByIds(idList);
        return Result.ok(null);
    }

}

