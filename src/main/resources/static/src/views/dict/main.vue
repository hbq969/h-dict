<script lang="ts" setup>
import {Edit} from '@element-plus/icons-vue'
import {onMounted, reactive, ref} from 'vue'
import axios from '@/network/index'
import {msg} from '@/utils/Utils'

const form = reactive({
  dictName: '',
  dictDesc: '',
  dictSource: '-1',
  app: '',
  pageSize: 10,
  pageNum: 1
});

const data = reactive({
  app: '',
  dictList: [],
  total: 0
});

const formLabelWidth = ref('140px')
const dictInfo = reactive({
  disabled: true,
  pageNum: 1,
  pageSize: 5,
  dictName: '',
  dictDesc: '',
  dictSource: '-1',
  keyColumn: 'key',
  valColumn: 'value',
  sqlContent: '',
  pairs: [],
  pairTotal: 0,
  pairPageNum: 1,
  pairPageSize: 10,
  init: function () {
    this.disabled = false
    this.dictName = ''
    this.dictDesc = ''
    this.dictSource = ''
    this.dictSource = '1'
    this.keyColumn = 'key'
    this.valColumn = 'value'
    this.sqlContent = ''
    this.pairs = []
    this.pairTotal = 0
    this.pairPageNum = 1
    this.pairPageSize = 5
  },
  valid: function () {
    return (this.dictSource == '1' && this.dictName != '' && this.dictDesc != '')
        || (this.dictSource == '2' && this.dictName != '' && this.dictDesc != '' && this.sqlContent != '')
  }
})

