<script lang="ts" setup>
import {Edit} from '@element-plus/icons-vue'
import {onMounted, reactive, ref} from 'vue'
import axios from '@/network/index'
import {msg} from '@/utils/Utils'
import {getLangData} from "@/i18n/locale";

const langData = getLangData()

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
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
  })
}

// 重载缓存字典数据
const reloadDict = () => {
  axios({
    url: '/reloadDict',
    method: 'post'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg(res.data.body, 'success')
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
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
      msg(res.data.body, 'success')
      queryDicts()
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
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
      msg(res.data.body, 'success')
      dialogFormVisible.value = false
      queryDicts()
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
  })
}

onMounted(() => {
  console.log('页面加载后')
  queryDicts()
});

const dialogFormVisible = ref(false)
const dialogPairFormVisible = ref(false)
const pairAddWays = reactive([{key: '1', label: langData.dictSingleAdd}, {key: '2', label: langData.dictBatchAdd}])
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
  console.debug('切换方式: ', pairInfo.pairAddWay)
  console.debug('切换方式类型: ', typeof pairInfo.pairAddWay)
}

const queryPairs = () => {
  axios({
    url: '/queryPairs?pageNum=' + dictInfo.pairPageNum + '&pageSize=' + dictInfo.pairPageSize + '&dictName=' + dictInfo.dictName,
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      dictInfo.pairs = res.data.body.list;
      dictInfo.pairTotal = res.data.body.total;
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
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
      msg(langData.dictBaseInfoNotExists, 'warning')
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
            msg(res.data.body, 'success')
            queryPairs()
          }else{
            let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
            msg(content, "warning")
          }
        }).catch((err: Error) => {
          msg(langData.axiosRequestErr, 'error')
        })
      } else {
        msg(langData.parasInvalidCheck, 'error');
      }
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
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
      msg(res.data.body, 'success')
      queryPairs()
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    msg(langData.axiosRequestErr, 'error')
  })
}

const debounce = (callback: (...args: any[]) => void, delay: number) => {
  let tid: any;
  return function (...args: any[]) {
    const ctx = window;
    tid && clearTimeout(tid);
    tid = setTimeout(() => {
      callback.apply(ctx, args);
    }, delay);
  };
};

const _ = (window as any).ResizeObserver;
(window as any).ResizeObserver = class ResizeObserver extends _ {
  constructor(callback: (...args: any[]) => void) {
    callback = debounce(callback, 20);
    super(callback);
  }
};

</script>

