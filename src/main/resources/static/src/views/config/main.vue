<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
          <el-menu-item index="0">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            配置中心
          </el-menu-item>
          <div class="flex-grow"/>
          <el-sub-menu index="create">
            <template #title>
              <el-icon>
                <Plus/>
              </el-icon>
              新建
            </template>
            <el-menu-item index="createFolder" @click="showCreateFolderDialog">
              <el-icon>
                <Folder/>
              </el-icon>
              目录
            </el-menu-item>
            <el-menu-item index="createProperty" @click="showCreatePropertyDialog">
              <el-icon>
                <Document/>
              </el-icon>
              属性
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="delete">
            <el-icon>
              <Delete/>
            </el-icon>
            <el-popconfirm title="你确定要删除选中目录下所有配置吗?" @confirm="deleteSelected" :icon="WarningFilled"
                           confirm-button-type="danger" cancel-button-type="info" icon-color="red">
              <template #reference>
                删除
              </template>
            </el-popconfirm>
          </el-menu-item>
          <el-menu-item index="export" @click="exportSelected">
            <el-icon>
              <Expand/>
            </el-icon>
            导出
          </el-menu-item>

          <el-sub-menu index="import">
            <template #title>
              <el-icon>
                <Upload/>
              </el-icon>
              导入
            </template>
            <el-menu-item index="txt" @click="showTxtImportDialog">
              <el-icon>
                <DocumentCopy/>
              </el-icon>
              .txt
            </el-menu-item>
            <el-menu-item index="prop" @click="showPropImportDialog">
              <el-icon>
                <DocumentCopy/>
              </el-icon>
              .prop
            </el-menu-item>
            <el-menu-item index="yaml" @click="showYamlImportDialog">
              <el-icon>
                <DocumentCopy/>
              </el-icon>
              .yaml
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="search" @click="searchDialogFormVisible = true">
            <el-icon>
              <Search/>
            </el-icon>
            查询
          </el-menu-item>
          <el-menu-item index="history" @click="historyDialogFormVisible = true">
            <el-icon>
              <Clock/>
            </el-icon>
            历史
          </el-menu-item>

          <el-menu-item index="highAvailability" @click="bkDialogFormVisible = true">
            <el-icon>
              <UploadFilled/>
            </el-icon>
            备份/恢复
          </el-menu-item>

          <el-menu-item index="refresh" @click="refreshDialogFormVisible = true">
            <el-icon>
              <Refresh/>
            </el-icon>
            配置刷新
          </el-menu-item>
          <el-menu-item index="logout" @click="logout">
            <el-icon>
              <SwitchButton/>
            </el-icon>
            注销
          </el-menu-item>


        </el-menu>
      </el-header>
      <el-container class="container">
        <el-aside class="aside">
          <el-scrollbar class="result-area" ref="asideScrollbar">
            <el-row class="tac">
              <el-col :span="24">
                <el-menu class="el-menu-vertical-demo">
                  <el-tree :props="props" :load="loadNode" lazy @node-click="clickNode" @node-collapse="nodeCollapse"
                           :expand-on-click-node="false" :render-after-expand="false" :highlight-current="true" class="tree"/>
                </el-menu>
              </el-col>
            </el-row>
          </el-scrollbar>
        </el-aside>
        <el-main class="main">
          <el-table :data="searchTableData" style="width: 100%" :border="false" table-layout="fixed" :stripe="true"
                    size="small" :highlight-current-row="true" class="leaf">
            <el-table-column fixed="left" width="120" header-align="center" align="center">
              <template #header>
                <el-input v-model="searchKey" size="small" placeholder="属性名关键字..."/>
              </template>
              <template #default="scope">
                <el-button :icon="Edit" circle size="small" title="编辑" @click="showEditDialog(scope)"/>
                <el-popconfirm title="你确定要删除本条配置?" @confirm="deleteConfig(scope)" :icon="WarningFilled"
                               confirm-button-type="danger" cancel-button-type="info" icon-color="red">
                  <template #reference>
                    <el-button :icon="Delete" circle size="small" title="删除"/>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="属性名称" :show-overflow-tooltip="true" header-align="center"
                             align="center"/>
            <el-table-column prop="strValue" label="属性值" :show-overflow-tooltip="true" header-align="center"
                             align="left"/>
          </el-table>
        </el-main>
      </el-container>
    </el-container>

    <el-dialog v-model="dialogFormVisible" title="编辑属性">
      <el-form :model="propForm">
        <el-form-item label="属性名：" :label-width="formLabelWidth">
          <el-input v-model="propForm.name" type="text" disabled/>
        </el-form-item>
        <el-form-item label="属性值：" :label-width="formLabelWidth">
          <el-input v-model="propForm.value" type="textarea" :rows="7"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="updateConfig">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="folderDialogFormVisible" title="创建目录 (节点收起/展开可查看)">
      <el-form :model="folderForm">
        <el-form-item label="目录名：" :label-width="formLabelWidth">
          <el-input v-model="folderForm.folderName" type="text"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="folderDialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="saveFolder">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="propertyDialogFormVisible" title="创建属性">
      <el-form :model="propertyForm">
        <el-form-item label="属性名：" :label-width="formLabelWidth">
          <el-input v-model="propertyForm.propertyName" type="text"/>
        </el-form-item>
        <el-form-item label="属性值：" :label-width="formLabelWidth">
          <el-input v-model="propertyForm.propertyValue" type="textarea" rows="7"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="propertyDialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="saveProperty">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="importDialogFormVisible" title="导入配置" @closed="closedImportDialog">
      <el-form :model="importForm">
        <el-upload class="upload-demo" :action="uploadAction" :data="{ cover: importForm.cover }" :drag="true" multiple
                   ref="importRef" :show-file-list="true" :limit="20" :auto-upload="false"
                   :on-success="(res: any) => { return uploadSuc(res); }">
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            拖拽文件至此 或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              尽量按照模块来导入配置，控制导入的文件大小
            </div>
          </template>
        </el-upload>
        <el-switch v-model="importForm.cover" inline-prompt active-text="覆盖" inactive-text="不覆盖"/>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importDialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="importFile">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="importPropDialogFormVisible" title="导入配置" width="70%" @closed="closedPropImportDialog">
      <el-form :model="importPropForm" :inline="true">

        <el-form-item>
          <el-upload class="avatar-uploader" ref="importBootstrapRef" action="#" :limit="1" :auto-upload="false"
                     :on-change="bootstrapBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择bootstrap.properties
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-upload class="avatar-uploader" file-listref="importPropDefaultRef" action="#" :limit="1"
                     :auto-upload="false" :on-change="propDefaultBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择application.properties
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-upload class="avatar-uploader" ref="importPropProfilesRef" action="#" :limit="1" :auto-upload="false"
                     :on-change="propProfilesBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择profile文件
            </el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <el-switch v-model="importPropForm.cover" inline-prompt active-text="覆盖" inactive-text="不覆盖"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importPropDialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="importPropFile">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="importYamlDialogFormVisible" title="导入配置" width="70%" @closed="closedYamlImportDialog">
      <el-form :model="importYamlForm" :inline="true">
        <el-form-item>
          <el-upload class="avatar-uploader" ref="importBootstrapRef" action="#" :limit="1" :auto-upload="false"
                     :on-change="yamlBootstrapBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择bootstrap.yml
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-upload class="avatar-uploader" ref="importPropDefaultRef" action="#" :limit="1" :auto-upload="false"
                     :on-change="yamlDefaultBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择application.yml
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-upload class="avatar-uploader" ref="importPropProfilesRef" action="#" :limit="1" :auto-upload="false"
                     :on-change="yamlProfilesBeforeAvatarUpload">
            <el-icon class="avatar-uploader-icon">
              <Plus/>
              选择profile文件
            </el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <el-switch v-model="importYamlForm.cover" inline-prompt active-text="覆盖" inactive-text="不覆盖"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importYamlDialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="importYamlFile">提交</el-button>
        </span>
      </template>
    </el-dialog>


    <el-dialog v-model="searchDialogFormVisible" title="查询配置" :fullscreen="false" width="90%">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="目录：" :label-width="formLabelWidth">
          <el-input v-model="searchForm.path" type="text"/>
        </el-form-item>
        <el-form-item label="属性名：" :label-width="formLabelWidth">
          <el-input v-model="searchForm.name" type="text"/>
        </el-form-item>
        <el-form-item label="属性值：" :label-width="formLabelWidth">
          <el-input v-model="searchForm.value" type="text"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchProperty">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="searchData" style="width: 100%;" height="400px" :border="true" table-layout="fixed" :stripe="true"
                size="small" :highlight-current-row="true">
        <el-table-column fixed="left" label="操作" width="120" header-align="center" align="center">
          <template #default="scope">
            <el-button :icon="Edit" circle size="small" title="编辑" @click="updateProperty(scope)"/>
            <el-popconfirm title="你确定要删除本条配置吗?" @confirm="deleteProperty(scope)" :icon="WarningFilled"
                           confirm-button-type="danger" cancel-button-type="info" icon-color="red">
              <template #reference>
                <el-button :icon="Delete" circle size="small" title="删除"/>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="目录" :show-overflow-tooltip="true" header-align="center"/>
        <el-table-column prop="name" label="属性名" :show-overflow-tooltip="true" header-align="center"/>
        <el-table-column prop="strValue" label="属性值" :show-overflow-tooltip="true" header-align="center"/>
      </el-table>
    </el-dialog>


    <el-dialog v-model="historyDialogFormVisible" title="历史操作记录" :fullscreen="false" width="90%">
      <el-form :model="historyForm" :inline="true" ref="historyFormRef" :rules="historyRules">
        <el-form-item label="时间：" :label-width="formLabelWidth" prop="time">
          <el-date-picker v-model="historyForm.time" type="daterange" unlink-panels range-separator="至"
                          start-placeholder="开始时间" end-placeholder="结束时间" size="default" format="YYYY-MM-DD"
                          value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="用户：" :label-width="formLabelWidth">
          <el-input v-model="historyForm.user" type="text"/>
        </el-form-item>
        <el-form-item label="操作：" :label-width="formLabelWidth">
          <el-select v-model="historyForm.operate" class="m-2" placeholder="请选择">
            <el-option
                v-for="item in [{ label: '请选择', value: '' }, { label: '更新配置', value: '更新配置' }, { label: '删除属性', value: '删除属性' }, { label: '创建目录', value: '创建目录' }, { label: '创建配置', value: '创建配置' }, { label: '批量删除配置', value: '批量删除配置' }, { label: '导入配置', value: '导入配置' }]"
                :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchHistory(historyFormRef)">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="historyData" style="width: 100%;" height="400px" :border="true" table-layout="fixed" :stripe="true"
                size="small" :highlight-current-row="true">
        <el-table-column prop="user" label="用户" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="operate" label="操作" :show-overflow-tooltip="true" header-align="center" align="center"/>
        <el-table-column prop="fmtOpTime" label="时间" :show-overflow-tooltip="true" header-align="center" align="center"/>
      </el-table>
      <el-pagination class="page" v-model:page-size="page.pageSize" v-model:current-page="page.pageNum"
                     layout="->, total, sizes, prev, pager, next, jumper" v-model:total="page.total"
                     @size-change="searchHistory(historyFormRef)" @current-change="searchHistory(historyFormRef)"
                     @prev-click="searchHistory(historyFormRef)" @next-click="searchHistory(historyFormRef)" :small="true"
                     :background="true" :page-sizes="[5, 10, 20, 50, 100]"/>
    </el-dialog>


    <el-dialog v-model="bkDialogFormVisible" title="备份记录" :fullscreen="false" width="90%">
      <el-form :model="haForm" :inline="true" ref="haFormRef" :rules="haRules">
        <el-form-item label="时间：" :label-width="formLabelWidth" prop="time">
          <el-date-picker v-model="haForm.time" type="daterange" unlink-panels range-separator="至"
                          start-placeholder="开始时间" end-placeholder="结束时间" size="default" format="YYYY-MM-DD"
                          value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchBackup(haFormRef)">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="backup">
            备份
            <el-icon class="el-icon--right">
              <Download/>
            </el-icon>
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="backupData" style="width: 100%;" height="400px" :border="true" table-layout="fixed" :stripe="true"
                size="small" :highlight-current-row="true">
        <el-table-column fixed="left" label="操作" width="120" header-align="center" align="center">
          <template #default="scope">
            <el-popconfirm title="是否使用本快照进行恢复?" @confirm="recovery(scope)" :icon="WarningFilled"
                           confirm-button-type="success" cancel-button-type="info" icon-color="green">
              <template #reference>
                <el-button :icon="Upload" size="small" circle title="恢复"/>
              </template>
            </el-popconfirm>
            <el-popconfirm title="是否确认删除此快照数据?" @confirm="deleteBackup(scope)" :icon="WarningFilled"
                           confirm-button-type="danger" cancel-button-type="info" icon-color="red">
              <template #reference>
                <el-button :icon="Delete" size="small" circle title="删除"/>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="快照名称" width="400" :show-overflow-tooltip="true" header-align="center"
                         align="center"/>
        <el-table-column prop="fmtBackupTime" label="备份时间" :show-overflow-tooltip="true" header-align="center"
                         align="center"/>
        <el-table-column prop="size" label="记录数" :show-overflow-tooltip="true" header-align="center" align="center"/>
      </el-table>
      <el-pagination class="page" v-model:page-size="bkPage.pageSize" v-model:current-page="bkPage.pageNum"
                     layout="->, total, sizes, prev, pager, next, jumper" v-model:total="bkPage.total"
                     @size-change="searchBackup(haFormRef)" @current-change="searchBackup(haFormRef)"
                     @prev-click="searchBackup(haFormRef)" @next-click="searchBackup(haFormRef)" :small="true" :background="true"
                     :page-sizes="[5, 10, 20, 50, 100]"/>
    </el-dialog>


    <el-dialog v-model="refreshDialogFormVisible" title="应用列表" :fullscreen="false" width="50%">
      <el-form :model="refreshForm" :inline="true">
        <el-form-item label="应用名称：" :label-width="formLabelWidth">
          <el-input v-model="refreshForm.name" type="text"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchApp">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="appData" style="width: 100%;" height="250px" :border="true" table-layout="fixed" :stripe="true"
                size="small" :highlight-current-row="true">
        <el-table-column fixed="left" label="操作" width="120" header-align="center" align="center">
          <template #default="scope">
            <el-popconfirm title="确定刷新此应用的配置, 可能需要等待一段时间?" @confirm="refreshAppConfig(scope)" :icon="WarningFilled"
                           confirm-button-type="danger" cancel-button-type="info" icon-color="red">
              <template #reference>
                <el-button :icon="Refresh" size="small" circle title="刷新"/>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="应用名称" :show-overflow-tooltip="true" header-align="center"/>
        <el-table-column prop="desc" label="应用描述" :show-overflow-tooltip="true" header-align="center"/>
        <el-table-column prop="instSize" label="实例数量(包括Down)" :show-overflow-tooltip="true" header-align="center"/>
      </el-table>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
  import type Node from 'element-plus/es/components/tree/src/model/node'
  import {ArrowRight} from '@element-plus/icons-vue'
  import {
    Document,
    DocumentCopy,
    Expand,
    Download,
    Upload,
    Search,
    Clock,
    Plus,
    Folder,
    Delete,
    WarningFilled,
    UploadFilled,
    HomeFilled,
    Refresh,
    Edit,
    SwitchButton
  } from '@element-plus/icons-vue'
  import {UploadInstance, UploadProps, UploadFile, FormInstance, FormRules, ElMessage} from 'element-plus'
  import {ref, reactive, computed} from 'vue'
  import router from '@/router/index'
  import axios from '@/network/index'
  import {msg, downFile} from '@/utils/Utils'

  const activeIndex = ref('1')
  // 顶部菜单
  const handleSelect = (key: string, keyPath: string[]) => {
  }

  // 加载树数据结构
  interface Tree {
    zkPath: string[]
    name: string
    leaf?: boolean
  }

  // 节点属性名指定
  const props = {
    label: 'name',
    children: 'zones',
    isLeaf: 'leaf',
  }

  // 右侧配置属性列表
  const searchKey = ref('')
  const data = reactive({
    leaf: <any>[],
    // 当前zk路径
    currentPathArray: <any>[]
  })

  const searchTableData = computed(() => {
    return data.leaf.filter((item: any) =>
        !searchKey.value || item.name.toLowerCase().includes(searchKey.value.toLowerCase()))
  })

  const joinPath = (array: Array<string>) => {
    return '/' + (array.length == 0 ? '' : array.join('/'))
  }

  // 点击树上节点
  const clickNode = (tree: Tree) => {
    // {zkPath, name, leaf?}

    const array = Array.from(tree.zkPath)
    array.push(tree.name)
    const currentLoadPath = joinPath(array)
    data.currentPathArray = array

    const url = '/queryNodes/v1.0'
    axios({
      url, method: 'post',
      data: {zkPath: currentLoadPath}
    }).then((res: any) => {
      if (res.data.code == 1) {
        data.leaf = res.data.body.leaf;
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const loadNode = (node: Node, resolve: (data: Tree[]) => void) => {
    let array: any = []
    if (node.data.name) {
      array = Array.from(node.data.zkPath)
      array.push(node.data.name)
      data.currentPathArray = array
    }
    const currentLoadPath = joinPath(data.currentPathArray)

    const url = '/queryNodes/v1.0'
    axios({
      url, method: 'post',
      data: {zkPath: currentLoadPath}
    }).then((res: any) => {
      if (res.data.code == 1) {
        resolve(res.data.body.tree.map((n: any): Tree => {
          return {zkPath: array, name: n.name, leaf: n.leaf};
        }))
        data.leaf = res.data.body.leaf;
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const nodeCollapse = (obj: any, node: Node, self: any) => {
    node.loaded = false
    node.childNodes = []
  }

  const dialogFormVisible = ref(false)
  const formLabelWidth = ref('140px')
  const propForm = reactive({
    zkPath: '',
    name: '',
    value: ''
  })
  let tscope: any;
  const showEditDialog = (scope: any) => {
    propForm.zkPath = joinPath(data.currentPathArray)
    propForm.name = scope.row.name
    propForm.value = scope.row.strValue
    dialogFormVisible.value = true
    tscope = scope
  }

  const updateConfig = () => {
    axios({
      url: '/updateProperty/v1.0',
      method: 'post',
      data: propForm
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('更新成功', 'success')
        dialogFormVisible.value = false
        tscope.row.name = propForm.name
        tscope.row.strValue = propForm.value
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const deleteConfig = (scope: any) => {
    console.log('', scope.row);
    const currentLoadPath = joinPath(data.currentPathArray)
    axios({
      url: '/deleteLeaves/v1.0',
      method: 'post',
      data: {
        zkPath: currentLoadPath,
        leafNames: [scope.row.name]
      }
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('删除成功', 'success')
        data.leaf = data.leaf.filter((item: any) => item.name != scope.row.name)
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const folderDialogFormVisible = ref(false)
  const folderForm = reactive({
    zkPath: '',
    folderName: ''
  })
  const showCreateFolderDialog = () => {
    folderDialogFormVisible.value = true;
    folderForm.folderName = ''
  }
  const saveFolder = () => {
    folderForm.zkPath = joinPath(data.currentPathArray)
    console.log('创建目录:', folderForm)
    axios({
      url: '/saveFolder/v1.0',
      method: 'post',
      data: folderForm
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('创建成功', 'success')
        folderDialogFormVisible.value = false

      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const propertyDialogFormVisible = ref(false)
  const propertyForm = reactive({
    zkPath: '',
    propertyName: '',
    propertyValue: ''
  })
  const showCreatePropertyDialog = () => {
    propertyDialogFormVisible.value = true;
    propertyForm.propertyName = '';
    propertyForm.propertyValue = ''
  }
  const saveProperty = () => {
    propertyForm.zkPath = joinPath(data.currentPathArray)
    console.log('创建属性:', propertyForm)
    axios({
      url: '/saveProperty/v1.0',
      method: 'post',
      data: propertyForm
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('创建成功', 'success')
        propertyDialogFormVisible.value = false
        data.leaf.push({
          name: propertyForm.propertyName,
          strValue: propertyForm.propertyValue
        })
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const deleteSelected = () => {
    const currentLoadPath = joinPath(data.currentPathArray)
    const leafNames: any = []
    data.leaf.forEach((item: any) => leafNames.push(item.name))
    axios({
      url: '/deleteLeaves/v1.0',
      method: 'post',
      data: {
        zkPath: currentLoadPath,
        leafNames: leafNames
      }
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('删除成功', 'success')
        data.leaf = []
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const exportSelected = () => {
    const len = data.currentPathArray.length
    const folderName = (len == 0 ? 'all' : data.currentPathArray[len - 1])
    downFile({
      url: '/export/v1.0',
      method: 'post',
      data: {zkPath: joinPath(data.currentPathArray)},
      responseType: 'blob',
      folderName,
      fileExtension: 'txt'
    })
  }

  const getUploadActionUrl = (url: string) => {
    return ('development' == process.env.NODE_ENV
        ? process.env.VUE_APP_DEV_BASE_URL
        : process.env.VUE_APP_PROD_BASE_URL)
        + url
  }
  const importDialogFormVisible = ref(false)
  const uploadAction = ref(getUploadActionUrl('/import/v1.0'))
  const importRef = ref<UploadInstance>()
  const importForm = reactive({
    cover: true
  })
  const showTxtImportDialog = () => {
    importDialogFormVisible.value = true;
    importForm.cover = true;
  }
  const closedImportDialog = () => {
    importRef.value.clearFiles(['success', 'fail']);
  }
  const importFile = () => {
    importRef.value!.submit()
  }
  const uploadSuc = (res: any) => {
    msg('上传成功', 'success')
  }
  const importPropDialogFormVisible = ref(false)
  const uploadPropAction = getUploadActionUrl('/propImport/v1.0')
  const importPropDefaultRef = ref<UploadInstance>()
  const importPropProfilesRef = ref<UploadInstance>()
  const importBootstrapRef = ref<UploadInstance>()
  const importPropForm = reactive({
    cover: true
  })
  const showPropImportDialog = () => {
    importPropDialogFormVisible.value = true
    importPropForm.cover = true
  }
  const closedPropImportDialog = () => {
    importPropDefaultRef.value.clearFiles(['success', 'fail', 'ready']);
    importPropProfilesRef.value.clearFiles(['success', 'fail', 'ready']);
    importBootstrapRef.value.clearFiles(['success', 'fail', 'ready'])
  }
  let bootstrapFile: any
  let defaultPropFile: any
  let profilesPropFile: any
  const bootstrapBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查bootstrap文件:', uploadFile.raw)
    if (uploadFile.name !== 'bootstrap.properties') {
      ElMessage.error('bootstrap文件格式不对!')
      return false
    }
    bootstrapFile = uploadFile.raw
    return true
  }
  const propDefaultBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查默认prop文件:', uploadFile.raw)
    if (uploadFile.name !== 'application.properties') {
      ElMessage.error('默认文件格式不对!')
      return false
    }
    defaultPropFile = uploadFile.raw
    return true
  }
  const propProfilesBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查profiles的prop文件:', uploadFile.raw)
    const array = uploadFile.name.split('.')
    if (array[1] !== 'properties') {
      ElMessage.error('profiles文件格式不对!')
      return false
    }
    profilesPropFile = uploadFile.raw
    return true
  }
  const importPropFile = () => {
    const formData = new FormData();
    formData.append("bootstrapFile", bootstrapFile)
    formData.append("defaultFile", defaultPropFile)
    formData.append("profilesFile", profilesPropFile)
    formData.append("cover", importPropForm.cover + '')
    axios({
      url: uploadPropAction,
      method: 'post',
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('导入成功', 'success')
      } else {
        msg('导入失败', 'error')
      }
    })
  }
  const propUploadSuc = (res: any) => {
    msg('上传成功', 'success')
  }
  const importYamlDialogFormVisible = ref(false)
  const uploadYamlAction = getUploadActionUrl('/yamlImport/v1.0')
  const importYamlForm = reactive({
    cover: true
  })
  const showYamlImportDialog = () => {
    importYamlDialogFormVisible.value = true
    importYamlForm.cover = true
  }
  const closedYamlImportDialog = () => {
    closedPropImportDialog()
  }
  const yamlBootstrapBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查bootstrap文件:', uploadFile.raw)
    if (uploadFile.name !== 'bootstrap.yml') {
      ElMessage.error('bootstrap文件格式不对!')
      return false
    }
    bootstrapFile = uploadFile.raw
    return true
  }
  const yamlDefaultBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查默认yml文件:', uploadFile.raw)
    if (uploadFile.name !== 'application.yml') {
      ElMessage.error('默认文件格式不对!')
      return false
    }
    defaultPropFile = uploadFile.raw
    return true
  }
  const yamlProfilesBeforeAvatarUpload = (uploadFile: UploadFile) => {
    console.log('提交前检查profiles的yaml文件:', uploadFile.raw)
    const array = uploadFile.name.split('.')
    if (array[1] !== 'yml') {
      ElMessage.error('profiles文件格式不对!')
      return false
    }
    profilesPropFile = uploadFile.raw
    return true
  }
  const importYamlFile = () => {
    const formData = new FormData();
    formData.append("bootstrapFile", bootstrapFile)
    formData.append("defaultFile", defaultPropFile)
    formData.append("profilesFile", profilesPropFile)
    formData.append("cover", importYamlForm.cover + '')
    console.log('导入yml文件提交参数:', formData)
    axios({
      url: uploadYamlAction,
      method: 'post',
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('导入成功', 'success')
      } else {
        msg('导入失败', 'error')
      }
    })
  }
  const yamlUploadSuc = (res: any) => {
    msg('上传成功', 'success')
  }

  interface PropertyInfo {
    path: string
    name: string
    value: string
    strValue: string
  }

  const searchDialogFormVisible = ref(false)
  const searchData = ref<PropertyInfo[]>()
  const searchForm = reactive({
    path: '',
    name: '',
    value: '',
  })
  const searchProperty = () => {
    console.log('点击树节点查询配置列表:', searchForm)
    axios({
      url: '/queryAllProperty/v1.0',
      method: 'post',
      data: searchForm
    }).then((res: any) => {
      if (res.data.code == 1) {
        searchData.value = res.data.body
      }
    }).catch((err: Error) => {
      msg('请求异常', 'error')
    })
  }
  const updateProperty = (scope: any) => {
    console.log('更新配置: %o', scope.row)
    propForm.zkPath = scope.row.path
    propForm.name = scope.row.name
    propForm.value = scope.row.strValue
    dialogFormVisible.value = true
  }
  const deleteProperty = (scope: any) => {
    console.log('删除配置: %o', scope)
    axios({
      url: '/deleteLeaves/v1.0',
      method: 'post',
      data: {
        zkPath: scope.row.path,
        leafNames: [scope.row.name]
      }
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('删除成功', 'success')
        searchProperty()
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  interface HistoryRecord {
    user: string
    operate: string
    opTime: number
    fmtOpTime: string
  }

  const historyDialogFormVisible = ref(false)
  const historyData = ref<HistoryRecord>()
  const historyFormRef = ref<FormInstance>()
  const historyRules = reactive<FormRules>({
    // time: [{required: true, message: '请选择时间', trigger: 'blur'}]
  })
  const page = reactive({
    pageNum: 1,
    pageSize: 10,
    total: 0
  })
  const historyForm = reactive({
    time: [],
    user: '',
    operate: ''
  })
  const searchHistory = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
      if (valid) {
        axios({
          url: '/queryHistoryOperates/v1.0',
          method: 'post',
          params: page,
          data: historyForm
        }).then((res: any) => {
          if (res.data.code == 1) {
            historyData.value = res.data.body.list
            page.total = res.data.body.total
          } else {
            msg('查询失败', 'error')
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }


  interface Backup {
    id: string
    backTime: number
    fmtBackTime: string
    size: number
  }

  const bkDialogFormVisible = ref(false)
  const bkPage = reactive({
    pageNum: 1,
    pageSize: 10,
    total: 0
  })
  const haForm = reactive({
    time: []
  })
  const backupData = ref<Backup>()
  const haFormRef = ref<FormInstance>()
  const haRules = reactive<FormRules>({
    // time: [{required: true, message: '请选择时间', trigger: 'blur'}]
  })
  const searchBackup = async (formEl: FormInstance | undefined) => {
    console.log('查询备份记录参数: ', haForm)
    if (!formEl) return
    await formEl.validate((valid, fields) => {
      if (valid) {
        axios({
          url: '/queryBackups/v1.0',
          method: 'post',
          params: bkPage,
          data: haForm
        }).then((res: any) => {
          if (res.data.code == 1) {
            backupData.value = res.data.body.list
            bkPage.total = res.data.body.total
          } else {
            msg('查询失败', 'error')
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }
  const backup = () => {
    axios({
      url: '/backup/v1.0',
      method: 'post'
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('备份成功', 'success')
        searchBackup(haFormRef.value)
      } else {
        msg('备份失败', 'error')
      }
    })
  }
  const recovery = (scope: any) => {
    axios({
      url: '/recovery/v1.0',
      method: 'post',
      data: {id: scope.row.id}
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('恢复成功', 'success')
      } else {
        msg('恢复失败', 'error')
      }
    })
  }

  const deleteBackup = (scope: any) => {
    axios({
      url: '/deleteBackup/v1.0',
      method: 'post',
      data: {id: scope.row.id}
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('删除成功', 'success')
        searchBackup(haFormRef.value)
      } else {
        msg('删除失败', 'error')
      }
    })
  }

  interface MicroService {
    name: string
    desc: string;
    instSize: number
  }

  const refreshDialogFormVisible = ref(false)
  const refreshForm = reactive({
    name: '',
  })
  const appData = ref<MicroService[]>()
  const searchApp = () => {
    axios({
      url: '/queryAppInfos/v1.0',
      method: 'post',
      data: refreshForm
    }).then((res: any) => {
      if (res.data.code == 1) {
        appData.value = res.data.body
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }
  const refreshAppConfig = (scope: any) => {
    axios({
      url: '/refreshConfig/v1.0',
      method: 'post',
      data: {name: scope.row.name}
    }).then((res: any) => {
      if (res.data.code == 1) {
        msg('刷新成功', 'success')
      } else {
        msg(res.data.msg, 'error')
      }
    })
  }

  const logout= ()=>{
    window.location.href='/logout'
  }
</script>

<style scoped>
  .flex-grow {
    flex-grow: 1;
  }

  .el-header {
    --el-header-padding: 0;
    background-color: transparent;
    display: block;
    justify-content: space-between;
    padding-left: 0;
    align-items: center;
    color: #fff;
    font-size: 20px;
  }

  .container {
    height: 500px;
    overflow: hidden;
  }

  .main {
    padding: 0px 0 0px 0px;
    height: 500px;
    overflow: hidden;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    /* margin-top: 10px; */
    width: 100%;
    min-height: 500px;
    overflow: hidden;
  }

  .aside {
    width: 500px;
    height: 500px;
    margin-top: 0px;
    overflow: hidden;
  }

  .result-area {
    height: 499px;
    padding: 0px;
    border: 1px solid var(--el-border-color);
    border-radius: 4px;
    margin-top: 0px;
    overflow-y: auto;
  }

  .tree {
    padding: 0;
    overflow-y: hidden;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 100%;
    min-height: 499px;
    overflow: hidden;
  }

  .el-main {
    --el-main-padding: 0px 0px 0px 0px;
    overflow: hidden;
  }

  .leaf {
    height: 500px;
  }

  .avatar-uploader .avatar {
    width: 250px;
    height: 50px;
    display: block;
  }
</style>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }

  .el-icon.avatar-uploader-icon {
    font-size: 14px;
    color: #8c939d;
    width: 250px;
    height: 50px;
    text-align: center;
  }
</style>
