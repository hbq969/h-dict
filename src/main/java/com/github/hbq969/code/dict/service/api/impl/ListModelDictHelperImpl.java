package com.github.hbq969.code.dict.service.api.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.service.api.DictHelper;
import com.github.hbq969.code.dict.service.api.DictModel;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/16 14:10
 */
public class ListModelDictHelperImpl implements DictHelper<List<? extends DictModel>> {

    private ModelDictHelperImpl proxy;

    public ListModelDictHelperImpl(SpringContext context) {
        this.proxy = context.getBean(ModelDictHelperImpl.class);
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
        return this.isDictKey(dictName, key);
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
    public List<Pair> queryPairList(String dictName) {
        return this.proxy.queryPairList(dictName);
    }

    @Override
    public void reloadImmediately() {
        this.proxy.reloadImmediately();
    }

    @Override
    public void tranForDict(List<? extends DictModel> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(d -> {
                try {
                    proxy.tranForDict(d);
                } catch (Exception e) {
                    // DO nothing
                }
            });
        }
    }

    @Override
    public void tranForDict(List<? extends DictModel> data, List<String> fs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tranForDict(List<? extends DictModel> data, String... fs) {
        throw new UnsupportedOperationException();
    }
}
