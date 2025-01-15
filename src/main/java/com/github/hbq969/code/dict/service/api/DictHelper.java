package com.github.hbq969.code.dict.service.api;

import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hbq969@gmail.com
 * @description : DictHelper
 * @createTime : 2024/5/11 17:49
 */
public interface DictHelper<T> {
    Collection<Dict> allDicts();

    Optional<Dict> queryDict(String dictName);

    boolean isDict(String dictName);

    boolean isDictKey(String dictName, String key);

    String queryValue(String dictName, String key);

    Map<String, String> queryPairs(String dictName);

    Map<String, String> queryPairs(String dictName, boolean flip);

    List<Pair> queryPairList(String dictName);

    List<Pair> queryPairList(String dictName, boolean flip);

    void reloadImmediately();

    void tranForDict(T data);

    void tranForDict(T data, List<String> fs);

    void tranForDict(T data, String... fs);
}
