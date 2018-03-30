import Vue from 'vue'
import iView from 'iview'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'
import Edit from './edit.vue'
import List from './list.vue'
import PersonList from './personList.vue'
import PersonAdd from './personAdd.vue'
import 'iview/dist/styles/iview.css'   // 使用 CSS
Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(Vuex)
Vue.use(iView)

const routes = [
  { path: '/list', component: List },
  { path: '/personList', component: PersonList },
  { path: '/personAdd', component: PersonAdd },
  { path: '/edit/:id', component: Edit },
  { path: '/', redirect: '/personList' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

const store = new Vuex.Store({
  state: {
    keyword: '',
    pageCurrent: '1'
  },
  mutations: {
    save (state, page) {
      state.keyword = page.keyword
      state.pageCurrent = page.pageCurrent
    }
  }
})
/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  store,
  template: '<router-view></router-view>'
}).$mount('#app')
