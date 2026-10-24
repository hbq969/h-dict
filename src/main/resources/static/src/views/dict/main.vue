<script lang="ts" setup>
import {IcEdit, IcView, IcDelete, IcPlus, IcSearch, IcRefresh, IcFilter, IcMaximize, IcMinimize, IcReset} from '@/utils/icons'
import {onMounted, reactive, ref} from 'vue'
import axios from '@/network/index'
import {ElMessageBox} from 'element-plus'
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

const filtersVisible = ref(true)
const drawerFull = ref(false)
const loading = ref(false)
const filterCount = () => [form.app, form.dictName, form.dictDesc, form.dictSource !== '-1' ? form.dictSource : '']
    .filter(v => v !== '' && v !== null && v !== undefined).length
const resetQuery = () => {
  form.app = ''
  form.dictName = ''
  form.dictDesc = ''
  form.dictSource = '-1'
  form.pageNum = 1
  queryDicts()
}
const confirmDelDict = (scope: any) => {
  ElMessageBox.confirm(langData.confirmDelete, langData.msgBoxTitle, {
    confirmButtonText: langData.btnConfirm,
    cancelButtonText: langData.btnCancel,
    type: 'warning'
  }).then(() => delDict(scope)).catch(() => {})
}
const confirmDelPair = (scope: any) => {
  ElMessageBox.confirm(langData.confirmDelete, langData.msgBoxTitle, {
    confirmButtonText: langData.btnConfirm,
    cancelButtonText: langData.btnCancel,
    type: 'warning'
  }).then(() => delPair(scope)).catch(() => {})
}
const formLabelWidth = ref('140px')
const dictInfo = reactive({
  disabled: true,
  pageNum: 1,
  pageSize: 10,
  dictName: '',
  dictDesc: '',
  dictSource: '1',
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
    this.dictSource = '1'
    this.keyColumn = 'key'
    this.valColumn = 'value'
    this.sqlContent = ''
    this.pairs = []
    this.pairTotal = 0
    this.pairPageNum = 1
    this.pairPageSize = 10
  },
  valid: function () {
    return (this.dictSource == '1' && this.dictName != '' && this.dictDesc != '')
        || (this.dictSource == '2' && this.dictName != '' && this.dictDesc != '' && this.sqlContent != '')
  }
})

