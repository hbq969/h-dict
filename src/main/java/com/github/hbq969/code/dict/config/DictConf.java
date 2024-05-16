package com.github.hbq969.code.dict.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : hbq969@gmail.com
 * @description : DictConf
 * @createTime : 2024/5/13 11:43
 */
@ConfigurationProperties(prefix = "dict")
@Data
public class DictConf {

    /**
     * 是否启用字典管理
     */
    private boolean enabled = true;

    /**
     * 字典api访问授权码
     */
    private String dictToken = "hbq969@gmail.com";

    /**
     * 转义map时，转义后key前缀
     */
    private String mapKeyPrefix = "fmt";

    /**
     * 重载配置
     */
    private ReloadConf reload;
}