// 查询字典列表
const queryDicts = () => {
  axios({
    url: '/queryDicts',
    method: 'post',
    data: form
  }).then((res: any) => {
    if (res.data.state == "OK") {
      data.dictList = res.data.body.pageInfo.list
      data.total = res.data.body.pageInfo.total;
      data.app = res.data.body.curApp;
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

// 重载缓存字典数据
const reloadDict = () => {
  axios({
    url: '/reloadDict',
    method: 'post'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg('重载成功', 'success')
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

// 删除字典信息
const delDict = (scope: any) => {
  axios({
    url: '/delDict',
    method: 'post',
    data: {
      dictName: scope.row.dictName,
      dictSource: scope.row.dictSource
    }
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg('删除成功', 'success')
      queryDicts()
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const showAddDictDialog = () => {
  dialogFormVisible.value = true
  dictInfo.init()
  edit.value = true
}

const edit = ref(true)
const showEditDictDialog = (scope: any) => {
  dialogFormVisible.value = true
  dictInfo.disabled = true
  dictInfo.dictName = scope.row.dictName;
  dictInfo.dictDesc = scope.row.dictDesc;
  dictInfo.dictSource = scope.row.dictSource + '';
  dictInfo.keyColumn = scope.row.keyColumn;
  dictInfo.valColumn = scope.row.valColumn;
  if (scope.row.dictSource == '1') {
    queryPairs()
  } else if (scope.row.dictSource == '2') {
    dictInfo.sqlContent = scope.row.sqlContent;
  }
  if (scope.row.app == data.app) {
    edit.value = true
  } else {
    edit.value = false
  }
}

const updateDict = () => {
  axios({
    url: '/updateDict',
    method: 'post',
    data: dictInfo
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg('修改成功', 'success')
      dialogFormVisible.value = false
      queryDicts()
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const headerCellStyle = () => {
  // 添加表头颜色
  return {backgroundColor: '#f5f5f5', color: '#333', fontWeight: 'bold'};
}

onMounted(() => {
  console.log('页面加载后')
  queryDicts()
});

const dialogFormVisible = ref(false)
const dialogPairFormVisible = ref(false)
const pairAddWays = reactive([{key: '1', label: '单个添加'}, {key: '2', label: '批量添加'}])
const pairInfo = reactive({
  pairAddWay: '1',
  key: '',
  value: '',
  pairString: '',
  init: function () {
    this.pairAddWay = '1'
    this.key = ''
    this.value = ''
    this.pairString = ''
  }
})
const changePairAddWay = () => {
  console.log('++++切换方式: ', pairInfo.pairAddWay)
  console.log('++++切换方式类型: ', typeof pairInfo.pairAddWay)
}

const queryPairs = () => {
  axios({
    url: '/queryPairs?pageNum=' + dictInfo.pairPageNum + '&pageSize=' + dictInfo.pairPageSize + '&dictName=' + dictInfo.dictName,
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      dictInfo.pairs = res.data.body.list;
      dictInfo.pairTotal = res.data.body.total;
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const showPairDialog = () => {
  dialogPairFormVisible.value = true
  pairInfo.init()
}

const addPair = () => {
  axios({
    url: '/queryDict?dn=' + dictInfo.dictName,
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK' && !res.data.body) {
      msg('字典基本信息不存在，请先添加', 'warning')
    } else {
      if (dictInfo.valid()) {
        axios({
          url: '/addPair',
          method: 'post',
          data: {
            dict: dictInfo,
            pair: pairInfo
          }
        }).then((res: any) => {
          if (res.data.state == 'OK') {
            msg('添加成功', 'success')
            queryPairs()
          }
        }).catch((err: Error) => {
          msg('请求异常', 'error')
        })
      } else {
        msg('参数不对，请检查', 'error');
      }
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
  dialogPairFormVisible.value = false
}

const delPair = (scope: any) => {
  axios({
    url: '/delPair',
    method: 'post',
    data: {
      dict: dictInfo,
      pair: {
        key: scope.row.key,
        value: scope.row.value
      }
    }
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg('删除成功', 'success')
      queryPairs()
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}


</script>

<template>
  <div class="container">
    <el-divider content-position="left">查询条件</el-divider>
    <el-form class="demo-form-inline" :inline="true" size="small">
      <el-form-item label="应用名" prop="app" placeholder="请输入..." @keyup.enter="queryDicts">
        <el-input v-model="form.app"/>
      </el-form-item>
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="form.dictName" placeholder="请输入..." @keyup.enter="queryDicts"/>
      </el-form-item>
      <el-form-item label="字典描述" prop="dictDesc">
        <el-input v-model="form.dictDesc" placeholder="请输入..." @keyup.enter="queryDicts"/>
      </el-form-item>
      <el-form-item label="来源类型" prop="dictSource">
        <el-select v-model="form.dictSource" class="m-2" @change="queryDicts">
          <el-option label="请选择" value="-1"/>
          <el-option label="固定枚举" value="1"/>
          <el-option label="数据表枚举" value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryDicts">查询</el-button>
        <el-button type="warning" @click="reloadDict">重载字典数据</el-button>
        <el-button type="primary" :icon="Edit" circle @click="showAddDictDialog()" title="添加字典配置"/>
      </el-form-item>
    </el-form>

    <el-divider content-position="left">字典列表</el-divider>
    <el-table :data="data.dictList" style="width: 100%" :border="true" table-layout="fixed" :stripe="true" size="small"
              :highlight-current-row="true" :header-cell-style="headerCellStyle">
      <el-table-column fixed="left" label="操作" width="100" header-align="center" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showEditDictDialog(scope)"
                     v-if="scope.row.app==data.app">编辑
          </el-button>
          <el-button link type="primary" size="small" @click="showEditDictDialog(scope)"
                     v-else>查看
          </el-button>
          <el-popconfirm title="你确定要删除本条记录吗?" @confirm="delDict(scope)"
                         icon-color="red"
                         confirm-button-type="danger">
            <template #reference>
              <el-button link type="danger" size="small" v-if="scope.row.app==data.app">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column prop="dictName" label="字典名称" width="150" :show-overflow-tooltip="true" header-align="center"
                       align="left"/>
      <el-table-column prop="dictDesc" label="字典描述" width="150" :show-overflow-tooltip="true" header-align="center"
                       align="left"/>
      <el-table-column prop="fmtDictSource" label="来源类型" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="keyColumn" label="KEY取值类型" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="valColumn" label="Value取值类型" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="dictNum" label="固定枚举数量" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="sqlContent" label="SQL枚举语句" :show-overflow-tooltip="true" header-align="center"
                       align="left"/>
      <el-table-column prop="app" label="创建应用" width="150" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
    </el-table>
    <el-pagination class="page" v-model:page-size="form.pageSize" v-model:current-page="form.pageNum"
                   layout="->, total, sizes, prev, pager, next, jumper" v-model:total="data.total"
                   @size-change="queryDicts"
                   @current-change="queryDicts" @prev-click="queryDicts" @next-click="queryDicts"
                   :small="true" :background="true"
                   :page-sizes="[5, 10, 20, 50, 100]"/>

    <el-dialog v-model="dialogFormVisible" title="编辑属性">
      <el-form :model="dictInfo" :label-width="formLabelWidth" label-position="right" size="small" inline>
        <el-divider content-position="left">基础信息</el-divider>
        <el-form-item label="字典名称：" style="width: 40%">
          <el-input v-model="dictInfo.dictName" type="text" :disabled="dictInfo.disabled"/>
        </el-form-item>
        <el-form-item label="字典描述：" style="width: 40%">
          <el-input v-model="dictInfo.dictDesc" type="text"/>
        </el-form-item>
        <el-form-item label="字典来源：" style="width: 40%">
          <el-select v-model="dictInfo.dictSource" class="m-2" :disabled="dictInfo.disabled">
            <el-option label="固定枚举" value="1"/>
            <el-option label="数据表枚举" value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Key取值类型：" style="width: 40%">
          <el-select v-model="dictInfo.keyColumn" class="m-2">
            <el-option label="Key" value="key"/>
            <el-option label="Value" value="value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Value取值类型：" style="width: 40%">
          <el-select v-model="dictInfo.valColumn" class="m-2">
            <el-option label="Key" value="key"/>
            <el-option label="Value" value="value"/>
          </el-select>
        </el-form-item>

        <el-divider content-position="left">枚举信息</el-divider>
        <div v-if="dictInfo.dictSource==1">
          <el-table :data="dictInfo.pairs" style="width: 100%" :border="true" table-layout="fixed" :stripe="true"
                    size="small"
                    :highlight-current-row="true" :header-cell-style="headerCellStyle">
            <el-table-column fixed="left" label="操作" width="180" header-align="center" align="center">
              <template #default="scope">
                <el-popconfirm title="你确定要删除本条记录吗?" @confirm="delPair(scope)"
                               icon-color="red"
                               confirm-button-type="danger">
                  <template #reference>
                    <el-button link type="danger" size="small" v-if="edit">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
            <el-table-column prop="key" label="Key值" :show-overflow-tooltip="true" header-align="center"
                             align="center"/>
            <el-table-column prop="value" label="Value值" :show-overflow-tooltip="true" header-align="center"
                             align="center"/>
          </el-table>
          <el-pagination class="page" v-model:page-size="dictInfo.pairPageSize"
                         v-model:current-page="dictInfo.pairPageNum"
                         layout="->, total, sizes, prev, pager, next, jumper" v-model:total="dictInfo.pairTotal"
                         @size-change="queryPairs"
                         @current-change="queryPairs" @prev-click="queryPairs" @next-click="queryPairs"
                         :small="true" :background="true"
                         :page-sizes="[5,10,20,50,100]"/>
          <div class="addBtn">
            <el-button :icon="Edit" size="small" round @click="showPairDialog" v-if="edit">添加
            </el-button>
          </div>
        </div>
        <div v-else-if="dictInfo.dictSource==2">
          <el-form-item label="枚举数据SQL：" :label-width="formLabelWidth" style="width: 85%">
            <el-input
                v-model="dictInfo.sqlContent"
                :rows="5"
                type="textarea"
                placeholder='请输入枚举值查询的sql，注意一定要带上key、value固定别名，比如 select c_key AS "key",c_name AS "value" from h_dict_sql_county'
            />
          </el-form-item>
        </div>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="updateDict" v-if="edit">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="dialogPairFormVisible" title="添加枚举">
      <el-form :model="pairInfo" label-position="right" size="small">
        <el-form-item label="添加方式" :label-width="formLabelWidth" style="width: 80%">
          <el-select v-model="pairInfo.pairAddWay" placeholder="请选择" size="small">
            <el-option :label="w.label" :value="w.key" v-for="w in pairAddWays"/>
          </el-select>
        </el-form-item>
        <div v-if="pairInfo.pairAddWay=='1'">
          <el-form-item label="Key值：" :label-width="formLabelWidth" style="width: 80%">
            <el-input v-model="pairInfo.key" type="text"/>
          </el-form-item>
          <el-form-item label="Value值：" :label-width="formLabelWidth" style="width: 80%">
            <el-input v-model="pairInfo.value" type="text"/>
          </el-form-item>
        </div>
        <div v-else>
          <el-form-item label="批量枚举" prop="pairString" :label-width="formLabelWidth" style="width: 80%">
            <el-input v-model="pairInfo.pairString" type="textarea" rows="4"/>
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogPairFormVisible = false">取消</el-button>
          <el-button type="primary" @click="addPair">
            添加
          </el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>
.container {
  flex-grow: 1;
  padding: 20px 5%;
  overflow: auto;
  width: 80%;

}

.addBtn {
  margin-top: 20px;
  text-align: center;
}
</style>