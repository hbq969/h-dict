package com.github.hbq969.code.dict.service.api.impl;

import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.service.api.DictHelper;
import com.github.hbq969.code.dict.service.api.DictModel;
import com.github.hbq969.code.dict.service.api.Td;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/15 20:51
 */
@Slf4j
public class ModelDictHelperImpl implements DictHelper<DictModel> {

    @Autowired
    private MapDictHelperImpl mapDict;

    @Override
    public Collection<Dict> allDicts() {
        return mapDict.allDicts();
    }

    @Override
    public Optional<Dict> queryDict(String dictName) {
        return mapDict.queryDict(dictName);
    }

    @Override
    public boolean isDict(String dictName) {
        return mapDict.isDict(dictName);
    }

    @Override
    public boolean isDictKey(String dictName, String key) {
        return mapDict.isDictKey(dictName, key);
    }

    @Override
    public String queryValue(String dictName, String key) {
        return mapDict.queryValue(dictName, key);
    }

    @Override
    public Map<String, String> queryPairs(String dictName) {
        return mapDict.queryPairs(dictName);
    }

    @Override
    public void reloadImmediately() {
        mapDict.reloadImmediately();
    }

    @Override
    public void tranForDict(DictModel data) {
        Class<?> clz = data.getClass();
        Field[] fs = clz.getDeclaredFields();
        for (Field f : fs) {
            if (f.isAnnotationPresent(Td.class)) {
                Td td = f.getAnnotation(Td.class);
                if (td.enable() && isDict(f.getName())) {
                    try {
                        f.setAccessible(true);
                        String value = queryValue(f.getName(), String.valueOf(f.get(data)));
                        String fmtFd = td.fmtFieldName();
                        Field fmtF = clz.getDeclaredField(fmtFd);
                        fmtF.setAccessible(true);
                        fmtF.set(data, value);
                    } catch (Exception e) {
                        log.warn("转义异常，待转义字段[{}], 转义后字段[{}]", f.getName(), td.fmtFieldName());
                    }
                }
            }
        }
    }

    @Override
    public void tranForDict(DictModel data, List<String> fs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tranForDict(DictModel data, String... fs) {
        throw new UnsupportedOperationException();
    }
}
