package com.lynn.ggkt.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.live.LiveCourseAccount;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-21
 */
public interface LiveCourseAccountService extends IService<LiveCourseAccount> {

    //获取直播账号信息
    LiveCourseAccount getByLiveCourseId(Long id);
}
