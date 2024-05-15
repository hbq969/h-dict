package com.github.hbq969.code.dict.control.vo;

import lombok.Data;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/14 15:41
 */
@Data
public class QueryDict {
    private String dictName;
    private String dictDesc;
    private Integer dictSource;
    private Integer pageSize = 10;
    private Integer pageNum = 1;
}
