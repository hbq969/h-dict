package com.github.hbq969.code.dict.service.spi.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.StrUtils;
import com.github.hbq969.code.dict.model.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : MysqlDictManageImpl
 * @createTime : 2024/5/11 18:02
 */
@Slf4j
public class FixDictManageImpl extends AbstractDictManage {

    private TransactionTemplate trant;

    private JdbcTemplate jt;

    private SpringContext context;


    public FixDictManageImpl(SpringContext context) {
        super(context);
        this.context = context;
        this.trant = context.getBean(TransactionTemplate.class);
        this.jt = context.getBean(JdbcTemplate.class);
    }

    @Override
    public Integer getKey() {
        return 1;
    }

    @Override
    public void addPair(String dictName, Pair pair) {
        if (StrUtils.strEmpty(dictName) || pair == null) {
            throw new IllegalArgumentException("dictName、pair不能为空");
        }
        pair.validCheck(context);
        jt.update("insert into h_dict_pairs(dict_name,pair_Key,pair_value) values(?,?,?)"
                , new Object[]{dictName, pair.getKey(), pair.getValue()}
                , new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
        log.info("添加字典枚举值成功: {}, {}", dictName, pair);
    }

    @Override
    public void addPairs(String dictName, List<Pair> pairs) {
        if (StrUtils.strEmpty(dictName) || CollectionUtils.isEmpty(pairs)) {
            throw new IllegalArgumentException("dictName、pairs不能为空");
        }
        pairs.forEach(pair -> pair.validCheck(context));
        jt.batchUpdate("insert into h_dict_pairs(dict_name,pair_Key,pair_value) values(?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pair pair = pairs.get(i);
                ps.setString(1, dictName);
                ps.setString(2, pair.getKey());
                ps.setString(2, pair.getValue());
            }

            @Override
            public int getBatchSize() {
                return pairs.size();
            }
        });
        log.info("批量添加字典枚举值成功: {}, {}", dictName, pairs);
    }

    @Override
    public void deletePair(String dictName, String pairKey) {
        if (StrUtils.strEmpty(dictName) || pairKey == null) {
            throw new IllegalArgumentException("dictName、pairKey不能为空");
        }
        jt.update("delete from h_dict_pairs where dict_name=? and pair_key=?"
                , new Object[]{dictName, pairKey}
                , new int[]{Types.VARCHAR, Types.VARCHAR});
        log.info("删除字典枚举值成功: {}, {}", dictName, pairKey);
    }

    @Override
    public void deletePairs(String dictName, List<String> pairKeys) {
        if (StrUtils.strEmpty(dictName) || CollectionUtils.isEmpty(pairKeys)) {
            throw new IllegalArgumentException("dictName、pairKeys不能为空");
        }
        jt.batchUpdate("delete from h_dict_pairs where dict_name=? and pair_key=?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, dictName);
                ps.setString(2, pairKeys.get(i));
            }

            @Override
            public int getBatchSize() {
                return pairKeys.size();
            }
        });
        log.info("批量删除字典枚举值成功: {}, {}", dictName, pairKeys);
    }

    @Override
    public void deleteDict(String dn) {
        super.deleteDict(dn);
        jt.update("delete from h_dict_pairs where dict_name=?", new Object[]{dn});
        log.info("删除字典fix枚举信息成功, {}", dn);
    }
}