// 查询字典列表
const queryDicts = () => {
  loading.value = true
  axios({
    url: '/queryDicts',
    method: 'post',
    data: form
  }).then((res: any) => {
    if (res.data.state == "OK") {
      data.dictList = res.data.body.pageInfo.list
      data.total = res.data.body.pageInfo.total;
      data.app = res.data.body.curApp;
      loading.value = false
    }else{
      let content = res.config.baseURL+res.config.url+': '+res.data.errorMessage;
      msg(content, "warning")
      loading.value = false
    }
  }).catch((err: Error) => {
    loading.value = false
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
  dictInfo.dictName = scope.row.dictName;
  dictInfo.dictDesc = scope.row.dictDesc;
  dictInfo.dictSource = scope.row.dictSource+'';
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
  if(edit.value){
    dictInfo.disabled = false
  }else{
    dictInfo.disabled = true
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

    <!-- 页头 -->
    <header class="page-header">
      <div class="page-head-left">
        <span class="page-title-sm">{{ langData.dictPageTitle }}</span>
      </div>
      <span class="page-count">{{ data.total }} 条记录</span>
    </header>

    <!-- 查询条件区 -->
    <div class="query-panel" v-show="filtersVisible">
      <el-form :inline="true" :model="form">
        <el-form-item :label="langData.dictAppName" prop="app">
          <el-input v-model="form.app" :placeholder="langData.formInputPlaceholder" clearable @keyup.enter="queryDicts"/>
        </el-form-item>
        <el-form-item :label="langData.dictName" prop="dictName">
          <el-input v-model="form.dictName" :placeholder="langData.formInputPlaceholder" clearable @keyup.enter="queryDicts"/>
        </el-form-item>
        <el-form-item :label="langData.dictDesc" prop="dictDesc">
          <el-input v-model="form.dictDesc" :placeholder="langData.formInputPlaceholder" clearable @keyup.enter="queryDicts"/>
        </el-form-item>
        <el-form-item :label="langData.dictSource" prop="dictSource">
          <el-select v-model="form.dictSource">
            <el-option :label="langData.formSelectPlaceholder" value="-1"/>
            <el-option :label="langData.fixedSource" value="1"/>
            <el-option :label="langData.dbSource" value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="IcSearch" @click="queryDicts">{{langData.btnSearch}}</el-button>
          <el-button :icon="IcReset" @click="resetQuery">{{langData.btnReset}}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏（纯图标） -->
    <div class="list-toolbar">
      <div class="tb-left">
        <el-tooltip :content="langData.addDictTitle" :show-after="1000" placement="bottom">
          <el-button type="primary" circle :icon="IcPlus" @click="showAddDictDialog()"/>
        </el-tooltip>
        <span class="tb-sep"></span>
        <el-tooltip :content="filtersVisible ? langData.filterHide : langData.filterShow" :show-after="1000" placement="bottom">
          <el-button class="filter-toggle" :class="{'on': filtersVisible}" circle text :icon="IcFilter" @click="filtersVisible = !filtersVisible"/>
        </el-tooltip>
        <span class="tb-sep"></span>
        <el-tooltip :content="langData.reloadDictData" :show-after="1000" placement="bottom">
          <el-button circle text :icon="IcRefresh" @click="reloadDict"/>
        </el-tooltip>
      </div>
    </div>

    <!-- 摘要栏：只放筛选状态 -->
    <div class="sum-bar" v-if="!filtersVisible && filterCount() > 0">
      <span class="seg">
        <el-icon class="seg-ic"><IcFilter/></el-icon>{{ langData.filteredPrefix }} {{ filterCount() }} {{ langData.filteredSuffix }}
        <span class="lnk" @click="filtersVisible = true">{{ langData.filterExpand }}</span>
      </span>
    </div>

    <!-- 主数据表格 -->
    <div class="table-card">
      <el-table
        v-loading="loading"
        :data="data.dictList"
        style="width: 100%"
        table-layout="fixed"
        :stripe="false"
        :highlight-current-row="true"
        row-class-name="dict-row"
      >
        <template #empty>
          <el-empty :description="filterCount() > 0 ? langData.emptyNoMatch : langData.emptyNoData" :image-size="70"/>
        </template>
        <el-table-column fixed="left" :label="langData.tableHeaderOp" width="100" header-align="center" align="center">
          <template #default="scope">
            <div class="table-actions">
              <el-tooltip :content="scope.row.app==data.app ? langData.btnEdit : langData.btnDetail" :show-after="1000" placement="top">
                <el-icon class="action-icon action-edit" @click="showEditDictDialog(scope)">
                  <IcEdit v-if="scope.row.app==data.app"/>
                  <IcView v-else/>
                </el-icon>
              </el-tooltip>
              <el-tooltip v-if="scope.row.app==data.app" :content="langData.btnDelete" :show-after="1000" placement="top">
                <el-icon class="action-icon action-delete" @click="confirmDelDict(scope)"><IcDelete/></el-icon>
              </el-tooltip>
            </div>
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
          :background="true"
          :page-sizes="[10, 20, 50, 100]"
        />
      </div>
    </div>

    <!-- 编辑/新建字典抽屉 -->
    <el-drawer
      v-model="dialogFormVisible"
      direction="rtl"
      :size="drawerFull ? '100%' : '620px'"
      custom-class="dict-drawer"
    >
      <template #header>
        <div class="drawer-head">
          <span class="drawer-head__title">{{ edit ? (dictInfo.disabled ? langData.editProperty : langData.addDictTitle) : langData.btnDetail }}</span>
          <el-tooltip :content="drawerFull ? langData.drawerRestore : langData.drawerMaximize" :show-after="1000" placement="bottom">
            <el-button link :icon="drawerFull ? IcMinimize : IcMaximize" @click="drawerFull = !drawerFull"/>
          </el-tooltip>
        </div>
      </template>
      <el-form class="drawer-form-width" :model="dictInfo" :label-width="formLabelWidth" label-position="right">
        <div class="drawer-section">
          <div class="drawer-section__title">{{langData.baseInfo}}</div>
          <div class="drawer-section__body">
            <el-form-item :label="langData.dictName" style="width: 100%">
              <el-input v-model="dictInfo.dictName" type="text" :disabled="dictInfo.disabled"/>
            </el-form-item>
            <el-form-item :label="langData.dictDesc" style="width: 100%">
              <el-input v-model="dictInfo.dictDesc" type="text"/>
            </el-form-item>
            <el-form-item :label="langData.dictSource">
              <el-radio-group v-model="dictInfo.dictSource" :disabled="dictInfo.disabled">
                <el-radio-button label="1">{{ langData.fixedSource }}</el-radio-button>
                <el-radio-button label="2">{{ langData.dbSource }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="langData.keyWay">
              <el-radio-group v-model="dictInfo.keyColumn" :disabled="dictInfo.disabled">
                <el-radio-button label="key">Key</el-radio-button>
                <el-radio-button label="value">Value</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="langData.valueWay">
              <el-radio-group v-model="dictInfo.valColumn" :disabled="dictInfo.disabled">
                <el-radio-button label="key">Key</el-radio-button>
                <el-radio-button label="value">Value</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </div>
        </div>

        <div class="drawer-section">
          <div class="drawer-section__title">{{langData.enumInfo}}</div>
          <div class="drawer-section__body">
            <div v-if="dictInfo.dictSource=='1'" class="pairs-panel">
              <div class="pairs-panel__header">
                <span class="pairs-panel__count">{{ dictInfo.pairTotal }} 个枚举项</span>
                <el-tooltip :content="langData.btnAdd" :show-after="1000" placement="bottom" v-if="edit">
                  <el-button circle text :icon="IcPlus" @click="showPairDialog"/>
                </el-tooltip>
              </div>
              <el-table
                :data="dictInfo.pairs"
                style="width: 100%"
                :stripe="false"
                :highlight-current-row="true"
                row-class-name="pair-row"
              >
                <el-table-column fixed="left" :label="langData.tableHeaderOp" width="80" header-align="center" align="center">
                  <template #default="scope">
                    <div class="table-actions" v-if="edit">
                      <el-tooltip :content="langData.btnDelete" :show-after="1000" placement="top">
                        <el-icon class="action-icon action-delete" @click="confirmDelPair(scope)"><IcDelete/></el-icon>
                      </el-tooltip>
                    </div>
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
                  :background="true"
                  :page-sizes="[10,20,50,100]"
                />
              </div>
            </div>
            <div v-else-if="dictInfo.dictSource=='2'">
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
      <el-form :model="pairInfo" label-position="top">
        <el-form-item :label="langData.addWay">
          <el-select v-model="pairInfo.pairAddWay" :placeholder="langData.formSelectPlaceholder" style="width:100%">
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
  height: 100vh;
  max-height: 100vh;
  overflow: hidden;
  box-sizing: border-box;
  padding: 16px 20px;
}

/* ---- 页头（§7.6） ---- */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 0 0 12px;
}

.page-head-left {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.page-title-sm {
  font-size: var(--el-font-size-medium);
  font-weight: 600;
  font-family: inherit;
}

.page-count {
  font-size: var(--el-font-size-extra-small);
  color: var(--el-text-color-placeholder);
  white-space: nowrap;
}

/* ---- 查询条件区（§7.7） ---- */
.query-panel {
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 10px 10px 0 0;
  border-bottom: none;
  padding: 12px 16px 2px;
}

.query-panel :deep(.el-form--inline) {
  row-gap: 8px;
}

.query-panel :deep(.el-form-item__label) {
  font-weight: 500;
  font-size: var(--el-font-size-extra-small);
  color: var(--el-text-color-secondary);
}

.query-panel :deep(.el-input) {
  width: 180px;
}

.query-panel :deep(.el-select) {
  width: 150px;
}

/* ---- 工具栏（纯图标，§7.7） ---- */
.list-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 8px 16px;
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-top: none;
  border-bottom: none;
}

.tb-left {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tb-sep {
  width: 1px;
  height: 16px;
  background: var(--el-border-color);
  margin: 0 2px;
}

.filter-toggle.on {
  color: var(--el-color-primary) !important;
  background: var(--el-color-primary-light-9) !important;
}

/* ---- 摘要栏（§7.7） ---- */
.sum-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0 16px 8px;
  padding: 6px 10px;
  font-size: var(--el-font-size-extra-small);
  color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
  border: 1px solid var(--el-color-primary-light-8);
  border-radius: 6px;
}

.sum-bar .seg {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.sum-bar .seg-ic {
  font-size: 1em;
}

.sum-bar .lnk {
  cursor: pointer;
}

.sum-bar .lnk:hover {
  text-decoration: underline;
}

/* ---- 表格操作列（纯图标，§7.5） ---- */
.table-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.action-icon {
  font-size: 1em;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 1.9em;
  min-height: 1.9em;
  padding: 0;
  border-radius: 4px;
  color: var(--el-text-color-regular);
  cursor: pointer;
  transition: all 0.15s ease;
}

.action-icon:hover {
  color: var(--el-color-primary);
  background: var(--el-fill-color-light);
}

.action-delete:hover {
  color: var(--el-color-danger);
  background: var(--el-color-danger-light-9);
}

/* ---- 表格卡片 ---- */
.table-card {
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 0 0 10px 10px;
  overflow: hidden;
  flex: 1;
  min-height: 0;
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
  font-size: var(--el-font-size-extra-small);
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

/* ---- 抽屉头部：标题 + 最大化切换（§7.1） ---- */
.drawer-form-width {
  max-width: 760px;
}

.drawer-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  width: 100%;
}

.drawer-head__title {
  font-size: var(--el-font-size-medium);
  font-weight: 600;
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
  font-size: var(--el-font-size-extra-small);
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
  font-size: var(--el-font-size-extra-small);
  color: var(--el-text-color-secondary);
  font-family: 'Menlo', 'SF Mono', 'Cascadia Code', monospace;
}

.pairs-panel :deep(.pair-row:hover > td) {
  background: var(--el-fill-color-light) !important;
}

.pairs-panel :deep(.el-table th.el-table__cell) {
  background: transparent;
  font-weight: 600;
  font-size: var(--el-font-size-extra-small);
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
  font-size: var(--el-font-size-extra-small);
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
