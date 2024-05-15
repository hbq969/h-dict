package com.github.hbq969.code.dict.config;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.service.api.impl.DictServiceImpl;
import com.github.hbq969.code.dict.service.api.impl.ListDictHelperImpl;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.dict.service.api.impl.ModelDictHelperImpl;
import com.github.hbq969.code.dict.service.spi.impl.DictManageFacade;
import com.github.hbq969.code.dict.service.spi.impl.FixDictManageImpl;
import com.github.hbq969.code.dict.service.spi.impl.SqlDictManageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hbq969@gmail.com
 * @description : DictConfig
 * @createTime : 2024/5/13 11:38
 */
@Configuration
public class DictConfig {

    @Autowired
    SpringContext context;

    @Bean
    DictConf dictConf() {
        return new DictConf();
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-DictManageFacade")
    DictManageFacade dictManageFacade() {
        return new DictManageFacade(context);
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-MysqlDictManage")
    FixDictManageImpl mysqlDictManage() {
        return new FixDictManageImpl(context);
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-OracleDictManage")
    SqlDictManageImpl oracleDictManage() {
        return new SqlDictManageImpl(context);
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-MapDictHelper")
    MapDictHelperImpl mapDictHelper() {
        return new MapDictHelperImpl(context);
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-ListDictHelper")
    ListDictHelperImpl listDictHelper() {
        return new ListDictHelperImpl(context);
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-ModelDictHelper")
    ModelDictHelperImpl modelDictHelper() {
        return new ModelDictHelperImpl();
    }

    @ConditionalOnExpression("${dict.enabled:false}")
    @Bean("h-dict-DictServiceImpl")
    DictServiceImpl dictService() {
        return new DictServiceImpl();
    }

    @Bean("h-dict-PermitHandlerInterceptor")
    PermitHandlerInterceptor permitHandlerInterceptor() {
        return new PermitHandlerInterceptor();
    }
}
