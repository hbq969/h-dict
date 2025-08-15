package com.github.hbq969.code.dict.model;

import com.github.hbq969.code.common.lang.ICheck;
import com.github.hbq969.code.common.spring.context.SpringContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : hbq969@gmail.com
 * @description : Pair
 * @createTime : 2024/5/11 17:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pair implements ICheck {
    @Schema(hidden = true)
    private int pairAddWay;
    private String key;
    private String value;
    @Schema(hidden = true)
    private String pairString;

    @Override
    public void validCheck(SpringContext context) {
    }

    @Override
    public void validCheck(SpringContext context, RuntimeException ex) {
        if (pairAddWay == 1 && key == null) {
            throw ex;
        }
        if (pairAddWay == 2 && StringUtils.isEmpty(pairString)) {
            throw ex;
        }
    }
}
