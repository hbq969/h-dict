package com.github.hbq969.code.dict.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author : hbq969@gmail.com
 * @description : 字典变化事件
 * @createTime : 2024/10/12 21:39
 */
public class DictChangeEvent extends ApplicationEvent {
    public DictChangeEvent(Object source) {
        super(source);
    }
}
