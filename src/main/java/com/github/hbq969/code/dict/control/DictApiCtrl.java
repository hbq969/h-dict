package com.github.hbq969.code.dict.control;

import cn.hutool.core.collection.CollectionUtil;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.dict.model.ApiDnModel;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(path = "/${dict.dict-ctrl-prefix:hbq969-dict}/dict-api")
@Tag(name = "维护使用-字典api接口")
@Slf4j
public class DictApiCtrl implements ICommonControl {
    @Autowired
    private MapDictHelperImpl mapDictHelper;

    @Operation(summary = "根据字段名查询字典信息")
    @RequestMapping(path = "/pairs", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<List<Pair>> getDictList(@Valid @RequestBody ApiDnModel model) {
        Map<String, List<Pair>> map = new HashMap<>(CollectionUtil.size(model.getFs()));
        for (String fn : model.getFs()) {
            List<Pair> dls = mapDictHelper.queryPairList(fn);
            if (CollectionUtil.isEmpty(dls))
                map.put(fn, Collections.EMPTY_LIST);
            else
                map.put(fn, dls);
        }
        return ReturnMessage.success(map);
    }
}
