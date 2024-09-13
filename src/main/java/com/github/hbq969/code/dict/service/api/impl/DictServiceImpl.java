package com.github.hbq969.code.dict.service.api.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.control.vo.DictPair;
import com.github.hbq969.code.dict.control.vo.QueryDict;
import com.github.hbq969.code.dict.dao.DictDao;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;
import com.github.hbq969.code.dict.service.api.DictService;
import com.github.hbq969.code.dict.service.spi.impl.DictManageFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/14 15:55
 */
public class DictServiceImpl implements DictService {

    @Autowired
    private SpringContext context;

    @Autowired
    private DictDao dao;

    @Override
    public List<SqlDict> queryDicts(QueryDict q) {
        return dao.queryDicts(q).stream().filter(d -> {
            d.ct(context);
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public void delDict(Dict dict) {
        dict.setApp(context.getProperty("spring.application.name"));
        context.getBean(DictManageFacade.class)
                .getService(dict.getDictSource()).deleteDict(dict.getDictName());
        context.getBean(MapDictHelperImpl.class).reloadImmediately();
    }

    @Override
    public void updateDict(SqlDict dict) {
        dict.setApp(context.getProperty("spring.application.name"));
        context.getBean(DictManageFacade.class)
                .getService(dict.getDictSource()).updateDict(dict);
        context.getBean(MapDictHelperImpl.class).reloadImmediately();
    }

    @Override
    public List<Pair> queryPairs(String dn) {
        return dao.queryPairs(dn);
    }

    @Override
    public void addPair(DictPair pair) {
        context.getBean(DictManageFacade.class)
                .getService(pair.getDict().getDictSource())
                .addPair(pair.getDict().getDictName(), pair.getPair());
        context.getBean(MapDictHelperImpl.class).reloadImmediately();
    }

    @Override
    public void delPair(DictPair pair) {
        context.getBean(DictManageFacade.class)
                .getService(pair.getDict().getDictSource())
                .deletePair(pair.getDict().getDictName(), pair.getPair().getKey());
        context.getBean(MapDictHelperImpl.class).reloadImmediately();
    }

    @Override
    public boolean queryDict(String dn) {
        return context.getBean(MapDictHelperImpl.class).queryDict(dn).isPresent();
    }
}
