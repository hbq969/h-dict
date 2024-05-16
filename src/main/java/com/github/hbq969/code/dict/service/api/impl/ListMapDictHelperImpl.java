package com.github.hbq969.code.dict.service.api.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.service.api.DictHelper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hbq969@gmail.com
 * @description : ListDictHelperImpl
 * @createTime : 2024/5/14 13:40
 */
public class ListMapDictHelperImpl implements DictHelper<List<Map>> {


    private MapDictHelperImpl proxy;

    public ListMapDictHelperImpl(SpringContext context) {
        this.proxy = context.getBean(MapDictHelperImpl.class);
    }

    @Override
    public Collection<Dict> allDicts() {
        return this.proxy.allDicts();
    }

    @Override
    public Optional<Dict> queryDict(String dictName) {
        return this.proxy.queryDict(dictName);
    }

    @Override
    public boolean isDict(String dictName) {
        return this.proxy.isDict(dictName);
    }

    @Override
    public boolean isDictKey(String dictName, String key) {
        return this.proxy.isDictKey(dictName, key);
    }

    @Override
    public String queryValue(String dictName, String key) {
        return this.proxy.queryValue(dictName, key);
    }

    @Override
    public Map<String, String> queryPairs(String dictName) {
        return this.proxy.queryPairs(dictName);
    }

    @Override
    public void reloadImmediately() {
        this.proxy.reloadImmediately();
    }

    @Override
    public void tranForDict(List<Map> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(d -> {
                try {
                    this.proxy.tranForDict(d);
                } catch (Exception e) {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void tranForDict(List<Map> data, List<String> fs) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(d -> {
                try {
                    this.proxy.tranForDict(d, fs);
                } catch (Exception e) {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void tranForDict(List<Map> data, String... fs) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(d -> {
                try {
                    this.proxy.tranForDict(d, fs);
                } catch (Exception e) {
                    // do nothing
                }
            });
        }
    }
}
