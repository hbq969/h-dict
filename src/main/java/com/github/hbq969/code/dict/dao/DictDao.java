package com.github.hbq969.code.dict.dao;

import com.github.hbq969.code.dict.control.vo.QueryDict;
import com.github.hbq969.code.dict.model.FixDict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : hbq969@gmail.com
 * @description :
 * @createTime : 2024/5/14 15:44
 */
@Repository("h-dict-DictDao")
@Mapper
public interface DictDao {

    void createDictBase();

    void createDictPairs();

    void createDictSql();

    void createDictSqlCounty();

    List<SqlDict> queryDicts(QueryDict q);

    List<Map> queryPairsWithSqlContent(@Param("sql") String sql);

    List<FixDict> queryFixDicts();

    List<SqlDict> querySqlDicts();

    List<Pair> queryPairs(@Param("dn") String dn);

    List<Map> queryFixDictPairs();
}
