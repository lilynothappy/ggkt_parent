package com.lynn.ggkt.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mtcloud") //找到配置文件中前缀为mtcloud的字段，将对应值赋给类中的变量
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
