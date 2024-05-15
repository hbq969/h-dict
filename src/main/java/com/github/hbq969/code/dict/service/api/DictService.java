package com.github.hbq969.code.dict.service.api;

import com.github.hbq969.code.dict.control.vo.DictPair;
import com.github.hbq969.code.dict.control.vo.QueryDict;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/14 15:55
 */
public interface DictService {
    List<SqlDict> queryDicts(QueryDict q);

    void delDict(Dict dict);

    void updateDict(SqlDict dict);

    List<Pair> queryPairs(String dn);

    void addPair(DictPair pair);

    void delPair(DictPair pair);

    boolean queryDict(String dn);
}
