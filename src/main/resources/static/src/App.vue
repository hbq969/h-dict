<template>
  <div class="common-layout">
    <!--    <el-container>-->
    <!--      <el-header height="30px">-->
    <!--        <span>HMIS微服务管理平台</span>-->
    <!--        <div>-->
    <!--          <el-popover-->
    <!--              ref="popover"-->
    <!--              placement="bottom"-->
    <!--              :title="userInfo"-->
    <!--              :width="200"-->
    <!--              trigger="hover"-->
    <!--          >-->
    <!--            <template #reference>-->
    <!--              <el-icon style="cursor: pointer">-->
    <!--                <More/>-->
    <!--              </el-icon>-->
    <!--            </template>-->
    <!--            <div style="display: flex">-->
    <!--              <el-button type="info" :icon="SwitchButton" circle title="注销"/>-->
    <!--            </div>-->


    <!--          </el-popover>-->
    <!--        </div>-->
    <!--      </el-header>-->
    <!--      <el-container>-->
    <!--        <el-aside width="200px">-->
    <!--          <el-scrollbar>-->
    <!--            <el-menu-->
    <!--                :default-active="activeIndex"-->
    <!--                class="el-menu-vertical-demo"-->
    <!--                active-text-color="#FE9200"-->
    <!--                @select="handleSelect"-->
    <!--                :router="true"-->
    <!--            >-->
    <!--              <el-sub-menu index="/route">-->
    <!--                <template #title>-->
    <!--                  <el-icon>-->
    <!--                    <Guide/>-->
    <!--                  </el-icon>-->
    <!--                  路由管理-->
    <!--                </template>-->
    <!--                <el-menu-item index="/route/list">路由配置</el-menu-item>-->
    <!--              </el-sub-menu>-->
    <!--              <el-sub-menu index="/dict">-->
    <!--                <template #title>-->
    <!--                  <el-icon>-->
    <!--                    <Grid/>-->
    <!--                  </el-icon>-->
    <!--                  字典管理-->
    <!--                </template>-->
    <!--                <el-menu-item index="/dict/list">字典配置</el-menu-item>-->
    <!--              </el-sub-menu>-->
    <!--              <el-sub-menu index="/agent">-->
    <!--                <template #title>-->
    <!--                  <el-icon>-->
    <!--                    <PieChart/>-->
    <!--                  </el-icon>-->
    <!--                  指标监控-->
    <!--                </template>-->
    <!--                <el-menu-item index="/agent/kafkaIn/appList">流控管理</el-menu-item>-->
    <!--                <el-menu-item index="/monitor/quotaList">指标列表</el-menu-item>-->
    <!--              </el-sub-menu>-->
    <!--              <el-sub-menu index="/config">-->
    <!--                <template #title>-->
    <!--                  <el-icon>-->
    <!--                    <PieChart/>-->
    <!--                  </el-icon>-->
    <!--                  配置中心-->
    <!--                </template>-->
    <!--                <el-menu-item index="/config/home">UI控制台</el-menu-item>-->
    <!--              </el-sub-menu>-->
    <!--              <el-sub-menu index="/open-ai">-->
    <!--                <template #title>-->
    <!--                  <el-icon><ChatDotRound /></el-icon>-->
    <!--                  OpenAI-->
    <!--                </template>-->
    <!--                <el-menu-item index="/open-ai/chatgpt">Chat-GPT</el-menu-item>-->
    <!--              </el-sub-menu>-->
    <!--            </el-menu>-->
    <!--          </el-scrollbar>-->
    <!--        </el-aside>-->
    <!--        <el-main>-->
    <!--          <el-page-header @back="goBack">-->
    <!--            <template #content>-->
    <!--              <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">-->
    <!--                <el-breadcrumb-item v-for="(item,index) in router.currentRoute.value.meta.breadcrumb">{{item}}</el-breadcrumb-item>-->
    <!--              </el-breadcrumb>-->
    <!--            </template>-->
    <!--          </el-page-header>-->
    <!--          <div class="frame">-->
    <!--            <router-view/>-->
    <!--          </div>-->
    <!--        </el-main>-->
    <!--      </el-container>-->
    <!--    </el-container>-->
    <router-view/>
  </div>
</template>

<script lang="ts" setup>
  import {ref} from 'vue'

  const activeIndex = ref('/route')
  const userInfo = ref('当前登录用户: admin')

  // const breadcrumb = reactive<any>(router.currentRoute.value.meta.breadcrumb)

  const handleSelect = (key: string, keyPath: string[]) => {

  }

  const goBack = () => {
    window.history.back()
  }
</script>

<style lang="scss">
  .frame {
    padding: 5px 5px 10px 5px;
    margin-top: 20px;
  }

  .breadcrumb {
    margin-left: 5px;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    /*margin-top: 10px;*/
    width: 200px;
    min-height: 600px;
  }

  .el-header {
    --el-header-padding: 0 5px;
    /*--el-header-height: 60px;*/
    /*padding: var(--el-header-padding);*/
    /*box-sizing: border-box;*/
    /*flex-shrink: 0;*/
    /*height: var(--el-header-height);*/
  }

  .home_container {
    height: 50em;
  }

  .el-header {
    background-color: #364040;
    // 给头部设置一下弹性布局
    display: flex;
    // 让它贴标左右对齐
    justify-content: space-between;
    // 清空图片左侧padding
    padding-left: 0;
    // 按钮居中
    align-items: center;
    // 文本颜色
    color: #fff;
    // 设置文本字体大小
    font-size: 20px;
    // 嵌套
    > div {
      // 弹性布局
      display: flex;
      // 纵向上居中对齐
      align-items: center;
      margin-right: 10px;
      // 给文本和图片添加间距，使用类选择器
      span {
        margin-left: 15px;
      }
    }

  }

  .el-aside {
    margin-top: 15px;
  }

  .el-main {
    background-color: #f6f8fa;
    --el-main-padding: 10px;
  }
</style>