<template>
  <div class="dict-console">

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-form :inline="true" size="small">
        <el-form-item :label="langData.dictAppName" prop="app">
          <el-input v-model="form.app" :placeholder="langData.formInputPlaceholder" @keyup.enter="queryDicts" style="width:140px"/>
        </el-form-item>
        <el-form-item :label="langData.dictName" prop="dictName">
          <el-input v-model="form.dictName" :placeholder="langData.formInputPlaceholder" @keyup.enter="queryDicts" style="width:140px"/>
        </el-form-item>
        <el-form-item :label="langData.dictDesc" prop="dictDesc">
          <el-input v-model="form.dictDesc" :placeholder="langData.formInputPlaceholder" @keyup.enter="queryDicts" style="width:140px"/>
        </el-form-item>
        <el-form-item :label="langData.dictSource" prop="dictSource">
          <el-select v-model="form.dictSource" @change="queryDicts" style="width:110px">
            <el-option :label="langData.formSelectPlaceholder" value="-1"/>
            <el-option :label="langData.fixedSource" value="1"/>
            <el-option :label="langData.dbSource" value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryDicts">{{langData.btnSearch}}</el-button>
          <el-button type="warning" @click="reloadDict" :dark="false">{{langData.reloadDictData}}</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar__actions">
        <span class="toolbar__count">{{ data.total }} 条记录</span>
        <el-button type="success" :icon="Edit" circle size="small" @click="showAddDictDialog()" :title="langData.addDictTitle"/>
      </div>
    </div>

    <!-- 主数据表格 -->
    <div class="table-card">
      <el-table
        :data="data.dictList"
        style="width: 100%"
        table-layout="fixed"
        :stripe="false"
        size="small"
        :highlight-current-row="true"
        row-class-name="dict-row"
      >
        <el-table-column fixed="left" :label="langData.tableHeaderOp" width="110" header-align="center" align="center">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="showEditDictDialog(scope)" v-if="scope.row.app==data.app">
              {{langData.btnEdit}}
            </el-button>
            <el-button link type="primary" size="small" @click="showEditDictDialog(scope)" v-else>
              {{langData.btnDetail}}
            </el-button>
            <el-popconfirm :title="langData.confirmDelete" @confirm="delDict(scope)" icon-color="red" confirm-button-type="danger">
              <template #reference>
                <el-button link type="danger" size="small" v-if="scope.row.app==data.app">{{langData.btnDelete}}</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column prop="dictName" :label="langData.dictName" width="250" :show-overflow-tooltip="true" header-align="center" align="left"/>
        <el-table-column prop="dictDesc" :label="langData.dictDesc" width="150" :show-overflow-tooltip="true" header-align="center" align="left"/>
        <el-table-column prop="fmtDictSource" :label="langData.dictSource" width="100" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="keyColumn" :label="langData.keyWay" width="120" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="valColumn" :label="langData.valueWay" width="120" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="dictNum" :label="langData.fixedNum" width="120" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="sqlContent" :label="langData.dbSql" :show-overflow-tooltip="true" header-align="center" align="left"/>
        <el-table-column prop="app" :label="langData.createApp" width="200" :show-overflow-tooltip="true" header-align="center" align="center"/>
      </el-table>

      <div class="table-card__footer">
        <el-pagination
          v-model:page-size="form.pageSize"
          v-model:current-page="form.pageNum"
          layout="total, sizes, prev, pager, next, jumper"
          v-model:total="data.total"
          @size-change="queryDicts"
          @current-change="queryDicts" @prev-click="queryDicts" @next-click="queryDicts"
          :small="true" :background="true"
          :page-sizes="[5, 10, 20, 50, 100]"
        />
      </div>
    </div>

    <!-- 编辑/新建字典抽屉 -->
    <el-drawer
      v-model="dialogFormVisible"
      :title="edit ? (dictInfo.disabled ? langData.editProperty : langData.addDictTitle) : langData.btnDetail"
      direction="rtl"
      size="620px"
      custom-class="dict-drawer"
    >
      <el-form :model="dictInfo" :label-width="formLabelWidth" label-position="right" size="small">
        <div class="drawer-section">
          <div class="drawer-section__title">{{langData.baseInfo}}</div>
          <div class="drawer-section__body">
            <el-form-item :label="langData.dictName" style="width: 100%">
              <el-input v-model="dictInfo.dictName" type="text" :disabled="dictInfo.disabled"/>
            </el-form-item>
            <el-form-item :label="langData.dictDesc" style="width: 100%">
              <el-input v-model="dictInfo.dictDesc" type="text"/>
            </el-form-item>
            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item :label="langData.dictSource">
                  <el-select v-model="dictInfo.dictSource" :disabled="dictInfo.disabled" style="width:100%">
                    <el-option :label="langData.fixedSource" value="1"/>
                    <el-option :label="langData.dbSource" value="2"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="langData.keyWay" label-width="60px">
                  <el-select v-model="dictInfo.keyColumn" style="width:100%">
                    <el-option label="Key" value="key"/>
                    <el-option label="Value" value="value"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="langData.valueWay" label-width="70px">
                  <el-select v-model="dictInfo.valColumn" style="width:100%">
                    <el-option label="Key" value="key"/>
                    <el-option label="Value" value="value"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <div class="drawer-section">
          <div class="drawer-section__title">{{langData.enumInfo}}</div>
          <div class="drawer-section__body">
            <div v-if="dictInfo.dictSource==1" class="pairs-panel">
              <div class="pairs-panel__header">
                <span class="pairs-panel__count">{{ dictInfo.pairTotal }} 个枚举项</span>
                <el-button :icon="Edit" size="small" text @click="showPairDialog" v-if="edit">{{langData.btnAdd}}</el-button>
              </div>
              <el-table
                :data="dictInfo.pairs"
                style="width: 100%"
                :stripe="false"
                size="small"
                :highlight-current-row="true"
                row-class-name="pair-row"
              >
                <el-table-column fixed="left" :label="langData.tableHeaderOp" width="160" header-align="center" align="center">
                  <template #default="scope">
                    <el-popconfirm :title="langData.confirmDelete" @confirm="delPair(scope)" icon-color="red" confirm-button-type="danger">
                      <template #reference>
                        <el-button link type="danger" size="small" v-if="edit">{{langData.btnDelete}}</el-button>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column>
                <el-table-column prop="key" :label="langData.key" :show-overflow-tooltip="true" header-align="center" align="center"/>
                <el-table-column prop="value" :label="langData.value" :show-overflow-tooltip="true" header-align="center" align="center"/>
              </el-table>
              <div class="pairs-panel__pagination">
                <el-pagination
                  v-model:page-size="dictInfo.pairPageSize"
                  v-model:current-page="dictInfo.pairPageNum"
                  layout="total, sizes, prev, pager, next"
                  v-model:total="dictInfo.pairTotal"
                  @size-change="queryPairs"
                  @current-change="queryPairs" @prev-click="queryPairs" @next-click="queryPairs"
                  :small="true" :background="true"
                  :page-sizes="[5,10,20,50,100]"
                />
              </div>
            </div>
            <div v-else-if="dictInfo.dictSource==2">
              <el-form-item :label="langData.dbSql" :label-width="formLabelWidth" style="width: 100%">
                <el-input
                  v-model="dictInfo.sqlContent"
                  :rows="5"
                  type="textarea"
                  :placeholder="langData.dbSqlPlaceHolder"
                />
              </el-form-item>
            </div>
          </div>
        </div>
      </el-form>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="dialogFormVisible = false">{{langData.btnCancel}}</el-button>
          <el-button type="primary" @click="updateDict" v-if="edit">{{langData.btnSave}}</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 添加枚举项对话框 -->
    <el-dialog v-model="dialogPairFormVisible" :title="langData.addEnum" width="480px" custom-class="pair-dialog">
      <el-form :model="pairInfo" label-position="top" size="small">
        <el-form-item :label="langData.addWay">
          <el-select v-model="pairInfo.pairAddWay" :placeholder="langData.formSelectPlaceholder" size="small" style="width:100%">
            <el-option :label="w.label" :value="w.key" v-for="w in pairAddWays"/>
          </el-select>
        </el-form-item>
        <div v-if="pairInfo.pairAddWay=='1'">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item :label="langData.key">
                <el-input v-model="pairInfo.key" type="text"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="langData.value">
                <el-input v-model="pairInfo.value" type="text"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-else>
          <el-form-item :label="langData.batchEnum" prop="pairString">
            <el-input v-model="pairInfo.pairString" type="textarea" rows="5"/>
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogPairFormVisible = false">{{langData.btnCancel}}</el-button>
          <el-button type="primary" @click="addPair">{{langData.btnAdd}}</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>
