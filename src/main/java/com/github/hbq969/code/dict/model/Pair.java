package com.github.hbq969.code.dict.model;

import cn.hutool.core.lang.Assert;
import com.github.hbq969.code.common.lang.ICheck;
import com.github.hbq969.code.common.spring.context.SpringContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hbq969@gmail.com
 * @description : Pair
 * @createTime : 2024/5/11 17:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pair implements ICheck {
    private String key;
    private String value;

    @Override
    public void validCheck(SpringContext context) {
        Assert.notNull(key, "字典枚举key值不能为空");
    }
}
