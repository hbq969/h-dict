package com.github.hbq969.code.dict.service.api.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.service.api.DictHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hbq969@gmail.com
 * @description : ListDictHelperImpl
 * @createTime : 2024/5/14 13:40
 */
public class ListDictHelperImpl implements DictHelper<List<Map>> {

    private SpringContext context;

    private MapDictHelperImpl mapDictHelper;

    public ListDictHelperImpl(SpringContext context) {
        this.context = context;
        this.mapDictHelper = context.getBean(MapDictHelperImpl.class);
    }

    @Override
    public Collection<Dict> allDicts() {
        return this.mapDictHelper.allDicts();
    }

    @Override
    public Optional<Dict> queryDict(String dictName) {
        return this.mapDictHelper.queryDict(dictName);
    }

    @Override
    public boolean isDict(String dictName) {
        return this.mapDictHelper.isDict(dictName);
    }

    @Override
    public boolean isDictKey(String dictName, String key) {
        return this.mapDictHelper.isDictKey(dictName, key);
    }

    @Override
    public String queryValue(String dictName, String key) {
        return this.mapDictHelper.queryValue(dictName, key);
    }

    @Override
    public Map<String, String> queryPairs(String dictName) {
        return this.mapDictHelper.queryPairs(dictName);
    }

    @Override
    public void reloadImmediately() {
        this.mapDictHelper.reloadImmediately();
    }

    @Override
    public void tranForDict(List<Map> data, String dictName) {
        for (Map d : data) {
            try {
                this.mapDictHelper.tranForDict(d, dictName);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
