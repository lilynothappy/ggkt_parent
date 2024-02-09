package com.lynn.ggkt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.user.UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-10
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserInfoOpenid(String openId);
}
