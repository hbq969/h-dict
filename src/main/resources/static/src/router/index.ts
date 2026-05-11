import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
  {
    path: '',
    name: '字典管理',
    component: () => import('@/views/dict/main.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes
})

export default router
