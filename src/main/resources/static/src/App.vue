<template>
  <div class="common-layout">
    <router-view/>
  </div>
</template>

<script lang="ts" setup>
  import {ref} from 'vue'

  const activeIndex = ref('/route')
  const userInfo = ref('当前登录用户: admin')

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
  width: 200px;
  min-height: 600px;
}

.el-header {
  --el-header-padding: 0 5px;
}

.home_container {
  height: 50em;
}

.el-header {
  background-color: #364040;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    margin-right: 10px;
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

<style>
/* ========================================
   全局基础
   ======================================== */
* { margin: 0; padding: 0; box-sizing: border-box; }

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  background: #F1F5F9;
  color: #0F172A;
  -webkit-font-smoothing: antialiased;
}

/* ========================================
   全局细滚动条 — 1px
   ======================================== */
* {
  scrollbar-width: none;
}
*::-webkit-scrollbar { width: 1px; height: 1px; }
*::-webkit-scrollbar-track { background: transparent; }
*::-webkit-scrollbar-thumb { background: rgba(0,0,0,0.80); }
*::-webkit-scrollbar-corner { background: transparent; }

/* ========================================
   Element Plus 主题变量（浅色）
   ======================================== */
:root {
  --el-color-primary: #4F46E5;
  --el-color-primary-light-3: #818CF8;
  --el-color-primary-light-5: #A5B4FC;
  --el-color-primary-light-7: #C7D2FE;
  --el-color-primary-light-8: #DDD6FE;
  --el-color-primary-light-9: #EEF2FF;
  --el-border-color: #E2E8F0;
  --el-border-color-light: #E2E8F0;
  --el-border-color-lighter: #F1F5F9;
  --el-bg-color: #FFF;
  --el-bg-color-page: #F1F5F9;
  --el-border-radius-base: 8px;
  --el-color-success: #059669;
  --el-color-danger: #DC2626;
  --el-color-warning: #D97706;
}

/* ========================================
   暗黑模式 — 全局覆盖
   ======================================== */
html.dark body {
  background: #0a0a0a;
  color: #e5e7eb;
}

html.dark *::-webkit-scrollbar-thumb {
  background: rgba(255,255,255,0.30);
}

html.dark {
  --el-color-primary: #A5B4FC;
  --el-color-primary-light-3: #818CF8;
  --el-color-primary-light-5: #6366F1;
  --el-color-primary-light-7: #4F46E5;
  --el-color-primary-light-8: #4338CA;
  --el-color-primary-light-9: #3730A3;
  --el-color-white: #e5e7eb;
  --el-color-black: #0a0a0a;
  --el-color-info: #818CF8;
  --el-border-color: #363738;
  --el-border-color-light: #4a4b4d;
  --el-border-color-lighter: #2a2b2d;
  --el-bg-color: #1d1e1f;
  --el-bg-color-page: #0a0a0a;
  --el-text-color-primary: #e5e7eb;
  --el-text-color-regular: #c8c9cc;
  --el-text-color-secondary: #98999c;
  --el-text-color-placeholder: #78797c;
  --el-fill-color-blank: #1d1e1f;
  --el-fill-color: #222324;
  --el-fill-color-light: #2a2b2d;
  --el-fill-color-lighter: #363738;
  --el-color-success: #34D399;
  --el-color-danger: #F87171;
  --el-color-warning: #FBBF24;
}

/* ========================================
   主题切换动画 — 圆心扩散/收缩
   ======================================== */
::view-transition-old(root),
::view-transition-new(root) {
  animation: none;
  mix-blend-mode: normal;
}

/* 浅→暗: old 向内收缩 */
html.tx-shrink::view-transition-old(root) {
  z-index: 9999;
  animation: theme-clip 0.8s cubic-bezier(0.4, 0, 0.2, 1) reverse;
}
html.tx-shrink::view-transition-new(root) {
  z-index: 1;
}

/* 暗→浅: new 向外扩散 */
html.tx-expand::view-transition-old(root) {
  z-index: 1;
}
html.tx-expand::view-transition-new(root) {
  z-index: 9999;
  animation: theme-clip 0.8s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes theme-clip {
  from { clip-path: circle(0px  at var(--tx-x, 50vw) var(--tx-y, 50vh)); }
  to   { clip-path: circle(150vw at var(--tx-x, 50vw) var(--tx-y, 50vh)); }
}
</style>
