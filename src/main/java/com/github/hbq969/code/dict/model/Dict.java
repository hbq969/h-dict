package com.github.hbq969.code.dict.model;

import com.github.hbq969.code.common.lang.IModel;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import lombok.Data;

/**
 * @author : hbq969@gmail.com
 * @description : DictModel
 * @createTime : 2024/5/11 17:50
 */
@Data
public class Dict implements IModel<Dict> {
    /**
     * 字典字段名称
     */
    private String dictName;
    /**
     * 字典字段描述
     */
    private String dictDesc;
    /**
     * 字典来源类型
     */
    private Integer dictSource;
    private String fmtDictSource;
    /**
     * 字典枚举数量
     */
    private Integer dictNum;
    /**
     * 字典枚举key取值类型
     */
    private String keyColumn;
    /**
     * 字典枚举value取值类型
     */
    private String valColumn;

    @Override
    public Dict ct(SpringContext context) {
        this.fmtDictSource = context.getBean(MapDictHelperImpl.class)
                .queryValue("dict_source", String.valueOf(dictSource));
        return this;
    }
}
