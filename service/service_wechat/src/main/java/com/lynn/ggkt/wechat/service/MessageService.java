package com.lynn.ggkt.wechat.service;

import java.util.Map;

public interface MessageService {

    //接收微信服务器发送来的消息
    String receiveMessage(Map<String, String> param);

    void pushPayMessage(long id);
}
