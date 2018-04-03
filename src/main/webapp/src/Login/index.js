import Vue from 'vue'
import iView from 'iview'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Login from './login.vue'
import 'iview/dist/styles/iview.css'   // 使用 CSS
Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(iView)

const routes = [
  { path: '/login', component: Login },
  { path: '/', redirect: '/login' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  template: '<router-view></router-view>'
}).$mount('#app')
