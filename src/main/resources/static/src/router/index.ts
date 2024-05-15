import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
  {
    path: '',
    name: '字典管理',
    component: () => import('@/views/dict/main.vue')
  }
]

const router = createRouter({
  // history: createWebHistory(process.env.BASE_URL),
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

export default router
