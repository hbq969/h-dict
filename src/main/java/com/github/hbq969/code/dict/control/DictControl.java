package com.github.hbq969.code.dict.control;

import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.dict.control.vo.DictPair;
import com.github.hbq969.code.dict.control.vo.QueryDict;
import com.github.hbq969.code.dict.model.Dict;
import com.github.hbq969.code.dict.model.Pair;
import com.github.hbq969.code.dict.model.SqlDict;
import com.github.hbq969.code.dict.service.api.DictService;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : hbq969@gmail.com
 * @description : DictControl
 * @createTime : 2024/5/14 15:39
 */
@RestController("h-dict-DictControl")
@RequestMapping(path = "/ui-dict")
@Api(tags = "字典管理接口")
@Slf4j
public class DictControl implements ICommonControl {

    @Autowired
    private DictService dictService;

    @Autowired
    private MapDictHelperImpl mapDictHelper;

    @ApiOperation("字典分页查询")
    @RequestMapping(path = "/queryDicts", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<List<SqlDict>> queryDicts(@RequestBody QueryDict q) {
        PageInfo<Dict> pi = PageHelper.startPage(q.getPageNum(), q.getPageSize())
                .doSelectPageInfo(() -> dictService.queryDicts(q));
        return ReturnMessage.success(pi);
    }

    @ApiOperation("删除字典信息")
    @RequestMapping(path = "/delDict", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> delDict(@RequestBody Dict dict) {
        dictService.delDict(dict);
        return ReturnMessage.success("删除成功");
    }

    @ApiOperation("新增、更新字典基本信息")
    @RequestMapping(path = "/updateDict", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> updateDict(@RequestBody SqlDict dict) {
        dictService.updateDict(dict);
        return ReturnMessage.success("操作成功");
    }

    @ApiOperation("字典固定枚举值分页查询")
    @RequestMapping(path = "/queryPairs", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<List<Pair>> queryPairs(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(name = "dictName") String dictName) {
        PageInfo<Pair> pi = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> dictService.queryPairs(dictName));
        return ReturnMessage.success(pi);
    }

    @ApiOperation("重载字典数据")
    @RequestMapping(path = "/reloadDict", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> reloadDict() {
        mapDictHelper.reloadImmediately();
        return ReturnMessage.success("操作成功");
    }

    @ApiOperation("保存固定枚举值")
    @RequestMapping(path = "/addPair", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> addPair(@RequestBody DictPair pair) {
        dictService.addPair(pair);
        return ReturnMessage.success("保存成功");
    }

    @ApiOperation("删除固定枚举值")
    @RequestMapping(path = "/delPair", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> delPair(@RequestBody DictPair pair) {
        dictService.delPair(pair);
        return ReturnMessage.success("保存成功");
    }

    @ApiOperation("查询字典基本信息是否存在")
    @RequestMapping(path = "/queryDict", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<Boolean> queryDict(@RequestParam(name = "dn") String dn) {
        return ReturnMessage.success(dictService.queryDict(dn));
    }
}