/* ========================================
   Dict Console — Precision Data Console
   ======================================== */

.dict-console {
  display: flex;
  flex-direction: column;
  gap: 0;
  height: 100%;
  padding: 16px 20px;
}

/* ---- 工具栏 ---- */
.toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 12px 16px;
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 10px 10px 0 0;
  border-bottom: none;
}

.toolbar :deep(.el-form--inline) {
  row-gap: 8px;
}

.toolbar :deep(.el-form-item__label) {
  font-weight: 500;
  font-size: 12px;
  color: var(--el-text-color-secondary);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.toolbar__actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
  padding-top: 2px;
}

.toolbar__count {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
  font-family: 'Menlo', 'SF Mono', 'Cascadia Code', monospace;
  white-space: nowrap;
}

/* ---- 表格卡片 ---- */
.table-card {
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 0 0 10px 10px;
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.table-card :deep(.el-table) {
  flex: 1;
  --el-table-border-color: var(--el-border-color-lighter);
}

.table-card :deep(.el-table th.el-table__cell) {
  background: var(--el-fill-color-light);
  font-weight: 600;
  font-size: 12px;
  letter-spacing: 0.03em;
  text-transform: uppercase;
  color: var(--el-text-color-secondary);
  border-bottom: 2px solid var(--el-border-color-light);
}

.table-card :deep(.dict-row) {
  transition: background 0.12s ease;
}

.table-card :deep(.dict-row:hover > td) {
  background: var(--el-fill-color-light) !important;
}

.table-card :deep(.dict-row.current-row > td) {
  background: var(--el-color-primary-light-9) !important;
}

.table-card__footer {
  display: flex;
  justify-content: flex-end;
  padding: 8px 16px;
  border-top: 1px solid var(--el-border-color-lighter);
  background: var(--el-fill-color-lighter);
}

/* ---- 抽屉 ---- */
.dict-drawer :deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  font-weight: 600;
}

.dict-drawer :deep(.el-drawer__body) {
  padding: 20px;
}

.drawer-section {
  margin-bottom: 24px;
}

.drawer-section__title {
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--el-color-primary);
  margin-bottom: 12px;
  padding-bottom: 6px;
  border-bottom: 2px solid var(--el-color-primary-light-7);
}

.drawer-section__body {
  padding-left: 0;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* ---- 枚举对面板 ---- */
.pairs-panel {
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  overflow: hidden;
}

.pairs-panel__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: var(--el-fill-color-lighter);
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.pairs-panel__count {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  font-family: 'Menlo', 'SF Mono', 'Cascadia Code', monospace;
}

.pairs-panel :deep(.pair-row:hover > td) {
  background: var(--el-fill-color-light) !important;
}

.pairs-panel :deep(.el-table th.el-table__cell) {
  background: transparent;
  font-weight: 600;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--el-text-color-secondary);
}

.pairs-panel__pagination {
  display: flex;
  justify-content: flex-end;
  padding: 6px 12px;
  border-top: 1px solid var(--el-border-color-lighter);
}

/* ---- 枚举添加对话框 ---- */
.pair-dialog :deep(.el-dialog__header) {
  padding: 16px 20px;
  font-weight: 600;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.pair-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.pair-dialog :deep(.el-form-item__label) {
  font-weight: 500;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

/* ========================================
   暗黑模式适配
   ======================================== */
html.dark .table-card :deep(.dict-row:hover > td) {
  background: var(--el-fill-color-light) !important;
}

html.dark .table-card :deep(.dict-row.current-row > td) {
  background: rgba(99, 102, 241, 0.12) !important;
}
</style>
