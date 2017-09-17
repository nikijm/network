// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import './lib/css'
import './lib/script'

import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import router from './router.js'
import EventBus from './lib/eventBus.js'
import promptReload from './lib/promptReload.js'
import axios from 'axios'
import Element from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import store from './vuex/store.js'
Vue.use(Element)
Vue.use(promptReload)

Vue.prototype.$bus = EventBus
Vue.prototype.$http = axios
// axios.defaults.headers.common['X_ETL_TOKEN'] = sessionStorage.getItem('token')

// http request 拦截器
axios.interceptors.request.use(config => {
  let token = sessionStorage.getItem('token')
  if (token) {
    config.headers.X_ETL_TOKEN = token
  }
  // vm.$store.commit('FETCH_LOADING', true)
  return config
}, err => {
  return Promise.reject(err)
})

// axios.defaults.timeout = 4000
// http response 拦截器
axios.interceptors.response.use(response => {
  // vm.$store.commit('FETCH_LOADING', false)
  return response
}, err => {
  // token过期了
  if (err.response.status === 40317) {
    vm.$router.push({ path: '/login' })
  } else if (err.response.status === 401) {
    vm.$message('没有权限')
    vm.$router.push({ path: '/welcome' })
  }

  return Promise.reject(err)
})

// 白名单
const whiteList = ['/login']
router.beforeEach((to, from, next) => {
  let token = sessionStorage.getItem('token')
  if (token) {
    if (to.path === '/') {
      next({ path: '/welcome' })
    } else {
      next()
    }
  } else {
    // token不存在
    if (whiteList.indexOf(to.path) !== -1) {
      //  在免登录白名单，直接进入
      next()
    } else {
      // 否则全部重定向到登录页
      next({ path: '/login' })
    }
  }
})

/* eslint-disable no-new */
var vm = new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {
    App
  },
  store
})
