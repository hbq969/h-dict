package com.github.hbq969.code.dict.service.spi;

import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : DictManage
 * @createTime : 2024/5/11 17:59
 */
public interface DictManage {
    @Transactional
    void addPair(String dictName, Pair pair);

    @Transactional
    void addPairs(String dictName, List<Pair> pairs);

    void deletePair(String dictName, String pairKey);

    @Transactional
    void deletePairs(String dictName, List<String> pairKeys);

    void addDict(Dict dict);

    @Transactional
    void deleteDict(String dn);

    @Transactional
    void updateDict(Dict dict);
}
