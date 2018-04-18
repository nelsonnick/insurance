import Vue from 'vue'
import iView from 'iview'
import VueRouter from 'vue-router'
import Edit from './edit.vue'
import 'iview/dist/styles/iview.css'   // 使用 CSS
Vue.use(VueRouter)
Vue.use(iView)

const routes = [
  { path: '/edit', component: Edit },
  { path: '/', redirect: '/edit' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  template: '<router-view></router-view>'
}).$mount('#app')
