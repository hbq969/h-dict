package com.github.hbq969.code.dict.service.api.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.InitScriptUtils;
import com.github.hbq969.code.dict.config.DictConf;
import com.github.hbq969.code.dict.dao.DictDao;
import com.github.hbq969.code.dict.event.DictChangeEvent;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.service.api.DictHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : hbq969@gmail.com
 * @description : MapDictHelperImpl
 * @createTime : 2024/5/11 17:58
 */
@Slf4j
public class MapDictHelperImpl implements DictHelper<Map>, InitializingBean {

    private volatile Map<String, Map<String, String>> pairsMap = new HashMap<>(2 << 8);

    private volatile Map<String, Dict> dictMap = new HashMap<>(2 << 6);

    private SpringContext context;

    private JdbcTemplate jt;

    private DictDao dictDao;

    private DictConf conf;

    public MapDictHelperImpl(SpringContext context) {
        this.context = context;
        this.jt = context.getBean(JdbcTemplate.class);
        this.dictDao = context.getBean("h-dict-DictDao", DictDao.class);
        this.conf = context.getBean(DictConf.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dictInit();
        reloadImmediately();
    }

    private void dictInit() {
        try {
            this.dictDao.createDictBase();
            log.info("创建h_dict_base成功");
        } catch (Exception e) {
            log.info("h_dict_base已存在");
        }
        try {
            this.dictDao.createDictPairs();
            log.info("创建h_dict_pairs成功");
        } catch (Exception e) {
            log.info("h_dict_pairs已存在");
        }
        try {
            this.dictDao.createDictSql();
            log.info("创建h_dict_sql成功");
        } catch (Exception e) {
            log.info("h_dict_sql已存在");
        }
        try {
            this.dictDao.createDictSqlCounty();
            log.info("创建h_dict_sql_county成功");
        } catch (Exception e) {
            log.info("h_dict_sql_county已存在");
        }
        InitScriptUtils.initial(this.context, "h-dict-data.sql", null);
    }

    @Override
    public Collection<Dict> allDicts() {
        return Collections.unmodifiableCollection(this.dictMap.values());
    }

    @Override
    public Optional<Dict> queryDict(String dictName) {
        return Optional.ofNullable(this.dictMap.get(dictName));
    }

    @Override
    public boolean isDict(String dictName) {
        return queryDict(dictName).isPresent();
    }

    @Override
    public boolean isDictKey(String dictName, String key) {
        return queryValue(dictName, key) != null;
    }

    @Override
    public String queryValue(String dictName, String key) {
        Map<String, String> pm = this.pairsMap.get(dictName);
        if (pm == null) {
            return null;
        }
        return pm.get(key);
    }

    @Override
    public Map<String, String> queryPairs(String dictName) {
        return this.pairsMap.get(dictName);
    }

    @Override
    public Map<String, String> queryPairs(String dictName, boolean flip) {
        if (flip) {
            Map<String, String> map = queryPairs(dictName);
            Map<String, String> fm = new HashMap<>(CollectionUtils.size(map));
            for (Map.Entry<String, String> e : map.entrySet()) {
                fm.put(e.getValue(), e.getKey());
            }
            return fm;
        } else {
            return queryPairs(dictName);
        }
    }

    @Override
    public List<Pair> queryPairList(String dictName) {
        Map<String, String> map = queryPairs(dictName);
        if (MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        List<Pair> pairList = new ArrayList<>(map.size());
        for (Map.Entry<String, String> e : map.entrySet()) {
            Pair p = new Pair();
            p.setKey(e.getKey());
            p.setValue(e.getValue());
            pairList.add(p);
        }
        return pairList;
    }

    @Override
    public List<Pair> queryPairList(String dictName, boolean flip) {
        if (flip) {
            Map<String, String> map = queryPairs(dictName);
            if (MapUtils.isEmpty(map)) {
                return Collections.emptyList();
            }
            List<Pair> pairList = new ArrayList<>(map.size());
            for (Map.Entry<String, String> e : map.entrySet()) {
                Pair p = new Pair();
                p.setKey(e.getValue());
                p.setValue(e.getKey());
                pairList.add(p);
            }
            return pairList;
        } else {
            return queryPairList(dictName);
        }
    }

    @EventListener(DictChangeEvent.class)
    @Scheduled(cron = "${dict.reload.cron:0,30 * * * * *}")
    @Override
    public void reloadImmediately() {
        try {
            Map<String, Dict> dictTmpMap = new HashMap<>(2 << 6);
            Map<String, Dict> fdm = dictDao.queryFixDicts().stream().collect(Collectors.toMap(d -> d.getDictName(), d -> d, (d1, d2) -> d2));
            dictTmpMap.putAll(fdm);

            Map<String, Dict> sdm = dictDao.querySqlDicts().stream().filter(d -> {
                List<Map> list = dictDao.queryPairsWithSqlContent(d.getSqlContent());
                d.setDictNum(list.size());
                return true;
            }).collect(Collectors.toMap(d -> d.getDictName(), d -> d, (d1, d2) -> d2));
            dictTmpMap.putAll(sdm);
            this.dictMap = dictTmpMap;
            if (log.isDebugEnabled()) {
                log.debug("重载字典基本信息数据: {} 个", this.dictMap.size());
            }
            Map<String, Map<String, String>> pairsTmpMap = new HashMap<>(2 << 8);
            dictDao.queryFixDictPairs().forEach(m -> {
                String dn = MapUtils.getString(m, "dn");
                String key = MapUtils.getString(m, "key");
                String val = MapUtils.getString(m, "value");
                Map<String, String> pm = pairsTmpMap.get(dn);
                if (pm == null) {
                    pm = new LinkedHashMap<>(16);
                    pairsTmpMap.put(dn, pm);
                }
                pm.put(key, val);
            });

            dictDao.querySqlDicts().forEach(sd -> {
                try {
                    dictDao.queryPairsWithSqlContent(sd.getSqlContent()).forEach(m -> {
                        String dn = sd.getDictName();
                        String key = MapUtils.getString(m, "key");
                        String val = MapUtils.getString(m, "value");
                        Map<String, String> pm = pairsTmpMap.get(dn);
                        if (pm == null) {
                            pm = new LinkedHashMap<>(16);
                            pairsTmpMap.put(dn, pm);
                        }
                        pm.put(key, val);
                    });
                } catch (Exception e) {
                    log.error("查询字典: {} 的枚举数据时异常, 可能是sql格式不对（必须是两列）: {}", sd.getDictName(), sd.getSqlContent());
                }
            });
            this.pairsMap = pairsTmpMap;
            if (log.isDebugEnabled()) {
                log.info("重载字典枚举数据: {} 个", this.pairsMap.size());
            }

        } catch (Exception e) {
            log.error("启动加载字典数据异常", e);
        }
    }

    @Override
    public void tranForDict(Map data) {
        Set<String> ks = data.keySet();
        ks.forEach(dn -> {
            if (isDict(dn)) {
                String pairKey = MapUtils.getString(data, dn);
                data.put(
                        String.join("", conf.getMapKeyPrefix(), StringUtils.capitalize(dn)),
                        queryValue(dn, pairKey)
                );
            }
        });
    }

    @Override
    public void tranForDict(Map data, List<String> fs) {
        if (CollectionUtils.isNotEmpty(fs)) {
            try {
                fs.forEach(dn -> {
                    if (isDict(dn)) {
                        data.put(
                                String.join("", conf.getMapKeyPrefix(), StringUtils.capitalize(dn)),
                                queryValue(dn, MapUtils.getString(data, dn))
                        );
                    }
                });
            } catch (Exception e) {
                // DO nothing
            }
        }
    }

    @Override
    public void tranForDict(Map data, String... fs) {
        if (ArrayUtil.isNotEmpty(fs)) {
            for (String dn : fs) {
                try {
                    if (isDict(dn)) {
                        data.put(
                                String.join("", conf.getMapKeyPrefix(), StringUtils.capitalize(dn)),
                                queryValue(dn, MapUtils.getString(data, dn))
                        );
                    }
                } catch (Exception e) {
                    // DO nothing
                }
            }
        }
    }
}
