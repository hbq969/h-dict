package com.github.hbq969.code.dict.service.api;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.service.api.impl.ModelDictHelperImpl;

/**
 * @author : hbq969@gmail.com
 * @description : 字典转义辅助接口
 * @createTime : 2024/9/20 22:29
 */
public interface DictAware {
    default void convertDict(SpringContext context) {
        context.getOptionalBean(ModelDictHelperImpl.class).ifPresent(dict -> {
            DictAware that = DictAware.this;
            if (that instanceof DictModel) {
                dict.tranForDict((DictModel) that);
            }
        });
    }
}
