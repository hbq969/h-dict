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
  font-size: var(--el-font-size-extra-large);
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

/* 图标规范 §7.5：行内图标 0.9em；圆形按钮被门户字号主题压方，这里强制正圆 */
.el-button .el-icon { font-size: 0.9em; }
.el-button.is-circle,
.el-button.is-circle:hover,
.el-button.is-circle:focus { border-radius: 50% !important; }
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

/* ===== 与门户 h-sm 完全一致的主题变量（浅色+暗黑，规范 §7.1） ===== */
html:root {
  /* 品牌色 */
  --el-color-primary: #1A6AFF;
  --el-color-primary-rgb: 26, 106, 255;
  --el-color-primary-light-3: #5F97FF;
  --el-color-primary-light-5: #8DB5FF;
  --el-color-primary-light-7: #BAD2FF;
  --el-color-primary-light-8: #D1E1FF;
  --el-color-primary-light-9: #E8F0FF;
  --el-color-primary-dark-2: #1555CC;
  /* 中性色 */
  --el-color-white: #FFFFFF;
  --el-color-black: #000000;
  /* 语义色 */
  --el-color-success: #059669;
  --el-color-success-light-3: #37B083;
  --el-color-success-light-5: #66C9A6;
  --el-color-success-light-7: #99DFC9;
  --el-color-success-light-8: #B2E9D8;
  --el-color-success-light-9: #CCF2E7;
  --el-color-success-dark-2: #047854;
  --el-color-warning: #D97706;
  --el-color-warning-light-3: #E39637;
  --el-color-warning-light-5: #EBB566;
  --el-color-warning-light-7: #F3D399;
  --el-color-warning-light-8: #F6E3B2;
  --el-color-warning-light-9: #F9EFCC;
  --el-color-warning-dark-2: #B86105;
  --el-color-danger: #DC2626;
  --el-color-danger-light-3: #E55353;
  --el-color-danger-light-5: #EC8080;
  --el-color-danger-light-7: #F3ADAD;
  --el-color-danger-light-8: #F6C5C5;
  --el-color-danger-light-9: #F9DEDE;
  --el-color-danger-dark-2: #B01E1E;
  --el-color-error: #DC2626;
  --el-color-error-light-3: #E55353;
  --el-color-error-light-5: #EC8080;
  --el-color-error-light-7: #F3ADAD;
  --el-color-error-light-8: #F6C5C5;
  --el-color-error-light-9: #F9DEDE;
  --el-color-error-dark-2: #B01E1E;
  --el-color-info: #909399;
  --el-color-info-light-3: #A6A9AD;
  --el-color-info-light-5: #BCBEC2;
  --el-color-info-light-7: #D3D4D6;
  --el-color-info-light-8: #DEDFE1;
  --el-color-info-light-9: #E9E9EB;
  --el-color-info-dark-2: #6B6E72;
  /* 文本色 */
  --el-text-color-primary: #0F172A;
  --el-text-color-regular: #334155;
  --el-text-color-secondary: #475569;
  --el-text-color-placeholder: #64748B;
  --el-text-color-disabled: #CBD5E1;
  /* 背景色 */
  --el-bg-color: #FFFFFF;
  --el-bg-color-page: #F1F5F9;
  --el-bg-color-overlay: #FFFFFF;
  /* 边框色 */
  --el-border-color: #CBD5E1;
  --el-border-color-light: #E2E8F0;
  --el-border-color-lighter: #F1F5F9;
  --el-border-color-extra-light: #F8FAFC;
  --el-border-color-dark: #94A3B8;
  --el-border-color-darker: #64748B;
  /* 填充色 */
  --el-fill-color: #F1F5F9;
  --el-fill-color-light: #F8FAFC;
  --el-fill-color-lighter: #FAFAFA;
  --el-fill-color-extra-light: #FAFCFF;
  --el-fill-color-dark: #E2E8F0;
  --el-fill-color-darker: #CBD5E1;
  --el-fill-color-blank: #FFFFFF;
  /* 阴影 */
  --el-box-shadow: 0 12px 32px 4px rgba(0,0,0,0.04), 0 8px 20px rgba(0,0,0,0.08);
  --el-box-shadow-light: 0 0 12px rgba(0,0,0,0.12);
  --el-box-shadow-lighter: 0 0 6px rgba(0,0,0,0.12);
  --el-box-shadow-dark: 0 16px 48px 16px rgba(0,0,0,0.08), 0 12px 32px rgba(0,0,0,0.12), 0 8px 16px -8px rgba(0,0,0,0.16);
  /* 圆角 */
  --el-border-radius-base: 8px;
  /* 禁用态 */
  --el-disabled-bg-color: var(--el-fill-color-light);
  --el-disabled-text-color: var(--el-text-color-disabled);
  --el-disabled-border-color: var(--el-border-color-lighter);
  /* 遮罩 */
  --el-overlay-color: rgba(0, 0, 0, 0.8);
  --el-overlay-color-light: rgba(0, 0, 0, 0.7);
  --el-overlay-color-lighter: rgba(0, 0, 0, 0.5);
  --el-mask-color: rgba(255, 255, 255, 0.9);
  --el-mask-color-extra-light: rgba(255, 255, 255, 0.3);
}
html.dark {
  /* 品牌色 */
  --el-color-primary: #8DB5FF;
  --el-color-primary-rgb: 141, 181, 255;
  --el-color-primary-light-3: #5F97FF;
  --el-color-primary-light-5: #4888FF;
  --el-color-primary-light-7: #1A6AFF;
  --el-color-primary-light-8: #1555CC;
  --el-color-primary-light-9: #104099;
  --el-color-primary-dark-2: #BAD2FF;
  /* 中性色 */
  --el-color-white: #e5e7eb;
  --el-color-black: #0a0a0a;
  /* 语义色 */
  --el-color-success: #34D399;
  --el-color-success-light-3: #5DDFB3;
  --el-color-success-light-5: #86E8CC;
  --el-color-success-light-7: #B0F0E0;
  --el-color-success-light-8: #C8F5EA;
  --el-color-success-light-9: #DFF9F3;
  --el-color-success-dark-2: #2AA978;
  --el-color-warning: #FBBF24;
  --el-color-warning-light-3: #FCCF5C;
  --el-color-warning-light-5: #FDDC8A;
  --el-color-warning-light-7: #FEE9B3;
  --el-color-warning-light-8: #FEF0CC;
  --el-color-warning-light-9: #FFF6E5;
  --el-color-warning-dark-2: #D49A1D;
  --el-color-danger: #F87171;
  --el-color-danger-light-3: #FA9595;
  --el-color-danger-light-5: #FBB8B8;
  --el-color-danger-light-7: #FDD6D6;
  --el-color-danger-light-8: #FDE4E4;
  --el-color-danger-light-9: #FEF0F0;
  --el-color-danger-dark-2: #D65A5A;
  --el-color-error: #F87171;
  --el-color-error-light-3: #FA9595;
  --el-color-error-light-5: #FBB8B8;
  --el-color-error-light-7: #FDD6D6;
  --el-color-error-light-8: #FDE4E4;
  --el-color-error-light-9: #FEF0F0;
  --el-color-error-dark-2: #D65A5A;
  --el-color-info: #A6A9AD;
  --el-color-info-light-3: #B8BABD;
  --el-color-info-light-5: #C9CBCE;
  --el-color-info-light-7: #DBDCDE;
  --el-color-info-light-8: #E4E4E5;
  --el-color-info-light-9: #ECEDED;
  --el-color-info-dark-2: #7E8286;
  /* 文本色 */
  --el-text-color-primary: #e5e7eb;
  --el-text-color-regular: #c8c9cc;
  --el-text-color-secondary: #98999c;
  --el-text-color-placeholder: #78797c;
  --el-text-color-disabled: #4a4b4d;
  /* 背景色 */
  --el-bg-color: #1d1e1f;
  --el-bg-color-page: #0a0a0a;
  --el-bg-color-overlay: #1d1e1f;
  /* 边框色 */
  --el-border-color: #363738;
  --el-border-color-light: #4a4b4d;
  --el-border-color-lighter: #2a2b2d;
  --el-border-color-extra-light: #2b2b2c;
  --el-border-color-dark: #58585b;
  --el-border-color-darker: #636466;
  /* 填充色 */
  --el-fill-color: #222324;
  --el-fill-color-light: #2a2b2d;
  --el-fill-color-lighter: #363738;
  --el-fill-color-extra-light: #2a2b2d;
  --el-fill-color-dark: #39393a;
  --el-fill-color-darker: #424243;
  --el-fill-color-blank: #1d1e1f;
  /* 阴影 */
  --el-box-shadow: 0 12px 32px 4px rgba(0,0,0,0.36), 0 8px 20px rgba(0,0,0,0.48);
  --el-box-shadow-light: 0 0 12px rgba(0,0,0,0.48);
  --el-box-shadow-lighter: 0 0 6px rgba(0,0,0,0.48);
  --el-box-shadow-dark: 0 16px 48px 16px rgba(0,0,0,0.48), 0 12px 32px rgba(0,0,0,0.6), 0 8px 16px -8px rgba(0,0,0,0.72);
  /* 禁用态 */
  --el-disabled-bg-color: var(--el-fill-color-light);
  --el-disabled-text-color: var(--el-text-color-disabled);
  --el-disabled-border-color: var(--el-border-color-lighter);
  /* 遮罩 */
  --el-overlay-color: rgba(0, 0, 0, 0.8);
  --el-overlay-color-light: rgba(0, 0, 0, 0.7);
  --el-overlay-color-lighter: rgba(0, 0, 0, 0.5);
  --el-mask-color: rgba(0, 0, 0, 0.8);
  --el-mask-color-extra-light: rgba(0, 0, 0, 0.3);
}
</style>
