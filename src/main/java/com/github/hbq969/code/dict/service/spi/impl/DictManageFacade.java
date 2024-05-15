package com.github.hbq969.code.dict.service.spi.impl;

import com.github.hbq969.code.common.decorde.DefaultOptionalFacade;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.service.spi.DictManage;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : DictManageFacade
 * @createTime : 2024/5/11 18:00
 */
public class DictManageFacade extends DefaultOptionalFacade<Integer, DictManage> implements DictManage {

    private SpringContext context;

    public DictManageFacade(SpringContext context) {
        this.context = context;
    }

    @Override
    public void addPair(String dictName, Pair pair) {
        getService().addPair(dictName, pair);
    }

    @Override
    public void addPairs(String dictName, List<Pair> pairs) {
        getService().addPairs(dictName, pairs);
    }

    @Override
    public void deletePair(String dictName, String pairKey) {
        getService().deletePair(dictName, pairKey);
    }

    @Override
    public void deletePairs(String dictName, List<String> pairKeys) {
        getService().deletePairs(dictName, pairKeys);
    }

    @Override
    public void addDict(Dict dict) {
        getService().addDict(dict);
    }

    @Override
    public void deleteDict(String dn) {
        getService().deleteDict(dn);
    }

    @Override
    public void updateDict(Dict dict) {
        getService().updateDict(dict);
    }
}
