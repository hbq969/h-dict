package com.github.hbq969.code.dict.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : hbq969@gmail.com
 * @description : ReloadConf
 * @createTime : 2024/5/13 13:38
 */
@ConfigurationProperties(prefix = "dict.reload")
@Data
public class ReloadConf {
    /**
     * 重载时间 Cron格式
     */
    private String cron = "0,30 * * * * *";
}
