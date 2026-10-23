import './assets/css/base.css'

import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import cn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import ja from 'element-plus/es/locale/lang/ja'

import { useDark } from '@/utils/theme'
import { createPinia } from 'pinia'

import { createApp } from 'vue'
import App from './App.vue'
import { addCollection } from '@iconify/vue'
import lucide from '@iconify-json/lucide/icons.json'

// 离线注册 Iconify 图标集（lucide），运行时不请求图标服务
addCollection(lucide)

const app = createApp(App)

const pinia = createPinia()
app.use(router).use(pinia)

let language = sessionStorage.getItem('h-sm-lang') || 'zh-CN'
app.use(ElementPlus, {
    locale: language=='en-US' ? en : language=='ja-JP' ? ja : cn,
    size: 'small',
    zIndex: 2000
})

app.mount('#app')
