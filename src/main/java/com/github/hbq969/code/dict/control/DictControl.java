package com.github.hbq969.code.dict.control;

import com.github.hbq969.code.common.log.api.Log;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.I18nUtils;
import com.github.hbq969.code.dict.control.vo.DictPair;
import com.github.hbq969.code.dict.control.vo.QueryDict;
import com.github.hbq969.code.dict.control.vo.QueryTran;
import com.github.hbq969.code.dict.model.AppDict;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;
import com.github.hbq969.code.dict.service.api.DictService;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : DictControl
 * @createTime : 2024/5/14 15:39
 */
@RequestMapping(path = "/hbq969-dict")
@Tag(name = "维护使用-字典管理接口")
@Slf4j
public class DictControl implements ICommonControl {

    @Autowired
    private DictService dictService;

    @Autowired
    private MapDictHelperImpl mapDictHelper;

    @Value("${spring.application.name}")
    private String app;

    @Autowired
    private SpringContext context;

    @Operation(summary = "字典分页查询")
    @RequestMapping(path = "/queryDicts", method = RequestMethod.POST)
    @ResponseBody
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "queryDicts",apiDesc = "字典分页查询")
    public ReturnMessage<AppDict> queryDicts(@RequestBody QueryDict q) {
        PageInfo<Dict> pi = PageHelper.startPage(q.getPageNum(), q.getPageSize())
                .doSelectPageInfo(() -> dictService.queryDicts(q));
        AppDict ad = new AppDict();
        ad.setPageInfo(pi);
        ad.setCurApp(app);
        return ReturnMessage.success(ad);
    }

    @Operation(summary = "删除字典信息")
    @RequestMapping(path = "/delDict", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "delDict",apiDesc = "删除字典信息")
    public ReturnMessage<?> delDict(@RequestBody Dict dict) {
        dictService.delDict(dict);
        return ReturnMessage.success(I18nUtils.getMessage(context,"delete.result"));
    }

    @Operation(summary = "新增、更新字典基本信息")
    @RequestMapping(path = "/updateDict", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "updateDict",apiDesc = "新增、更新字典基本信息")
    public ReturnMessage<?> updateDict(@RequestBody SqlDict dict) {
        dictService.updateDict(dict);
        return ReturnMessage.success(I18nUtils.getMessage(context,"update.result"));
    }

    @Operation(summary = "字典固定枚举值分页查询")
    @RequestMapping(path = "/queryPairs", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "queryPairs",apiDesc = "字典固定枚举值分页查询")
    public ReturnMessage<List<Pair>> queryPairs(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(name = "dictName") String dictName) {
        PageInfo<Pair> pi = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> dictService.queryPairs(dictName));
        return ReturnMessage.success(pi);
    }

    @Operation(summary = "重载字典数据")
    @RequestMapping(path = "/reloadDict", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "reloadDict",apiDesc = "重载字典数据")
    public ReturnMessage<?> reloadDict() {
        mapDictHelper.reloadImmediately();
        return ReturnMessage.success(I18nUtils.getMessage(context,"op.result"));
    }

    @Operation(summary = "保存固定枚举值")
    @RequestMapping(path = "/addPair", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "addPair",apiDesc = "保存固定枚举值")
    public ReturnMessage<?> addPair(@RequestBody DictPair pair) {
        dictService.addPair(pair);
        return ReturnMessage.success(I18nUtils.getMessage(context,"save.result"));
    }

    @Operation(summary = "删除固定枚举值")
    @RequestMapping(path = "/delPair", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "delPair",apiDesc = "删除固定枚举值")
    public ReturnMessage<?> delPair(@RequestBody DictPair pair) {
        dictService.delPair(pair);
        return ReturnMessage.success(I18nUtils.getMessage(context,"delete.result"));
    }

    @Operation(summary = "查询字典基本信息是否存在")
    @RequestMapping(path = "/queryDict", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "queryDict",apiDesc = "查询字典基本信息是否存在")
    public ReturnMessage<Boolean> queryDict(@RequestParam(name = "dn") String dn) {
        return ReturnMessage.success(dictService.queryDict(dn));
    }

    @Operation(summary = "测试字典转义")
    @RequestMapping(path = "/dictTransCheck", method = RequestMethod.POST)
    @ResponseBody
    @SMRequiresPermissions(menu = "Dictionary",apiKey = "dictTransCheck",apiDesc = "测试字典转义")
    public ReturnMessage<?> dictTransCheck(@RequestBody QueryTran q) {
        return ReturnMessage.success(mapDictHelper.queryValue(q.getDn(), q.getPairKey()));
    }
}
