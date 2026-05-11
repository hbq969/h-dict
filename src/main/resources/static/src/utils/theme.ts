import { ref } from 'vue'

const isDark = ref(false)
const STORAGE_KEY = 'h-sm-theme'

// 解析当前主题状态
const syncFromDOM = () => {
  isDark.value = document.documentElement.classList.contains('dark')
}

// 初始化
const saved = localStorage.getItem(STORAGE_KEY)
console.log('读取到theme：', saved)
if (saved === 'dark') {
  isDark.value = true
  document.documentElement.classList.add('dark')
}

// 跨标签页/跨工程同步：监听其他文档的修改
window.addEventListener('storage', (e) => {
  if (e.key === STORAGE_KEY) {
    const dark = e.newValue === 'dark'
    isDark.value = dark
    if (dark) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
    // storage 事件来自其他文档，不加动画，直接切换
  }
})

export function useDark() {
  const applyTheme = (dark: boolean, x?: number, y?: number) => {
    const toggle = () => {
      if (dark) {
        document.documentElement.classList.add('dark')
        localStorage.setItem(STORAGE_KEY, 'dark')
      } else {
        document.documentElement.classList.remove('dark')
        localStorage.setItem(STORAGE_KEY, 'light')
      }
    }

    if (document.startViewTransition) {
      const html = document.documentElement
      if (x !== undefined && y !== undefined) {
        html.style.setProperty('--tx-x', x + 'px')
        html.style.setProperty('--tx-y', y + 'px')
      }
      html.classList.remove('tx-expand', 'tx-shrink')
      html.classList.add(dark ? 'tx-shrink' : 'tx-expand')
      document.startViewTransition(() => toggle())
    } else {
      toggle()
    }
  }

  return { isDark, applyTheme }
}
