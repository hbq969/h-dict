package com.github.hbq969.code.dict.service.spi.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : OracleDictManageImpl
 * @createTime : 2024/5/11 18:29
 */
@Slf4j
public class SqlDictManageImpl extends AbstractDictManage {

    private JdbcTemplate jt;

    private TransactionTemplate trant;


    public SqlDictManageImpl(SpringContext context) {
        super(context);
        this.jt = context.getBean(JdbcTemplate.class);
        this.trant = context.getBean(TransactionTemplate.class);
    }

    @Override
    public Integer getKey() {
        return 2;
    }

    @Override
    public void addPair(String dictName, Pair pair) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPairs(String dictName, List<Pair> pairs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePair(String dictName, String pairKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePairs(String dictName, List<String> pairKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteDict(String dn) {
        super.deleteDict(dn);
        jt.update("delete from h_dict_sql where dict_name=?", new Object[]{dn});
        log.info("删除字典sql枚举信息成功, {}", dn);
    }

    @Override
    public void updateDict(Dict dict) {
        super.updateDict(dict);
        if (dict instanceof SqlDict) {
            SqlDict sd = (SqlDict) dict;
            jt.update("delete from h_dict_sql where dict_name=?", new Object[]{sd.getDictName()});
            jt.update("insert into h_dict_sql(dict_name,sql_content) values(?,?)", ps -> {
                ps.setString(1, sd.getDictName());
                ps.setString(2, sd.getSqlContent());
            });
            log.info("删除字典sql枚举信息成功, {}", dict);
        }
    }
}
