<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import store from './vuex/store.js'
import './lib/css/style.css'
export default {
  name: 'app',
  store
}
</script>

<style>
  html, body, #app{
    height: 100% !important;
  }
  #app>div{
    height: 100%;
  }
</style>
