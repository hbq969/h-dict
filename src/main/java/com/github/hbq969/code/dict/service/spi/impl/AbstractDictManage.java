package com.github.hbq969.code.dict.service.spi.impl;

import com.github.hbq969.code.common.decorde.OptionalFacade;
import com.github.hbq969.code.common.decorde.OptionalFacadeAware;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.service.spi.DictManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @author : hbq969@gmail.com
 * @description : DictManageImpl
 * @createTime : 2024/5/11 18:31
 */
@Slf4j
public abstract class AbstractDictManage implements DictManage, OptionalFacadeAware<Integer, DictManage> {

    private SpringContext context;

    private DictManageFacade facade;

    private JdbcTemplate jt;

    private CountDownLatch cdl = new CountDownLatch(1);


    public AbstractDictManage(SpringContext context) {
        this.context = context;
        this.facade = context.getBean("h-dict-DictManageFacade", DictManageFacade.class);
        this.jt = context.getBean(JdbcTemplate.class);
    }

    @Override
    public OptionalFacade<Integer, DictManage> getOptionalFacade() {
        return this.facade;
    }

    @Override
    public void addDict(Dict dict) {
        jt.update("insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values(?,?,?,?,?)", ps -> {
            ps.setString(1, dict.getDictName());
            ps.setString(2, dict.getDictDesc());
            ps.setInt(3, dict.getDictSource());
            ps.setString(4, dict.getKeyColumn());
            ps.setString(5, dict.getValColumn());
        });
        log.info("保存字典基本信息成功, {}", dict);
    }

    @Override
    public void deleteDict(String dn) {
        jt.update("delete from h_dict_base where dict_name=?", new Object[]{dn});
        log.info("删除字典基本信息成功, {}", dn);
    }

    @Override
    public void updateDict(Dict dict) {
        jt.update("delete from h_dict_base where dict_name=?", new Object[]{dict.getDictName()});
        addDict(dict);
        log.info("更新字典基本信息成功, {}", dict);
    }
}
