package com.github.hbq969.code.dict.model;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @author : hbq969@gmail.com
 * @description : 包含应用名称的字典信息
 * @createTime : 2024/10/13 16:59
 */
@Data
public class AppDict {
    private String curApp;
    PageInfo<Dict> pageInfo;
}
