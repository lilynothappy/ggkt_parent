package com.lynn.ggkt.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.wechat.Menu;
import com.lynn.ggkt.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
public interface MenuService extends IService<Menu> {

    //获取所有一级菜单
    List<Menu> findFirst();

    //获取所有菜单，按照一级和二级菜单封装
    List<MenuVo> findAll();

    //同步菜单方法
    void syncMenu();

    //公众号菜单删除
    void removeMenu();
}
